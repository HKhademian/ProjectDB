@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.User
import app.model.Invitation

fun listUserInvitations(userId: Int): List<Invitation> =
	connect {
		val SQL = """
			SELECT * FROM Invitation WHERE userId=? OR by_userId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, userId)
		stmt.executeQuery()
			.listOf<Invitation>()
	} ?: emptyList()

fun sendInvitation(fromUserId: Int, toUserId: Int, message: String): Invitation? =
	connect {
		val SQL = """
			INSERT INTO Invitation
			(userId, by_userId, time, message, status)
			VALUES (?,?,?,?,0) RETURNING *;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, toUserId)
		stmt.setInt(2, fromUserId)
		stmt.setLong(3, System.currentTimeMillis())
		stmt.setString(4, message)
		stmt.executeQuery()
			.singleOf<Invitation>()
	}

fun deleteInvitation(fromUserId: Int, toUserId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Invitation WHERE userId=? AND by_userId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, toUserId)
		stmt.setInt(2, fromUserId)
		stmt.executeUpdate() > 0
	} == true

fun acceptInvitation(fromUserId: Int, toUserId: Int): Invitation? =
	connect {
		val SQL_CON = """
			INSERT OR REPLACE INTO Connection (from_userId, to_userId, time)
			SELECT userId,by_userId,? FROM Invitation WHERE userId=? AND by_userId=? AND status!=1
			UNION
			SELECT by_userId,userId,? FROM Invitation WHERE userId=? AND by_userId=? AND status!=1
		""".trimIndent()
		val SQL_INV = """
			UPDATE Invitation SET status=1 WHERE userId=? AND by_userId=? RETURNING *;
		""".trimIndent()

		val time = System.currentTimeMillis()
		val stmt1 = it.prepareStatement(SQL_CON)
		stmt1.setLong(1, time)
		stmt1.setInt(2, toUserId)
		stmt1.setInt(3, fromUserId)
		stmt1.setLong(4, time)
		stmt1.setInt(5, fromUserId)
		stmt1.setInt(6, toUserId)
		if (stmt1.executeUpdate() <= 0) return@connect null

		val stmt2 = it.prepareStatement(SQL_INV)
		stmt1.setInt(1, toUserId)
		stmt1.setInt(2, fromUserId)
		stmt2.executeQuery()
			.singleOf<Invitation>()
	}

fun rejectInvitation(fromUserId: Int, toUserId: Int): Invitation? =
	connect {
		val SQL_CON = """
			DELETE FROM Connection WHERE (from_userId=? AND to_userId=?) OR (to_userId=? AND from_userId=?);
		""".trimIndent()
		val SQL_INV = """
			UPDATE Invitation SET status=-1 WHERE userId=? AND by_userId=? RETURNING *;
		""".trimIndent()

		val stmt1 = it.prepareStatement(SQL_CON)
		stmt1.setInt(1, fromUserId)
		stmt1.setInt(2, toUserId)
		stmt1.setInt(3, toUserId)
		stmt1.setInt(4, fromUserId)
		if (stmt1.executeUpdate() <= 0) return@connect null

		val stmt2 = it.prepareStatement(SQL_INV)
		stmt1.setInt(1, toUserId)
		stmt1.setInt(2, fromUserId)
		stmt2.executeQuery()
			.singleOf<Invitation>()
	}


fun listMyNetworkProfiles(userId: Int): List<User> =
	connect {
		val SQL = """
			select U.*
			from MyNetwork MN
			JOIN User U ON U.userId=MN.to_userId
			WHERE MN.from_userId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.executeQuery()
			.listOf<User>()
	} ?: emptyList()

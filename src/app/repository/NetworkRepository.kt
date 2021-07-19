@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.User
import app.model.Invitation

fun listUserInvitations(userId: Int): List<Invitation> =
	connect {
		val SQL = """
			SELECT * FROM Invitation WHERE receive_userId=? OR sender_userId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, userId)
		stmt.executeQuery()
			.listOf<Invitation>()
	} ?: emptyList()

fun sendInvitation(senderUserId: Int, receiverUserId: Int, message: String): Invitation? =
	connect {
		val SQL = """
			INSERT INTO Invitation
			(sender_userId, receive_userId, time, message, status)
			VALUES (?,?,?,?,0) RETURNING *;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, senderUserId)
		stmt.setInt(2, receiverUserId)
		stmt.setLong(3, System.currentTimeMillis())
		stmt.setString(4, message)
		stmt.executeQuery()
			.singleOf<Invitation>()
	}

fun deleteInvitation(senderUserId: Int, receiverUserId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Invitation WHERE receive_userId=? AND sender_userId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, receiverUserId)
		stmt.setInt(2, senderUserId)
		stmt.executeUpdate() > 0
	} == true

fun acceptInvitation(senderUserId: Int, receiverUserId: Int): Invitation? =
	connect {
		val SQL_CON = """
			INSERT OR REPLACE INTO Connection (from_userId, to_userId, time)
			SELECT I.receive_userId,I.sender_userId,? FROM Invitation I WHERE I.receive_userId=? AND I.sender_userId=? AND I.status!=1
			UNION
			SELECT I.sender_userId,I.receive_userId,? FROM Invitation I WHERE I.receive_userId=? AND I.sender_userId=? AND I.status!=1
		""".trimIndent()
		val SQL_INV = """
			UPDATE Invitation SET status=1 WHERE receive_userId=? AND sender_userId=? RETURNING *;
		""".trimIndent()

		val time = System.currentTimeMillis()
		val stmt1 = it.prepareStatement(SQL_CON)
		stmt1.setLong(1, time)
		stmt1.setInt(2, receiverUserId)
		stmt1.setInt(3, senderUserId)
		stmt1.setLong(4, time)
		stmt1.setInt(5, senderUserId)
		stmt1.setInt(6, receiverUserId)
		if (stmt1.executeUpdate() <= 0) return@connect null

		val stmt2 = it.prepareStatement(SQL_INV)
		stmt1.setInt(1, receiverUserId)
		stmt1.setInt(2, senderUserId)
		stmt2.executeQuery()
			.singleOf<Invitation>()
	}

fun rejectInvitation(senderUserId: Int, receiverUserId: Int): Invitation? =
	connect {
		val SQL_CON = """
			DELETE FROM Connection WHERE (from_userId=? AND to_userId=?) OR (to_userId=? AND from_userId=?);
		""".trimIndent()
		val SQL_INV = """
			UPDATE Invitation SET status=-1 WHERE receive_userId=? AND sender_userId=? RETURNING *;
		""".trimIndent()

		val stmt1 = it.prepareStatement(SQL_CON)
		stmt1.setInt(1, senderUserId)
		stmt1.setInt(2, receiverUserId)
		stmt1.setInt(3, receiverUserId)
		stmt1.setInt(4, senderUserId)
		if (stmt1.executeUpdate() <= 0) return@connect null

		val stmt2 = it.prepareStatement(SQL_INV)
		stmt1.setInt(1, receiverUserId)
		stmt1.setInt(2, senderUserId)
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

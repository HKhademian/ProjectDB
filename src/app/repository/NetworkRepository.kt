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

fun acceptInvitation(senderUserId: Int, receiverUserId: Int, isAccepted: Boolean): Boolean =
	connect { conn ->
		val SQL_INV = """
			UPDATE Invitation SET status=? WHERE receive_userId=? AND sender_userId=?;
		""".trimIndent()
		val SQL_CON_ADD = """
			INSERT OR REPLACE INTO Connection (time, from_userId, to_userId)
			SELECT ?,?,? UNION SELECT ?,?,?
		""".trimIndent()
		val SQL_CON_DEL = """
			DELETE FROM Connection WHERE (from_userId=? AND to_userId=?) OR (to_userId=? AND from_userId=?);
		""".trimIndent()

		val time = System.currentTimeMillis()
		conn.autoCommit = false

		val isUpdated = conn.prepareStatement(SQL_INV).let { stmt ->
			stmt.setInt(1, if (isAccepted) 1 else -1)
			stmt.setInt(2, receiverUserId)
			stmt.setInt(3, senderUserId)
			stmt.executeUpdate() > 0
		}

		if (!isUpdated) {
			conn.rollback()
			return@connect false
		}

		val isConnected =
			if (isAccepted)
				conn.prepareStatement(SQL_CON_ADD).let { stmt ->
					stmt.setLong(1, time)
					stmt.setInt(2, receiverUserId)
					stmt.setInt(3, senderUserId)
					stmt.setLong(4, time)
					stmt.setInt(5, senderUserId)
					stmt.setInt(6, receiverUserId)
					stmt.executeUpdate() > 0
				}
			else
				conn.prepareStatement(SQL_CON_DEL).let { stmt ->
					stmt.setInt(1, receiverUserId)
					stmt.setInt(2, senderUserId)
					stmt.setInt(3, receiverUserId)
					stmt.setInt(4, senderUserId)
					stmt.executeUpdate()
					true
				}

		if (!isConnected) {
			conn.rollback()
			return@connect false
		}

		conn.commit()
		true
	} == true

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


fun searchProfiles(
	name: String?,
	location: String?,
	skillId: Int?,
	langCode: String?,
	backgroundTitle: String?
): List<User> =
	connect {
		val SQL = """
		SELECT * FROM User U WHERE 1
		  AND (? OR (U.firstname || U.lastname like '%' || ? || '%'))
		  AND (? OR (U.location like '%' || ? || '%'))
		  AND (? OR EXISTS(select * from User_Skill U_S WHERE U.userId = U_S.userId AND U_S.skillId = ?))
		  AND (? OR EXISTS(select * from User_Lang U_L WHERE U.userId = U_L.userId AND U_L.langCode = ?))
		  AND (? OR EXISTS(select * from User_Background U_B WHERE U.userId = U_B.userId AND U_B.title LIKE '%' || ? || '%'))
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		var i = 0

		stmt.setInt(++i, if (name != null) 0 else 1)
		stmt.setString(++i, name)

		stmt.setInt(++i, if (location != null) 0 else 1)
		stmt.setString(++i, location)

		stmt.setInt(++i, if (skillId != null) 0 else 1)
		stmt.setInt(++i, skillId ?: 0)

		stmt.setInt(++i, if (langCode != null) 0 else 1)
		stmt.setString(++i, langCode)

		stmt.setInt(++i, if (backgroundTitle != null) 0 else 1)
		stmt.setString(++i, backgroundTitle)

		stmt.executeQuery()
			.listOf<User>()
	} ?: emptyList()

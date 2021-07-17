@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Invitation

fun listInvitation(userId: Int): List<Invitation> =
	connect {
		val SQL = """SELECT * FROM Invitation WHERE `userId`=? OR `by_userId`=?;"""
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, userId)
		stmt.executeQuery()
			.list<Invitation>()
	} ?: emptyList()

fun sendInvitation(userId: Int, byUserId: Int, message: String): Invitation? =
	connect {
		val SQL = """INSERT INTO `Invitation`
				(`userId`, `by_userId`, `time`, `message`, `status`)
				VALUES (?,?,?,?,0) RETURNING *;"""
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, byUserId)
		stmt.setLong(3, System.currentTimeMillis())
		stmt.setString(4, message)
		stmt.executeQuery()
			.tryRead<Invitation>()
	}

fun deleteInvitation(userId: Int, byUserId: Int): Invitation? =
	connect {
		val SQL = """DELETE FROM `Invitation` WHERE `userId`=? AND `by_userId`=? RETURNING *;"""
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, byUserId)
		stmt.executeQuery()
			.tryRead<Invitation>()
	}

fun acceptInvitation(userId: Int, byUserId: Int): Invitation? =
	connect {
		val SQL = """
			INSERT INTO `Connection` (`userId1`, `userId2`, `time`) VALUES (?,?,?);
			UPDATE `Invitation` SET `status`=1 WHERE `userId`=? AND `by_userId`=? RETURNING *;
		"""
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, byUserId)
		stmt.setLong(3, System.currentTimeMillis())
		stmt.setInt(4, userId)
		stmt.setInt(5, byUserId)
		stmt.executeQuery()
			.tryRead<Invitation>()
	}

fun rejectInvitation(userId: Int, byUserId: Int): Invitation? =
	connect {
		val SQL = """
			SELECT ?,?,?,?;	--	DELETE FROM `Connection` WHERE (`user1`=? AND `user2`=?) OR (`user2`=? AND `user1`=?);
			UPDATE `Invitation` SET `status`=-1 WHERE `userId`=? AND `by_userId`=? RETURNING *;
		"""
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, byUserId)
		stmt.setInt(3, userId)
		stmt.setInt(4, byUserId)
		stmt.setInt(5, userId)
		stmt.setInt(6, byUserId)
		stmt.executeQuery()
			.tryRead<Invitation>()
	}

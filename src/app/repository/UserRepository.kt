@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Notification
import app.model.User
import java.sql.Connection
import java.sql.Types
import java.util.Date

fun getUserById(userId: Int): User? {
	val SQL = "SELECT * from `User` where `userId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.tryRead<User>()
	}
}

fun loginUser(username: String?, password: String?): User? {
	val SQL = "select * from `User` where `username`=? and `password`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setString(1, username)
		statement.setString(2, password)
		statement.executeQuery()
			.tryRead<User>()
	}
}

fun registerUser(user: User, password: String): User? {
	val SQL = """INSERT INTO `User`
			(`userId`, `username`, `password`, `firstname`, `lastname`, `intro`, `about`, `avatar`, `accomp`, `birthday`, `location`)
			VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?)
			RETURNING *;"""
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setString(1, user.username)
		statement.setString(2, password)
		statement.setString(3, user.firstname)
		statement.setString(4, user.lastname)
		statement.setString(5, user.intro)
		statement.setString(6, user.about)
		statement.setBytes(7, user.avatar)
		if (user.birthday != null)
			statement.setLong(8, user.birthday!!.time)
		else statement.setNull(8, Types.INTEGER)
		statement.setString(9, user.location)
		statement.executeQuery()
			.tryRead<User>()
	}
}

fun toggleUserLike(userId: Int, articleId: Int, commentId: Int): Boolean {
	val SQL1: String
	val SQL2: String
	val SQL3: String
	if (commentId > 0) {
		SQL1 = "SELECT count(*) from `User_Like` where `userId`=? and `articleId`=? and `commentId`=?;"
		SQL2 = "DELETE from `User_Like` where `userId`=? and `articleId`=? and `commentId`=?;"
		SQL3 = "INSERT into `User_Like` (`userId`, `articleId`, `time`, `commentId`, `notified`) VALUES (?,?,?,?,0);"
	} else {
		SQL1 = "SELECT count(*) from `User_Like` where `userId`=? and `articleId`=? and `commentId` is null;"
		SQL2 = "DELETE from `User_Like` where `userId`=? and `articleId`=? and `commentId` is null;"
		SQL3 = "INSERT into `User_Like` (`userId`, `articleId`, `time`, `commentId`, `notified`) VALUES (?,?,?,null,0);"
	}
	val res = connect { connection: Connection ->
		val statement1 = connection.prepareStatement(SQL1)
		statement1.setInt(1, userId)
		statement1.setInt(2, articleId)
		if (commentId > 0) statement1.setInt(3, commentId)
		val res1 = statement1.executeQuery()
		if (res1.next() && res1.getInt(1) > 0) {
			val statement2 = connection.prepareStatement(SQL2)
			statement2.setInt(1, userId)
			statement2.setInt(2, articleId)
			if (commentId > 0) statement2.setInt(3, commentId)
			statement2.executeUpdate()
			return@connect true
		} else {
			val statement3 = connection.prepareStatement(SQL3)
			statement3.setInt(1, userId)
			statement3.setInt(2, articleId)
			statement3.setLong(3, System.currentTimeMillis())
			if (commentId > 0) statement3.setInt(4, commentId)
			statement3.executeUpdate()
			return@connect true
		}
	}
	return res != null && res
}

fun getUserNotification(userId: Int): List<Notification> {
	val SQL = "select * from `Notification` where `userId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Notification>()
	}!!
}

fun addUserSkill(userId: Int, skillId: Int, level: Int): Boolean {
	val SQL = "INSERT INTO `User_Skill` (`userId`, `skillId`, `level`, `time`) VALUES (?,?,?,?);"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, skillId)
		statement.setInt(3, level)
		statement.setLong(4, System.currentTimeMillis())
		statement.executeUpdate() > 0
	} == true
}

fun removeUserSkill(userId: Int, skillId: Int): Boolean {
	val SQL = "DELETE FROM `User_Skill` WHERE `userId`=? AND `skillId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, skillId)
		statement.executeUpdate() > 0
	} == true
}

fun addUserLanguage(userId: Int, langCode: String): Boolean {
	val SQL = "INSERT INTO `User_Lang` (`userId`, `langCode`) VALUES (?,?);"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setString(2, langCode)
		statement.executeUpdate() > 0
	} == true
}

fun removeUserLanguage(userId: Int, langCode: String): Boolean {
	val SQL = "DELETE FROM `User_Lang` WHERE `userId`=? AND `langCode`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setString(2, langCode)
		statement.executeUpdate() > 0
	} == true
}

@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Language
import java.sql.Connection

fun listLanguages(): List<Language> {
	val SQL = "SELECT * from `Language`;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.executeQuery()
			.list<Language>()
	} ?: emptyList()
}

fun getLanguageById(langCode: String): Language? {
	val SQL = "SELECT * from `Language` where `langCode`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setString(1, langCode)
		statement.executeQuery()
			.tryRead<Language>()
	}
}

fun addLanguage(langCode: String, title: String): Language? {
	val SQL = "INSERT INTO `Language` VALUES (?,?) RETURNING *;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setString(1, langCode)
		statement.setString(2, title)
		statement.executeQuery()
			.tryRead<Language>()
	}
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

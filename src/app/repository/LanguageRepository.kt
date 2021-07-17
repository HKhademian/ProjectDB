@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Language

fun listLanguages(): List<Language> =
	connect {
		val SQL = """SELECT * from `Language`;"""
		val statement = it.prepareStatement(SQL)
		statement.executeQuery()
			.list<Language>()
	} ?: emptyList()

fun getLanguageById(langCode: String): Language? =
	connect {
		val SQL = """SELECT * from `Language` where `langCode`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setString(1, langCode)
		statement.executeQuery()
			.tryRead<Language>()
	}

fun addLanguage(langCode: String, title: String): Language? =
	connect {
		val SQL = """INSERT INTO `Language` VALUES (?,?) RETURNING *;"""
		val statement = it.prepareStatement(SQL)
		statement.setString(1, langCode)
		statement.setString(2, title)
		statement.executeQuery()
			.tryRead<Language>()
	}

fun listUserLanguage(userId: Int): List<Language> =
	connect {
		val SQL = """SELECT L.* from `Language` L JOIN 'User_Lang' UL ON L.langCode=UL.langCode where UL.`userId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Language>()
	} ?: emptyList()


fun getLastUserLang(userId: Int): Language? =
	connect {
		val SQL = """SELECT * FROM `User_Lang` WHERE `userId`=? ORDER BY ROWID DESC LIMIT 1;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.tryRead<Language>()
	}

fun addUserLanguage(userId: Int, langCode: String): Boolean =
	connect {
		val SQL = """INSERT INTO `User_Lang` (`userId`, `langCode`) VALUES (?,?);"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setString(2, langCode)
		statement.executeUpdate() > 0
	} == true

fun removeUserLanguage(userId: Int, langCode: String): Boolean =
	connect {
		val SQL = """DELETE FROM `User_Lang` WHERE `userId`=? AND `langCode`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setString(2, langCode)
		statement.executeUpdate() > 0
	} == true

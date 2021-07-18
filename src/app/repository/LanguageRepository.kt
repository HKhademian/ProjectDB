@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Language

fun listLanguages(): List<Language> =
	connect {
		val SQL = """
			SELECT * from Language;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.executeQuery()
			.listOf<Language>()
	} ?: emptyList()

fun getLanguageByCode(langCode: String): Language? =
	connect {
		val SQL = """
			SELECT * from Language where langCode=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setString(1, langCode)
		statement.executeQuery()
			.singleOf<Language>()
	}

fun addLanguage(langCode: String, title: String): Language? =
	connect {
		val SQL = """
			INSERT INTO Language VALUES (?,?) RETURNING *;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setString(1, langCode)
		statement.setString(2, title)
		statement.executeQuery()
			.singleOf<Language>()
	}

fun listUserLanguage(userId: Int): List<Language> =
	connect {
		val SQL = """
			SELECT L.* from Language L
			JOIN 'User_Lang' UL ON L.langCode=UL.langCode
			where UL.userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.listOf<Language>()
	} ?: emptyList()

fun addUserLanguage(userId: Int, langCode: String): Boolean =
	connect {
		val SQL = """
			INSERT INTO User_Lang (userId, langCode) VALUES (?,?);
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setString(2, langCode)
		statement.executeUpdate() > 0
	} == true

fun removeUserLanguage(userId: Int, langCode: String): Boolean =
	connect {
		val SQL = """
			DELETE FROM User_Lang WHERE userId=? AND langCode=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setString(2, langCode)
		statement.executeUpdate() > 0
	} == true

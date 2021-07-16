package app.repository

import app.model.Language
import java.sql.Connection

object LanguageRepository {
	fun listLanguages(): List<Language> {
		val SQL = "SELECT * from `Language`;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.executeQuery()
				.list<Language>()
		}!!
	}

	fun getLanguage(langCode: String?): Language? {
		val SQL = "SELECT * from `Language` where `langCode`=?;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setString(1, langCode)
			statement.executeQuery()
				.tryRead<Language>()
		}
	}

	@JvmStatic
	fun addLanguage(langCode: String?, title: String?): Language? {
		val SQL = "INSERT INTO `Language` VALUES (?,?) RETURNING *;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setString(1, langCode)
			statement.setString(2, title)
			statement.executeQuery()
				.tryRead<Language>()
		}
	}
}

package app.repository

import app.model.Language
import java.sql.Connection
import java.util.ArrayList

object LanguageRepository {
	fun listLanguages(): List<Language> {
		val SQL = "SELECT * from `Language`;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			val res = statement.executeQuery()
			val result: MutableList<Language> = ArrayList()
			while (res.next()) result.add(Language.from(res))
			result
		}!!
	}

	fun getLanguage(langCode: String?): Language? {
		val SQL = "SELECT * from `Language` where `langCode`=?;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setString(1, langCode)
			val res = statement.executeQuery()
			if (res.next()) Language.from(res) else null
		}
	}

	@JvmStatic
	fun addLanguage(langCode: String?, title: String?): Language? {
		val SQL = "INSERT INTO `Language` VALUES (?,?) RETURNING *;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setString(1, langCode)
			statement.setString(2, title)
			val res = statement.executeQuery()
			if (res.next()) Language.from(res) else null
		}
	}
}

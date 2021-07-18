@file:JvmName("Database")

package app.repository

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*

const val DB_NAME = "db.sqlite"
const val TEST_DB_NAME = "db-test.sqlite"

@JvmField
var lastError: Throwable? = null

@JvmField
var test: Boolean = false

fun <T> connect(action: /*Connection.*/(Connection) -> T?): T? {
	val db = "jdbc:sqlite:${if (test) TEST_DB_NAME else DB_NAME}"
	try {
		val properties = Properties()
		properties.setProperty("foreign_keys", "true")
		DriverManager.getConnection(db, properties).use { connection ->
			lastError = null

			// return connection.run { action(connection) } as T?
			return action(connection) as T?
		}
	} catch (ex: Throwable) {
		lastError = ex
		ex.printStackTrace()
		return null
	}
}

fun scalarQuery(sql: String): String? =
	connect<String?> {
		val res: ResultSet = it.createStatement().executeQuery(sql)
		res.next()
		res.getString(1)
	}

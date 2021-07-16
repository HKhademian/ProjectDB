@file:JvmName("Database")

package app.repository

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*


private const val DB_NAME = "db.sqlite"

@JvmField
var lastError: Throwable? = null

fun <T> connect(action: (Connection) -> T?): T? {
	val db = "jdbc:sqlite:$DB_NAME"
	try {
		val properties = Properties()
		properties.setProperty("foreign_keys", "true")
		DriverManager.getConnection(db, properties).use { connection ->
			lastError = null
			// connection.createStatement().execute("PRAGMA foreign_keys=ON;")
			return action(connection)
		}
	} catch (ex: SQLException) {
		lastError = ex
		ex.printStackTrace()
		return null
	}
}

fun scalarQuery(sql: String): String? =
	connect {
		val res: ResultSet = it.createStatement().executeQuery(sql)
		res.next()
		res.getString(1)
	}

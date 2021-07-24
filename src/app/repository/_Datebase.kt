@file:JvmName("Database")

package app.repository

import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import kotlin.jvm.Throws

const val DB_NAME = "db.sqlite"
const val TEST_DB_NAME = "db-test.sqlite"

@JvmField
var lastError: Throwable? = null

@JvmField
var test: Boolean = false

fun interface ConnectionAction<D, T> {
	@Throws(Exception::class)
	operator fun invoke(it: D): T?
}

fun <T> connect(action: ConnectionAction<Connection, T>): T? {
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


fun <T> connect(sql: String, action: ConnectionAction<PreparedStatement, T>): T? =
	connect {
		val stmt = it.prepareStatement(sql)
		action(stmt)
	}

fun scalarQuery(sql: String): String? =
	connect<String> {
		val res: ResultSet = it.createStatement().executeQuery(sql)
		res.next()
		res.getString(1)
	}

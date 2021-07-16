@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Notification
import java.sql.Connection

fun getUserNotification(userId: Int): List<Notification> {
	val SQL = "select * from `Notification` where `userId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Notification>()
	} ?: emptyList()
}

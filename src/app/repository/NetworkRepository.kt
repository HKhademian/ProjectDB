@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.User
import java.sql.Connection

fun listUserNetworkUsers(userId: Int): List<User> {
	val SQL = "select * from `User` U JOIN `MyNetwork` MN ON U.userId=MN.user2 where MN.`user1`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<User>()
	} ?: emptyList()
}

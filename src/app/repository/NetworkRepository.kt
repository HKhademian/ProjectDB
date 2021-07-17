@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.User

fun listUserNetworkUsers(userId: Int): List<User> =
	connect {
		val SQL = """select * from `User` U JOIN `MyNetwork` MN ON U.userId=MN.to_userId where MN.from_userId=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<User>()
	} ?: emptyList()

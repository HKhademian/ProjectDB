@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Notification

fun getUserNotification(userId: Int): List<Notification> =
	connect {
		val SQL = """select * from `Notification` where `userId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Notification>()
	} ?: emptyList()

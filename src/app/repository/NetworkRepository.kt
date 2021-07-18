@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.MyNetwork
import app.model.User

fun listUserNetworkUsers(userId: Int): List<User> =
	connect {
		val SQL = """select U.*
			from MyNetwork MN
			JOIN User U ON U.userId=MN.to_userId
			WHERE MN.from_userId=?;"""
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.executeQuery()
			.listOf<User>()
	} ?: emptyList()

fun listMyNetwork(userId: Int): List<MyNetwork> =
	connect {
		val SQL = """select MN.*, U.*
			from MyNetwork MN
			JOIN User U ON U.userId=MN.to_userId
			WHERE MN.from_userId=?;"""
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.executeQuery()
			.listOf<MyNetwork>()
	} ?: emptyList()

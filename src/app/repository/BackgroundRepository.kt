@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Background
import java.sql.Types

fun suggestBackground(): List<Background> =
	connect {
		val SQL = """SELECT * from `SuggestBackground`;"""
		val statement = it.prepareStatement(SQL)
		statement.executeQuery()
			.listOf<Background>()
	} ?: emptyList()

fun listUserBackground(userId: Int): List<Background> =
	connect {
		val SQL = """SELECT * from `User_Background` WHERE `userId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.listOf<Background>()
	} ?: emptyList()

fun saveUserBackground(bg: Background): Background? =
	connect {
		val SQL = """
		INSERT OR REPLACE INTO `User_Background`
		(`bgId`, `userId`, `type`, `title`, `fromTime`, `toTime`)
		VALUES (?,?,?,?,?,?) RETURNING *;
		"""
		val statement = it.prepareStatement(SQL)
		if (bg.bgId > 0)
			statement.setInt(1, bg.bgId)
		else
			statement.setNull(1, Types.INTEGER)
		statement.setInt(2, bg.userId)
		statement.setInt(3, bg.bgType.ordinal)
		statement.setString(4, bg.title)
		statement.setLong(5, bg.fromTime.time)
		if (bg.toTime?.time ?: 0 > 0)
			statement.setLong(6, bg.fromTime.time)
		else
			statement.setNull(6, Types.INTEGER)
		statement.executeQuery()
			.singleOf<Background>()
	}

fun deleteUserBackground(bgId: Int): Background? =
	connect {
		val SQL = """DELETE FROM `User_Background` WHERE `bgId`=? RETURNING *;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, bgId)
		statement.executeQuery()
			.singleOf<Background>()
	}

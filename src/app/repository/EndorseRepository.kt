@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.SkillEndorse
import java.sql.Connection

fun listSkillEndorsedByUser(byUserId: Int): List<SkillEndorse> {
	val SQL = "SELECT * from `Skill_Endorse` WHERE `by_userId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, byUserId)
		statement.executeQuery()
			.list<SkillEndorse>()
	} ?: emptyList()
}

fun listSkillEndorseOnUser(userId: Int): List<SkillEndorse> {
	val SQL = "SELECT * from `Skill_Endorse` WHERE `userId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<SkillEndorse>()
	} ?: emptyList()
}

fun addSkillEndorse(byUserId: Int, userId: Int, skillId: Int): SkillEndorse? {
	val SQL =
		"INSERT INTO `Skill_Endorse` (`by_userId`, `userId`, `skillId`, `time`, `notified`) VALUES (?,?,?,?,0) RETURNING *;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, byUserId)
		statement.setInt(2, userId)
		statement.setInt(3, skillId)
		statement.setLong(4, System.currentTimeMillis())
		statement.executeQuery()
			.tryRead<SkillEndorse>()
	}
}

fun removeSkillEndorse(byUserId: Int, userId: Int, skillId: Int): SkillEndorse? {
	val SQL = "DELETE FROM `Skill_Endorse` WHERE `by_userId`=? AND `userId`=? AND `skillId`=? RETURNING *;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, byUserId)
		statement.setInt(2, userId)
		statement.setInt(3, skillId)
		statement.executeQuery()
			.tryRead<SkillEndorse>()
	}
}


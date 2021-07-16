@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Skill
import java.sql.Connection

fun listSkills(): List<Skill> {
	val SQL = "SELECT * from `Skill`;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.executeQuery()
			.list<Skill>()
	}!!
}

fun getSkill(skillId: Int): Skill? {
	val SQL = "SELECT * from `Skill` where `skillId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, skillId)
		statement.executeQuery()
			.tryRead<Skill>()
	}
}

fun addSkill(title: String?): Skill? {
	val SQL = "INSERT INTO `Skill` (`skillId`, `title`) VALUES (null,?) RETURNING *;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setString(1, title)
		statement.executeQuery()
			.tryRead<Skill>()
	}
}

fun addSkillEndorse(byUserId: Int, userId: Int, skillId: Int): Boolean {
	val SQL = "INSERT INTO `Skill_Endorse` (`by_userId`, `userId`, `skillId`, `time`, `notified`) VALUES (?,?,?,?,0);"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, byUserId)
		statement.setInt(2, userId)
		statement.setInt(3, skillId)
		statement.setLong(4, System.currentTimeMillis())
		statement.executeUpdate() > 0
	} == true
}

fun removeSkillEndorse(byUserId: Int, userId: Int, skillId: Int): Boolean {
	val SQL = "DELETE FROM `Skill_Endorse` WHERE `by_userId`=? AND `userId`=? AND `skillId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, byUserId)
		statement.setInt(2, userId)
		statement.setInt(3, skillId)
		statement.executeUpdate() > 0
	} == true
}

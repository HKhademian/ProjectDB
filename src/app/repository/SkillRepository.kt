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
	} ?: emptyList()
}

fun getSkillById(skillId: Int): Skill? {
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


fun listUserSkill(userId: Int): List<Skill> {
	val SQL = "SELECT S.* from `Skill` S JOIN 'User_Skill' US where US.`userId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Skill>()
	} ?: emptyList()
}

fun addUserSkill(userId: Int, skillId: Int, level: Int): Boolean {
	val SQL = "INSERT INTO `User_Skill` (`userId`, `skillId`, `level`, `time`) VALUES (?,?,?,?);"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, skillId)
		statement.setInt(3, level)
		statement.setLong(4, System.currentTimeMillis())
		statement.executeUpdate() > 0
	} == true
}

fun removeUserSkill(userId: Int, skillId: Int): Boolean {
	val SQL = "DELETE FROM `User_Skill` WHERE `userId`=? AND `skillId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, skillId)
		statement.executeUpdate() > 0
	} == true
}

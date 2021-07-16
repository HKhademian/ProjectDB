@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Skill

fun listSkills(): List<Skill> =
	connect {
		val SQL = """SELECT * from `Skill`;"""
		val statement = it.prepareStatement(SQL)
		statement.executeQuery()
			.list<Skill>()
	} ?: emptyList()

fun getSkillById(skillId: Int): Skill? =
	connect {
		val SQL = """SELECT * from `Skill` where `skillId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, skillId)
		statement.executeQuery()
			.tryRead<Skill>()
	}

fun addSkill(title: String?): Skill? =
	connect {
		val SQL = """INSERT INTO `Skill` (`skillId`, `title`) VALUES (null,?) RETURNING *;"""
		val statement = it.prepareStatement(SQL)
		statement.setString(1, title)
		statement.executeQuery()
			.tryRead<Skill>()
	}

fun listUserSkill(userId: Int): List<Skill> =
	connect {
		val SQL = """SELECT S.* from `Skill` S JOIN 'User_Skill' US ON S.skillId=US.skillId where US.`userId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Skill>()
	} ?: emptyList()

fun addUserSkill(userId: Int, skillId: Int, level: Int): Boolean =
	connect {
		val SQL = """INSERT INTO `User_Skill` (`userId`, `skillId`, `level`, `time`) VALUES (?,?,?,?);"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, skillId)
		statement.setInt(3, level)
		statement.setLong(4, System.currentTimeMillis())
		statement.executeUpdate() > 0
	} == true

fun removeUserSkill(userId: Int, skillId: Int): Boolean =
	connect {
		val SQL = """DELETE FROM `User_Skill` WHERE `userId`=? AND `skillId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, skillId)
		statement.executeUpdate() > 0
	} == true

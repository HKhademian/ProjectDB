@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.SkillEndorse

fun listSkillEndorsedByUser(byUserId: Int): List<SkillEndorse> =
	connect {
		val SQL = """SELECT * from `Skill_Endorse` WHERE `by_userId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, byUserId)
		statement.executeQuery()
			.listOf<SkillEndorse>()
	} ?: emptyList()

fun listSkillEndorseOnUser(userId: Int): List<SkillEndorse> =
	connect {
		val SQL = """SELECT * from `Skill_Endorse` WHERE `userId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.listOf<SkillEndorse>()
	} ?: emptyList()

fun addSkillEndorse(byUserId: Int, userId: Int, skillId: Int): SkillEndorse? =
	connect {
		val SQL = """INSERT INTO `Skill_Endorse` (`by_userId`, `userId`, `skillId`, `time`, `notified`)
			VALUES (?,?,?,?,0) RETURNING *;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, byUserId)
		statement.setInt(2, userId)
		statement.setInt(3, skillId)
		statement.setLong(4, System.currentTimeMillis())
		statement.executeQuery()
			.singleOf<SkillEndorse>()
	}

fun removeSkillEndorse(byUserId: Int, userId: Int, skillId: Int): SkillEndorse? =
	connect {
		val SQL = """DELETE FROM `Skill_Endorse` WHERE `by_userId`=? AND `userId`=? AND `skillId`=? RETURNING *;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, byUserId)
		statement.setInt(2, userId)
		statement.setInt(3, skillId)
		statement.executeQuery()
			.singleOf<SkillEndorse>()
	}

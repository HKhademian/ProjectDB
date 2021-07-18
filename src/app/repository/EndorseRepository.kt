@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.SkillEndorse

fun listEndorseFromUser(fromUserId: Int): List<SkillEndorse> =
	connect {
		val SQL = """
			SELECT * from Skill_Endorse WHERE by_userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, fromUserId)
		statement.executeQuery()
			.listOf<SkillEndorse>()
	} ?: emptyList()

fun listEndorseToUserSkill(userId: Int, skillId: Int): List<SkillEndorse> =
	connect {
		val SQL = """
			SELECT * from Skill_Endorse WHERE userId=? ${if (skillId > 0) "AND skillId=?" else ""};
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		if (skillId > 0)
			statement.setInt(2, skillId)
		statement.executeQuery()
			.listOf<SkillEndorse>()
	} ?: emptyList()

fun addSkillEndorse(fromUserId: Int, userId: Int, skillId: Int): SkillEndorse? =
	connect {
		val SQL = """
			INSERT INTO Skill_Endorse (by_userId, userId, skillId, time, notified)
			VALUES (?,?,?,?,0) RETURNING *;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, fromUserId)
		statement.setInt(2, userId)
		statement.setInt(3, skillId)
		statement.setLong(4, System.currentTimeMillis())
		statement.executeQuery()
			.singleOf<SkillEndorse>()
	}

fun removeSkillEndorse(fromUserId: Int, userId: Int, skillId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Skill_Endorse WHERE by_userId=? AND userId=? AND skillId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, fromUserId)
		statement.setInt(2, userId)
		statement.setInt(3, skillId)
		statement.executeUpdate() > 0
	} == true

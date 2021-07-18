@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Skill
import app.model.SkillEndorse

fun listSkills(): List<Skill> =
	connect {
		val SQL = """
			SELECT * from Skill;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.executeQuery()
			.listOf<Skill>()
	} ?: emptyList()

fun getSkillById(skillId: Int): Skill? =
	connect {
		val SQL = """
			SELECT * from Skill where skillId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, skillId)
		statement.executeQuery()
			.singleOf<Skill>()
	}

fun addSkill(title: String?): Skill? =
	connect {
		val SQL = """
			INSERT INTO Skill (skillId, title) VALUES (null,?) RETURNING *;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setString(1, title)
		statement.executeQuery()
			.singleOf<Skill>()
	}

fun removeSkill(skillId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Skill WHERE skillId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, skillId)
		statement.executeUpdate() > 0
	} == true


fun listUserSkills(userId: Int): List<Skill> =
	connect {
		val SQL = """
			SELECT S.* from Skill S JOIN 'User_Skill' US ON S.skillId=US.skillId where US.userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.listOf<Skill>()
	} ?: emptyList()


fun addUserSkill(userId: Int, skillId: Int, level: Int): Boolean =
	connect {
		val SQL = """
			INSERT INTO User_Skill (userId, skillId, level, time) VALUES (?,?,?,?);
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, skillId)
		statement.setInt(3, level)
		statement.setLong(4, System.currentTimeMillis())
		statement.executeUpdate() > 0
	} == true

fun removeUserSkill(userId: Int, skillId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM User_Skill WHERE userId=? AND skillId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, skillId)
		statement.executeUpdate() > 0
	} == true


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

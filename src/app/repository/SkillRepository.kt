package app.repository

import app.model.Skill
import java.sql.Connection

object SkillRepository {
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
			statement.executeQuery()
				.tryRead<Skill>()
		}
	}

	@JvmStatic
	fun addSkill(title: String?): Skill? {
		val SQL = "INSERT INTO `Skill` VALUES (null,?) RETURNING *;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setString(1, title)
			statement.executeQuery()
				.tryRead<Skill>()
		}
	}
}

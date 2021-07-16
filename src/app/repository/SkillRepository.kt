package app.repository

import app.model.Skill
import java.sql.Connection
import java.util.ArrayList

object SkillRepository {
	fun listSkills(): List<Skill> {
		val SQL = "SELECT * from `Skill`;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			val res = statement.executeQuery()
			val result: MutableList<Skill> = ArrayList()
			while (res.next()) result.add(Skill.from(res))
			result
		}!!
	}

	fun getSkill(skillId: Int): Skill? {
		val SQL = "SELECT * from `Skill` where `skillId`=?;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			val res = statement.executeQuery()
			if (res.next()) Skill.from(res) else null
		}
	}

	@JvmStatic
	fun addSkill(title: String?): Skill? {
		val SQL = "INSERT INTO `Skill` VALUES (null,?) RETURNING *;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setString(1, title)
			val res = statement.executeQuery()
			if (res.next()) Skill.from(res) else null
		}
	}
}

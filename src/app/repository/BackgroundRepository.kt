package app.repository

import app.model.Background
import java.sql.Connection
import java.sql.Types

object BackgroundRepository {

	@JvmStatic
	fun saveBG(bg: Background): Background? {
		val SQL = """
		INSERT OR REPLACE INTO `User_Background`
		(`bgId`, `userId`, `type`, `title`, `fromTime`, `toTime`)
		VALUES (?,?,?,?,?,?) RETURNING *;
		"""

		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			if (bg.bgId > 0)
				statement.setInt(1, bg.bgId)
			else
				statement.setNull(1, Types.INTEGER)
			statement.setInt(2, bg.userId)
			statement.setInt(3, bg.bgType.ordinal)
			statement.setLong(4, bg.fromTime.time)
			statement.setString(5, bg.title)
			if (bg.toTime?.time ?: 0 > 0)
				statement.setLong(6, bg.fromTime.time)
			else
				statement.setNull(6, Types.INTEGER)
			statement.executeQuery()?.let { if (it.next()) Background.from(it) else null }
		}
	}

	@JvmStatic
	fun deleteBG(bgId: Int): Background? {
		val SQL = """DELETE FROM `User_Background` WHERE `bgId`=? RETURNING *;"""

		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setInt(1, bgId)
			statement.executeQuery()?.let { if (it.next()) Background.from(it) else null }
		}
	}


}

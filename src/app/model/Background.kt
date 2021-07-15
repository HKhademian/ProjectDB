package app.model

import java.sql.ResultSet
import java.util.*

enum class BgType {
	Work, Study, Research,
}

data class Background(
	var bgId: Int,
	var userId: Int,
	var title: String,
	var bgType: BgType,
	var fromTime: Date,
	var toTime: Date?,
) {

	companion object {

		@JvmStatic
		fun from(res: ResultSet): Background {
			return Background(
				res.tryInt("bgId") ?: 0,
				res.tryInt("userId") ?: 0,
				res.tryString("title") ?: "",
				BgType.values()[res.tryInt("type") ?: 0],
				res.tryDate("fromTime") ?: Date(0),
				res.tryDate("toTime"),
			)
		}
	}

}

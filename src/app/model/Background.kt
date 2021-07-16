package app.model

import java.util.Date

data class Background(
	var bgId: Int,
	var userId: Int,
	var title: String,
	var bgType: BgType,
	var fromTime: Date,
	var toTime: Date?,
) {
	enum class BgType {
		Work, Study, Research,
	}
}

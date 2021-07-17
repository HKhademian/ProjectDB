package app.model

import java.util.Date

data class User(
	var userId: Int,
	var username: String,
	var firstname: String?,
	var lastname: String?,
	var intro: String?,
	var about: String?,
	var birthday: Date?,
	var location: String?,
) {
	var avatar: ByteArray? = null
}

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

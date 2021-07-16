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

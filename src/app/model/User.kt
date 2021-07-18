package app.model

import java.util.Date

enum class NetworkType {
	MayKnow, Requested, Invited, SameSkill, SameLocation,
}

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

	// for each network
	var mutualCount: Int = 0
	var networkType: NetworkType = NetworkType.MayKnow
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

class Invitation(
	var fromUserId: Int,
	var toUserId: Int,
	var time: Date,
	var message: String,
	var status: Int,
)

package app.model

import java.util.Date

class Invitation(
	var userId: Int,
	var fromUserId: Int,
	var time: Date,
	var message: String,
	var status: Int,
)

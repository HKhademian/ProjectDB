package app.model

import java.util.Date

data class SkillEndorse(
	var byUserId: Int,
	var userId: Int,
	var skillId: Int,
	var time: Date,
)

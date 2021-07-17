package app.model

import java.util.*

data class Skill(
  var skillId: Int,
  var title: String,
)

data class SkillEndorse(
	var byUserId: Int,
	var userId: Int,
	var skillId: Int,
	var time: Date,
)

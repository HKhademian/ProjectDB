package app.model

import java.util.Date

sealed class Notification private constructor() {
	open var byUserId: Int = 0
	open var time: Date = Date(0)

	data class BirthdayNotification(
		override var byUserId: Int,
		override var time: Date,
	) : Notification()

	data class ProfileVisitNotification(
		override var byUserId: Int,
		override var time: Date,
	) : Notification()

	data class LikeArticleNotification(
		override var byUserId: Int,
		override var time: Date,
		var articleId: Int,
	) : Notification()

	data class LikeCommentNotification(
		override var byUserId: Int,
		override var time: Date,
		var commentId: Int,
	) : Notification()

	data class CommentNotification(
		override var byUserId: Int,
		override var time: Date,
		var commentId: Int,
	) : Notification()

	data class ReplyCommentNotification(
		override var byUserId: Int,
		override var time: Date,
		var commentId: Int,
	) : Notification()

	data class SkillEndorseNotification(
		override var byUserId: Int,
		override var time: Date,
		var skillId: Int,
	) : Notification()
}

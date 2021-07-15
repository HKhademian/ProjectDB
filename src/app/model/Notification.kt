package app.model

import java.sql.ResultSet
import java.util.Date

sealed class Notification private constructor(
	var byUserId: Int,
	var time: Date?,
) {

	companion object {
		@JvmStatic
		fun from(res: ResultSet): Notification {
			val byUserId = res.tryInt("by_userId") ?: 0
			val time = res.tryDate("time")
			val targetId = res.tryInt("targetId") ?: 0

			return when (res.tryString("event")) {
				"birth" -> BirthdayNotification(byUserId, time)
				"visit" -> ProfileVisitNotification(byUserId, time)
				"like_article" -> LikeArticleNotification(byUserId, time, targetId)
				"like_comment" -> LikeCommentNotification(byUserId, time, targetId)
				"comment" -> CommentNotification(byUserId, time, targetId)
				"reply" -> ReplyCommentNotification(byUserId, time, targetId)
				"endorse" -> SkillEndorseNotification(byUserId, time, targetId)
				else -> throw RuntimeException("must not happened")
			}
		}
	}

}

class BirthdayNotification(
	byUserId: Int,
	time: Date?,
) : Notification(byUserId, time)

class ProfileVisitNotification(
	byUserId: Int,
	time: Date?,
) : Notification(byUserId, time)

class LikeArticleNotification(
	byUserId: Int,
	time: Date?,
	var articleId: Int,
) : Notification(byUserId, time)

class LikeCommentNotification(
	byUserId: Int,
	time: Date?,
	var commentId: Int,
) : Notification(byUserId, time)

class CommentNotification(
	byUserId: Int,
	time: Date?,
	var commentId: Int,
) : Notification(byUserId, time)

class ReplyCommentNotification(
	byUserId: Int,
	time: Date?,
	var commentId: Int,
) : Notification(byUserId, time)

class SkillEndorseNotification(
	byUserId: Int,
	time: Date?,
	var skillId: Int,
) : Notification(byUserId, time)

package app.model

import java.util.Date

sealed class Notification private constructor() {
    var byUserId = 0
    var time: Date? = null
}

class BirthdayNotification : Notification()

class LikeArticleNotification : Notification() {
    var articleId = 0
}

class LikeCommentNotification : Notification() {
    var commentId = 0
}

class CommentNotification : Notification() {
    var commentId = 0
}

class ReplyCommentNotification : Notification() {
    var commentId = 0
}

class SkillEndorseNotification : Notification() {
    var skillId = 0
}

class ProfileVisitNotification : Notification()

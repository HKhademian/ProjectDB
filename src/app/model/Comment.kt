package app.model

import java.util.Date

data class Comment(
	var commentId: Int,
	var articleId: Int,
	var replyCommentId: Int,
	var userId: Int,
	var content: String,
	var time: Date,
) {
	var likeCount: Int = 0
	var replyCount: Int = 0
	// for each home user
	var home_isLiked: Boolean = false
}

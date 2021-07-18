@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Comment

fun getCommentById(commentId: Int): Comment? =
	connect {
		val SQL = """SELECT * from `CommentCounted` where `commentId`=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, commentId)
		statement.executeQuery()
			.singleOf<Comment>()
	}

/**
 * returns all comment and replies on this articleId
 */
fun listComments(articleId: Int): List<Comment> =
	connect {
		val SQL = """SELECT * from `CommentCounted` where `articleId`=?; --and `reply_commentId` is null"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, articleId)
		statement.executeQuery()
			.listOf<Comment>()
	} ?: emptyList()

fun deleteComment(commentId: Int): Comment? =
	connect {
		val SQL = """DELETE FROM `Comment` WHERE `commentId`=? RETURNING *;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, commentId)
		statement.executeQuery()
			.singleOf<Comment>()
	}

/**
 * add comment to an [app.model.Article] or reply on another [app.model.Comment] (if replyCommentId>0)
 */
fun sendCommentOn(userId: Int, articleId: Int, replyCommentId: Int, content: String): Comment? =
	connect {
		val SQL_C = """INSERT INTO `Comment`
			(`userId`, `content`, `time`, `notified`, `articleId`, `reply_commentId`)
			VALUES (?,?,?,0,?,null) RETURNING *;"""
		val SQL_R = """INSERT INTO `Comment`
			(`userId`, `content`, `time`, `notified`, `articleId`, `reply_commentId`)
			SELECT ?,?,?,0,`articleId`,`commentId` from `Comment` where `commentId`=? RETURNING *;"""
		val statement = if (replyCommentId > 0)
			it.prepareStatement(SQL_R)
		else it.prepareStatement(SQL_C)
		statement.setInt(1, userId)
		statement.setString(2, content)
		statement.setLong(3, System.currentTimeMillis())
		statement.setInt(4, if (replyCommentId > 0) replyCommentId else articleId)
		statement.executeQuery()
			.singleOf<Comment>()
	}

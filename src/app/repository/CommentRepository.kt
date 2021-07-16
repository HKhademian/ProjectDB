package app.repository

import app.model.Comment
import java.sql.Connection
import java.util.ArrayList

object CommentRepository {
	@JvmStatic
	fun getComment(commentId: Int): Comment? {
		val SQL = "SELECT * from `CommentCounted` where `commentId`=?;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setInt(1, commentId)
			val res = statement.executeQuery()
			if (res.next()) Comment.from(res) else null
		}
	}

	/**
	 * returns all comment and replies on this articleId
	 */
	@JvmStatic
	fun listComments(articleId: Int): List<Comment> {
		val SQL = "SELECT * from `CommentCounted` where `articleId`=?; --and `reply_commentId` is null"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setInt(1, articleId)
			val res = statement.executeQuery()
			val result: MutableList<Comment> = ArrayList()
			while (res.next()) result.add(Comment.from(res))
			result
		}!!
	}

	@JvmStatic
	fun deleteComment(commentId: Int): Comment? {
		val SQL = "DELETE FROM `Comment` WHERE `commentId`=? RETURNING *;"
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(SQL)
			statement.setInt(1, commentId)
			val res = statement.executeQuery()
			if (res.next()) Comment.from(res) else null
		}
	}

	/**
	 * add comment to an [app.model.Article] or reply on another [app.model.Comment] (if replyCommentId>0)
	 */
	@JvmStatic
	fun commentOn(userId: Int, articleId: Int, replyCommentId: Int, content: String?): Comment? {
		val SQL_C = """INSERT INTO `Comment`
			(userId, content, time, notified, articleId, reply_commentId)
			VALUES (?,?,?,0,?,null) RETURNING *;"""
		val SQL_R = """INSERT INTO `Comment`
			(userId, content, time, notified, articleId, reply_commentId)
			SELECT ?,?,?,0,articleId,commentId from Comment where commentId=? RETURNING *;"""
		return connect { connection: Connection ->
			val statement = connection.prepareStatement(if (replyCommentId > 0) SQL_R else SQL_C)
			statement.setInt(1, userId)
			statement.setString(2, content)
			statement.setLong(3, System.currentTimeMillis())
			statement.setInt(4, if (replyCommentId > 0) replyCommentId else articleId)
			val res = statement.executeQuery()
			if (res.next()) Comment.from(res) else null
		}
	}
}

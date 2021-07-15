package app.model

import java.sql.ResultSet
import java.util.Date

data class Comment(
  var commentId: Int,
  var articleId: Int,
  var replyCommentId: Int,
  var userId: Int,
  var content: String,
  var time: Date,
  var likeCount: Int,
  var replyCount: Int,
) {

  companion object {
    @JvmStatic
    fun from(res: ResultSet): Comment {
      return Comment(
        res.tryInt("commentId") ?: 0,
        res.tryInt("articleId") ?: 0,
        res.tryInt("replyCommentId") ?: 0,
        res.tryInt("userId") ?: 0,
        res.tryString("content") ?: "",
        res.tryDate("time") ?: Date(0),
        res.tryInt("like_count") ?: -1,
        res.tryInt("reply_count") ?: -1,
      )
    }
  }

}

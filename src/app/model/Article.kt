package app.model

import java.sql.ResultSet
import java.util.Date

data class Article(
  var articleId: Int,
  var writerUserId: Int,
  var title: String,
  var content: String,
  var time: Date,
  var featured: Boolean,
  var likeCount: Int,
  var commentCount: Int,
) {

  companion object {
    @JvmStatic
    fun from(res: ResultSet): Article {
      return Article(
        res.tryInt("articleId") ?: 0,
        res.tryInt("writeUserId") ?: 0,
        res.tryString("title") ?: "",
        res.tryString("content") ?: "",
        res.tryDate("time") ?: Date(0),
        (res.tryInt("featured") ?: 0) != 0,
        res.tryInt("like_count") ?: 0,
        res.tryInt("comment_count") ?: 0,
      )
    }
  }

}

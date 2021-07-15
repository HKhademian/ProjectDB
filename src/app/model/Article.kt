package app.model

import app.model._BaseModel
import java.sql.ResultSet
import app.model.Article
import java.util.*

data class Article(
  var articleId: Int,
  var writerUserId: Int,
  var title: String,
  var content: String,
  var time: Date,
  var featured: Boolean,
  var likeCount: Int,
  var commentCount: Int,
) : _BaseModel() {
  companion object {
    @JvmStatic
    fun from(res: ResultSet?): Article {
      return Article(
        tryInt(res, "articleId", 0),
        tryInt(res, "writeUserId", 0),
        tryString(res, "title", ""),
        tryString(res, "content", ""),
        Date(tryLong(res, "time", 0)),
        tryInt(res, "featured", 0) != 0,
        tryInt(res, "like_count", 0),
        tryInt(res, "comment_count", 0)
      )
    }
  }
}

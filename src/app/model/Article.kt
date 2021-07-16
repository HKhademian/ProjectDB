package app.model

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
)

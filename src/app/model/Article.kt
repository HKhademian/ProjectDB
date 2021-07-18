package app.model

import java.util.Date

data class Article
@JvmOverloads
constructor(
	var articleId: Int,
	var writerUserId: Int,
	var title: String,
	var content: String,
	var time: Date = Date(),
	var featured: Boolean = false,
	var likeCount: Int = 0,
	var commentCount: Int = 0,
	// for each user
	var homeUserId: Int = 0,
	var homeTime: Date = Date(0),
	var homeCount: Int = 0,
)

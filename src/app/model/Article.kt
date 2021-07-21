package app.model

import java.util.Date

data class Article(
	var articleId: Int,
	var writerUserId: Int,
	var title: String,
	var content: String,
	var featured: Boolean,
	var time: Date = Date(),
) {
	// for each article
	var likeCount: Int = 0
	var commentCount: Int = 0

	// for each user is different
	var homeUserId: Int = 0
	var homeTime: Date = Date(0)
	var homeCount: Int = 0
	var isHomeUserLiked: Boolean = false
}

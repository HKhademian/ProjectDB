package app.model

import java.util.*

data class HomeArticle(
	var homeUserId: Int,
	var homeTime: Date,
	var homeCount: Int,
	var article: Article,
)

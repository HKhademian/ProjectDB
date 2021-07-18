package app.repository

import app.model.Article
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ArticleRepositoryTest {

	@Test
	fun `test saveArticle`() {
		println(
			saveArticle(
				Article(
					articleId = 0,
					writerUserId = 1,
					title = "test article",
					content = "ok article",
					time = Date(),
					featured = true,
				)
			)
		)
	}
}

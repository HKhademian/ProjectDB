package app.repository

import app.BaseTest
import app.model.Article
import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ArticleRepositoryTest : BaseTest() {

	@Test
	fun `01- test getArticle exists user1`() {
		println(getArticle(1, 12))
	}

	@Test
	fun `02- test getArticle exists user2`() {
		println(getArticle(2, 12))
	}

	@Test
	fun `03- test getArticle exists user-non`() {
		println(getArticle(0, 12))
	}

	@Test
	fun `04- test getArticle non-exists`() {
		println(getArticle(1, 0))
	}

	@Test
	fun `05- test getUserArticles user1`() {
		println(getUserArticles(1))
	}

	@Test
	fun `06- test getUserArticles user2`() {
		println(getUserArticles(2))
	}

	@Test
	fun `07- test getUserArticles user-non`() {
		assertEquals(getUserArticles(0).size, 0)
	}

	@Test
	fun `08- test getUserHomeArticles user1`() {
		println(getUserHomeArticles(1))
	}

	@Test
	fun `09- test getUserHomeArticles user2`() {
		println(getUserHomeArticles(2))
	}

	@Test
	fun `10- test getUserHomeArticles user-non`() {
		assertEquals(getUserHomeArticles(0).size, 0)
	}

	@Test
	fun `11- test saveArticle new`() {
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

	@Test
	fun `12- test saveArticle edit`() {
		println(
			saveArticle(
				Article(
					articleId = 16,
					writerUserId = 1,
					title = "test article",
					content = "ok article",
					time = Date(),
					featured = true,
				)
			)
		)
	}

	@Test
	fun `13- test deleteArticle exists`() {
		assertTrue(deleteArticle(16))
	}

	@Test
	fun `14- test deleteArticle non-exists`() {
		assertFalse(deleteArticle(0))
	}
}

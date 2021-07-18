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
	fun `01- test getUserArticle exists user1`() {
		println(getUserArticle(1, 12))
	}

	@Test
	fun `02- test getUserArticle exists user2`() {
		println(getUserArticle(2, 12))
	}

	@Test
	fun `03- test getUserArticle exists user-non`() {
		println(getUserArticle(0, 12))
	}

	@Test
	fun `04- test getUserArticle non-exists`() {
		println(getUserArticle(1, 0))
	}

	@Test
	fun `11- test listUserArticles user1`() {
		println(listUserArticles(1))
	}

	@Test
	fun `12- test listUserArticles user2`() {
		println(listUserArticles(2))
	}

	@Test
	fun `13- test listUserArticles user-non`() {
		println(listUserArticles(0))
	}

	@Test
	fun `21- test listHomeUserArticles user1`() {
		println(listHomeUserArticles(1))
	}

	@Test
	fun `22- test getUserHomeArticles user2`() {
		println(listHomeUserArticles(2))
	}

	@Test
	fun `23- test getUserHomeArticles user-non`() {
		println(listHomeUserArticles(0))
	}

	@Test
	fun `31- test saveArticle new`() {
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
	fun `32- test saveArticle edit nonwriter`() {
		println(
			saveArticle(
				Article(
					articleId = 18,
					writerUserId = 7,
					title = "test article edited",
					content = "ok article edited",
					time = Date(),
					featured = true,
				)
			)
		)
	}


	@Test
	fun `33- test saveArticle edit writer`() {
		println(
			saveArticle(
				Article(
					articleId = 18,
					writerUserId = 1,
					title = "test article edited",
					content = "ok article edited",
					time = Date(),
					featured = true,
				)
			)
		)
	}

	@Test
	fun `41- test deleteArticle exists nonwriter`() {
		println(getUserArticle(2, 18))
		println(deleteArticle(2, 18))
		println(getUserArticle(2, 18))
	}

	@Test
	fun `42- test deleteArticle exists writer`() {
		println(getUserArticle(1, 18))
		println(deleteArticle(1, 18))
		println(getUserArticle(1, 18))
	}

	@Test
	fun `43- test deleteArticle non-exists`() {
		println(getUserArticle(1, 100))
		println(deleteArticle(1, 100))
		println(getUserArticle(1, 100))
	}
}

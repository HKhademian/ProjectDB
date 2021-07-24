package app.repository

import app.BaseTest
import app.model.User
import org.junit.Test

import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserRepositoryTest : BaseTest() {

	@Test
	fun `01- test suggestBackground`() {
		val res = suggestBackground()
		println(res)
	}

	@Test
	fun `02- test suggestLocation`() {
		val res = suggestLocation()
		println(res)
	}

	@Test
	fun `11- test getUserById user-1 no-visitor`() {
		val res = getUserById(-1, 1)
		println(res)
	}

	@Test
	fun `12- test getUserById user-1 same-visitor`() {
		val res = getUserById(1, 1)
		println(res)
	}

	@Test
	fun `13- test getUserById user-1 visitor-2`() {
		val res = getUserById(2, 1)
		println(res)
	}

	@Test
	fun `14- test getUserById user-non`() {
		val res = getUserById(2, 0)
		println(res)
	}


	@Test
	fun `21- test loginUser correct`() {
		val res = loginUser("admin", "admin")
		println(res)
	}

	@Test
	fun `22- test loginUser wrong-user`() {
		val res = loginUser("admins", "admin")
		println(res)
	}

	@Test
	fun `23- test loginUser wrong-pass`() {
		val res = loginUser("admin", "admins")
		println(res)
	}


	@Test
	fun `31- test registerUser exists`() {
		val res = registerUser(
			User(
				-1, "admin",
				"Admin", "Adminestan",
				"Salute", "Just to happy",
				null, "Shiraz",
			),
			"admin"
		)
		println(res)
	}

	@Test
	fun `32- test registerUser exists`() {
		val res = registerUser(
			User(
				-1, "adminX",
				"Admin", "Adminestan",
				"Salute", "Just to happy",
				null, "Shiraz",
			),
			"admin"
		)
		println(res)
	}

	@Test
	fun `33- test registerUser correct`() {
		val res = registerUser(
			User(
				-1, "tom",
				"Tom", "TomPoor",
				null, null,
				Date(1980, 1, 5), "NewYork",
			),
			"tomtom"
		)
		println(res)
	}


	@Test
	fun `41- test toggleLike article`() {
		println(connect {
			it.createStatement()
				.executeQuery("SELECT (userId||':'||articleId||':'||COALESCE(commentId, '')) from User_Like;")
				.listOf<String>()
		})
		println(
			getUserArticle(1, 10)?.isLiked
		)

		val res1 = toggleUserLike(1, 10, -1)
		println(res1)

		println(connect {
			it.createStatement()
				.executeQuery("SELECT (userId||':'||articleId||':'||COALESCE(commentId, '')) from User_Like;")
				.listOf<String>()
		})
		println(
			getUserArticle(1, 10)?.isLiked
		)

		val res2 = toggleUserLike(1, 10, -1)
		println(res2)

		println(connect {
			it.createStatement()
				.executeQuery("SELECT (userId||':'||articleId||':'||COALESCE(commentId, '')) from User_Like;")
				.listOf<String>()
		})
		println(
			getUserArticle(1, 10)?.isLiked
		)
	}

	@Test
	fun `42- test toggleLike comment`() {
		println(connect {
			it.createStatement()
				.executeQuery("SELECT (userId||':'||articleId||':'||COALESCE(commentId, '')) from User_Like;")
				.listOf<String>()
		})
		println(
			getCommentById(1, 3)?.home_isLiked
		)

		val res1 = toggleUserLike(1, 10, 3)
		println(res1)

		println(connect {
			it.createStatement()
				.executeQuery("SELECT (userId||':'||articleId||':'||COALESCE(commentId, '')) from User_Like;")
				.listOf<String>()
		})
		println(
			getCommentById(1, 3)?.home_isLiked
		)

		val res2 = toggleUserLike(1, 10, 3)
		println(res2)

		println(connect {
			it.createStatement()
				.executeQuery("SELECT (userId||':'||articleId||':'||COALESCE(commentId, '')) from User_Like;")
				.listOf<String>()
		})
		println(
			getCommentById(1, 3)?.home_isLiked
		)
	}


}

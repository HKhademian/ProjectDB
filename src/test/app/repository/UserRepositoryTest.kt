package app.repository

import org.junit.Test

import org.junit.Assert.*

class UserRepositoryTest {

	@Test
	fun `test suggestBackground`() {
		val res = suggestBackground()
		println(res)
	}

	@Test
	fun `test suggestLocation`() {
		val res = suggestLocation()
		println(res)
	}
}

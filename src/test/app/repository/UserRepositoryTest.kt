package app.repository

import app.BaseTest
import org.junit.Test

import org.junit.Assert.*

class UserRepositoryTest : BaseTest() {

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

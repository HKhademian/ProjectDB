package app.repository

import app.BaseTest
import app.model.User
import org.junit.Test

import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class NetworkRepositoryTest : BaseTest() {

	@Test
	fun `41- test searchProfiles name`() {
		val res = searchProfiles("ali", null, null, null, null)
		println(res)
	}

	@Test
	fun `42- test searchProfiles location`() {
		val res = searchProfiles(null, "shiraz", null, null, null)
		println(res)
	}

	@Test
	fun `43- test searchProfiles skill`() {
		val res = searchProfiles(null, null, 1, null, null)
		println(res)
	}

	@Test
	fun `44- test searchProfiles lang`() {
		val res = searchProfiles(null, null, null, "fa", null)
		println(res)
	}

	@Test
	fun `45- test searchProfiles background`() {
		val res = searchProfiles(null, null, null, null, "Google")
		println(res)
	}

	@Test
	fun `46- test searchProfiles skill+location`() {
		val res = searchProfiles(null, "iran", 1, null, null)
		println(res)
	}
}

package app.repository

import app.BaseTest

import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ChatRepositoryTest : BaseTest() {

	@Test
	fun `01- test createChat user-1`() {
		println(createChat(1, "my test chat 2"))
	}

	@Test
	fun `02- test createChat user-2`() {
		println(createChat(2, "my test chat 3"))
	}

	@Test
	fun `03- test createChat user-non`() {
		println(createChat(0, "my test chat 4"))
	}


	@Test
	fun `11- test getUserChat chat-2 user-1`() {
		println(getUserChat(2, 1))
	}

	@Test
	fun `12- test getUserChat chat-2 user-2`() {
		println(getUserChat(2, 2))
	}

	@Test
	fun `13- test getUserChat chat-2 user-non`() {
		println(getUserChat(2, 0))
	}

	@Test
	fun `14- test getUserChat chat-non`() {
		println(getUserChat(0, 2))
	}

	@Test
	fun `15- test addChatUser chat-2 user1`() {
		println(addChatUser(2, 1, false))
	}

	@Test
	fun `16- test addChatUser chat-2 user-2`() {
		println(addChatUser(2, 2, false))
	}

	@Test
	fun `17- test addChatUser chat-2 user-5`() {
		println(addChatUser(2, 5, false))
	}

	@Test
	fun `18- test getUserChat chat-2 user-2`() {
		println(getUserChat(2, 2))
	}

	@Test
	fun `19- test getUserChat chat-2 user-non`() {
		println(getUserChat(2, 0))
	}


	@Test
	fun `21- test updateChat chat-2 user-1`() {
		println(updateChat(2, 1, "chat2 1-virus"))
		println(getUserChat(2, 1))
	}

	@Test
	fun `22- test updateChat chat-2 user-2`() {
		println(updateChat(2, 2, "chat2 2-virus"))
		println(getUserChat(2, 2))
	}

	@Test
	fun `23- test updateChat chat-2 user-5`() {
		println(updateChat(2, 5, "chat2 5-virus"))
		println(getUserChat(2, 5))
	}

	@Test
	fun `24- test updateChat chat-2 user-non`() {
		println(updateChat(2, 0, "chat2 non-virus"))
		println(getUserChat(2, 0))
	}


	@Test
	fun `31- test updateChat chat-2 user-2`() {
		println(getUserChat(2, 2))
		println(updateChat(2, 2, isArchived = true, isMuted = true))
		println(getUserChat(2, 2))
	}

	@Test
	fun `31- test updateChat chat-2 user-4`() {
		println(getUserChat(2, 4))
		println(updateChat(2, 4, isArchived = true, isMuted = true))
		println(getUserChat(2, 4))
	}


	@Test
	fun `41- test deleteChat chat-3 user-5`() {
		println(getUserChat(3, 5))
		println(deleteChat(3, 5))
		println(getUserChat(3, 5))
	}

	@Test
	fun `42- test deleteChat chat-3 user-2`() {
		println(getUserChat(3, 2))
		println(deleteChat(3, 2))
		println(getUserChat(3, 2))
	}


	@Test
	fun `51- test listUserChats user-1`() {
		println(listUserChats(1))
	}

	@Test
	fun `52- test listUserChats user-2`() {
		println(listUserChats(2))
	}

	@Test
	fun `53- test listUserChats user-3`() {
		println(listUserChats(3))
	}

	@Test
	fun `54- test listUserChats user-5`() {
		println(listUserChats(5))
	}

	@Test
	fun `55- test listUserChats user-non`() {
		println(listUserChats(0))
	}


}

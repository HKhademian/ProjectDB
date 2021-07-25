@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Chat
import app.model.Message
import app.model.User

/** get chat detail for given UserId */
fun getUserChat(chatId: Int, userId: Int): Chat? =
	connect {
		val SQL = """
			SELECT * FROM UserChat WHERE chatId=? AND userId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.setInt(2, userId)
		stmt.executeQuery()
			.singleOf<Chat>()
	}

/** List all chat details for given UserId  */
fun listUserChats(userId: Int): List<Chat> =
	connect {
		val SQL = """
			SELECT * FROM UserChat WHERE userId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.executeQuery()
			.listOf<Chat>()
	} ?: emptyList()


/** List all user participant in a chat */
fun listChatUsers(chatId: Int): List<User> =
	connect {
		val SQL = """
			SELECT * FROM User
			WHERE userId in (SELECT C_U.userId from Chat_User C_U where chatId=?);
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.executeQuery()
			.listOf<User>()
	} ?: emptyList()

/** add new user to chat by its userId */
fun addChatUser(chatId: Int, userId: Int, isAdmin: Boolean): Boolean =
	connect {
		val SQL = """
			INSERT INTO Chat_User(chatId, userId, joinTime, isAdmin) VALUES(?,?,?,?);
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.setInt(2, userId)
		stmt.setLong(3, System.currentTimeMillis())
		stmt.setInt(4, if (isAdmin) 1 else 0)
		stmt.executeUpdate() > 0
	} == true


/** create new chat group and set given userId as admin */
fun createChat(userId: Int, title: String): Chat? {
	val chatId = connect {
		val SQL = """
			INSERT INTO Chat (chatId, time, title) VALUES (NULL,?,?) RETURNING chatId;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setLong(1, System.currentTimeMillis())
		stmt.setString(2, title)
		stmt.executeQuery()
			.singleOf<Int>()
	} ?: return null

	val res = addChatUser(chatId, userId, true)

	return if (!res) null else getUserChat(chatId, userId)
}

/** update chat detail for given UserId */
fun updateChat(chatId: Int, userId: Int, isArchived: Boolean, isMuted: Boolean): Boolean =
	connect {
		val SQL = """
			UPDATE Chat_User SET isArchived=?,isMuted=? WHERE chatId=? AND userId=?;
		""".trimIndent()

		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, if (isArchived) 1 else 0)
		stmt.setInt(2, if (isMuted) 1 else 0)
		stmt.setInt(3, chatId)
		stmt.setInt(4, userId)
		stmt.executeUpdate() > 0
	} == true

/** update chat detail (admin only) for all */
fun updateChat(chatId: Int, userId: Int, title: String): Boolean =
	connect {
		val SQL = """
			UPDATE Chat SET title=?
			WHERE chatId IN (
				SELECT chatId FROM Chat_User CU WHERE CU.isAdmin=1 AND CU.chatId=? AND CU.userId=?
			);
		""".trimIndent()

		val stmt = it.prepareStatement(SQL)
		stmt.setString(1, title)
		stmt.setInt(2, chatId)
		stmt.setInt(3, userId)
		stmt.executeUpdate() > 0
	} == true


/** delete given chat (only admin) */
fun deleteChat(chatId: Int, userId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Chat WHERE chatId IN (
				SELECT chatId FROM Chat_User CU WHERE CU.isAdmin=1 AND CU.chatId=? AND CU.userId=?
			);
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.setInt(2, userId)
		stmt.executeUpdate() > 0
	} == true

/** get Message Detail for userId */
fun getMessage(userId: Int, messageId: Int): Message? =
	connect {
		val SQL = """
			SELECT * FROM UserMessage WHERE userId=? AND messageId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, messageId)
		stmt.executeQuery()
			.singleOf<Message>()
	}

/** list messages of a chat include user specific data */
fun listUserMessages(userId: Int, chatId: Int): List<Message> =
	connect {
		val SQL = """
			SELECT * FROM UserMessage WHERE userId=? AND chatId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, chatId)
		stmt.executeQuery()
			.listOf<Message>()
	} ?: emptyList()

///** use listUserMessages */
//fun listMessageStat(userId: Int, messageId: Int): Message? =
//	connect {
//		val SQL = """
//			SELECT * FROM Message_State WHERE userId=? AND messageId=?;
//		""".trimIndent()
//		val stmt = it.prepareStatement(SQL)
//		stmt.setInt(1, userId)
//		stmt.setInt(2, messageId)
//		stmt.executeQuery()
//			.singleOf<Message>()
//	}

/** send message to a chat (user must be participant to chat to allow send) */
fun sendMessage(chatId: Int, userId: Int, replyMessageId: Int, content: String): Message? =
	connect {
		val SQL = """
			INSERT INTO Message(messageId, chatId, sender_userId, reply_messageId, content, time)
			SELECT NULL,chatId,userId,?,?,? FROM Chat_User CU WHERE CU.chatId=? AND CU.userId=?
			RETURNING *;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, replyMessageId)
		stmt.setString(2, content)
		stmt.setLong(3, System.currentTimeMillis())
		stmt.setInt(4, chatId)
		stmt.setInt(5, userId)
		stmt.executeQuery()
			.singleOf<Message>()
	}

/** update message stat for given user */
fun statMessage(userId: Int, messageId: Int, isReceived: Boolean, isSeen: Boolean): Boolean {
	val setStat = { field: String, value: Boolean ->
		connect {
			val SQL = """
			INSERT OR REPLACE INTO Message_State (userId, messageId, $field)
			SELECT userId,messageId,?
			FROM Chat_User CU
			JOIN Message M on CU.chatId = M.chatId
			WHERE CU.userId=? AND M.messageId=?;
			values (?,?,?);
		""".trimIndent()
			val stmt = it.prepareStatement(SQL)
			stmt.setLong(1, if (!value) 0 else System.currentTimeMillis())
			stmt.setInt(2, userId)
			stmt.setInt(3, messageId)
			stmt.executeUpdate() > 0
		} == true
	}

	return setStat("receiveTime", isReceived) && setStat("seenTime", isSeen)
}

/** remove message */
fun deleteMessage(messageId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Message WHERE messageId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, messageId)
		stmt.executeUpdate() > 0
	} == true


/** list chat where have messages matching search */
fun searchChat(userId: Int, search: String): List<Chat> =
	connect {
		val SQL = """
			SELECT * FROM UserChat UC
			WHERE UC.userId=? AND UC.chatId in (SELECT chatId FROM Message M WHERE M.content like '%'||?||'%');
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setString(2, search)
		stmt.executeQuery()
			.listOf<Chat>()
	} ?: emptyList()

/** list message in a chat (or all) have matching search */
fun searchChat(userId: Int, chatId: Int, search: String): List<Message> =
	connect {
		val SQL = """
			SELECT * FROM UserMessage UM WHERE UM.userId=? AND (? OR UM.chatId=?) AND UM.content like '%'||?||'%'
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(1, if (chatId > 0) 0 else 1)
		stmt.setInt(2, chatId)
		stmt.setString(3, search)
		stmt.executeQuery()
			.listOf<Message>()
	} ?: emptyList()

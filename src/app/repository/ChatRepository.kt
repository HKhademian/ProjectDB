@file:JvmName("ChatRepository")
@file:JvmMultifileClass

package app.repository

import app.model.Chat
import app.model.Message

fun createChat(userId: Int, title: String): Chat? =
	connect {
		val SQL1 = """
			INSERT INTO Chat (chatId, time, title) VALUES (NULL, ?, ?);
		""".trimIndent()
		val SQL2 = """
			INSERT INTO Chat_User(chatId, userId, joinTime) select chatId, ?, ? from Chat where ROWID=last_insert_rowid();
		""".trimIndent()
		val SQL3 = """
			select CU.* FROM ChatUser CU
			JOIN Chat_User CU ON CU.userId = CU.userId AND CU.chatId = CU.chatId AND CU.ROWID=last_insert_rowid();
		""".trimIndent()

		val stmt1 = it.prepareStatement(SQL1)
		stmt1.setLong(1, System.currentTimeMillis())
		stmt1.setString(2, title)
		if (stmt1.executeUpdate() <= 0)
			return@connect null

		val stmt2 = it.prepareStatement(SQL2)
		stmt2.setInt(1, userId)
		stmt2.setLong(2, System.currentTimeMillis())
		if (stmt2.executeUpdate() <= 0)
			return@connect null

		val stmt3 = it.prepareStatement(SQL3)
		stmt3.executeQuery()
			.singleOf<Chat>()
	}

fun updateChat(chatId: Int, userId: Int, isArchived: Boolean, isMuted: Boolean): Chat? =
	connect {
		val SQL1 = """
			INSERT OR REPLACE INTO Chat_User(chatId, userId) VALUES (?,?);
		""".trimIndent()
		val SQL2 = """
			UPDATE Chat_User SET isArchived=?,isMuted=? WHERE ROWID=last_insert_rowid() RETURNING *;
		""".trimIndent()

		val stmt1 = it.prepareStatement(SQL1)
		stmt1.setInt(1, chatId)
		stmt1.setInt(2, userId)
		if (stmt1.executeUpdate() <= 0)
			return@connect null

		val stmt2 = it.prepareStatement(SQL2)
		stmt2.setInt(1, if (isArchived) 1 else 0)
		stmt2.setInt(2, if (isMuted) 1 else 0)
		stmt2.executeQuery()
			.singleOf<Chat>()
	}

fun updateChat(chatId: Int, userId: Int, title: String): Chat? =
	connect {
		val SQL1 = """
			UPDATE Chat SET title=? WHERE chatId=?;
		""".trimIndent()
		val SQL2 = """
			SELECT * FROM UserChat WHERE chatId=? AND userId=?;
		""".trimIndent()

		val stmt1 = it.prepareStatement(SQL1)
		stmt1.setString(1, title)
		stmt1.setInt(2, chatId)
		if (stmt1.executeUpdate() <= 0)
			return@connect null

		val stmt2 = it.prepareStatement(SQL2)
		stmt2.setInt(1, chatId)
		stmt2.setInt(2, userId)
		stmt2.executeQuery()
			.singleOf<Chat>()
	}

fun deleteChat(chatId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Chat WHERE chatId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.executeUpdate() > 0
	} == true

fun getUserChat(userId: Int, chatId: Int): Chat? =
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


fun sendMessage(chatId: Int, userId: Int, replyMessageId: Int, content: String): Message? =
	connect {
		val SQL = """
			INSERT INTO Message(messageId, chatId, sender_userId, reply_messageId, content, time)
			VALUES (NULL,?,?,?,?,?) RETURNING *;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.setInt(2, userId)
		stmt.setInt(3, replyMessageId)
		stmt.setString(4, content)
		stmt.setLong(5, System.currentTimeMillis())
		stmt.executeQuery()
			.singleOf<Message>()
	}

fun listMessageStat(userId: Int, messageId: Int): Message? =
	connect {
		val SQL = """
			SELECT * FROM Message_State WHERE userId=? AND messageId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, messageId)
		stmt.executeQuery()
			.singleOf<Message>()
	}

fun statMessage(userId: Int, messageId: Int, isReceived: Boolean, isSeen: Boolean): Boolean =
	connect {
		val SQL1 = """
			INSERT OR REPLACE INTO Message_State (userId, messageId, receiveTime) values (?,?,?);
		""".trimIndent()
		val SQL2 = """
			INSERT OR REPLACE INTO Message_State (userId, messageId, seenTime) values (?,?,?);
		""".trimIndent()

		val stmt1 = it.prepareStatement(SQL1)
		stmt1.setInt(1, userId)
		stmt1.setInt(2, messageId)
		stmt1.setLong(3, if (!isReceived) 0 else System.currentTimeMillis())
		if (stmt1.executeUpdate() <= 0)
			return@connect false

		val stmt2 = it.prepareStatement(SQL2)
		stmt2.setInt(1, userId)
		stmt2.setInt(2, messageId)
		stmt2.setLong(3, if (!isSeen) 0 else System.currentTimeMillis())
		if (stmt2.executeUpdate() <= 0)
			return@connect false

		true
	} == true

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

fun deleteMessage(messageId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Message WHERE messageId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, messageId)
		stmt.executeUpdate() > 0
	} == true


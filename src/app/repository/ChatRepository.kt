@file:JvmName("ChatRepository")
@file:JvmMultifileClass

package app.repository

import app.model.Chat
import app.model.Message

fun createChat(userId: Int, title: String): Chat? =
	connect {
		val SQL = """
			begin transaction;
			INSERT INTO Chat (chatId, time, title) VALUES (NULL, ?, ?);
			INSERT INTO Chat_User(chatId, userId, joinTime) select chatId, ?, ? from Chat where ROWID=last_insert_rowid();
			select CU.* FROM ChatUser CU
			JOIN Chat_User CU ON CU.userId = CU.userId AND CU.chatId = CU.chatId AND CU.ROWID=last_insert_rowid();
			commit;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setLong(1, System.currentTimeMillis())
		stmt.setString(2, title)
		stmt.setInt(3, userId)
		stmt.setLong(4, System.currentTimeMillis())
		stmt.executeQuery()
			.tryRead<Chat>()
	}

fun updateChat(chatId: Int, userId: Int, isArchived: Boolean, isMuted: Boolean): Chat? =
	connect {
		val SQL = """
			begin transaction;
			INSERT OR REPLACE INTO Chat_User(chatId, userId) VALUES (?,?);
			UPDATE Chat_User SET isArchived=?,isMuted=? WHERE ROWID=last_insert_rowid() RETURNING *;
			commit;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.setInt(2, userId)
		stmt.setInt(3, if (isArchived) 1 else 0)
		stmt.setInt(4, if (isMuted) 1 else 0)
		stmt.executeQuery()
			.tryRead<Chat>()
	}

fun updateChat(chatId: Int, userId: Int, title: String): Chat? =
	connect {
		val SQL = """
			begin transaction;
			UPDATE Chat SET title=? WHERE chatId=?;
			SELECT * FROM ChatUser WHERE userId=? AND chatId=?;
			commit;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setString(1, title)
		stmt.setInt(2, chatId)
		stmt.setInt(3, userId)
		stmt.setInt(4, chatId)
		stmt.executeQuery()
			.tryRead<Chat>()
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

fun getUserChats(userId: Int, chatId: Int): Chat? =
	connect {
		val SQL = """
			SELECT * FROM ChatUser WHERE userId=? AND chatId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, chatId)
		stmt.executeQuery()
			.tryRead<Chat>()
	}

fun listUserChats(userId: Int): List<Chat> =
	connect {
		val SQL = """
			SELECT * FROM ChatUser WHERE userId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.executeQuery()
			.list<Chat>()
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
			.tryRead<Message>()
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
			.tryRead<Message>()
	}

fun statMessage(userId: Int, messageId: Int, isReceived: Boolean, isSeen: Boolean): Message? =
	connect {
		val SQL = """
			begin transaction ;
			INSERT OR REPLACE INTO Message_State (userId, messageId, receiveTime) values (?,?,?);
			INSERT OR REPLACE INTO Message_State (userId, messageId, seenTime) values (?,?,?) RETURNING *;
			commit ;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, messageId)
		stmt.setLong(3, if (!isReceived) 0 else System.currentTimeMillis())
		stmt.setInt(4, userId)
		stmt.setInt(5, messageId)
		stmt.setLong(6, if (!isSeen) 0 else System.currentTimeMillis())
		stmt.executeQuery()
			.tryRead<Message>()
	}

fun listUserMessages(userId: Int, chatId: Int): List<Message> =
	connect {
		val SQL = """
			SELECT * FROM UserMessage WHERE userId=? AND chatId=?;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, chatId)
		stmt.executeQuery()
			.list<Message>()
	} ?: emptyList()

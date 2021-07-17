@file:JvmName("ChatRepository")
@file:JvmMultifileClass

package app.repository

import app.model.Chat
import app.model.ChatUser

fun createChat(userId: Int, title: String): Chat? =
	connect {
		val SQL = """
			begin transaction;
			INSERT INTO Chat (chatId, time, title) VALUES (NULL, ?, ?);
			INSERT INTO Chat_User(chatId, userId, joinTime) select chatId, ?, ? from Chat where ROWID=last_insert_rowid();
			select C.* FROM Chat C JOIN Chat_User CU ON C.chatId = CU.chatId AND CU.ROWID=last_insert_rowid();
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

fun updateChat(chatId: Int, userId: Int, isArchived: Boolean, isMuted: Boolean): ChatUser? =
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
			.tryRead<ChatUser>()
	}

fun updateChat(chatId: Int, userId: Int, title: String): Chat? =
	connect {
		val SQL = """
			UPDATE Chat SET title=? WHERE chatId=? RETURNING *;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setString(1, title)
		stmt.setInt(2, chatId)
		stmt.executeQuery()
			.tryRead<Chat>()
	}

fun deleteChat(chatId: Int): Chat? =
	connect {
		val SQL = """
			DELETE FROM Chat WHERE chatId=? RETURNING *;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.executeQuery()
			.tryRead<Chat>()
	}

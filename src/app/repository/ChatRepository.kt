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

fun archiveChat(chatId: Int, userId: Int): ChatUser? =
	connect {
		val SQL = """
			begin transaction;
			INSERT OR REPLACE INTO Chat_User(chatId, userId) VALUES (?,?);
			UPDATE Chat_User SET isArchived=1 WHERE ROWID=last_insert_rowid() RETURNING *;
			commit;
		""".trimIndent()
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, chatId)
		stmt.setInt(2, userId)
		stmt.executeQuery()
			.tryRead<ChatUser>()
	}

package app.model

import java.util.*

data class Chat(
	val chatId: Int,
	val title: String,
	val createTime: Date,
)

data class ChatUser(
	val chatId: Int,
	val userId: Int,
	val joinTime: Date,
	val isAdmin: Boolean,
	val isArchived: Boolean,
	val isMuted: Boolean,
)

data class Message(
	val messageId: Int,
	val chatId: Int,
	val senderUserId: Int,
	val replyMessageId: Int,
	val content: String,
	val sendTime: Date,
)

data class MessageState(
	val messageId: Int,
	val userId: Int,
	val receivedTime: Date?,
	val seenTime: Date?,
) {
	val isReceived get() = receivedTime != null && receivedTime.time > 0
	val isSeen get() = seenTime != null && seenTime.time > 0
}

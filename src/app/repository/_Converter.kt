@file:JvmName("Converter")

package app.repository

import app.model.*
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Date

inline fun <reified T> ResultSet?.listOf(): List<T> =
	generateSequence {
		singleOf<T>()
	}.toList()

inline fun <reified T> ResultSet?.singleOf(): T? =
	if (this != null && next())
		extract<T>()
	else
		null

@PublishedApi
internal inline fun <reified T> ResultSet.extract(): T? =
	when (T::class) {
		Article::class -> extractArticle() as T?
		Background::class -> extractBackground() as T?
		Comment::class -> extractComment() as T?
		Invitation::class -> extractInvitation() as T?
		Language::class -> extractLanguage() as T?
		Notification::class -> extractNotification() as T?
		Skill::class -> extractSkill() as T?
		User::class -> extractUser() as T?
		SkillEndorse::class -> extractSkillEndorse() as T?
		Chat::class -> extractChat() as T?
		Message::class -> extractMessage() as T?
		Int::class -> extractInt() as T?
		Long::class -> extractLong() as T?
		String::class -> extractString() as T?
		Date::class -> extractDate() as T?
		else -> throw RuntimeException("must not happened")
	}

@PublishedApi
internal fun ResultSet.extractArticle(): Article =
	Article(
		extractInt("articleId") ?: 0,
		extractInt("writer_userId") ?: 0,
		extractString("title") ?: "",
		extractString("content") ?: "",
		extractDate("time") ?: Date(0),
		(extractInt("featured") ?: 0) != 0,
		extractInt("like_count") ?: 0,
		extractInt("comment_count") ?: 0,
		extractInt("home_userId") ?: 0,
		extractDate("home_time") ?: Date(0),
		extractInt("home_count") ?: 0,
	)

@PublishedApi
internal fun ResultSet.extractBackground(): Background =
	Background(
		extractInt("bgId") ?: 0,
		extractInt("userId") ?: 0,
		extractString("title") ?: "",
		Background.BgType.values()[extractInt("type") ?: 0],
		extractDate("fromTime") ?: Date(0),
		extractDate("toTime"),
	)

@PublishedApi
internal fun ResultSet.extractComment(): Comment =
	Comment(
		extractInt("commentId") ?: 0,
		extractInt("articleId") ?: 0,
		extractInt("replyCommentId") ?: 0,
		extractInt("userId") ?: 0,
		extractString("content") ?: "",
		extractDate("time") ?: Date(0),
		extractInt("like_count") ?: -1,
		extractInt("reply_count") ?: -1,
	)

@PublishedApi
internal fun ResultSet.extractInvitation(): Invitation =
	Invitation(
		extractInt("from_userId") ?: 0,
		extractInt("userId") ?: 0,
		extractDate("time") ?: Date(0),
		extractString("message") ?: "",
		extractInt("status") ?: 0,
	)

@PublishedApi
internal fun ResultSet.extractLanguage(): Language =
	Language(
		extractString("langCode") ?: "",
		extractString("title") ?: "",
	)

@PublishedApi
internal fun ResultSet.extractNotification(): Notification {
	val byUserId = extractInt("by_userId") ?: 0
	val time = extractDate("time")
	val targetId = extractInt("targetId") ?: 0
	val event = extractString("event")

	return when (event) {
		"birth" -> Notification.BirthdayNotification(byUserId, time)
		"visit" -> Notification.ProfileVisitNotification(byUserId, time)
		"like_article" -> Notification.LikeArticleNotification(byUserId, time, targetId)
		"like_comment" -> Notification.LikeCommentNotification(byUserId, time, targetId)
		"comment" -> Notification.CommentNotification(byUserId, time, targetId)
		"reply" -> Notification.ReplyCommentNotification(byUserId, time, targetId)
		"endorse" -> Notification.SkillEndorseNotification(byUserId, time, targetId)
		else -> throw RuntimeException("must not happened")
	}
}

@PublishedApi
internal fun ResultSet.extractSkill(): Skill =
	Skill(
		extractInt("skillId") ?: 0,
		extractString("title") ?: "",
	)

@PublishedApi
internal fun ResultSet.extractUser(): User =
	User(
		extractInt("userId") ?: 0,
		extractString("username") ?: "",
		extractString("firstname"),
		extractString("lastname"),
		extractString("intro"),
		extractString("about"),
		extractDate("birthday"),
		extractString("location"),
	).apply {
		avatar = extractByteArray("avatar")

		mutualCount = extractInt("mutual_count") ?: 0
		networkType = when (extractString("type")) {
			"may_know" -> NetworkType.MayKnow
			"requested" -> NetworkType.Requested
			"invited" -> NetworkType.Invited
			else -> throw RuntimeException("must not happened")
		}
	}

@PublishedApi
internal fun ResultSet.extractSkillEndorse(): SkillEndorse =
	SkillEndorse(
		extractInt("by_userId") ?: 0,
		extractInt("userId") ?: 0,
		extractInt("skillId") ?: 0,
		extractDate("time") ?: Date(0),
	)


@PublishedApi
internal fun ResultSet.extractChat(): Chat =
	Chat(
		extractInt("chatId") ?: 0,
		extractString("title") ?: "",
		extractDate("time") ?: Date(0),
		extractInt("unread_count") ?: 0,
		extractDate("lastSeen_time") ?: Date(0),
		extractDate("joinTime") ?: Date(0),
		extractInt("isAdmin") == 1,
		extractInt("isArchived") == 1,
		extractInt("isMuted") == 1,
	)

@PublishedApi
internal fun ResultSet.extractMessage(): Message =
	Message(
		extractInt("messageId") ?: 0,
		extractInt("chatId") ?: 0,
		extractInt("userId") ?: 0,
		extractInt("reply_messageId") ?: 0,
		extractString("content") ?: "",
		extractDate("time") ?: Date(0),
		extractDate("received_time") ?: Date(0),
		extractDate("seen_time") ?: Date(0),
	)


@PublishedApi
internal fun ResultSet.extractInt(columnName: String): Int? =
	try {
		getInt(columnName)
	} catch (ex: SQLException) {
		null
	}

@PublishedApi
internal fun ResultSet.extractInt(columnIndex: Int = 1): Int? =
	try {
		getInt(columnIndex)
	} catch (ex: SQLException) {
		null
	}

@PublishedApi
internal fun ResultSet.extractLong(columnName: String): Long? =
	try {
		getLong(columnName)
	} catch (ex: SQLException) {
		null
	}

@PublishedApi
internal fun ResultSet.extractLong(columnIndex: Int = 1): Long? =
	try {
		getLong(columnIndex)
	} catch (ex: SQLException) {
		null
	}

@PublishedApi
internal fun ResultSet.extractString(columnName: String): String? =
	try {
		getString(columnName)
	} catch (ex: SQLException) {
		null
	}

@PublishedApi
internal fun ResultSet.extractString(columnIndex: Int = 1): String? =
	try {
		getString(columnIndex)
	} catch (ex: SQLException) {
		null
	}

@PublishedApi
internal fun ResultSet.extractByteArray(columnName: String): ByteArray? =
	try {
		getBytes(columnName)
	} catch (ex: SQLException) {
		null
	}

@PublishedApi
internal fun ResultSet.extractByteArray(columnIndex: Int = 1): ByteArray? =
	try {
		getBytes(columnIndex)
	} catch (ex: SQLException) {
		null
	}


@PublishedApi
internal fun ResultSet.extractDate(column: String): Date? =
	try {
		getLong(column).let { if (it > 0) Date(it) else null }
	} catch (ex: SQLException) {
		null
	}

@PublishedApi
internal fun ResultSet.extractDate(columnIndex: Int = 1): Date? =
	try {
		getLong(columnIndex).let { if (it > 0) Date(it) else null }
	} catch (ex: SQLException) {
		null
	}

@file:JvmName("Converter")

package app.repository

import app.model.*
import java.sql.ResultSet
import java.sql.SQLException
import java.util.Date

inline fun <reified T> ResultSet.list(): List<T> =
	generateSequence { tryRead<T>() }.toList()

inline fun <reified T> ResultSet.tryRead(): T? =
	if (next()) read<T>() else null

inline fun <reified T> ResultSet.read(): T =
	when (T::class) {
		Article::class -> readArticle() as T
		Background::class -> readBackground() as T
		Comment::class -> readComment() as T
		Invitation::class -> readInvitation() as T
		Language::class -> readLanguage() as T
		Notification::class -> readNotification() as T
		Skill::class -> readSkill() as T
		User::class -> readUser() as T
		SkillEndorse::class -> readSkillEndorse() as T
		else -> throw RuntimeException("must not happened")
	}

fun ResultSet.readArticle(): Article =
	Article(
		tryInt("articleId") ?: 0,
		tryInt("writeUserId") ?: 0,
		tryString("title") ?: "",
		tryString("content") ?: "",
		tryDate("time") ?: Date(0),
		(tryInt("featured") ?: 0) != 0,
		tryInt("like_count") ?: 0,
		tryInt("comment_count") ?: 0,
	)

fun ResultSet.readBackground(): Background =
	Background(
		tryInt("bgId") ?: 0,
		tryInt("userId") ?: 0,
		tryString("title") ?: "",
		Background.BgType.values()[tryInt("type") ?: 0],
		tryDate("fromTime") ?: Date(0),
		tryDate("toTime"),
	)

fun ResultSet.readComment(): Comment =
	Comment(
		tryInt("commentId") ?: 0,
		tryInt("articleId") ?: 0,
		tryInt("replyCommentId") ?: 0,
		tryInt("userId") ?: 0,
		tryString("content") ?: "",
		tryDate("time") ?: Date(0),
		tryInt("like_count") ?: -1,
		tryInt("reply_count") ?: -1,
	)

fun ResultSet.readInvitation(): Invitation =
	Invitation(
		tryInt("userId") ?: 0,
		tryInt("from_userId") ?: 0,
		tryDate("time") ?: Date(0),
		tryString("message") ?: "",
		tryInt("status") ?: 0,
	)

fun ResultSet.readLanguage(): Language =
	Language(
		tryString("langCode") ?: "",
		tryString("title") ?: "",
	)

fun ResultSet.readNotification(): Notification {
	val byUserId = tryInt("by_userId") ?: 0
	val time = tryDate("time")
	val targetId = tryInt("targetId") ?: 0
	val event = tryString("event")

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

fun ResultSet.readSkill(): Skill =
	Skill(
		tryInt("skillId") ?: 0,
		tryString("title") ?: "",
	)

fun ResultSet.readUser(): User =
	User(
		tryInt("userId") ?: 0,
		tryString("username") ?: "",
		tryString("firstname"),
		tryString("lastname"),
		tryString("intro"),
		tryString("about"),
		tryDate("birthday"),
		tryString("location"),
	).apply {
		avatar = tryByteArray("avatar")
	}

fun ResultSet.readSkillEndorse(): SkillEndorse =
	SkillEndorse(
		tryInt("byUserId") ?: 0,
		tryInt("userId") ?: 0,
		tryInt("skillId") ?: 0,
		tryDate("time") ?: Date(0),
	)

fun ResultSet.tryInt(column: String?): Int? =
	try {
		getInt(column)
	} catch (ex: SQLException) {
		null
	}

fun ResultSet.tryLong(column: String?): Long? =
	try {
		getLong(column)
	} catch (ex: SQLException) {
		null
	}

fun ResultSet.tryString(column: String?): String? =
	try {
		getString(column)
	} catch (ex: SQLException) {
		null
	}

fun ResultSet.tryByteArray(column: String?): ByteArray? =
	try {
		getBytes(column)
	} catch (ex: SQLException) {
		null
	}


fun ResultSet.tryDate(column: String?): Date? =
	try {
		getLong(column).let { if (it > 0) Date(it) else null }
	} catch (ex: SQLException) {
		null
	}

@file:JvmName("Repository")
@file:JvmMultifileClass
@file:OptIn(ExperimentalStdlibApi::class)

package app.repository

import app.model.Accomplishment
import app.model.Background
import app.model.User
import java.sql.Types
import java.util.Date

/** get User Detail from its username */
fun getUserByUsername(username: String): User? =
	connect {
		val SQL = """
			SELECT * from User where username=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setString(1, username)
		statement.executeQuery()
			.singleOf<User>()
	}

/** visit User Profile */
fun getUserById(visitorUserId: Int, userId: Int): User? {
	val user = connect {
		val SQL = """
			SELECT * from User where userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.singleOf<User>()
	} ?: return null

	if (visitorUserId > 0 && visitorUserId != userId) connect {
		val SQL = """
			INSERT INTO Event_ProfileVisit (by_userId, userId, time, notified)
			VALUES (?,?,?,0);
			""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, visitorUserId)
		statement.setInt(2, userId)
		statement.setLong(3, System.currentTimeMillis())
		statement.executeUpdate() > 0
	}

	return user
}


/** login by username and password */
fun loginUser(username: String?, password: String?): User? =
	connect {
		val SQL = """
			select * from User where username=? and password=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setString(1, username)
		statement.setString(2, password)
		statement.executeQuery()
			.singleOf<User>()
	}

/** register new user by given detail */
fun registerUser(user: User, password: String): User? =
	connect {
		val SQL = """
			INSERT INTO User
			(userId, username, password, firstname, lastname, intro, about, avatar,  birthday, location)
			VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			RETURNING *;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setString(1, user.username)
		statement.setString(2, password)
		statement.setString(3, user.firstname)
		statement.setString(4, user.lastname)
		statement.setString(5, user.intro)
		statement.setString(6, user.about)
		statement.setBytes(7, user.avatar)
		if (user.birthday != null)
			statement.setLong(8, user.birthday!!.time)
		else statement.setNull(8, Types.INTEGER)
		statement.setString(9, user.location)
		statement.executeQuery()
			.singleOf<User>()
	}

/** like or unlike given article or comment */
fun toggleUserLike(userId: Int, articleId: Int, commentId: Int): Boolean {
	val isLiked = (connect {
		val SQL = if (commentId > 0) {
			"""
				SELECT count(*) from User_Like where userId=? and articleId=? and commentId=?;
			""".trimIndent()
		} else {
			"""
				SELECT count(*) from User_Like where userId=? and articleId=? and commentId is null;
			""".trimIndent()
		}
		val stmt = it.prepareStatement(SQL)
		stmt.setInt(1, userId)
		stmt.setInt(2, articleId)
		if (commentId > 0) stmt.setInt(3, commentId)
		stmt.executeQuery()
			.extract<Int>()
	} ?: 0) > 0

	return if (isLiked) {
		connect {
			val SQL = if (commentId > 0) {
				"""
					DELETE from User_Like where userId=? and articleId=? and commentId=?;
				""".trimIndent()
			} else {
				"""
					DELETE from User_Like where userId=? and articleId=? and commentId is null;
				""".trimIndent()
			}
			val stmt = it.prepareStatement(SQL)
			stmt.setInt(1, userId)
			stmt.setInt(2, articleId)
			if (commentId > 0) stmt.setInt(3, commentId)
			stmt.executeUpdate() > 0
		} == true
	} else {
		connect {
			val SQL = if (commentId > 0) {
				"""
					INSERT into User_Like (userId, articleId, time, commentId, notified) VALUES (?,?,?,?,0);
				""".trimIndent()
			} else {
				"""
					INSERT into User_Like (userId, articleId, time, commentId, notified) VALUES (?,?,?,null,0);
				""".trimIndent()
			}
			val stmt = it.prepareStatement(SQL)
			stmt.setInt(1, userId)
			stmt.setInt(2, articleId)
			stmt.setLong(3, System.currentTimeMillis())
			if (commentId > 0) stmt.setInt(4, commentId)
			stmt.executeUpdate() > 0
		} == true
	}
}

/** update all user info */
fun updateUserPersonalInfo(
	userId: Int,
	firstName: String?,
	lastName: String?,
	intro: String?,
	about: String?,
	avatar: ByteArray?,
	accomp: String?,
	birthday: Date?,
	location: String?,
): Boolean =
	connect {
		val sql = "UPDATE User SET " + buildList {
			if (firstName != null) add("firstname=?")
			if (lastName != null) add("lastname=?")
			if (intro != null) add("intro=?")
			if (about != null) add("about=?")
			if (avatar != null) add("avatar=?")
			if (accomp != null) add("accomp=?")
			if (birthday != null) add("birthday=?")
			if (location != null) add("location=?")
		}.joinToString(",") + " WHERE userId=?;"

		var i = 0
		val stmt = it.prepareStatement(sql)
		if (firstName != null) stmt.setString(++i, firstName)
		if (lastName != null) stmt.setString(++i, lastName)
		if (intro != null) stmt.setString(++i, intro)
		if (about != null) stmt.setString(++i, about)
		if (avatar != null) stmt.setBytes(++i, avatar)
		if (accomp != null) stmt.setString(++i, accomp)
		if (birthday != null) stmt.setLong(++i, birthday.time)
		if (location != null) stmt.setString(++i, location)
		stmt.setInt(++i, userId)

		stmt.executeUpdate() > 0
	} == true

fun updateAvatar(userId: Int, avatar: ByteArray) =
	updateUserPersonalInfo(userId, null, null, null, null, avatar, null, null, null)

fun updateAvatar(userId: Int, firstName: String, lastName: String, location: String) =
	updateUserPersonalInfo(userId, firstName, lastName, null, null, null, null, null, location)

fun updateInfo(userId: Int, info: String) =
	updateUserPersonalInfo(userId, null, null, info, null, null, null, null, null)

fun updateAbout(userId: Int, about: String) =
	updateUserPersonalInfo(userId, null, null, null, about, null, null, null, null)


/** list location suggestion */
fun suggestLocation(): List<String> =
	connect {
		val SQL = """
			SELECT * from SuggestLocation;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.executeQuery()
			.listOf<String>()
	} ?: emptyList()

/** list background suggestion */
fun suggestBackground(): List<Background> =
	connect {
		val SQL = """
			SELECT * from SuggestBackground;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.executeQuery()
			.listOf<Background>()
	} ?: emptyList()

/** list all given user Backgrounds */
fun listUserBackground(userId: Int): List<Background> =
	connect {
		val SQL = """
			SELECT * from User_Background WHERE userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.listOf<Background>()
	} ?: emptyList()

/** add/edit user background data */
fun saveUserBackground(bg: Background): Background? =
	connect {
		val SQL = """
			INSERT OR REPLACE INTO User_Background
			(bgId, userId, type, title, fromTime, toTime)
			VALUES (?,?,?,?,?,?) RETURNING *;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		if (bg.bgId > 0)
			statement.setInt(1, bg.bgId)
		else
			statement.setNull(1, Types.INTEGER)
		statement.setInt(2, bg.userId)
		statement.setInt(3, bg.bgType.ordinal)
		statement.setString(4, bg.title)
		statement.setLong(5, bg.fromTime.time)
		if ((bg.toTime?.time ?: 0) > 0)
			statement.setLong(6, bg.fromTime.time)
		else
			statement.setNull(6, Types.INTEGER)
		statement.executeQuery()
			.singleOf<Background>()
	}

/** remove userBackground */
fun deleteUserBackground(bgId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM User_Background WHERE bgId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, bgId)
		statement.executeUpdate() > 0
	} == true

/** list all user accomplishments */
fun listUserAccomplishment(userId: Int): List<Accomplishment> =
	connect {
		val SQL = """
			SELECT * from User_Accomplishment WHERE userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.listOf<Accomplishment>()
	} ?: emptyList()

/** add or edit user accomplishment */
fun saveUserAccomplishment(acc: Accomplishment): Accomplishment? =
	connect {
		val SQL = """
			INSERT OR REPLACE INTO User_Accomplishment
			(accId, userId, title, content, time)
			VALUES (?,?,?,?,?) RETURNING *;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		if (acc.accId > 0)
			statement.setInt(1, acc.accId)
		else
			statement.setNull(1, Types.INTEGER)
		statement.setInt(2, acc.userId)
		statement.setString(3, acc.title)
		statement.setString(3, acc.content)
		statement.setLong(4, acc.time.time)
		statement.executeQuery()
			.singleOf<Accomplishment>()
	}

/** remove user accomplishment */
fun deleteUserAccomplishment(accId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM User_Accomplishment WHERE accId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, accId)
		statement.executeUpdate() > 0
	} == true

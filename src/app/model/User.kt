package app.model

import java.sql.ResultSet
import java.util.*

data class User(
  var userId: Int,
  var username: String,
  var firstname: String?,
  var lastname: String?,
  var intro: String?,
  var about: String?,
  var birthday: Date?,
) : _BaseModel() {
  var avatar: ByteArray? = null

  companion object {
    @JvmStatic
    fun from(res: ResultSet?): User {
      return User(
        tryInt(res, "userId") ?: 0,
        tryString(res, "username") ?: "",
        tryString(res, "firstname"),
        tryString(res, "lastname"),
        tryString(res, "intro"),
        tryString(res, "about"),
        tryLong(res, "birthday")?.let { Date(it) }
      ).apply {
        avatar = tryByteArray(res, "avatar")
      }
    }
  }

}

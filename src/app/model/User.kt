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
) {
  var avatar: ByteArray? = null

  companion object {
    @JvmStatic
    fun from(res: ResultSet): User {
      return User(
        res.tryInt("userId") ?: 0,
        res.tryString("username") ?: "",
        res.tryString("firstname"),
        res.tryString("lastname"),
        res.tryString("intro"),
        res.tryString("about"),
        res.tryDate("birthday")
      ).apply {
        avatar = res.tryByteArray("avatar")
      }
    }
  }

}

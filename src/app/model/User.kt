package app.model

import java.sql.ResultSet
import java.util.*

data class User(
  var userId: Int,
  var username: String,
  var email: String,
  var phone: String,
  var name: String,
  var intro: String,
  var about: String,
  var avatar: ByteArray,
  var birthday: Date,
) : _BaseModel() {

  companion object {
    @JvmStatic
    fun from(res: ResultSet?): User {
      return User(
        tryInt(res, "userId") ?: 0,
        tryString(res, "username") ?: "",
        tryString(res, "email") ?: "",
        tryString(res, "phone") ?: "",
        tryString(res, "name") ?: "",
        tryString(res, "intro") ?: "",
        tryString(res, "about") ?: "",
        tryByteArray(res, "avatar") ?: byteArrayOf(),
        Date(tryLong(res, "birthday") ?: 0)
      )
    }
  }

}

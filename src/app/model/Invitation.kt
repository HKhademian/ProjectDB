package app.model

import java.sql.ResultSet
import java.util.*

class Invitation(
  var userId: Int,
  var fromUserId: Int,
  var time: Date,
  var message: String,
  var status: Int,
) {

  companion object {
    @JvmStatic
    fun from(res: ResultSet): Invitation {
      return Invitation(
        res.tryInt("userId") ?: 0,
        res.tryInt("from_userId") ?: 0,
        res.tryDate("time") ?: Date(0),
        res.tryString("message") ?: "",
        res.tryInt("status") ?: 0,
      )
    }
  }

}


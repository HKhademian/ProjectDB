package app.model

import java.sql.ResultSet

data class Skill(
  var skillId: Int,
  var title: String,
) {

  companion object {
    @JvmStatic
    fun from(res: ResultSet): Skill {
      return Skill(
        res.tryInt("skillId") ?: 0,
        res.tryString("title") ?: "",
      )
    }
  }

}

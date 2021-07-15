package app.model

import java.sql.ResultSet

data class Skill(
  var skillId: Int,
  var title: String,
) : _BaseModel() {
  companion object {
    @JvmStatic
    fun from(res: ResultSet?): Skill {
      return Skill(
        tryInt(res, "skillId", 0),
        tryString(res, "title", "")
      )
    }
  }
}

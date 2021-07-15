package app.model

import java.sql.ResultSet

class Language(
  var code: String,
  var title: String,
) {

  companion object {
    @JvmStatic
    fun from(res: ResultSet): Language {
      return Language(
        res.tryString("langCode") ?: "",
        res.tryString("title") ?: "",
      )
    }
  }

}

package app.model;

import java.sql.ResultSet;

public class Language extends _BaseModel {
  public String code;
  public String title;

  public Language(String code, String title) {
    this.code = code;
    this.title = title;
  }

  public static Language from(ResultSet res) {
    return new Language(
      tryString(res, "langCode"),
      tryString(res, "title")
    );
  }
}

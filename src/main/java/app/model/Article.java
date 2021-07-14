package app.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public record Article(
  int articleId,
  int writeUserId,
  String title,
  String content,
  Date time,
  boolean featured
) {
  public static Article from(ResultSet res) throws SQLException {
    return new Article(
      res.getInt("articleId"),
      res.getInt("writeUserId"),
      res.getString("title"),
      res.getString("content"),
      new Date(res.getLong("time")),
      res.getInt("featured") != 0
    );
  }
}

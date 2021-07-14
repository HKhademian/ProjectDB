package app.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Article {
  public int articleId;
  public int writeUserId;
  public String title;
  public String content;
  public Date time;
  public boolean featured;

  public Article(int articleId, int writeUserId, String title, String content, Date time, boolean featured) {
    this.articleId = articleId;
    this.writeUserId = writeUserId;
    this.title = title;
    this.content = content;
    this.time = time;
    this.featured = featured;
  }

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

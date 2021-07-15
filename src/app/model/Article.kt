package app.model;

import java.sql.ResultSet;
import java.util.Date;

public class Article extends _BaseModel {
  public int articleId;
  public int writerUserId;
  public String title;
  public String content;
  public Date time;
  public boolean featured;
  public int likeCount;
  public int commentCount;

  public Article(int articleId, int writerUserId, String title, String content, Date time, boolean featured, int likeCount, int commentCount) {
    this.articleId = articleId;
    this.writerUserId = writerUserId;
    this.title = title;
    this.content = content;
    this.time = time;
    this.featured = featured;
    this.likeCount = likeCount;
    this.commentCount = commentCount;
  }

  public static Article from(ResultSet res) {
    return new Article(
      tryInt(res, "articleId", 0),
      tryInt(res, "writeUserId", 0),
      tryString(res, "title"),
      tryString(res, "content"),
      new Date(tryLong(res, "time", 0)),
      tryInt(res, "featured", 0) != 0,
      tryInt(res, "like_count", 0),
      tryInt(res, "comment_count", 0)
    );
  }
}

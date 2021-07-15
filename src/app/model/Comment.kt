package app.model;

import java.sql.ResultSet;
import java.util.Date;

public class Comment extends _BaseModel {
  public int commentId;
  public int articleId;
  public int replyCommentId;
  public int userId;
  public String content;
  public Date time;
  public int likeCount;
  public int replyCount;

  public Comment(int commentId, int articleId, int replyCommentId, int userId, String content, Date time, int likeCount, int replyCount) {
    this.commentId = commentId;
    this.articleId = articleId;
    this.replyCommentId = replyCommentId;
    this.userId = userId;
    this.content = content;
    this.time = time;
    this.likeCount = likeCount;
    this.replyCount = replyCount;
  }

  public static Comment from(ResultSet res) {
    return new Comment(
      tryInt(res, "commentId", 0),
      tryInt(res, "articleId", 0),
      tryInt(res, "replyCommentId", 0),
      tryInt(res, "userId", 0),
      tryString(res, "content"),
      new Date(tryLong(res, "time", 0)),
      tryInt(res, "like_count", -1),
      tryInt(res, "reply_count", -1)
    );
  }
}

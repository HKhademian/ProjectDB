package app.model;

import java.util.Date;

public class Comment {
  public int commentId;
  public int articleId;
  public int replyCommentId;
  public int userId;
  public String content;
  public Date time;
}

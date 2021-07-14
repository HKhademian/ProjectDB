package app.model;

import java.util.Date;

public class Notification {
  public int byUserId;
  public Date time;

  private Notification() {
  }

  public static class BirthdayNotification extends Notification {
  }

  public static class LikeArticleNotification extends Notification {
    public int articleId;
  }

  public static class LikeCommentNotification extends Notification {
    public int commentId;
  }

  public static class CommentNotification extends Notification {
    public int commentId;
  }

  public static class ReplyCommentNotification extends Notification {
    public int commentId;
  }

  public static class SkillEndorseNotification extends Notification {
    public int skillId;
  }

  public static class ProfileVisitNotification extends Notification {
  }
}

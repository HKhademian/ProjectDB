package app.repository;

import app.model.Article;
import app.model.Notification;
import app.model.User;

import java.util.List;

public final class UserRepository extends _BaseRepository {
  private UserRepository() {
  }

  public static User login(String username, String password) {
    return null;
  }

  public static User register(String name, String username, String password) {
    return null;
  }

  public static List<Article> getUserHome(int userId) {
    return null;
  }

  public static List<Notification> getNotification(int userId) {
    return null;
  }


  public static void toggleLikeArticle(int userId, int articleId) {
  }

  public static void toggleLikeComment(int userId, int articleId, int commentId) {
  }

  public static void commentOn(int userId, int articleId, int commentId) {
  }

}

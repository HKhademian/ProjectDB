package app.repository;

import app.model.Article;
import app.model.Notification;
import app.model.User;

import java.util.Date;
import java.util.List;

public final class UserRepository extends _BaseRepository {
  private UserRepository() {
  }

  public static User login(String username, String password) {
    final var QUERY = "select * from `User` where `username`=? and `password`=?";
    return connect(connection -> {
      final var statement = connection.prepareStatement(QUERY);
      statement.setString(1, username);
      statement.setString(2, password);
      final var res = statement.executeQuery();
      if (res.next()) {
        return new User(
          res.getInt("userId"),
          res.getString("username"),
          res.getString("email"),
          res.getString("phone"),
          res.getString("name"),
          res.getString("intro"),
          res.getString("about"),
          res.getBytes("avatar"),
          new Date(res.getInt("birthday"))
        );
      }
      return null;
    });
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

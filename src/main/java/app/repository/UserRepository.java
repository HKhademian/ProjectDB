package app.repository;

import app.model.Article;
import app.model.Notification;
import app.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public final class UserRepository extends _BaseRepository {
  private UserRepository() {
  }

  public static User login(String username, String password) {
    final var SQL = "select * from `User` where `username`=? and `password`=?";
    return connect(connection -> {
      final var statement = connection.prepareStatement(SQL);
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

  public static User register(String username, String password, String name, String email, String phone, String intro, String about, byte[] avatar, Date birthday, String location) {
    final var SQL = "INSERT INTO `User` VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?);";
    return connect(connection -> {
      final var statement = connection.prepareStatement(SQL);
      statement.setString(1, username);
      statement.setString(2, password);
      statement.setString(3, name);
      statement.setString(4, email);
      statement.setString(5, phone);
      statement.setString(6, intro);
      statement.setString(7, about);
      statement.setBytes(8, avatar);
      statement.setLong(9, birthday.getTime());
      statement.setString(10, location);
      final var res = statement.executeUpdate();
      if (res > 0) return login(username, password);
      return null;
    });
  }

  public static List<Article> getUserHome(int userId) {
    final var SQL = "SELECT * from Article where articleId in (select articleId from Home where userId=? order by time desc)";
    return connect(connection -> {
      final var statement = connection.prepareStatement(SQL);
      statement.setInt(1, userId);
      final var res = statement.executeQuery();
      final var result = new ArrayList<Article>();
      while (res.next())
        result.add(Article.from(res));
      return result;
    });
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

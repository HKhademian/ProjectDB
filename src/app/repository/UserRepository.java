package app.repository;

import app.model.Article;
import app.model.Notification;
import app.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class UserRepository extends _BaseRepository {
  private UserRepository() {
  }

  public static User login(String username, String password) {
    final String SQL = "select * from `User` where `username`=? and `password`=?";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setString(1, username);
      statement.setString(2, password);
      final ResultSet res = statement.executeQuery();
      if (res.next()) return User.from(res);
      return null;
    });
  }

  public static User register(String username, String password, String name, String email, String phone, String intro, String about, byte[] avatar, Date birthday, String location) {
    final String SQL = "INSERT INTO `User` VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?);";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
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
      final int res = statement.executeUpdate();
      if (res > 0) return login(username, password);
      return null;
    });
  }

  public static List<Article> getUserHome(int userId) {
    final String SQL = "SELECT * from `Article` where `articleId` in (select `articleId` from Home where `userId`=? order by `time` desc)";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, userId);
      final ResultSet res = statement.executeQuery();
      final List<Article> result = new ArrayList<>();
      while (res.next())
        result.add(Article.from(res));
      return result;
    });
  }

  public static List<Notification> getNotification(int userId) {
    return null;
  }

  public static void toggleLike(int userId, int articleId, int commentId) {
    final String SQL1 = "SELECT count(*) from `User_Like` where `userId`=? and `articleId`=? and `commentId`=?";
    final String SQL2 = "DELETE from `User_Like` where `userId`=? and `articleId`=? and `commentId`=?";
    final String SQL3 = "INSERT into `User_Like` VALUES (?,?,?,?,0)";
    connect(connection -> {
      final PreparedStatement statement1 = connection.prepareStatement(SQL1);
      statement1.setInt(1, userId);
      statement1.setInt(2, articleId);
      if (commentId > 0)
        statement1.setInt(3, articleId);
      else
        statement1.setNull(3, Types.INTEGER);
      final ResultSet res1 = statement1.executeQuery();

      if (res1.next() && res1.getInt(1) > 0) {
        final PreparedStatement statement2 = connection.prepareStatement(SQL2);
        statement2.setInt(1, userId);
        statement2.setInt(2, articleId);
        if (commentId > 0)
          statement2.setInt(3, articleId);
        else
          statement2.setNull(3, Types.INTEGER);
        statement2.executeUpdate();
      } else {
        final PreparedStatement statement3 = connection.prepareStatement(SQL3);
        statement3.setInt(1, userId);
        statement3.setInt(2, articleId);
        if (commentId > 0)
          statement3.setInt(3, articleId);
        else
          statement3.setNull(3, Types.INTEGER);
        statement3.setLong(4, System.currentTimeMillis());
        statement3.executeUpdate();
      }
      return null;
    });
  }

  public static void commentOn(int userId, int articleId, int replyCommentId, String content) {
    final String SQL = "INSERT INTO `Comment` VALUES (null,?,?,?,?,?,0)";
    connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, articleId);
      if (replyCommentId > 0)
        statement.setInt(2, replyCommentId);
      else
        statement.setNull(2, Types.INTEGER);
      statement.setInt(3, userId);
      statement.setString(4, content);
      statement.setLong(5, System.currentTimeMillis());
      statement.executeUpdate();
      return null;
    });
  }

}

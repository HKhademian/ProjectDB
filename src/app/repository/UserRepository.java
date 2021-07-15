package app.repository;

import app.model.Notification;
import app.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;
import java.util.List;

public final class UserRepository extends _BaseRepository {
  private UserRepository() {
  }

  public static User getUser(int userId) {
    final String SQL = "SELECT * from `User` where `userId`=?";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, userId);
      final ResultSet res = statement.executeQuery();
      return res.next() ? User.from(res) : null;
    });
  }

  public static User login(String username, String password) {
    final String SQL = "select * from `User` where `username`=? and `password`=?";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setString(1, username);
      statement.setString(2, password);
      final ResultSet res = statement.executeQuery();
      return res.next() ? User.from(res) : null;
    });
  }

  public static User register(String username, String password, String name, String email, String phone, String intro, String about, byte[] avatar, Date birthday, String location) {
    final String SQL = "INSERT INTO `User` VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?) RETURNING *;";
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
      if (birthday != null)
        statement.setLong(9, birthday.getTime());
      else
        statement.setNull(9, Types.INTEGER);
      statement.setString(10, location);
      final ResultSet res = statement.executeQuery();
      return res.next() ? User.from(res) : null;
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

}

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

  public static User getUser(int userId) {
    final String SQL = "SELECT * from `User` where `userId`=?;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, userId);
      final ResultSet res = statement.executeQuery();
      return res.next() ? User.from(res) : null;
    });
  }

  public static User login(String username, String password) {
    final String SQL = "select * from `User` where `username`=? and `password`=?;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setString(1, username);
      statement.setString(2, password);
      final ResultSet res = statement.executeQuery();
      return res.next() ? User.from(res) : null;
    });
  }

  public static User register(String username, String password, String firstname, String lastname, String intro, String about, byte[] avatar, Date birthday, String location) {
    final String SQL = "INSERT INTO `User` (userId, username, password, firstname, lastname, intro, about, avatar, accomp, birthday, location) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, NULL, ?, ?) RETURNING *;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setString(1, username);
      statement.setString(2, password);
      statement.setString(3, firstname);
      statement.setString(4, lastname);
      statement.setString(5, intro);
      statement.setString(6, about);
      statement.setBytes(7, avatar);
      if (birthday != null)
        statement.setLong(8, birthday.getTime());
      else
        statement.setNull(8, Types.INTEGER);
      statement.setString(9, location);
      final ResultSet res = statement.executeQuery();
      return res.next() ? User.from(res) : null;
    });
  }

  public static boolean toggleLike(int userId, int articleId, int commentId) {
    final String SQL1;
    final String SQL2;
    final String SQL3;
    if (commentId > 0) {
      SQL1 = "SELECT count(*) from `User_Like` where `userId`=? and `articleId`=? and `commentId`=?;";
      SQL2 = "DELETE from `User_Like` where `userId`=? and `articleId`=? and `commentId`=?;";
      SQL3 = "INSERT into `User_Like` (`userId`, `articleId`, `time`, `commentId`, `notified`) VALUES (?,?,?,?,0);";
    } else {
      SQL1 = "SELECT count(*) from `User_Like` where `userId`=? and `articleId`=? and `commentId` is null;";
      SQL2 = "DELETE from `User_Like` where `userId`=? and `articleId`=? and `commentId` is null;";
      SQL3 = "INSERT into `User_Like` (`userId`, `articleId`, `time`, `commentId`, `notified`) VALUES (?,?,?,null,0);";
    }
    Boolean res = connect(connection -> {
      final PreparedStatement statement1 = connection.prepareStatement(SQL1);
      statement1.setInt(1, userId);
      statement1.setInt(2, articleId);
      if (commentId > 0)
        statement1.setInt(3, commentId);
      final ResultSet res1 = statement1.executeQuery();
      if (res1.next() && res1.getInt(1) > 0) {
        final PreparedStatement statement2 = connection.prepareStatement(SQL2);
        statement2.setInt(1, userId);
        statement2.setInt(2, articleId);
        if (commentId > 0)
          statement2.setInt(3, commentId);
        statement2.executeUpdate();
        return true;
      } else {
        final PreparedStatement statement3 = connection.prepareStatement(SQL3);
        statement3.setInt(1, userId);
        statement3.setInt(2, articleId);
        statement3.setLong(3, System.currentTimeMillis());
        if (commentId > 0)
          statement3.setInt(4, commentId);
        statement3.executeUpdate();
        return true;
      }
    });
    return res != null && res;
  }

  public static List<Notification> getNotification(int userId) {
    final String SQL = "select * from `Notification` where `userId`=?;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, userId);
      final ResultSet res = statement.executeQuery();
      final List<Notification> result = new ArrayList<>();
      while (res.next())
        result.add(Notification.from(res));
      return result;
    });
  }

}

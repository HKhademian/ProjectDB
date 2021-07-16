package app.repository;

import app.model.Comment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class CommentRepository extends _BaseRepository {
  private CommentRepository() {
  }

  public static Comment getComment(int commentId) {
    final String SQL = "SELECT * from `CommentCounted` where `commentId`=?;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, commentId);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Comment.from(res) : null;
    });
  }

  /**
   * returns all comment and replies on this articleId
   */
  public static List<Comment> listComments(int articleId) {
    final String SQL = "SELECT * from `CommentCounted` where `articleId`=?; --and `reply_commentId` is null";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, articleId);
      final ResultSet res = statement.executeQuery();
      final List<Comment> result = new ArrayList<>();
      while (res.next())
        result.add(Comment.from(res));
      return result;
    });
  }

  public static Comment deleteComment(int commentId) {
    final String SQL = "DELETE FROM `Comment` WHERE `commentId`=? RETURNING *;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, commentId);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Comment.from(res) : null;
    });
  }

  /**
   * add comment to an {@link app.model.Article} or reply on another {@link app.model.Comment} (if replyCommentId>0)
   */
  public static Comment commentOn(int userId, int articleId, int replyCommentId, String content) {
    final String SQL_C = "INSERT INTO `Comment` (userId, content, time, notified, articleId, reply_commentId) VALUES (?,?,?,0,?,null) RETURNING *;";
    final String SQL_R = "INSERT INTO `Comment` (userId, content, time, notified, articleId, reply_commentId) SELECT ?,?,?,0,articleId,commentId from Comment where commentId=? RETURNING *;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(replyCommentId > 0 ? SQL_R : SQL_C);
      statement.setInt(1, userId);
      statement.setString(2, content);
      statement.setLong(3, System.currentTimeMillis());
      statement.setInt(4, replyCommentId > 0 ? replyCommentId : articleId);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Comment.from(res) : null;
    });
  }

}

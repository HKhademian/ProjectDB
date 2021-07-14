package app.repository;

import app.model.Comment;

import java.util.List;

public final class CommentRepository extends _BaseRepository {
  private CommentRepository() {
  }

  public static List<Comment> listComments(int articleId) {
    return null;
  }

  public static void deleteComment(int commentId) {
  }
}

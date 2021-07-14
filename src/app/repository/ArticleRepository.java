package app.repository;

import app.model.Article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class ArticleRepository extends _BaseRepository {
  private ArticleRepository() {
  }

  public static List<Article> getUserArticles(int userId) {
    final String SQL = "SELECT * from `ArticleCounted` where `writer_userId`=?";
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

  public static Article addArticle(int writerUserId, String title, String content) {
    return null;
  }

  public static Article editArticle(int articleId, String title, String content) {
    return null;
  }

  public static Article deleteArticle(int articleId) {
    return null;
  }


}

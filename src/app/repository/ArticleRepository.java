package app.repository;

import app.model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class ArticleRepository extends _BaseRepository {
  private ArticleRepository() {
  }

  public static Article getArticle(int articleId) {
    final String SQL = "SELECT * from `ArticleCounted` where `articleId`=?";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, articleId);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Article.from(res) : null;
    });
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

  public static Article saveArticle(Article article) {
    final String SQL = "INSERT OR REPLACE INTO `Article` (`articleId`, `writer_userId`, `title`, `content`, `time`, `featured`) VALUES (?,?,?,?,?,?) RETURNING *;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);

      if (article.articleId > 0)
        statement.setInt(1, article.articleId);
      else
        statement.setNull(1, Types.INTEGER);
      statement.setInt(2, article.writerUserId);
      statement.setString(3, article.title);
      statement.setString(4, article.content);
      if (article.time != null)
        statement.setLong(5, article.time.getTime());
      else
        statement.setLong(5, System.currentTimeMillis());
      statement.setInt(6, article.featured ? 1 : 0);

      final ResultSet res = statement.executeQuery();
      return res.next() ? Article.from(res) : null;
    });
  }

  public static Article deleteArticle(int articleId) {
    final String SQL = "DELETE FROM `Article` WHERE `articleId`=? RETURNING *;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setInt(1, articleId);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Article.from(res) : null;
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
}

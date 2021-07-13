package repository;

import model.Article;

import java.util.List;

public final class ArticleRepository extends _BaseRepository {
  private ArticleRepository() {
  }

  public static List<Article> getUserArticles(int userId) {
    return null;
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

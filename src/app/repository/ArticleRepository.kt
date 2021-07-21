@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Article

fun getUserArticle(homeUserId: Int, articleId: Int): Article? =
	connect {
		val SQL = """
			SELECT AST.*, HA.home_userId , HA.home_count, HA.home_time, HA.home_isLiked
			from ArticleStat AST
			LEFT JOIN HomeArticle HA ON AST.articleId= HA.articleId AND HA.home_userId=?
			where AST.articleId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, homeUserId)
		statement.setInt(2, articleId)
		statement.executeQuery()
			.singleOf<Article>()
	}

fun listUserArticles(homeUserId: Int): List<Article> =
	connect {
		val SQL = """
			SELECT * from HomeArticle where home_userId=? AND writer_userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, homeUserId)
		statement.setInt(2, homeUserId)
		statement.executeQuery()
			.listOf<Article>()
	} ?: emptyList()

fun listHomeUserArticles(homeUserId: Int): List<Article> =
	connect {
		val SQL = """
			SELECT * from HomeArticle where home_userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, homeUserId)
		statement.executeQuery()
			.listOf<Article>()
	} ?: emptyList()

fun saveArticle(article: Article): Article? {
	val articleId = connect {
		val SQL_INSERT = """
			INSERT INTO Article (articleId, title, content, time, featured, writer_userId)
			VALUES (NULL,?,?,?,?,?) RETURNING articleId;
	 """.trimIndent()
		val SQL_UPDATE = """
			UPDATE Article SET title=?, content=?, time=?, featured=?
			WHERE writer_userId=? AND articleId=? RETURNING articleId;
	 """.trimIndent()

		val stmt = it.prepareStatement(if (article.articleId > 0) SQL_UPDATE else SQL_INSERT)
		stmt.setString(1, article.title)
		stmt.setString(2, article.content)
		stmt.setLong(3, System.currentTimeMillis())
		stmt.setInt(4, if (article.featured) 1 else 0)
		stmt.setInt(5, article.writerUserId)
		if (article.articleId > 0)
			stmt.setInt(6, article.articleId)

		stmt.executeQuery()
			.singleOf<Int>()
	} ?: return null

	return getUserArticle(article.writerUserId, articleId)
}

fun deleteArticle(homeUserId: Int, articleId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Article WHERE articleId=? AND writer_userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, articleId)
		statement.setInt(2, homeUserId)
		statement.executeUpdate() > 0
	} == true

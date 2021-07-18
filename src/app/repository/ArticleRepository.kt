@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Article
import java.sql.Types

fun getArticle(userId: Int, articleId: Int): Article? =
	connect {
		val SQL = """
			SELECT * from HomeArticle where home_userId=? AND articleId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, articleId)
		statement.executeQuery()
			.singleOf<Article>()
	}

fun getUserArticles(userId: Int): List<Article> =
	connect {
		val SQL = """
			SELECT * from HomeArticle where home_userId=? AND writer_userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, userId)
		statement.executeQuery()
			.listOf<Article>()
	} ?: emptyList()

fun saveArticle(article: Article): Article? =
	connect {
		val SQL = """
			begin transaction ;
			INSERT OR REPLACE INTO Article
				(articleId, writer_userId, title, content, time, featured) VALUES (?,?,?,?,?,?);
			SELECT HA.* from HomeArticle HA
				JOIN Article A on HA.articleId = A.articleId
				where A.ROWID=last_insert_rowid() AND HA.home_userId=?;
			commit ;
	 """.trimIndent()
		val statement = it.prepareStatement(SQL)
		if (article.articleId > 0) statement.setInt(1, article.articleId) else statement.setNull(1, Types.INTEGER)
		statement.setInt(2, article.writerUserId)
		statement.setString(3, article.title)
		statement.setString(4, article.content)
		statement.setLong(5, article.time.time)
		statement.setInt(6, if (article.featured) 1 else 0)
		statement.setInt(7, article.writerUserId)
		statement.executeQuery()
			.singleOf<Article>()
	}

fun deleteArticle(articleId: Int): Boolean =
	connect {
		val SQL = """
			DELETE FROM Article WHERE articleId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, articleId)
		statement.executeUpdate() > 0
	} == true

fun getUserHomeArticles(userId: Int): List<Article> =
	connect {
		val SQL = """
			SELECT * from HomeArticle where home_userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.listOf<Article>()
	} ?: emptyList()

@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Article
import java.sql.Types

fun getArticle(userId: Int, articleId: Int): Article? =
	connect {
		val SQL = """
			SELECT AC.*, HA.home_userId , HA.home_count, HA.home_time
			from ArticleCounted AC
			LEFT JOIN HomeArticle HA ON AC.articleId= HA.articleId AND HA.home_userId=?
			where AC.articleId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.setInt(2, articleId)
		statement.executeQuery()
			.singleOf<Article>()
	}

fun getUserArticles(writerUserId: Int): List<Article> =
	connect {
		val SQL = """
			SELECT * from HomeArticle where home_userId=? AND writer_userId=?;
		""".trimIndent()
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, writerUserId)
		statement.setInt(2, writerUserId)
		statement.executeQuery()
			.listOf<Article>()
	} ?: emptyList()

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

fun saveArticle(article: Article): Article? =
	connect {
		val SQL1 = """
			INSERT OR REPLACE INTO Article
				(articleId, writer_userId, title, content, time, featured) VALUES (?,?,?,?,?,?) RETURNING articleId;
	 """.trimIndent()
		val SQL2 = """
			SELECT HA.* from HomeArticle HA
				JOIN Article A on HA.articleId = A.articleId
				where A.articleId=? AND HA.home_userId=?;
	 """.trimIndent()

		val stmt1 = it.prepareStatement(SQL1)
		if (article.articleId > 0)
			stmt1.setInt(1, article.articleId)
		else
			stmt1.setNull(1, Types.INTEGER)
		stmt1.setInt(2, article.writerUserId)
		stmt1.setString(3, article.title)
		stmt1.setString(4, article.content)
		stmt1.setLong(5, article.time.time)
		stmt1.setInt(6, if (article.featured) 1 else 0)
		val articleId = stmt1.executeQuery().singleOf<Int>() ?: return@connect null

		val stmt2 = it.prepareStatement(SQL2)
		stmt2.setInt(1, articleId)
		stmt2.setInt(2, article.writerUserId)
		stmt2.executeQuery()
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

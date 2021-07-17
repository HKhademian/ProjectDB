@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Article
import java.sql.Types

fun getArticle(articleId: Int): Article? =
	connect {
		val SQL = "SELECT * from `ArticleCounted` where `articleId`=?;"
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, articleId)
		statement.executeQuery()
			.tryRead<Article>()
	}

fun getUserArticles(userId: Int): List<Article> =
	connect {
		val SQL = "SELECT * from `ArticleCounted` where `writer_userId`=?;"
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Article>()
	} ?: emptyList()

fun saveArticle(article: Article): Article? =
	connect {
		val SQL = """INSERT OR REPLACE INTO `Article`
				 (`articleId`, `writer_userId`, `title`, `content`, `time`, `featured`)
				 VALUES (?,?,?,?,?,?)
				 RETURNING *;"""
		val statement = it.prepareStatement(SQL)
		if (article.articleId > 0) statement.setInt(1, article.articleId) else statement.setNull(1, Types.INTEGER)
		statement.setInt(2, article.writerUserId)
		statement.setString(3, article.title)
		statement.setString(4, article.content)
		statement.setLong(5, article.time.time)
		statement.setInt(6, if (article.featured) 1 else 0)
		statement.executeQuery()
			.tryRead<Article>()
	}

fun deleteArticle(articleId: Int): Article? =
	connect {
		val SQL = "DELETE FROM `Article` WHERE `articleId`=? RETURNING *;"
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, articleId)
		statement.executeQuery()
			.tryRead<Article>()
	}

fun getUserHomeArticles(userId: Int): List<Article> =
	connect {
		val SQL = """SELECT * from `HomeArticle` where home_userId=?;"""
		val statement = it.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Article>()
	} ?: emptyList()

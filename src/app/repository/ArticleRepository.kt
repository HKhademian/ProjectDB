@file:JvmName("Repository")
@file:JvmMultifileClass

package app.repository

import app.model.Article
import java.sql.Connection
import java.sql.Types

fun getArticle(articleId: Int): Article? {
	val SQL = "SELECT * from `ArticleCounted` where `articleId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, articleId)
		statement.executeQuery()
			.tryRead<Article>()
	}
}

fun getUserArticles(userId: Int): List<Article> {
	val SQL = "SELECT * from `ArticleCounted` where `writer_userId`=?;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Article>()
	}!!
}

fun saveArticle(article: Article): Article? {
	val SQL = """INSERT OR REPLACE INTO `Article`
				 (`articleId`, `writer_userId`, `title`, `content`, `time`, `featured`)
				 VALUES (?,?,?,?,?,?)
				 RETURNING *;"""
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		if (article.articleId > 0) statement.setInt(1, article.articleId) else statement.setNull(1, Types.INTEGER)
		statement.setInt(2, article.writerUserId)
		statement.setString(3, article.title)
		statement.setString(4, article.content)
		statement.setLong(5, article.time.time)
		statement.setInt(6, if (article.featured) 1 else 0)
		statement.executeQuery()
			.tryRead<Article>()
	}
}

fun deleteArticle(articleId: Int): Article? {
	val SQL = "DELETE FROM `Article` WHERE `articleId`=? RETURNING *;"
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, articleId)
		statement.executeQuery()
			.tryRead<Article>()
	}
}

fun getUserHomeArticles(userId: Int): List<Article> {
	val SQL = """SELECT * from `Article`
			 where `articleId` in	(select `articleId` from Home where `userId`=? order by `time` desc);"""
	return connect { connection: Connection ->
		val statement = connection.prepareStatement(SQL)
		statement.setInt(1, userId)
		statement.executeQuery()
			.list<Article>()
	}!!
}

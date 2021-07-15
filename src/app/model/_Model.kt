package app.model

import java.sql.ResultSet
import java.sql.SQLException
import java.util.*


fun ResultSet.tryInt(column: String?): Int? =
  try {
    getInt(column)
  } catch (ex: SQLException) {
    null
  }

fun ResultSet.tryLong(column: String?): Long? =
  try {
    getLong(column)
  } catch (ex: SQLException) {
    null
  }

fun ResultSet.tryString(column: String?): String? =
  try {
    getString(column)
  } catch (ex: SQLException) {
    null
  }

fun ResultSet.tryByteArray(column: String?): ByteArray? =
  try {
    getBytes(column)
  } catch (ex: SQLException) {
    null
  }


fun ResultSet.tryDate(column: String?): Date? =
  try {
    getLong(column).let { if (it > 0) Date(it) else null }
  } catch (ex: SQLException) {
    null
  }

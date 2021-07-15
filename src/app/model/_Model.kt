package app.model;

import com.sun.istack.internal.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class _BaseModel {
  protected static Integer tryInt(ResultSet res, String column) {
    try {
      return res.getInt(column);
    } catch (SQLException ex) {
      return null;
    }
  }

  protected static int tryInt(ResultSet res, String column, int defVal) {
    try {
      return res.getInt(column);
    } catch (SQLException ex) {
      return defVal;
    }
  }

  protected static Long tryLong(ResultSet res, String column) {
    try {
      return res.getLong(column);
    } catch (SQLException ex) {
      return null;
    }
  }

  protected static long tryLong(ResultSet res, String column, long defVal) {
    try {
      return res.getLong(column);
    } catch (SQLException ex) {
      return defVal;
    }
  }

  protected static String tryString(ResultSet res, String column) {
    try {
      return res.getString(column);
    } catch (SQLException ex) {
      return null;
    }
  }

  protected static @NotNull
  String tryString(ResultSet res, String column, @NotNull String defVal) {
    try {
      return res.getString(column);
    } catch (SQLException ex) {
      return defVal;
    }
  }

  protected static byte[] tryByteArray(ResultSet res, String column) {
    try {
      return res.getBytes(column);
    } catch (SQLException ex) {
      return null;
    }
  }

  protected static @NotNull
  byte[] tryByteArray(ResultSet res, String column, @NotNull byte[] defVal) {
    try {
      return res.getBytes(column);
    } catch (SQLException ex) {
      return defVal;
    }
  }
}

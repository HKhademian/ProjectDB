package app.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
  private static final String DB_PATH = "./db.sqlite";
  private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

  private static Connection connection;

  private Database() {
  }

  public static Connection getConnection() {
    return connection;
  }

  public static void connect() {
    try {
      connection = DriverManager.getConnection(DB_URL);
      System.out.println("Connection to SQLite has been established.");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (connection != null) {
          connection.close();
          connection = null;
        }
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public static void disconnect() {
    try {
      if (connection != null) {
        connection.close();
        connection = null;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}

package app.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class _BaseRepository {
  private static final String DB_NAME = "db.sqlite";
  public static Throwable lastError = null;

  public static <T> T connect(final Command<T> action) {
    final String db = "jdbc:sqlite:" + DB_NAME;
    try (Connection connection = DriverManager.getConnection(db)) {
      lastError = null;
      return action.run(connection);
    } catch (SQLException ex) {
      lastError = ex;
      ex.printStackTrace();
      return null;
    }
  }

  public static interface Command<T> {
    T run(Connection connection) throws SQLException;
  }
}

package app.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class _BaseRepository {
  private static final String DB_URL = "jdbc:sqlite:";
  private static final String DB_NAME = "db.sqlite";

  public static <T> T connect(final Command<T> action) {
    final var db = DB_URL + DB_NAME;//new File(System.getProperty("user.dir"), DB_NAME).
    try (var connection = DriverManager.getConnection(db)) {
      return action.run(connection);
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  protected interface Command<T> {
    T run(Connection connection) throws SQLException;
  }
}

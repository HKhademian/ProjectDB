package repository;

import java.sql.Connection;

public class _BaseRepository {

  public static Connection getConnection() {
    return Database.getConnection();
  }

}

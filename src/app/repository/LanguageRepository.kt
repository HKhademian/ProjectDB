package app.repository;

import app.model.Language;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class LanguageRepository extends _BaseRepository {
  private LanguageRepository() {
  }

  public static List<Language> listLanguages() {
    final String SQL = "SELECT * from `Language`;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      final ResultSet res = statement.executeQuery();
      final List<Language> result = new ArrayList<>();
      while (res.next())
        result.add(Language.from(res));
      return result;
    });
  }

  public static Language getLanguage(String langCode) {
    final String SQL = "SELECT * from `Language` where `langCode`=?;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setString(1, langCode);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Language.from(res) : null;
    });
  }

  public static Language addLanguage(String langCode, String title) {
    final String SQL = "INSERT INTO `Language` VALUES (?,?) RETURNING *;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setString(1, langCode);
      statement.setString(2, title);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Language.from(res) : null;
    });
  }

}

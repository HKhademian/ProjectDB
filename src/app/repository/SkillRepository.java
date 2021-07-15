package app.repository;

import app.model.Skill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class SkillRepository extends _BaseRepository {
  private SkillRepository() {
  }

  public static List<Skill> listSkills() {
    final String SQL = "SELECT * from `Skill`;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      final ResultSet res = statement.executeQuery();
      final List<Skill> result = new ArrayList<>();
      while (res.next())
        result.add(Skill.from(res));
      return result;
    });
  }

  public static Skill getSkill(int skillId) {
    final String SQL = "SELECT * from `Skill` where `skillId`=?;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Skill.from(res) : null;
    });
  }

  public static Skill addSkill(String title) {
    final String SQL = "INSERT INTO `Skill` VALUES (null,?) RETURNING *;";
    return connect(connection -> {
      final PreparedStatement statement = connection.prepareStatement(SQL);
      statement.setString(1, title);
      final ResultSet res = statement.executeQuery();
      return res.next() ? Skill.from(res) : null;
    });
  }


}

package app.model;

import java.sql.ResultSet;

public class Skill extends _BaseModel {
  public int skillId;
  public String title;

  public Skill(int skillId, String title) {
    this.skillId = skillId;
    this.title = title;
  }

  public static Skill from(ResultSet res) {
    return new Skill(
      tryInt(res, "skillId", 0),
      tryString(res, "title")
    );
  }
}

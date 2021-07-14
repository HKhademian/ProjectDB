package app.model;

import java.sql.ResultSet;
import java.util.Date;

public class User extends _BaseModel {
  public int userId;
  public String username;
  public String email;
  public String phone;
  public String name;
  public String intro;
  public String about;
  public byte[] avatar;
  public Date birthday;

  public User(int userId, String username, String email, String phone, String name, String intro, String about, byte[] avatar, Date birthday) {
    this.userId = userId;
    this.username = username;
    this.email = email;
    this.phone = phone;
    this.name = name;
    this.intro = intro;
    this.about = about;
    this.avatar = avatar;
    this.birthday = birthday;
  }

  public static User from(ResultSet res) {
    return new User(
      tryInt(res, "userId", 0),
      tryString(res, "username"),
      tryString(res, "email"),
      tryString(res, "phone"),
      tryString(res, "name"),
      tryString(res, "intro"),
      tryString(res, "about"),
      tryByteArray(res, "avatar"),
      new Date(tryInt(res, "birthday", 0))
    );
  }
}

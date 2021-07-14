package app.model;

import java.util.Date;

public class User {
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

}

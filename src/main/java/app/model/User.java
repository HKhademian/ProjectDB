package app.model;

import java.util.Date;

public record User(
  int userId,
  String username,
  String email,
  String phone,
  String name,
  String intro,
  String about,
  byte[] avatar,
  Date birthday
) {
}

package ch.heigvd.dai.users;

import java.util.Objects;

public class User {

  public Integer id;
  public String firstName;
  public String lastName;
  public String email;
  public String password;

  public User() {
    // Empty constructor for serialisation/deserialization
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, password);
  }
}

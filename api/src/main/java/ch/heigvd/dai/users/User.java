package ch.heigvd.dai.users;

import java.util.Objects;

/** Represents a User entity with basic attributes like ID, name, email, and password. */
public class User {

  public Integer id;
  public String firstName;
  public String lastName;
  public String email;
  public String password;

  /** Empty constructor for serialization/deserialization. */
  public User() {}

  /**
   * Computes the hash code for the User object based on its fields.
   *
   * @return The hash code of the User object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, password);
  }
}

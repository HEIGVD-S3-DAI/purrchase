package ch.heigvd.dai.cats;

import java.util.Objects;

/** Represents a Cat entity with various attributes and methods to manage it. */
public class Cat {

  public Integer id;
  public String name;
  public String breed;
  public Integer age;
  public String color;
  public String imageURL;
  public Integer userId;

  /** Default constructor for serialization and deserialization. */
  public Cat() {}

  /**
   * Computes the hash code for the Cat based on its attributes.
   *
   * @return The hash code for the Cat.
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, name, breed, age, color, imageURL, userId);
  }

  /**
   * Checks if the Cat matches the specified filters.
   *
   * @param breed The breed filter.
   * @param color The color filter.
   * @param age The age filter.
   * @param userId The user ID filter.
   * @return True if the Cat matches all filters, false otherwise.
   */
  public boolean matchesFilters(String breed, String color, String age, String userId) {
    if (breed != null && !this.breed.equals(breed)) return false;
    if (color != null && !this.color.equals(color)) return false;
    if (age != null && !this.age.equals(Integer.valueOf(age))) return false;
    if (userId != null && !this.userId.equals(Integer.valueOf(userId))) return false;
    return true;
  }
}

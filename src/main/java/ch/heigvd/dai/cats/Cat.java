package ch.heigvd.dai.cats;

import java.util.Objects;

public class Cat {

  public Integer id;
  public String name;
  public String breed;
  public Integer age;
  public String color;
  public String imageURL;

  public Integer userId;

  public Cat() {
    // Empty constructor for serialisation/deserialization
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, breed, age, color, imageURL, userId);
  }

  public boolean matchesFilters(String breed, String color, String age, String userId) {
    if (breed != null && !this.breed.equals(breed)) return false;
    if (color != null && !this.color.equals(color)) return false;
    if (age != null && !this.age.equals(Integer.valueOf(age))) return false;
    if (userId != null && !this.userId.equals(Integer.valueOf(userId))) return false;
    return true;
  }
}

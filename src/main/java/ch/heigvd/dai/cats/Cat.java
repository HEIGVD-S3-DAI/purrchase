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
}

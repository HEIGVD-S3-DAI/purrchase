package ch.heigvd.dai.cats;

public class CatFilters {
  public String breed;
  public String color;
  public String age;
  public String userId;

  public CatFilters(String breed, String color, String age, String userId) {
    this.breed = breed;
    this.color = color;
    this.age = age;
    this.userId = userId;
  }

  public String toString() {
    StringBuilder filter = new StringBuilder();
    filter.append("breed=");
    if (breed != null) filter.append(breed);
    filter.append("&color=");
    if (color != null) filter.append(color);
    filter.append("&age=");
    if (age != null) filter.append(age);
    filter.append("&userId=");
    if (userId != null) filter.append(userId);
    return filter.toString();
  }
}

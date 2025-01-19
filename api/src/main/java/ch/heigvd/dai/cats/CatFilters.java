package ch.heigvd.dai.cats;

/** Represents filters for querying Cat entities. */
public class CatFilters {
  public String breed;
  public String color;
  public String age;
  public String userId;

  /**
   * Constructor for CatFilters.
   *
   * @param breed The breed filter.
   * @param color The color filter.
   * @param age The age filter.
   * @param userId The user ID filter.
   */
  public CatFilters(String breed, String color, String age, String userId) {
    this.breed = breed;
    this.color = color;
    this.age = age;
    this.userId = userId;
  }

  /**
   * Converts the filter attributes to a string representation.
   *
   * @return A string representing the filter values.
   */
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

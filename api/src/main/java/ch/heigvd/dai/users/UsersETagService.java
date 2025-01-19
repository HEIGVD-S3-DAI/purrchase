package ch.heigvd.dai.users;

import ch.heigvd.dai.common.etag.ETagResourceService;
import java.util.concurrent.ConcurrentHashMap;

/** Service for managing ETags for a collection of User resources. */
public class UsersETagService extends ETagResourceService<User> {
  private static final String COLLECTION_ID = "users_collection";

  /**
   * Constructor for UsersETagService.
   *
   * @param users The collection of User resources managed by the service.
   */
  public UsersETagService(ConcurrentHashMap<Integer, User> users) {
    super(users);
  }

  /**
   * Retrieves the unique identifier for the User collection.
   *
   * @return The collection identifier as a string.
   */
  @Override
  protected String getCollectionId() {
    return COLLECTION_ID;
  }

  /**
   * Retrieves the unique identifier for a User resource.
   *
   * @param user The User resource.
   * @return The ID of the User resource.
   */
  @Override
  protected Integer getResourceId(User user) {
    return user.id;
  }
}

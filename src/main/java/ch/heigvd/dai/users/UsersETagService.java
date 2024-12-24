package ch.heigvd.dai.users;

import ch.heigvd.dai.common.etag.ETagResourceService;
import java.util.concurrent.ConcurrentHashMap;

public class UsersETagService extends ETagResourceService<User> {
  private static final String COLLECTION_ID = "users_collection";

  public UsersETagService(ConcurrentHashMap<Integer, User> users) {
    super(users);
  }

  @Override
  protected String getCollectionId() {
    return COLLECTION_ID;
  }

  @Override
  protected Integer getResourceId(User user) {
    return user.id;
  }
}

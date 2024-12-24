package ch.heigvd.dai.cats;

import ch.heigvd.dai.common.etag.ETagResourceService;
import java.util.concurrent.ConcurrentHashMap;

public class CatsETagService extends ETagResourceService<Cat> {
  private static final String COLLECTION_ID = "cats_collection";

  public CatsETagService(ConcurrentHashMap<Integer, Cat> cats) {
    super(cats);
  }

  @Override
  protected String getCollectionId() {
    return COLLECTION_ID;
  }

  @Override
  protected Integer getResourceId(Cat cat) {
    return cat.id;
  }
}

package ch.heigvd.dai.cats;

import ch.heigvd.dai.common.etag.ETagResourceService;
import java.util.concurrent.ConcurrentHashMap;

/** Service for managing ETags for a collection of Cat resources. */
public class CatsETagService extends ETagResourceService<Cat> {
  private static final String COLLECTION_ID = "cats_collection";

  /**
   * Constructor for CatsETagService.
   *
   * @param cats The collection of Cat resources managed by the service.
   */
  public CatsETagService(ConcurrentHashMap<Integer, Cat> cats) {
    super(cats);
  }

  /**
   * Retrieves the unique identifier for the Cat collection.
   *
   * @return The collection identifier as a string.
   */
  @Override
  protected String getCollectionId() {
    return COLLECTION_ID;
  }

  /**
   * Retrieves the unique identifier for a Cat resource.
   *
   * @param cat The Cat resource.
   * @return The ID of the Cat resource.
   */
  @Override
  protected Integer getResourceId(Cat cat) {
    return cat.id;
  }
}

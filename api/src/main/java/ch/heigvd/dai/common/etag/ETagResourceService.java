package ch.heigvd.dai.common.etag;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract service for managing ETags for a collection of resources.
 *
 * @param <T> The type of the resource managed by the service.
 */
public abstract class ETagResourceService<T> {
  private final ETagService etagService;
  private final ConcurrentHashMap<Integer, T> resources;

  /**
   * Retrieves the unique identifier for the collection.
   *
   * @return The unique identifier for the collection.
   */
  protected abstract String getCollectionId();

  /**
   * Constructor for the ETagResourceService.
   *
   * @param resources The collection of resources managed by the service.
   */
  public ETagResourceService(ConcurrentHashMap<Integer, T> resources) {
    this.resources = resources;
    this.etagService = new ETagService(new DefaultETagGenerator());
    updateCollectionETag();
  }

  /** Updates the ETag for the entire resource collection. */
  public void updateCollectionETag() {
    etagService.updateETag(getCollectionId(), resources);
  }

  /**
   * Updates the ETag for the collection with additional filters applied.
   *
   * @param filters A string representing the filters applied to the collection.
   */
  public void updateCollectionETagWithFilters(String filters) {
    etagService.updateETag(getCollectionId() + filters, resources);
  }

  /**
   * Retrieves the ETag for the entire resource collection.
   *
   * @return The ETag for the collection as a string.
   */
  public String getCollectionETag() {
    return etagService.getETag(getCollectionId());
  }

  /**
   * Retrieves the ETag for the resource collection with filters applied.
   *
   * @param filters A string representing the filters applied to the collection.
   * @return The ETag for the filtered collection as a string.
   */
  public String getCollectionETagWithFilters(String filters) {
    return etagService.getETag(getCollectionId() + filters);
  }

  /**
   * Retrieves the ETag for a specific resource.
   *
   * @param resource The resource to retrieve the ETag for.
   * @return The ETag for the resource as a string.
   */
  public String getResourceETag(T resource) {
    String resourceId = String.valueOf(getResourceId(resource));
    etagService.updateETag(resourceId, resource);
    return etagService.getETag(resourceId);
  }

  /**
   * Removes the ETag for a specific resource.
   *
   * @param resourceId The ID of the resource to remove the ETag for.
   */
  public void removeResourceETag(Integer resourceId) {
    etagService.removeETag(String.valueOf(resourceId));
  }

  /**
   * Validates the ETag for a specific resource.
   *
   * @param resourceId The ID of the resource.
   * @param clientETag The ETag provided by the client.
   * @return True if the ETag matches, false otherwise.
   */
  public boolean validateResourceETag(Integer resourceId, String clientETag) {
    return etagService.validateETag(String.valueOf(resourceId), clientETag);
  }

  /**
   * Validates the ETag for the resource collection.
   *
   * @param clientETag The ETag provided by the client.
   * @return True if the ETag matches, false otherwise.
   */
  public boolean validateCollectionETag(String clientETag) {
    return etagService.validateETag(getCollectionId(), clientETag);
  }

  /**
   * Validates the ETag for the resource collection with filters applied.
   *
   * @param clientETag The ETag provided by the client.
   * @param filters A string representing the filters applied to the collection.
   * @return True if the ETag matches, false otherwise.
   */
  public boolean validateCollectionETagWithFilter(String clientETag, String filters) {
    return etagService.validateETag(getCollectionId() + filters, clientETag);
  }

  /**
   * Retrieves the unique identifier for a resource.
   *
   * @param resource The resource to retrieve the ID for.
   * @return The unique identifier for the resource.
   */
  protected abstract Integer getResourceId(T resource);
}

package ch.heigvd.dai.common.etag;

import java.util.concurrent.ConcurrentHashMap;

/** Service for managing ETags for resources. */
public class ETagService {
  private final ETagGenerator etagGenerator;
  private final ConcurrentHashMap<String, String> resourceETags;

  /**
   * Constructor for ETagService.
   *
   * @param generator The ETagGenerator used to generate ETags.
   */
  public ETagService(ETagGenerator generator) {
    this.etagGenerator = generator;
    this.resourceETags = new ConcurrentHashMap<>();
  }

  /**
   * Retrieves the ETag for a resource by its ID.
   *
   * @param resourceId The ID of the resource.
   * @return The ETag as a string, or null if not found.
   */
  public String getETag(String resourceId) {
    return resourceETags.get(resourceId);
  }

  /**
   * Updates the ETag for a resource.
   *
   * @param resourceId The ID of the resource.
   * @param resource The resource to update the ETag for.
   */
  public void updateETag(String resourceId, Object resource) {
    String newETag = etagGenerator.generateETag(resource);
    resourceETags.put(resourceId, newETag);
  }

  /**
   * Validates the ETag for a resource.
   *
   * @param resourceId The ID of the resource.
   * @param clientETag The ETag provided by the client.
   * @return True if the ETag matches, false otherwise.
   */
  public boolean validateETag(String resourceId, String clientETag) {
    if (clientETag == null) return false;
    String currentETag = getETag(resourceId);
    return currentETag != null && currentETag.equals(clientETag);
  }

  /**
   * Removes the ETag for a resource.
   *
   * @param resourceId The ID of the resource.
   */
  public void removeETag(String resourceId) {
    resourceETags.remove(resourceId);
  }
}

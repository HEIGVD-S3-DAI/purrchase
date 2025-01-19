package ch.heigvd.dai.common.etag;

/** Interface for generating ETags for resources. */
public interface ETagGenerator {
  /**
   * Generates an ETag for the given resource.
   *
   * @param resource The resource to generate the ETag for.
   * @return The generated ETag as a string.
   */
  String generateETag(Object resource);
}

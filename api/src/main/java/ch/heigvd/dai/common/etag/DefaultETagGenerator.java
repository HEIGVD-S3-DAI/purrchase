package ch.heigvd.dai.common.etag;

/**
 * Default implementation of ETagGenerator that generates ETags using the hash code of the resource.
 */
public class DefaultETagGenerator implements ETagGenerator {
  /**
   * Generates an ETag for the given resource based on its hash code.
   *
   * @param resource The resource to generate the ETag for.
   * @return A string representation of the hash code as the ETag.
   */
  @Override
  public String generateETag(Object resource) {
    return Integer.toHexString(resource.hashCode());
  }
}

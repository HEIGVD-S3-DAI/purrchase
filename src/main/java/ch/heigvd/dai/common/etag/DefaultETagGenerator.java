package ch.heigvd.dai.common.etag;

public class DefaultETagGenerator implements ETagGenerator {
  @Override
  public String generateETag(Object resource) {
    return Integer.toHexString(resource.hashCode());
  }
}

package ch.heigvd.dai.common.etag;

public interface ETagGenerator {
  String generateETag(Object resource);
}

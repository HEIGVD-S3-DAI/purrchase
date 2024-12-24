package ch.heigvd.dai.common.etag;

import java.util.concurrent.ConcurrentHashMap;

public class ETagService {
  private final ETagGenerator etagGenerator;
  private final ConcurrentHashMap<String, String> resourceETags;

  public ETagService(ETagGenerator generator) {
    this.etagGenerator = generator;
    this.resourceETags = new ConcurrentHashMap<>();
  }

  public String getETag(String resourceId) {
    return resourceETags.get(resourceId);
  }

  public void updateETag(String resourceId, Object resource) {
    String newETag = etagGenerator.generateETag(resource);
    resourceETags.put(resourceId, newETag);
  }

  public boolean validateETag(String resourceId, String clientETag) {
    if (clientETag == null) return false;
    String currentETag = getETag(resourceId);
    return currentETag != null && currentETag.equals(clientETag);
  }

  public void removeETag(String resourceId) {
    resourceETags.remove(resourceId);
  }
}

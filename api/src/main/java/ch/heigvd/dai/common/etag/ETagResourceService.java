package ch.heigvd.dai.common.etag;

import java.util.concurrent.ConcurrentHashMap;

public abstract class ETagResourceService<T> {
  private final ETagService etagService;
  private final ConcurrentHashMap<Integer, T> resources;

  protected abstract String getCollectionId();

  public ETagResourceService(ConcurrentHashMap<Integer, T> resources) {
    this.resources = resources;
    this.etagService = new ETagService(new DefaultETagGenerator());
    updateCollectionETag();
  }

  public void updateCollectionETag() {
    etagService.updateETag(getCollectionId(), resources);
  }

  public void updateCollectionETagWithFilters(String filters) {
    etagService.updateETag(getCollectionId() + filters, resources);
  }

  public String getCollectionETag() {
    return etagService.getETag(getCollectionId());
  }

  public String getCollectionETagWithFilters(String filters) {
    return etagService.getETag(getCollectionId() + filters);
  }

  public String getResourceETag(T resource) {
    String resourceId = String.valueOf(getResourceId(resource));
    etagService.updateETag(resourceId, resource);
    return etagService.getETag(resourceId);
  }

  public void removeResourceETag(Integer resourceId) {
    etagService.removeETag(String.valueOf(resourceId));
  }

  public boolean validateResourceETag(Integer resourceId, String clientETag) {
    return etagService.validateETag(String.valueOf(resourceId), clientETag);
  }

  public boolean validateCollectionETag(String clientETag) {
    return etagService.validateETag(getCollectionId(), clientETag);
  }

  public boolean validateCollectionETagWithFilter(String clientETag, String filters) {
    return etagService.validateETag(getCollectionId() + filters, clientETag);
  }

  protected abstract Integer getResourceId(T resource);
}

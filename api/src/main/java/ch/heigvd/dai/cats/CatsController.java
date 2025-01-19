package ch.heigvd.dai.cats;

import ch.heigvd.dai.auth.AuthMiddleware;
import io.javalin.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Controller for managing Cat entities, including creation, retrieval, updates, and deletion. */
public class CatsController {
  private final ConcurrentHashMap<Integer, Cat> cats;
  private final AtomicInteger catId = new AtomicInteger(1);
  private final CatsETagService etagService;

  /**
   * Constructor for CatsController.
   *
   * @param cats The collection of Cat entities.
   * @param etagService The ETag service for managing Cat ETags.
   */
  public CatsController(ConcurrentHashMap<Integer, Cat> cats, CatsETagService etagService) {
    this.cats = cats;
    this.etagService = etagService;
  }

  /**
   * Handles the creation of a new Cat entity.
   *
   * @param ctx The request context.
   * @throws HttpResponseException if validation fails or input data is missing.
   */
  public void create(Context ctx) {
    Cat newCat =
        ctx.bodyValidator(Cat.class)
            .check(obj -> obj.name != null, "Missing name")
            .check(obj -> obj.breed != null, "Missing breed")
            .check(obj -> obj.age != null, "Missing age")
            .check(obj -> obj.age >= 0, "Age must be positive")
            .check(obj -> obj.color != null, "Missing color")
            .check(obj -> obj.imageURL != null, "Missing imageURL")
            .get();

    Cat cat = new Cat();
    cat.id = catId.getAndIncrement();
    cat.name = newCat.name;
    cat.breed = newCat.breed;
    cat.age = newCat.age;
    cat.color = newCat.color;
    cat.imageURL = newCat.imageURL;
    cat.userId = Integer.valueOf(ctx.cookie("user"));

    cats.put(cat.id, cat);

    etagService.updateCollectionETag();
    ctx.header("ETag", etagService.getResourceETag(cat));
    ctx.status(HttpStatus.CREATED);
    ctx.json(cat);
  }

  /**
   * Retrieves a specific Cat entity by its ID.
   *
   * @param ctx The request context.
   * @throws NotModifiedResponse if the ETag matches the current resource.
   * @throws NotFoundResponse if the Cat is not found.
   */
  public void getOne(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();

    if (etagService.validateResourceETag(id, ctx.header("If-None-Match"))) {
      throw new NotModifiedResponse();
    }

    Cat cat = cats.get(id);
    if (cat == null) {
      throw new NotFoundResponse("Cat not found");
    }

    ctx.header("ETag", etagService.getResourceETag(cat));
    ctx.json(cat);
  }

  /**
   * Retrieves a collection of Cat entities matching the specified filters.
   *
   * @param ctx The request context.
   * @throws NotModifiedResponse if the ETag matches the current filtered collection.
   * @throws NotFoundResponse if no Cats match the filters.
   */
  public void getMany(Context ctx) {
    String breed = ctx.queryParam("breed");
    String color = ctx.queryParam("color");
    String age = ctx.queryParam("age");
    String userId = ctx.queryParam("userId");
    CatFilters catFilter = new CatFilters(breed, color, age, userId);
    String filters = catFilter.toString();

    if (etagService.validateCollectionETagWithFilter(ctx.header("If-None-Match"), filters)) {
      throw new NotModifiedResponse();
    }

    List<Cat> cats = new ArrayList<>();
    for (Cat cat : this.cats.values()) {
      if (cat.matchesFilters(breed, color, age, userId)) {
        cats.add(cat);
      }
    }

    if (cats.isEmpty()) {
      throw new NotFoundResponse("Cat not found");
    }

    etagService.updateCollectionETagWithFilters(filters);
    ctx.header("ETag", etagService.getCollectionETagWithFilters(filters));
    ctx.json(cats);
  }

  /**
   * Updates a specific Cat entity.
   *
   * @param ctx The request context.
   * @throws PreconditionFailedResponse if the client's ETag does not match.
   * @throws NotFoundResponse if the Cat is not found.
   * @throws ForbiddenResponse if the user does not have permission to update the Cat.
   */
  public void update(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();

    if (!etagService.validateResourceETag(id, ctx.header("If-Match"))) {
      throw new PreconditionFailedResponse();
    }

    Cat cat = cats.get(id);
    if (cat == null) {
      throw new NotFoundResponse("Cat not found");
    }

    Integer userId = ctx.attribute(AuthMiddleware.USER_ID_KEY);
    if (!cat.userId.equals(userId)) {
      throw new ForbiddenResponse("You don't have permission to update this cat");
    }

    Cat updatedCat =
        ctx.bodyValidator(Cat.class)
            .check(obj -> obj.name != null, "Missing name")
            .check(obj -> obj.breed != null, "Missing breed")
            .check(obj -> obj.age != null, "Missing age")
            .check(obj -> obj.age >= 0, "Age must be positive")
            .check(obj -> obj.color != null, "Missing color")
            .check(obj -> obj.imageURL != null, "Missing imageURL")
            .get();

    cat.name = updatedCat.name;
    cat.breed = updatedCat.breed;
    cat.age = updatedCat.age;
    cat.color = updatedCat.color;
    cat.imageURL = updatedCat.imageURL;

    cats.put(id, cat);
    etagService.updateCollectionETag();

    ctx.header("ETag", etagService.getResourceETag(cat));
    ctx.json(cat);
  }

  /**
   * Deletes a specific Cat entity.
   *
   * @param ctx The request context.
   * @throws PreconditionFailedResponse if the client's ETag does not match.
   * @throws NotFoundResponse if the Cat is not found.
   * @throws ForbiddenResponse if the user does not have permission to delete the Cat.
   */
  public void delete(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();

    if (!etagService.validateResourceETag(id, ctx.header("If-Match"))) {
      throw new PreconditionFailedResponse();
    }

    Cat cat = cats.get(id);
    if (cat == null) {
      throw new NotFoundResponse("Cat not found");
    }

    Integer userId = ctx.attribute(AuthMiddleware.USER_ID_KEY);
    if (!cat.userId.equals(userId)) {
      throw new ForbiddenResponse("You don't have permission to delete this cat");
    }

    cats.remove(id);
    etagService.removeResourceETag(id);
    etagService.updateCollectionETag();

    ctx.status(HttpStatus.NO_CONTENT);
  }
}

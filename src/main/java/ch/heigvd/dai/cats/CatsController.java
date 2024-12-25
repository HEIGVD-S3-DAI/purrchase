package ch.heigvd.dai.cats;

import ch.heigvd.dai.auth.AuthMiddleware;
import io.javalin.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CatsController {
  private final ConcurrentHashMap<Integer, Cat> cats;
  private final AtomicInteger catId = new AtomicInteger(1);

  private final CatsETagService etagService;

  public CatsController(ConcurrentHashMap<Integer, Cat> cats, CatsETagService etagService) {
    this.cats = cats;
    this.etagService = etagService;
  }

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

    cats.put(cat.id, cat);

    // Generate ETag for the cat and store it in the cache
    etagService.updateCollectionETag();
    ctx.header("ETag", etagService.getResourceETag(cat));

    // Return response
    ctx.status(HttpStatus.CREATED);

    ctx.json(cat);
  }

  public void getOne(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();

    // Check the If-None-Match header
    if (etagService.validateResourceETag(id, ctx.header("If-None-Match"))) {
      throw new NotModifiedResponse();
    }

    // Check if the cat exists
    Integer userId = ctx.attribute(AuthMiddleware.USER_ID_KEY);
    Cat cat = cats.get(id);
    if (cat == null || cat.userId != userId) {
      throw new NotFoundResponse();
    }

    // Add the ETag to the response header
    ctx.header("ETag", etagService.getResourceETag(cat));
    ctx.json(cat);
  }

  public void getMany(Context ctx) {
    // Check the If-None-Match header
    if (etagService.validateCollectionETag(ctx.header("If-None-Match"))) {
      throw new NotModifiedResponse();
    }

    Integer userId = ctx.attribute(AuthMiddleware.USER_ID_KEY);

    List<Cat> cats = new ArrayList<>();

    for (Cat cat : this.cats.values()) {
      if (!cat.userId.equals(userId)) {
        continue;
      }
      cats.add(cat);
    }

    ctx.header("ETag", etagService.getCollectionETag());
    ctx.json(cats);
  }

  public void update(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();

    // Get the client's ETag from the If-Match header
    if (!etagService.validateResourceETag(id, ctx.header("If-Match"))) {
      throw new PreconditionFailedResponse();
    }

    // Check if the cat exists
    Cat cat = cats.get(id);
    Integer userId = ctx.attribute(AuthMiddleware.USER_ID_KEY);
    if (cat == null || cat.userId != userId) {
      throw new NotFoundResponse();
    }

    // Validate and parse the updated cat from the request
    Cat updatedCat =
        ctx.bodyValidator(Cat.class)
            .check(obj -> obj.name != null, "Missing name")
            .check(obj -> obj.breed != null, "Missing breed")
            .check(obj -> obj.age != null, "Missing age")
            .check(obj -> obj.age >= 0, "Age must be positive")
            .check(obj -> obj.color != null, "Missing color")
            .check(obj -> obj.imageURL != null, "Missing imageURL")
            .get();

    // Update cat fields
    cat.name = updatedCat.name;
    cat.breed = updatedCat.breed;
    cat.age = updatedCat.age;
    cat.color = updatedCat.color;
    cat.imageURL = updatedCat.imageURL;

    // Save the updated cat
    cats.put(id, cat);
    etagService.updateCollectionETag();

    // Set the new ETag in the response header
    ctx.header("ETag", etagService.getResourceETag(cat));
    ctx.json(cat);
  }

  public void delete(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();

    // Get the client's ETag from the If-Match header
    if (!etagService.validateResourceETag(id, ctx.header("If-Match"))) {
      throw new PreconditionFailedResponse();
    }

    // Check if the cat exists
    Cat cat = cats.get(id);
    Integer userId = ctx.attribute(AuthMiddleware.USER_ID_KEY);
    if (cat == null || cat.userId != userId) {
      throw new NotFoundResponse();
    }

    cats.remove(id);
    etagService.removeResourceETag(id);
    etagService.updateCollectionETag();

    ctx.status(HttpStatus.NO_CONTENT);
  }
}

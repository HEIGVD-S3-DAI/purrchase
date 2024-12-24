package ch.heigvd.dai.users;

import ch.heigvd.dai.auth.AuthMiddleware;
import io.javalin.http.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UsersController {
  private final ConcurrentHashMap<Integer, User> users;
  private final AtomicInteger userId = new AtomicInteger(1);

  private final UsersETagService etagService;

  public UsersController(ConcurrentHashMap<Integer, User> users, UsersETagService etagService) {
    this.users = users;
    this.etagService = etagService;
  }

  public void create(Context ctx) {
    User newUser =
        ctx.bodyValidator(User.class)
            .check(obj -> obj.firstName != null, "Missing first name")
            .check(obj -> obj.lastName != null, "Missing last name")
            .check(obj -> obj.email != null, "Missing email")
            .check(obj -> obj.password != null, "Missing password")
            .get();

    for (User user : users.values()) {
      if (user.email.equalsIgnoreCase(newUser.email)) {
        throw new ConflictResponse();
      }
    }

    User user = new User();

    user.id = userId.getAndIncrement();
    user.firstName = newUser.firstName;
    user.lastName = newUser.lastName;
    user.email = newUser.email;
    user.password = newUser.password;

    users.put(user.id, user);

    // Generate ETag for the user and store it in the cache
    etagService.updateCollectionETag();
    ctx.header("ETag", etagService.getResourceETag(user));

    // Return response
    ctx.status(HttpStatus.CREATED);

    ctx.json(user);
  }

  public void update(Context ctx) {
    Integer id = ctx.attribute(AuthMiddleware.USER_ID_KEY);

    // Get the client's ETag from the If-Match header
    if (!etagService.validateResourceETag(id, ctx.header("If-Match"))) {
      throw new PreconditionFailedResponse();
    }

    // Check if the user exists
    User user = users.get(id);
    if (user == null) {
      throw new NotFoundResponse();
    }

    // Validate and parse the updated user from the request
    User updatedUser =
        ctx.bodyValidator(User.class)
            .check(obj -> obj.firstName != null, "Missing first name")
            .check(obj -> obj.lastName != null, "Missing last name")
            .check(obj -> obj.email != null, "Missing email")
            .check(obj -> obj.password != null, "Missing password")
            .get();

    // Update user fields
    user.firstName = updatedUser.firstName;
    user.lastName = updatedUser.lastName;
    user.email = updatedUser.email;
    user.password = updatedUser.password;

    // Save the updated user
    users.put(id, user);
    etagService.updateCollectionETag();

    // Set the new ETag in the response header
    ctx.header("ETag", etagService.getResourceETag(user));
    ctx.json(user);
  }

  public void delete(Context ctx) {
    Integer id = ctx.attribute(AuthMiddleware.USER_ID_KEY);

    // Get the client's ETag from the If-Match header
    if (!etagService.validateResourceETag(id, ctx.header("If-Match"))) {
      throw new PreconditionFailedResponse();
    }

    // Check if the user exists
    User user = users.get(id);
    if (user == null) {
      throw new NotFoundResponse();
    }

    users.remove(id);
    etagService.removeResourceETag(id);
    etagService.updateCollectionETag();

    ctx.status(HttpStatus.NO_CONTENT);
  }
}

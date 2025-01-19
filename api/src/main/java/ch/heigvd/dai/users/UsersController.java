package ch.heigvd.dai.users;

import ch.heigvd.dai.auth.AuthMiddleware;
import io.javalin.http.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Controller for handling User-related operations such as create, update, and delete. */
public class UsersController {
  private final ConcurrentHashMap<Integer, User> users;
  private final AtomicInteger userId = new AtomicInteger(1);
  private final UsersETagService etagService;

  /**
   * Constructor for UsersController.
   *
   * @param users The collection of users managed by the controller.
   * @param etagService The ETag service used for managing ETags for users.
   */
  public UsersController(ConcurrentHashMap<Integer, User> users, UsersETagService etagService) {
    this.users = users;
    this.etagService = etagService;
  }

  /**
   * Creates a new user.
   *
   * @param ctx The Javalin context.
   * @throws ConflictResponse If a user with the same email already exists.
   * @throws BadRequestResponse If required fields are missing.
   */
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

    etagService.updateCollectionETag();
    ctx.header("ETag", etagService.getResourceETag(user));
    ctx.status(HttpStatus.CREATED);
    ctx.json(user);
  }

  /**
   * Updates an existing user.
   *
   * @param ctx The Javalin context.
   * @throws PreconditionFailedResponse If the client's ETag does not match the resource's ETag.
   * @throws NotFoundResponse If the user does not exist.
   * @throws BadRequestResponse If required fields are missing.
   */
  public void update(Context ctx) {
    Integer id = ctx.attribute(AuthMiddleware.USER_ID_KEY);

    if (!etagService.validateResourceETag(id, ctx.header("If-Match"))) {
      throw new PreconditionFailedResponse();
    }

    User user = users.get(id);
    if (user == null) {
      throw new NotFoundResponse();
    }

    User updatedUser =
        ctx.bodyValidator(User.class)
            .check(obj -> obj.firstName != null, "Missing first name")
            .check(obj -> obj.lastName != null, "Missing last name")
            .check(obj -> obj.email != null, "Missing email")
            .check(obj -> obj.password != null, "Missing password")
            .get();

    user.firstName = updatedUser.firstName;
    user.lastName = updatedUser.lastName;
    user.email = updatedUser.email;
    user.password = updatedUser.password;

    users.put(id, user);
    etagService.updateCollectionETag();
    ctx.header("ETag", etagService.getResourceETag(user));
    ctx.json(user);
  }

  /**
   * Deletes an existing user.
   *
   * @param ctx The Javalin context.
   * @throws PreconditionFailedResponse If the client's ETag does not match the resource's ETag.
   * @throws NotFoundResponse If the user does not exist.
   */
  public void delete(Context ctx) {
    Integer id = ctx.attribute(AuthMiddleware.USER_ID_KEY);

    if (!etagService.validateResourceETag(id, ctx.header("If-Match"))) {
      throw new PreconditionFailedResponse();
    }

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

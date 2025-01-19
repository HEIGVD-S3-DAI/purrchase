package ch.heigvd.dai.auth;

import ch.heigvd.dai.users.User;
import ch.heigvd.dai.users.UsersETagService;
import io.javalin.http.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller for handling authentication operations such as login, logout, and profile retrieval.
 */
public class AuthController {
  private final ConcurrentHashMap<Integer, User> users;
  private final UsersETagService etagService;

  /**
   * Constructor for AuthController.
   *
   * @param users The collection of users.
   * @param etagService The ETag service for managing user ETags.
   */
  public AuthController(ConcurrentHashMap<Integer, User> users, UsersETagService etagService) {
    this.users = users;
    this.etagService = etagService;
  }

  /**
   * Logs in a user by setting a cookie.
   *
   * @param ctx The Javalin context.
   * @throws UnauthorizedResponse If the email or password is incorrect.
   */
  public void login(Context ctx) {
    User loginUser =
        ctx.bodyValidator(User.class)
            .check(obj -> obj.email != null, "Missing email")
            .check(obj -> obj.password != null, "Missing password")
            .get();

    for (User user : users.values()) {
      if (user.email.equalsIgnoreCase(loginUser.email)
          && user.password.equals(loginUser.password)) {
        ctx.cookie("user", String.valueOf(user.id));
        ctx.status(HttpStatus.NO_CONTENT);
        return;
      }
    }

    throw new UnauthorizedResponse();
  }

  /**
   * Logs out a user by removing their cookie.
   *
   * @param ctx The Javalin context.
   */
  public void logout(Context ctx) {
    ctx.removeCookie("user");
    ctx.status(HttpStatus.NO_CONTENT);
  }

  /**
   * Retrieves the profile of the logged-in user.
   *
   * @param ctx The Javalin context.
   * @throws UnauthorizedResponse If the user is not logged in.
   * @throws NotModifiedResponse If the client's ETag matches the resource's ETag.
   */
  public void profile(Context ctx) {
    Integer userId = ctx.attribute(AuthMiddleware.USER_ID_KEY);

    if (etagService.validateResourceETag(userId, ctx.header("If-None-Match"))) {
      throw new NotModifiedResponse();
    }

    User user = users.get(userId);
    if (user == null) {
      throw new UnauthorizedResponse();
    }

    ctx.header("ETag", etagService.getResourceETag(user));
    ctx.json(user);
  }
}

package ch.heigvd.dai.auth;

import ch.heigvd.dai.users.User;
import ch.heigvd.dai.users.UsersETagService;
import io.javalin.http.*;
import java.util.concurrent.ConcurrentHashMap;

public class AuthController {
  private final ConcurrentHashMap<Integer, User> users;

  private final UsersETagService etagService;

  public AuthController(ConcurrentHashMap<Integer, User> users, UsersETagService etagService) {
    this.users = users;
    this.etagService = etagService;
  }

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

  public void logout(Context ctx) {
    ctx.removeCookie("user");
    ctx.status(HttpStatus.NO_CONTENT);
  }

  public void profile(Context ctx) {
    Integer userId = ctx.attribute(AuthMiddleware.USER_ID_KEY);

    if (etagService.validateResourceETag(userId, ctx.header("If-None-Match"))) {
      throw new NotModifiedResponse();
    }

    User user = users.get(userId);
    if (user == null) {
      throw new UnauthorizedResponse();
    }

    // Add the ETag to the response header
    ctx.header("ETag", etagService.getResourceETag(user));
    ctx.json(user);
  }
}

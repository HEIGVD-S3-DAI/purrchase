package ch.heigvd.dai.auth;

import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;

/** Middleware for handling user authentication. */
public class AuthMiddleware {
  public static final String USER_ID_KEY = "userId";

  /**
   * Requires authentication for the given handler.
   *
   * @param handler The handler to protect with authentication.
   * @return A wrapped handler that checks authentication.
   * @throws UnauthorizedResponse If the user is not authenticated.
   */
  public static Handler requireAuth(Handler handler) {
    return ctx -> {
      String userIdCookie = ctx.cookie("user");
      if (userIdCookie == null || userIdCookie.isEmpty()) {
        throw new UnauthorizedResponse("You must be logged in to access this route");
      }

      ctx.attribute(USER_ID_KEY, Integer.parseInt(userIdCookie));
      handler.handle(ctx);
    };
  }
}

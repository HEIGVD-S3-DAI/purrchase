package ch.heigvd.dai.auth;

import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;

public class AuthMiddleware {
  public static final String USER_ID_KEY = "userId";

  public static Handler requireAuth(Handler handler) {
    return ctx -> {
      String userIdCookie = ctx.cookie("user");
      if (userIdCookie == null || userIdCookie.isEmpty()) {
        throw new UnauthorizedResponse("You must be logged in to access this route");
      }

      // Inject the user ID into the context
      ctx.attribute(USER_ID_KEY, Integer.parseInt(userIdCookie));

      // Proceed to the handler
      handler.handle(ctx);
    };
  }
}

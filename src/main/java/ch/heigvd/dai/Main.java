package ch.heigvd.dai;

import ch.heigvd.dai.auth.AuthController;
import ch.heigvd.dai.auth.AuthMiddleware;
import ch.heigvd.dai.users.User;
import ch.heigvd.dai.users.UsersController;
import ch.heigvd.dai.users.UsersETagService;
import io.javalin.Javalin;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
  public static final int PORT = 8080;

  public static void main(String[] args) {
    Javalin app =
        Javalin.create(
            // Add custom configuration to Javalin
            config -> {
              // This will allow us to parse LocalDateTime
              config.validation.register(LocalDateTime.class, LocalDateTime::parse);
            });

    // This will serve as our database
    ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();

    // This will serve as our cache
    //
    // The key is to identify the user(s)
    // The value is the etag of the user(s)
    UsersETagService etagService = new UsersETagService(users);

    // Controllers
    AuthController authController = new AuthController(users, etagService);
    UsersController usersController = new UsersController(users, etagService);

    // Auth routes
    app.post("/login", authController::login);
    app.post("/logout", authController::logout);
    app.get("/profile", AuthMiddleware.requireAuth(authController::profile));

    // Users routes
    app.post("/users", usersController::create);
    app.put("/users", AuthMiddleware.requireAuth(usersController::update));
    app.delete("/users", AuthMiddleware.requireAuth(usersController::delete));

    app.start(PORT);
  }
}

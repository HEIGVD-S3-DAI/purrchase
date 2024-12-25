package ch.heigvd.dai;

import ch.heigvd.dai.auth.AuthController;
import ch.heigvd.dai.auth.AuthMiddleware;
import ch.heigvd.dai.cats.Cat;
import ch.heigvd.dai.cats.CatsController;
import ch.heigvd.dai.cats.CatsETagService;
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
    ConcurrentHashMap<Integer, Cat> cats = new ConcurrentHashMap<>();

    // This will serve as our cache
    //
    // The key is to identify the user(s)
    // The value is the etag of the user(s)
    UsersETagService usersETagService = new UsersETagService(users);
    CatsETagService catsETagService = new CatsETagService(cats);

    // Controllers
    AuthController authController = new AuthController(users, usersETagService);
    UsersController usersController = new UsersController(users, usersETagService);
    CatsController catsController = new CatsController(cats, catsETagService);

    // Auth routes
    app.post("/login", authController::login);
    app.post("/logout", authController::logout);
    app.get("/profile", AuthMiddleware.requireAuth(authController::profile));

    // Users routes
    app.post("/users", usersController::create);
    app.put("/users", AuthMiddleware.requireAuth(usersController::update));
    app.delete("/users", AuthMiddleware.requireAuth(usersController::delete));

    // Cat routes
    app.post("/cats", AuthMiddleware.requireAuth(catsController::create));
    app.put("/cats", AuthMiddleware.requireAuth(catsController::update));
    app.delete("/cats", AuthMiddleware.requireAuth(usersController::delete));

    app.start(PORT);
  }
}

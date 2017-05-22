package controllers;

import models.User;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;

import static java.util.stream.Collectors.toList;
import static play.libs.Json.toJson;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.mvc.Result;
import services.UserService;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;


public class UserController extends Controller {

    private UserService userService;
    private final FormFactory formFactory;

    @Inject
    public UserController(FormFactory formFactory, UserService userService) {
        this.formFactory = formFactory;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Result getUsers() {
        List<ObjectNode> users = userService.findAll().stream()
                .map(this::getUserResponse)
                .collect(toList());

        ObjectNode response = Json.newObject();
        response.put("create-user", routes.UserController.createUser().toString());
        response.set("users", toJson(users));

        return ok(toJson(response));
    }

    @Transactional
    public Result createUser() {
        User user = formFactory.form(User.class).bindFromRequest().get();

        User savedUser = userService.create(user);
        ObjectNode userResponse = getUserResponse(savedUser);
        userResponse.put("all-users", routes.UserController.getUsers().toString());

        return created(toJson(userResponse));
    }

    @Transactional(readOnly = true)
    public Result getUser(Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            ObjectNode userResponse = getUserResponse(userOptional.get());
            userResponse.put("all-users", routes.UserController.getUsers().toString());

            return ok(toJson(userResponse));
        }

        ObjectNode response = Json.newObject();
        response.put("error", String.format("User with id {%d} not found", id));
        response.put("all-users", routes.UserController.getUsers().toString());

        return notFound(toJson(response));
    }

    private ObjectNode getUserResponse(User user) {
        ObjectNode response = Json.newObject();
        response.set("user", toJson(user));
        response.put("self-link", routes.UserController.getUser(user.getId()).toString());
        response.put("transfer-money", routes.MoneyTransferController.transferFunds(user.getId()).toString());

        return response;
    }
}

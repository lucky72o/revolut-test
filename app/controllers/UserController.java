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

    private static final String CREATE_USER_REL = "create-user";
    private static final String ALL_USERS_REL = "all-users";
    private static final String SELF_LINK_REL = "self-link";
    private static final String TRANSFER_MONEY_REl = "transfer-money";

    private static final String USERS = "users";
    private static final String ERROR = "error";
    private static final String USER = "user";

    private final UserService userService;
    private final FormFactory formFactory;

    @Inject
    public UserController(FormFactory formFactory, UserService userService) {
        this.formFactory = formFactory;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Result getUsers() {
        List<User> users = userService.findAll();

        List<ObjectNode> usersJson = users.stream()
                .map(this::getUserResponse)
                .collect(toList());

        ObjectNode response = Json.newObject();
        response.put(CREATE_USER_REL, routes.UserController.createUser().toString());
        response.set(USERS, toJson(usersJson));

        return ok(toJson(response));
    }

    @Transactional
    public Result createUser() {
        User user = formFactory.form(User.class).bindFromRequest().get();

        User savedUser = userService.create(user);
        ObjectNode userResponse = getUserResponse(savedUser);
        userResponse.put(ALL_USERS_REL, routes.UserController.getUsers().toString());

        return created(toJson(userResponse));
    }

    @Transactional(readOnly = true)
    public Result getUser(Long id) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            ObjectNode userResponse = getUserResponse(userOptional.get());
            userResponse.put(ALL_USERS_REL, routes.UserController.getUsers().toString());

            return ok(toJson(userResponse));
        }

        ObjectNode response = Json.newObject();
        response.put(ERROR, String.format("User with id {%d} not found", id));
        response.put(ALL_USERS_REL, routes.UserController.getUsers().toString());

        return notFound(toJson(response));
    }

    private ObjectNode getUserResponse(User user) {
        ObjectNode response = Json.newObject();
        response.set(USER, toJson(user));
        response.put(SELF_LINK_REL, routes.UserController.getUser(user.getId()).toString());
        response.put(TRANSFER_MONEY_REl, routes.MoneyTransferController.transferFunds(user.getId()).toString());

        return response;
    }
}

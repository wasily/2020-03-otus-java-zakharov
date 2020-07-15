package ru.otus.hw14.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw14.core.model.User;
import ru.otus.hw14.core.service.DBServiceUser;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private final DBServiceUser dbServiceUser;
    private static final String START_PAGE = "/users";

    @GetMapping(path = "/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("get request on /users");
        var userList = dbServiceUser.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(path = "/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") long id) {
        logger.info("get request on /api/users/" + id);
        return dbServiceUser.getUser(id).map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/users/{id}")
    public ResponseEntity deleteUserById(@PathVariable(name = "id") long id) {
        logger.info("delete request on /api/users/" + id);
        int result = dbServiceUser.deleteUser(id);
        if (result > 0) {
            return new ResponseEntity(HttpStatus.OK);
        }
        if (result == 0) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/api/users")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        logger.info("post request on /api/users");
        try {
            dbServiceUser.saveUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(path = "/users")
    public String userListView(Model model) {
        List<User> users = dbServiceUser.getAllUsers();
        model.addAttribute("users", users);
        return "users.html";
    }

    @GetMapping(path = {"/edit/user", "/edit/user/{id}"})
    public String addUserView(Model model, @PathVariable("id") Optional<Long> id) {
        if (id.isPresent()) {
            var optionalUser = dbServiceUser.getUser(id.get());
            optionalUser.ifPresent(user -> model.addAttribute("user", user));
        } else {
            model.addAttribute("user", new User());
        }
        return "edituser.html";
    }

    @PostMapping(path = "/save/user")
    public String createOrUpdateUser(User user) {
        this.saveUser(user);
        return "redirect:" + START_PAGE;
    }

    @GetMapping(path = "/delete/user/{id}")
    public String deleteUser(@PathVariable(name = "id") long id) {
        this.deleteUserById(id);
        return "redirect:" + START_PAGE;
    }

    @GetMapping(path = "/")
    public String redirect() {
        return "redirect:" + START_PAGE;
    }
}

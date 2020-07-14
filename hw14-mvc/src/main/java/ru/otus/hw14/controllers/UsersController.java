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
import ru.otus.hw14.core.service.DbServiceException;
import ru.otus.hw14.core.sessionmanager.SessionManagerException;

import java.util.List;

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
        logger.info("get request on /users/{id}");
        var optionalUser = dbServiceUser.getUser(id);
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/api/users/{id}")
    public void deleteUserById(@PathVariable(name = "id") long id) {
        logger.info("delete request on /users/{id}");
        dbServiceUser.deleteUser(id);
    }

    @PostMapping(path = "/api/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            dbServiceUser.saveUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (SessionManagerException | DbServiceException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(path = "/users")
    public String userListView(Model model) {
        List<User> users = dbServiceUser.getAllUsers();
        model.addAttribute("users", users);
        return "users.html";
    }

    @GetMapping(path = "/delete/user/{id}")
    public String deleteUser(@PathVariable(name = "id") long id) {
        this.deleteUserById(id);
        return "redirect:" + START_PAGE;
    }


}

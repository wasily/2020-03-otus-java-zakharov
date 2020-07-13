package ru.otus.hw14.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw14.core.model.User;
import ru.otus.hw14.core.service.DBServiceUser;
import ru.otus.hw14.core.service.DbServiceException;
import ru.otus.hw14.core.sessionmanager.SessionManagerException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private final DBServiceUser dbServiceUser;

    @GetMapping(path = "/api/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("get request on /users");
        var userList = dbServiceUser.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(path = "/api/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") long id) {
        logger.info("get request on /users/{id}");
        var optionalUser = dbServiceUser.getUser(id);
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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

}

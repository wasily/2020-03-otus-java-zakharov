package ru.otus.hw14.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw14.core.model.User;
import ru.otus.hw14.core.service.DBServiceUser;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersApiController {
    private final Logger logger = LoggerFactory.getLogger(UsersApiController.class);
    private final DBServiceUser dbServiceUser;

    @GetMapping(path = "/api/users")
    public List<User> getAllUsers() {
        logger.info("get request on /users");
        return dbServiceUser.getAllUsers();
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
}

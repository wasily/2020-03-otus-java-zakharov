package ru.otus.hw14.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw14.core.model.User;
import ru.otus.hw14.core.service.DBServiceUser;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);
    private final DBServiceUser dbServiceUser;

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("get request on /users " + LocalDateTime.now());
        var userList = dbServiceUser.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") long id) {
        logger.info("get request on /users/{id}" + LocalDateTime.now());
        var optionalUser = dbServiceUser.getUser(id);
        if (optionalUser.isPresent()){
            return new ResponseEntity<User>(optionalUser.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

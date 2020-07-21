package ru.otus.hw14.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw14.core.model.User;
import ru.otus.hw14.core.service.DBServiceUser;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UsersViewController {
    private final DBServiceUser dbServiceUser;
    private static final String START_PAGE = "/users";

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
        dbServiceUser.saveUser(user);
        return "redirect:" + START_PAGE;
    }

    @GetMapping(path = "/delete/user/{id}")
    public String deleteUser(@PathVariable(name = "id") long id) {
        dbServiceUser.deleteUser(id);
        return "redirect:" + START_PAGE;
    }

    @GetMapping(path = "/")
    public String redirect() {
        return "redirect:" + START_PAGE;
    }
}

package hw16.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UsersViewController {
    private static final String START_PAGE = "/users.html";

    @GetMapping(path = "/")
    public String redirect() {
        return "redirect:" + START_PAGE;
    }
}

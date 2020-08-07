package hw16.controller;

import hw16.core.model.User;
import hw16.core.service.DBServiceUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final DBServiceUser dbServiceUser;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @MessageMapping("/getAllUsers{uuid}")
    @SendTo("/user/{uuid}/getAll")
    @ResponseBody
    public List<User> getAllUsers(@DestinationVariable String uuid, String id) {
        logger.warn(" " + dbServiceUser.getAllUsers().size());
        return dbServiceUser.getAllUsers();
    }

    @MessageMapping("/deleteUser{uuid}")
    @SendTo("/user/{uuid}/delete")
    public String deleteUser(@DestinationVariable String uuid, long id) {
        boolean status = dbServiceUser.deleteUser(id) != 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{").append("\"id\"").append(":").append(id).append(",")
                .append("\"status\"").append(":").append(status).append("}");
        return stringBuilder.toString();
    }

    @MessageMapping("/saveUser{uuid}")
    @SendTo("/user/{uuid}/save")
    @ResponseBody
    public User saveUser(@DestinationVariable String uuid, User user) {
        dbServiceUser.saveUser(user);
        return user;
    }
}

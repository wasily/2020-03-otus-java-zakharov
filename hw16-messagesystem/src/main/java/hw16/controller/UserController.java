package hw16.controller;

import hw16.core.model.User;
import hw16.services.front.FrontendService;
import hw16.services.front.dto.StatusData;
import hw16.services.front.dto.UserData;
import hw16.services.front.dto.UserListData;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/getAllUsers{uuid}")
    public void getAllUsers(@DestinationVariable String uuid, String id) {
        String destination = String.format("/user/%s/getAll", uuid);
        frontendService.getAllUsersData(data -> send(destination, data));
    }

    @MessageMapping("/deleteUser{uuid}")
    public void deleteUser(@DestinationVariable String uuid, long id) {
        String destination = String.format("/user/%s/delete", uuid);
        frontendService.deleteUser(id, data -> send(destination, data));
    }

    @MessageMapping("/saveUser{uuid}")
    public void saveUser(@DestinationVariable String uuid, User user) {
        String destination = String.format("/user/%s/save", uuid);
        frontendService.saveUser(user, data -> send(destination, data));
    }

    private void send(String destination, UserListData userListData) {
        template.convertAndSend(destination, userListData.getData());
    }

    private void send(String destination, StatusData statusData) {
        template.convertAndSend(destination, statusData);
    }

    private void send(String destination, UserData userData) {
        template.convertAndSend(destination, userData.getData());
    }
}

package hw16.services.front;


import hw16.core.model.User;
import hw16.messagesystem.client.MessageCallback;
import hw16.services.front.dto.StatusData;
import hw16.services.front.dto.UserData;
import hw16.services.front.dto.UserListData;

public interface FrontendService {
    void getAllUsersData(MessageCallback<UserListData> dataConsumer);
    void saveUser(User user, MessageCallback<UserData> dataConsumer);
    void deleteUser(long userId, MessageCallback<StatusData> dataConsumer);
}


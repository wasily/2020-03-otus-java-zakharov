package hw16.front;


import hw16.core.model.User;
import messagesystem.client.MessageCallback;
import hw16.front.dto.StatusData;
import hw16.front.dto.UserData;
import hw16.front.dto.UserListData;

public interface FrontendService {
    void getAllUsersData(MessageCallback<UserListData> dataConsumer);
    void saveUser(User user, MessageCallback<UserData> dataConsumer);
    void deleteUser(long userId, MessageCallback<StatusData> dataConsumer);
}


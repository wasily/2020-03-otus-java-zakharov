package hw16.front;

import hw16.core.model.User;
import messagesystem.client.MessageCallback;
import messagesystem.client.MsClient;
import messagesystem.message.Message;
import messagesystem.message.MessageType;
import hw16.front.dto.StatusData;
import hw16.front.dto.UserData;
import hw16.front.dto.UserListData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FrontendServiceImpl implements FrontendService {
    private final MsClient msClient;
    private final String databaseServiceClientName;

    @Override
    public void getAllUsersData(MessageCallback<UserListData> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new UserListData(null),
                MessageType.ALL_USERS_DATA, dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void deleteUser(long userId, MessageCallback<StatusData> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new StatusData(userId),
                MessageType.DELETE_USER_DATA, dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void saveUser(User user, MessageCallback<UserData> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new UserData(user),
                MessageType.SAVE_USER_DATA, dataConsumer);
        msClient.sendMessage(outMsg);
    }
}

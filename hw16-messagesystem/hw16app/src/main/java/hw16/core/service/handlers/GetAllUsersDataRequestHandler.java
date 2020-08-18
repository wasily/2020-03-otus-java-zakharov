package hw16.core.service.handlers;

import hw16.core.service.DBServiceUser;
import messagesystem.RequestHandler;
import messagesystem.message.Message;
import messagesystem.message.MessageBuilder;
import hw16.front.dto.UserListData;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GetAllUsersDataRequestHandler implements RequestHandler<UserListData> {
    private final DBServiceUser dbServiceUser;

    @Override
    public Optional<Message> handle(Message msg) {
        UserListData data = new UserListData(dbServiceUser.getAllUsers());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data));
    }
}

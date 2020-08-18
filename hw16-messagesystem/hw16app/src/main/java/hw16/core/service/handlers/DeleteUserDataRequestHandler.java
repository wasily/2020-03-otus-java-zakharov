package hw16.core.service.handlers;

import hw16.core.service.DBServiceUser;
import messagesystem.RequestHandler;
import messagesystem.message.Message;
import messagesystem.message.MessageBuilder;
import messagesystem.message.MessageHelper;
import hw16.front.dto.StatusData;
import hw16.front.dto.UserData;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DeleteUserDataRequestHandler implements RequestHandler<UserData> {
    private final DBServiceUser dbServiceUser;

    @Override
    public Optional<Message> handle(Message msg) {
        long userId = ((StatusData) MessageHelper.getPayload(msg)).getId();
        StatusData statusData = new StatusData(userId, dbServiceUser.deleteUser(userId));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, statusData));
    }
}

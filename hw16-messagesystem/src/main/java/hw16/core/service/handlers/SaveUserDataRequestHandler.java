package hw16.core.service.handlers;

import hw16.core.model.User;
import hw16.core.service.DBServiceUser;
import hw16.messagesystem.RequestHandler;
import hw16.messagesystem.message.Message;
import hw16.messagesystem.message.MessageBuilder;
import hw16.messagesystem.message.MessageHelper;
import hw16.services.front.dto.UserData;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class SaveUserDataRequestHandler implements RequestHandler<UserData> {
    private final DBServiceUser dbServiceUser;

    @Override
    public Optional<Message> handle(Message msg) {
        User user = ((UserData) MessageHelper.getPayload(msg)).getData();
        dbServiceUser.saveUser(user);
        UserData data = new UserData(user);
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data));
    }
}

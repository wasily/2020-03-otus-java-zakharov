package messagesystem;


import messagesystem.client.ResultDataType;
import messagesystem.message.MessageType;

public interface HandlersStore {
    RequestHandler<? extends ResultDataType> getHandlerByType(String messageTypeName);
    void addHandler(MessageType messageType, RequestHandler<? extends ResultDataType> handler);
}

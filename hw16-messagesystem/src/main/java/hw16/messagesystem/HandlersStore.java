package hw16.messagesystem;


import hw16.messagesystem.client.ResultDataType;
import hw16.messagesystem.message.MessageType;

public interface HandlersStore {
    RequestHandler<? extends ResultDataType> getHandlerByType(String messageTypeName);
    void addHandler(MessageType messageType, RequestHandler<? extends ResultDataType> handler);
}

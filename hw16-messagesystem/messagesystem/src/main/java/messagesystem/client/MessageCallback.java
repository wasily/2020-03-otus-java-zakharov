package messagesystem.client;

import java.util.function.Consumer;

public interface MessageCallback<T extends ResultDataType> extends Consumer<T> {
}

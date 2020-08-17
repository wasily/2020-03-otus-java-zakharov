package messagesystem.message;

public enum MessageType {
    ALL_USERS_DATA("ALL_USERS_DATA"),
    DELETE_USER_DATA("DELETE_USER_DATA"),
    SAVE_USER_DATA("SAVE_USER_DATA");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package hw16.services.front.dto;

import hw16.core.model.User;
import hw16.messagesystem.client.ResultDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserData extends ResultDataType {
    private final User data;
}

package hw16.front.dto;

import hw16.core.model.User;
import messagesystem.client.ResultDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UserData extends ResultDataType {
    private final User data;
}

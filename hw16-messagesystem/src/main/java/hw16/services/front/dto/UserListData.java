package hw16.services.front.dto;

import hw16.core.model.User;
import hw16.messagesystem.client.ResultDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class UserListData extends ResultDataType {
    private final List<User> data;
}

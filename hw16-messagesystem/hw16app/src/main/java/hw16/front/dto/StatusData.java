package hw16.front.dto;

import messagesystem.client.ResultDataType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StatusData extends ResultDataType {
    private final long id;
    private final Boolean status;

    public StatusData(long id) {
        this.id = id;
        status = null;
    }

    public StatusData(long id, int arg) {
        this.id = id;
        this.status = arg == 1;
    }
}

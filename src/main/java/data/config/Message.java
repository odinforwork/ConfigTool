package data.config;

import lombok.Data;
import utils.ConfigUtils;

@Data
public class Message {
    private final ConfigUtils.MessageType mType;
    private final Object mObject;
}

package data.DTO;

import lombok.Data;

@Data
public class Message {
    private final int mAction;
    private final Object mObject;

    public static final int SET_OLD_DIR = 0;
    public static final int GET_OLD_DIR = 1;
    public static final int SET_NEW_DIR = 2;
    public static final int GET_NEW_DIR = 3;
    public static final int COMPARE = 4;
}

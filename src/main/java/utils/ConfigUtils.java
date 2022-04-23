package utils;

public interface ConfigUtils {
    String COMMAND_SELECT_OLD = "select old dir";
    String COMMAND_SELECT_NEW = "select new dir";
    String COMMAND_COMPARE = "compare";

    String CONFIG_PATH = "./config";

    int ALL = 3;
    int NEW = 2;
    int OLD = 1;

    public enum MessageType {
        DIFFERENT_ALREADY
    }
}

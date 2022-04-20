import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

@Log4j2
public class FileText {
    public static void main(String[] args) {
        String s = "algo_type\tmonitor";
        String[] strings = s.split(" ");

        log.info(strings.length);
        for(String str : strings) {
            log.info(str.length() + "   " + str);
        }
    }
}

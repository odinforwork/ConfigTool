import fileOperation.FileComparator;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

@Log4j2
public class FileText {
    public static void main(String[] args) {
        String o = "clr\t32000\t35000\t37000\t39000\t41000\t43000\t44000";
        String n = "clr\t32000\t35000\t37000\t39000\t41000\t43000\t44000 ";

        var fc = new FileComparator("", "");
        var result = fc.compare("clr", o, n);

        log.debug(result);

        result.stream()
                .filter(r -> {
                    return r.getMDeleteStartAt() != -1 && r.getMInsertStartAt()!=-1;
                })
                .forEach(r -> {
                    log.debug(o.substring(r.getMDeleteStartAt(), r.getMDeleteEndAt()));
                    log.debug(n.substring(r.getMInsertStartAt(), r.getMInsertEndAt()));
                });

        result.stream()
                .filter(r -> r.getMDeleteStartAt()==-1)
                .forEach(r -> log.debug(n.substring(r.getMInsertStartAt(), r.getMInsertEndAt())));

        result.stream()
                .filter(r -> r.getMInsertStartAt()==-1)
                .forEach(r -> log.debug(o.substring(r.getMDeleteStartAt(), r.getMDeleteEndAt())));
    }
}

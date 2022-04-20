import data.Different.DifferentInDir;
import fileOperation.FileComparator;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Test {
    public static void main(String[] args) {
        String oldDir = "/home/odin/workspace/old";
        String newDir = "/home/odin/workspace/new";
        var fileComparator = new FileComparator(oldDir, newDir);

        DifferentInDir did = null;
        try {
            did = fileComparator.compare();
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        if(did == null) return;
        log.trace("get did: " + did);
        log.trace(did.getMDifferentInFiles().size());
    }
}

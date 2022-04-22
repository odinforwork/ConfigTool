import data.DTO.Different;
import fileOperation.FileComparator;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Test {
    public static void main(String[] args) {
        String oldDir = "/home/odin/workspace/old";
        String newDir = "/home/odin/workspace/new";
        var fileComparator = new FileComparator(oldDir, newDir);

        Different different = null;
        try {
            different = fileComparator.compare();
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        if(different == null) return;
        log.trace("get different: " + different);
        log.trace(different.getMDifferentInFiles().size());
    }
}

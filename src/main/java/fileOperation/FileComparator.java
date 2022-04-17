package fileOperation;

import data.Different.DifferentInBlock;
import data.Different.DifferentInDir;
import data.Different.DifferentInFile;
import data.File.BlockInfo;
import data.File.FileInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.beans.BeanInfo;
import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class FileComparator {
    private static volatile FileComparator sInstance;
    private static final String EXTENSION = "conf_modified";

    @Getter @Setter
    private final String mOldDir;
    @Getter @Setter
    private final String mNewDir;

    public DifferentInDir compare() throws Exception {
        File oDir = new File(mOldDir);
        Map<String, File> oldMap;
        try {
            oldMap = Arrays.stream(
                    Objects.requireNonNull(oDir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.isFile() && pathname.getName().endsWith(EXTENSION);
                        }}))
                    )
                    .collect(Collectors.toMap(File::getName, f -> f));
        } catch (Exception e){
            log.error("old dir error");
            throw new Exception("old dir error");
        }

        File nDir = new File(mNewDir);
        Map<String, File> newMap;
        try {
            newMap = Arrays.stream(
                            Objects.requireNonNull(nDir.listFiles(new FileFilter() {
                                @Override
                                public boolean accept(File pathname) {
                                    return pathname.isFile() && pathname.getName().endsWith(EXTENSION);
                                }
                            }))
                    )
                    .collect(Collectors.toMap(File::getName, f -> f));
        } catch (Exception e){
            log.error("new dir error");
            throw new Exception("new dir error");
        }

        log.trace(oldMap.size() + " old map size");
        log.trace(newMap.size() + " new map size");

        DifferentInDir did = new DifferentInDir();
        for(String fileName : newMap.keySet()){
            if(!oldMap.containsKey(fileName)) {
                did.getMNewFile().add(fileName);
                continue;
            }
            DifferentInFile dif = compareFile(oldMap.get(fileName), newMap.get(fileName));
            if(dif != null) {
                did.getMDif().add(dif);
            }
            oldMap.remove(fileName);
        }

        if(!oldMap.isEmpty()) {
            did.getMDeleteFile().addAll(oldMap.keySet());
        }

        return did;
    }

    private DifferentInFile compareFile(File oldFile, File newFile) {
        String fileName = oldFile.getName();
        FileInfo oldFI = new FileInfo(oldFile);
        FileInfo newFI = new FileInfo(newFile);
        DifferentInFile dif = new DifferentInFile(fileName);

        for(String name : newFI.getMBlockInfos().keySet()) {
            if(!oldFI.getMBlockInfos().containsKey(name)) {
                DifferentInBlock dib = new DifferentInBlock(fileName, null, name);
                dif.getMDIB().add(dib);
            } else {
                DifferentInBlock dib = compareBlock(oldFI.getMBlockInfos().get(name), newFI.getMBlockInfos().get(name));
                if(dib != null) dif.getMDIB().add(dib);
            }
        }

        return null;
    }

    private DifferentInBlock compareBlock(BlockInfo oldBlock, BlockInfo newBlock) {
        return null;
    }
}

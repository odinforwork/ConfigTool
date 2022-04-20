package fileOperation;

import data.Different.DifferentInBlock;
import data.Different.DifferentInDir;
import data.Different.DifferentInFile;
import data.Different.DifferentInLine;
import data.File.BlockInfo;
import data.File.FileInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import utils.ConfigUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class FileComparator {
    private static final String EXTENSION = "conf_modified";

    @Getter
    private final String mOldDir;
    @Getter
    private final String mNewDir;

    private final List<String> mTitleList = Arrays.asList(ConfigUtils.mTitles);

    public DifferentInDir compare() throws Exception {
        var oDir = new File(mOldDir);
        Map<String, File> oldMap;
        try {
            oldMap = Arrays.stream(
                            Objects.requireNonNull(oDir.listFiles(new FileFilter() {
                                @Override
                                public boolean accept(File pathname) {
                                    return pathname.isFile() && pathname.getName().endsWith(EXTENSION);
                                }
                            }))
                    )
                    .collect(Collectors.toMap(File::getName, f -> f));
        } catch (Exception e) {
            log.error("old dir error");
            throw new Exception("old dir error");
        }

        var nDir = new File(mNewDir);
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
        } catch (Exception e) {
            log.error("new dir error");
            throw new Exception("new dir error");
        }

        var did = new DifferentInDir();
        for (String fileName : newMap.keySet()) {
            if (!oldMap.containsKey(fileName)) {
                did.getMNewFiles().add(fileName);
                continue;
            }
            var dif = compareFile(oldMap.get(fileName), newMap.get(fileName));
            if (dif != null) {
                did.getMDifferentInFiles().add(dif);
            }
            oldMap.remove(fileName);
        }

        if (!oldMap.isEmpty()) {
            did.getMDeleteFiles().addAll(oldMap.keySet());
        }

        return did;
    }

    private DifferentInFile compareFile(File oldFile, File newFile) throws Exception {
        if (!oldFile.getName().equals(newFile.getName())) {
            throw new Exception("compareFile error");
        }
        String fileName = oldFile.getName();
        var oldFI = new FileInfo(oldFile);
        var newFI = new FileInfo(newFile);
        var dif = new DifferentInFile(fileName);
        log.warn(fileName);

        for (var name : newFI.getMBlockInfos().keySet()) {
            if (!oldFI.getMBlockInfos().containsKey(name)) {
                dif.getMNewBlocks().add(newFI.getMBlockInfos().get(name));
            } else {
                var dib = compareBlock(oldFI.getMBlockInfos().get(name), newFI.getMBlockInfos().get(name));
                if (dib != null) dif.getMDifferentInBlocks().add(dib);
            }
            oldFI.getMBlockInfos().remove(name);
        }

        if (!oldFI.getMBlockInfos().isEmpty()) {
            oldFI.getMBlockInfos().values()
                    .forEach(s -> {
                        dif.getMDeleteBlocks().add(s);
                    });
        }

        if (dif.getMNewBlocks().isEmpty() && dif.getMDeleteBlocks().isEmpty() && dif.getMDifferentInBlocks().isEmpty())
            return null;
        return dif;
    }

    private DifferentInBlock compareBlock(BlockInfo oldBlock, BlockInfo newBlock) throws Exception {
        if (!oldBlock.getMBlockName().equals(newBlock.getMBlockName())) {
            throw new Exception("compareBlock error");
        }

        var dib = new DifferentInBlock(oldBlock.getMBlockName(), oldBlock, newBlock);

        for (int i = 0; i < newBlock.getMInfos().size(); i++) {
            String newLine = newBlock.getMInfos().get(i);
            String title = newLine.split("\t")[0];
            int index = mTitleList.indexOf(title);
            if (index != -1) {
                log.warn(oldBlock.getMBlockName());
                log.warn(oldBlock.getMInfos().size() + "!!!!!!!!!!!!");
                oldBlock.getMInfos().forEach(log::warn);
                log.warn("");
                var dils = compareLine(index, i, oldBlock.getMInfos().get(index), newLine);
                if (!dils.isEmpty()) {
                    dib.getMDifferentInLines().addAll(dils);
                }
            } else {
                dib.getMDifferentInLines().add(new DifferentInLine(index, i, null, null, null, null));
            }
        }

        oldBlock.getMInfos().removeAll(newBlock.getMInfos());
        if (!oldBlock.getMInfos().isEmpty()) {
            for (String str : oldBlock.getMInfos()) {
                int index = mTitleList.indexOf(str.split("\t")[0]);
                log.warn(str.split("\t")[0]);
                if (index == -1) throw new Exception("is time update title name ");
                dib.getMDifferentInLines().add(new DifferentInLine(index, -1, null, null, null, null));
            }
        }

        if (dib.getMDifferentInLines().isEmpty()) return null;
        return dib;
    }

    private List<DifferentInLine> compareLine(int oldLine, int newLine, String o, String n) {
        List<DifferentInLine> result = new ArrayList<>();
        if (o.equals(n)) return result;

        String[] olds = o.split("\t");
        String[] news = n.split("\t");
        int nLength = news.length;
        int oLength = olds.length;
        int deleteStart = 0;
        int deleteEnd = 0;
        int insertStart = 0;
        int insertEnd = 0;

        for (int i = 0; i < nLength && i < oLength; i++) {
            if (!olds[i].equals(news[i])) {
                deleteEnd = deleteStart + olds[i].length();
                insertEnd = insertStart + news[i].length();
                result.add(new DifferentInLine(oldLine, newLine, deleteStart, deleteEnd, insertStart, insertEnd));
            }
            deleteStart = deleteEnd + 1;
            insertStart = insertEnd + 1;
        }
        if(nLength > oLength) {
            result.add(new DifferentInLine(oldLine, newLine, -1, -1, insertStart, nLength));
        } else {
            result.add(new DifferentInLine(oldLine, newLine, deleteStart, oLength, -1, -1));
        }

        return result;
    }
}

package fileOperation;

import data.DTO.DifferentDTO;
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

    private Map<String, File> loadingDir(String dir) throws Exception {
        return Arrays.stream(Objects.requireNonNull(
                new File(dir).listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isFile() && pathname.getName().endsWith(EXTENSION);
                    }
                })))
                .collect(Collectors.toMap(File::getName, f -> f));
    }

    public DifferentDTO compare() throws Exception {
        var oldMap = loadingDir(mOldDir);
        var newMap = loadingDir(mNewDir);

        oldMap.forEach((key, value) -> log.info("old: " + key + "  " + value));
        newMap.forEach((key, value) -> log.info("new: " + key + "  " + value));

        var different = new DifferentDTO();
        //new比old多的部分，放在新增
        newMap.entrySet().stream()
                .filter(e -> !oldMap.containsKey(e.getKey()))
                .forEach(e -> different.getMNewFiles().add(e.getKey()));

        //old比new多的部分，放在删除
        oldMap.entrySet().stream()
                .filter(e -> !newMap.containsKey(e.getKey()))
                .forEach(e -> different.getMDeleteFiles().add(e.getKey()));

        //共同的部分对比内容
        newMap.entrySet().stream()
                .filter(e -> oldMap.containsKey(e.getKey()))
                .map(e -> compareFile(oldMap.get(e.getKey()), e.getValue()))
                .filter(Objects::nonNull)
                .forEach(different.getMDifferentInFiles()::add);

        return different;
    }

    private DifferentDTO.DifferentInFile compareFile(File oldFile, File newFile) throws RuntimeException {
        if (!oldFile.getName().equals(newFile.getName())) {
            throw new RuntimeException("compareFile error");
        }

        String fileName = oldFile.getName();
        FileInfo oldFI = null;
        FileInfo newFI = null;
        try {
            oldFI = new FileInfo(oldFile);
            newFI = new FileInfo(newFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        var oldBlocks = oldFI.getMBlockInfos();
        var newBlocks = newFI.getMBlockInfos();
        var dif = new DifferentDTO.DifferentInFile(fileName);
        log.warn(fileName);

        //new比old多的部分，放在新增
        newBlocks.entrySet().stream()
                .filter(e -> !oldBlocks.containsKey(e.getKey()))
                .forEach(e -> dif.getMNewBlocks().add(e.getValue()));

        //old比new多的部分，放在删除
        oldBlocks.entrySet().stream()
                .filter(e -> !newBlocks.containsKey(e.getKey()))
                .forEach(e -> dif.getMDeleteBlocks().add(e.getValue()));

        newBlocks.entrySet().stream()
                .filter(e -> oldBlocks.containsKey(e.getKey()))
                .forEach(e -> {
                    log.trace(oldBlocks.get(e.getKey()).getMBlockName() + "   " + e.getValue().getMBlockName() + "   " + e.getKey());
                });

        //共同的部分对比内容
        newBlocks.entrySet().stream()
                .filter(e -> oldBlocks.containsKey(e.getKey()))
                .map(e -> compareBlock(oldBlocks.get(e.getKey()), e.getValue()))
                .filter(Objects::nonNull)
                .forEach(dif.getMDifferentInBlocks()::add);

        if (dif.getMNewBlocks().isEmpty() && dif.getMDeleteBlocks().isEmpty() && dif.getMDifferentInBlocks().isEmpty())
            return null;
        return dif;
    }

    private DifferentDTO.DifferentInBlock compareBlock(BlockInfo oldBlock, BlockInfo newBlock) throws RuntimeException {
        if (!oldBlock.getMBlockName().equals(newBlock.getMBlockName())) {
            throw new RuntimeException("compareBlock error");
        }

        var dib = new DifferentDTO.DifferentInBlock(oldBlock.getMBlockName(), oldBlock, newBlock);
        Map<String, String> newInfos = newBlock.getMInfos();
        Map<String, String> oldInfos = oldBlock.getMInfos();

        //new比old多的部分，是新增
        newInfos.entrySet().stream()
                .filter(e -> !oldInfos.containsKey(e.getKey()))
                .map(e -> {
                    return new DifferentDTO.DifferentInLine(
                            e.getKey(),
                            -1,
                            -1,
                            0,
                            e.getValue().length() - 1
                    );
                })
                .forEach(dib.getMDifferentInLines()::add);

        //old比new多的部分，是删除
        oldInfos.entrySet().stream()
                .filter(e -> !newInfos.containsKey(e.getKey()))
                .map(e -> new DifferentDTO.DifferentInLine(
                        e.getKey(),
                        0,
                        e.getValue().length() - 1,
                        -1,
                        -1
                        )
                )
                .forEach(dib.getMDifferentInLines()::add);

        //共同的部分对比内容
        newInfos.entrySet().stream()
                .filter(e -> oldInfos.containsKey(e.getKey()))
                .map(e -> compareLine(e.getKey(), oldInfos.get(e.getKey()), e.getValue()))
                .filter(Objects::nonNull)
                .forEach(dib.getMDifferentInLines()::addAll);

        if (dib.getMDifferentInLines().isEmpty()) return null;
        return dib;
    }

    public List<DifferentDTO.DifferentInLine> compare(String title, String o, String n) {
        return compareLine(title, o, n);
    }

    private List<DifferentDTO.DifferentInLine> compareLine(String title, String o, String n) {
        if (o.equals(n)) return null;

        List<DifferentDTO.DifferentInLine> result = new ArrayList<>();
        String[] olds = o.split("\t");
        String[] news = n.split("\t");
        int nLength = news.length;
        int oLength = olds.length;
        int deleteStart = 0;
        int deleteEnd = 0;
        int insertStart = 0;
        int insertEnd = 0;

        //一一对应的部分
        for (int i = 0; i < nLength && i < oLength; i++) {
            if (!olds[i].equals(news[i])) {
                deleteEnd = deleteStart + olds[i].length();
                insertEnd = insertStart + news[i].length();
                result.add(new DifferentDTO.DifferentInLine(title, deleteStart, deleteEnd, insertStart, insertEnd));
            }
            deleteStart += olds[i].length() + 1;
            insertStart += news[i].length() + 1;
        }

        //多出来的部分
        if (nLength > oLength) {
            result.add(new DifferentDTO.DifferentInLine(title, -1, -1, insertStart, n.length()));
        } else if(nLength < oLength){
            result.add(new DifferentDTO.DifferentInLine(title, deleteStart, o.length(), -1, -1));
        }

        //行末尾的制表符
        int end = n.length()-1;
        for(; end>=0; end--) {
            if(n.charAt(end) != '\t') break;
        }
        if(end != n.length()-1) {
            result.add(new DifferentDTO.DifferentInLine(title, -1, -1, end+1, n.length()));
        }

        return result;
    }
}

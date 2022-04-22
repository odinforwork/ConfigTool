package data.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Different {
    private final List<String> mNewFiles = new ArrayList<>(10);
    private final List<String> mDeleteFiles = new ArrayList<>(10);
    private final List<DifferentInFile> mDifferentInFiles = new ArrayList<>(10);

    @Data
    @RequiredArgsConstructor
    public static class DifferentInFile {
        private final String mFileName;
        private List<BlockInfo> mNewBlocks = new ArrayList<>();
        private List<BlockInfo> mDeleteBlocks = new ArrayList<>();
        private List<DifferentInBlock> mDifferentInBlocks = new ArrayList<>();
    }

    @Data
    @RequiredArgsConstructor
    public static class DifferentInBlock {
        private final String mBlockName;
        private final BlockInfo mOldBlockInfo;
        private final BlockInfo mNewBlockInfo;
        private List<DifferentInLine> mDifferentInLines = new ArrayList<>();
    }

    @Data
    @RequiredArgsConstructor
    public static class DifferentInLine {
        private final String mTitle;
        private final Integer mDeleteStartAt;
        private final Integer mDeleteEndAt;
        private final Integer mInsertStartAt;
        private final Integer mInsertEndAt;
    }
}

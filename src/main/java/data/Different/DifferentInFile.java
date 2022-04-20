package data.Different;

import data.File.BlockInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DifferentInFile {
    private final String mFileName;
    private List<BlockInfo> mNewBlocks = new ArrayList<>();
    private List<BlockInfo> mDeleteBlocks = new ArrayList<>();
    private List<DifferentInBlock> mDifferentInBlocks = new ArrayList<>();
}

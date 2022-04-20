package data.Different;

import data.File.BlockInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DifferentInBlock {
    private final String mBlockName;
    private final BlockInfo mOldBlockInfo;
    private final BlockInfo mNewBlockInfo;
    private List<DifferentInLine> mDifferentInLines = new ArrayList<>();
}

package data.Different;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DifferentInBlock {
    private final String mBlockName;
    private final String mOldBlock;
    private final String mNewBlock;
    private List<DifferentInLine> mDIL = new ArrayList<>();
}

package data.Different;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DifferentInLine {
    private final Integer mOldLine;
    private final Integer mNewLine;
    private final Integer mDeleteStartAt;
    private final Integer mDeleteEndAt;
    private final Integer mInsertStartAt;
    private final Integer mInsertEndAt;
}

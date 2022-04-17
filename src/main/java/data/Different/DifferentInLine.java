package data.Different;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DifferentInLine {
    private final Integer mLine;
    private final List<Integer> mDeleteStartAt = new ArrayList<>();
    private final List<Integer> mDeleteEndAt = new ArrayList<>();
    private final List<Integer> mInsertStartAt = new ArrayList<>();
    private final List<Integer> mInsertEndAt = new ArrayList<>();
}

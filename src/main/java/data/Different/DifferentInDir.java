package data.Different;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DifferentInDir {
    private final List<String> mNewFiles = new ArrayList<>(10);
    private final List<String> mDeleteFiles = new ArrayList<>(10);
    private final List<DifferentInFile> mDifferentInFiles = new ArrayList<>(10);
}

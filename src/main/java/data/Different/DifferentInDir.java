package data.Different;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DifferentInDir {
    private final List<String> mNewFile = new ArrayList<>(10);
    private final List<String> mDeleteFile = new ArrayList<>(10);
    private final List<DifferentInFile> mDif = new ArrayList<>(10);
}

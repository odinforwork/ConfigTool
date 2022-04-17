package data.Different;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DifferentInFile {
    private final String mFileName;
    private List<DifferentInBlock> mDIB = new ArrayList<>();
}

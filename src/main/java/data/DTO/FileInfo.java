package data.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class FileInfo {
    private final String mFileName;
    private final Map<String, BlockInfo> mBlockInfos;

    public FileInfo(File file) throws Exception{
        mFileName = file.getName();
        mBlockInfos = BlockInfo.getBlockInfos(file);
    }
}

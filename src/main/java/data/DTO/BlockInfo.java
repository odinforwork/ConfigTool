package data.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Log4j2
@RequiredArgsConstructor
public class BlockInfo implements Cloneable{
    private final String mBlockName;
    private Map<String, String> mInfos = new HashMap<>(8);

    public static Map<String, BlockInfo> getBlockInfos(File file) throws Exception{
        Map<String, BlockInfo> result = new HashMap<>();
        BufferedReader br = null;
        BlockInfo bi = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                if (str.equals("")) {

                } else if (isTitle(str)) {
                    if (bi != null) {
                        result.put(bi.getMBlockName(), bi);
                    }
                    bi = new BlockInfo(str);
                } else {
                    if(bi == null){
                        throw new Exception(file.getAbsolutePath() + "getBlockInfo error");
                    }
                    String title = str.split("\t")[0];
                    bi.getMInfos().put(title, str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static boolean isTitle(String str) {
        return str.startsWith("[") && str.endsWith("]");
    }
}

package data.File;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import data.Different.DifferentInBlock;
import data.Different.DifferentInLine;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Log4j2
@RequiredArgsConstructor
public class BlockInfo implements Cloneable{
    private final String mBlockName;
    private List<String> mInfos = new ArrayList<>(7);

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
                        result.put(str, bi);

                        if(bi.getMInfos().size() != 7){
                            log.info(file.getName());
                            log.info(bi.getMBlockName());
                            bi.getMInfos().forEach(log::info);
                            log.trace("");
                        }
                    }
                    bi = new BlockInfo(str);
                } else {
                    if(bi == null){
                        throw new Exception(file.getAbsolutePath() + "getBlockInfo error");
                    }
                    bi.getMInfos().add(str);
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
        return str.charAt(0) == '[' && str.endsWith("]");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public BlockInfo copyList() {
        try {
            return  (BlockInfo) this.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

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
public class BlockInfo {

    private final String mFileName;
    private final String mBlockName;
    private final String[] mTitles = {"algo_type", "sensor", "device", "polling", "trig", "clr", "target",};
    private List<String> mInfos = new ArrayList<>(7);

    public static Map<String, BlockInfo> getBlockInfos(File file) {
        Map<String, BlockInfo> result = new HashMap<>();
        BufferedReader br;
        BlockInfo bi = null;

        try{
            br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                if(isTitle(str)){
                    bi = new BlockInfo(file.getName(), str);
                    result.put(str, bi);
                } else if (str.equals("")){

                } else {
                    bi.getMInfos().add(str);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new HashMap<>();
        }

        return result;
    }

    private static boolean isTitle(String str) {
        return str.charAt(0)=='[' && str.endsWith("]");
    }

    public DifferentInBlock compareTo(BlockInfo newBlock) throws Exception {
        if(!mFileName.equals(newBlock.getMFileName())
                || !mBlockName.equals(newBlock.getMBlockName())){
            throw new Exception("compare to error object");
        }

        DifferentInBlock dib = new DifferentInBlock(mBlockName);
        int size = mInfos.size();
        for(int i=0, j=0; i<size; i++,j++){
            DifferentInLine dil = compareLine(mInfos.get(i), newBlock.getMInfos().get(i), i);
            if(dil != null){
                dib.getMDIL().add(dil);
            }
        }

        return null;
    }

    private boolean startWith(String str, String sub) {
        int length = sub.length();
        return sub.equals(str.substring(0,length));
    }

    private DifferentInLine compareLine(String oldLine, String newLine, int line) {
        if(oldLine.equals(newLine)) return null;

        DifferentInLine dil = new DifferentInLine(line);
        return dil;
    }

}

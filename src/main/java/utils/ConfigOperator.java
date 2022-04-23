package utils;

import com.google.gson.Gson;
import data.config.ConfigData;

import java.io.*;

import static utils.ConfigUtils.CONFIG_PATH;

public class ConfigOperator {

    private final File mConfigFile = new File(CONFIG_PATH);
    private final Gson mGson = new Gson();

    public void writeConfig(ConfigData data) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(mConfigFile));
            String config = mGson.toJson(data);
            bw.write(config);
        } catch (Exception e) {

        } finally {
            try {
                bw.close();
            } catch (Exception e) {

            }
        }
    }

    public ConfigData readConfig() {
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(mConfigFile));
            String config = br.readLine();
            return mGson.fromJson(config, ConfigData.class);
        } catch (Exception e) {

        } finally {
            try {
                br.close();
            } catch (Exception e) {

            }
        }

        return null;
    }
}

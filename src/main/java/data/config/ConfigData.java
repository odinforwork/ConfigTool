package data.config;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ConfigData {
    @SerializedName("old_path")
    private final String mOldPath;
    @SerializedName("new_path")
    private final String mNewPath;
}

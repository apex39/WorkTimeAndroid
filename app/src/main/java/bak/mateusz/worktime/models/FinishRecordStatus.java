package bak.mateusz.worktime.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by phpstorm on 15.02.17.
 */
public class FinishRecordStatus {
    @SerializedName("status")
    @Expose
    public Boolean status;
}

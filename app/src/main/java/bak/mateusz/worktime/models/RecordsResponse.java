package bak.mateusz.worktime.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

public class RecordsResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("action_id")
    @Expose
    public Integer actionId;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("finished")
    @Expose
    public Integer finished;
    @SerializedName("minutes_spent")
    @Expose
    public Integer minutesSpent;

    public LocalDateTime getCreatedAt(){
            return DateTime.parse(createdAt, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toLocalDateTime();
    }
    public LocalDateTime getUpdatedAt(){
        return DateTime.parse(updatedAt, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toLocalDateTime();
    }
    public boolean getFinished(){
        return finished == 1;
    }
}
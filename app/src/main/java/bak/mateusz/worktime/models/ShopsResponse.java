package bak.mateusz.worktime.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopsResponse {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("opening_time")
    @Expose
    public String openingTime;
    @SerializedName("closing_time")
    @Expose
    public String closingTime;
    @SerializedName("break_time")
    @Expose
    public Integer breakTime;

}
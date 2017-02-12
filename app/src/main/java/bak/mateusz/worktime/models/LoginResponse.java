package bak.mateusz.worktime.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("role")
    @Expose
    public String role;
    @SerializedName("status")
    @Expose
    public Boolean status;

}
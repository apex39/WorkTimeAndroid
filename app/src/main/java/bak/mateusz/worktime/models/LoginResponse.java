package bak.mateusz.worktime.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    public LoginResponse(String role, Boolean status) {
        this.role = role;
        this.status = status;
    }

    @SerializedName("role")
    @Expose

    public String role;
    @SerializedName("status")
    @Expose
    public Boolean status;

}
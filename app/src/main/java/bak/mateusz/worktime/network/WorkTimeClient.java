package bak.mateusz.worktime.network;

import java.util.ArrayList;
import java.util.List;

import bak.mateusz.worktime.models.FinishRecordStatus;
import bak.mateusz.worktime.models.LoginResponse;
import bak.mateusz.worktime.models.RecordStatus;
import bak.mateusz.worktime.models.RecordsResponse;
import bak.mateusz.worktime.models.ShopsResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by timo on 08.02.17.
 */

public interface WorkTimeClient {
    @FormUrlEncoded
    @POST("login/")
    Call<LoginResponse> loginWithCredentials(@Field("username") String username, @Field("password") String password,
        @Field("shop") String shop);
    @FormUrlEncoded
    @POST("shops/")
    Call<List<ShopsResponse>> getAllShops(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("managershops/")
    Call<List<ShopsResponse>> getManagerShops(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("records/")
    Call<ArrayList<RecordsResponse>> getRecords(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("activateuser/")
    Call<LoginResponse> activateUser(@Field("username") String username, @Field("password") String password,
         @Field("new_password") String newPassword);
    @FormUrlEncoded
    @POST("details/")
    Call<String> getUserDetails(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("addrecord/")
    Call<RecordStatus> addRecord(@Field("username") String username, @Field("password") String password,
                                 @Field("type") String requestType);
    @FormUrlEncoded
    @POST("finishrecord/")
    Call<FinishRecordStatus> finishRecord(@Field("username") String username, @Field("password") String password,
                                          @Field("record_id") int recordId);
}

package bak.mateusz.worktime.network;

import java.util.List;

import bak.mateusz.worktime.models.Credentials;
import bak.mateusz.worktime.models.LoginResponse;
import bak.mateusz.worktime.models.RecordsResponse;
import bak.mateusz.worktime.models.ShopsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by timo on 08.02.17.
 */

public interface WorkTimeClient {
    @FormUrlEncoded
    @POST("login/")
    Call<LoginResponse> loginWithCredentials(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("shops/")
    Call<List<ShopsResponse>> getAllShops(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("managershops/")
    Call<List<ShopsResponse>> getManagerShops(@Field("username") String username, @Field("password") String password);
    @FormUrlEncoded
    @POST("records/")
    Call<List<RecordsResponse>> getRecords(@Field("username") String username, @Field("password") String password);
}

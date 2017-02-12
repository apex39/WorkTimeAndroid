package bak.mateusz.worktime.network;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 2016.07.12..
 */
public class ClientGenerator {
    private static final String API_BASE_URL = "http://192.168.10.10/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder;


    public static WorkTimeClient createService() {
        builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(WorkTimeClient.class);
    }

}
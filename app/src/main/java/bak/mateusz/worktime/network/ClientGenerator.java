package bak.mateusz.worktime.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by user on 2016.07.12..
 */
public class ClientGenerator {
    public static final String API_BASE_URL = "http://192.168.10.10/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder;
    static Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    static WorkTimeClient createService() {
        builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(WorkTimeClient.class);
    }

}
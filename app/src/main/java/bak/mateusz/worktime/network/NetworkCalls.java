package bak.mateusz.worktime.network;

import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.Annotation;
import java.util.List;

import bak.mateusz.worktime.R;
import bak.mateusz.worktime.models.LoginResponse;
import bak.mateusz.worktime.models.RecordsResponse;

import bak.mateusz.worktime.models.ShopsResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;


public class NetworkCalls {
    private String username;
    private String password;
    private WorkTimeClient client;

    public void login(String username, String password, String setShop) {
        EventBus.getDefault().register(this);
        this.username = username;
        this.password = password;
        client = ClientGenerator.createService();

        Call<LoginResponse> loginCall =
                client.loginWithCredentials(username, password, setShop);
        Callback<LoginResponse> loginCallCallback = new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = new LoginResponse(null,false);
                if(response.isSuccessful()){
                     loginResponse = response.body();
                }
                    EventBus.getDefault().post(loginResponse);

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                EventBus.getDefault().post(t);
            }
        };

        loginCall.enqueue(loginCallCallback);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginResponse(LoginResponse loginResponse) {
        if(loginResponse.status == true){
            switch (loginResponse.role) {
                case "worker":
                    Call<List<RecordsResponse>> recordsCall =
                            client.getRecords(username, password);
                    Callback<List<RecordsResponse>> recordsCallback = new Callback<List<RecordsResponse>>() {
                        @Override
                        public void onResponse(Call<List<RecordsResponse>> call, Response<List<RecordsResponse>> response) {
                            List<RecordsResponse> recordsResponse = response.body();
                            EventBus.getDefault().post(recordsResponse);
                        }

                        @Override
                        public void onFailure(Call<List<RecordsResponse>> call, Throwable t) {
                            EventBus.getDefault().post(t);
                        }
                    };
                    recordsCall.enqueue(recordsCallback);
                    break;
                case "admin":
                    Call<List<ShopsResponse>> allShopsCall =
                            client.getAllShops(username, password);
                    Callback<List<ShopsResponse>> allShopsCallback = new Callback<List<ShopsResponse>>() {
                        @Override
                        public void onResponse(Call<List<ShopsResponse>> call, Response<List<ShopsResponse>> response) {
                            List<ShopsResponse> shopsResponse = response.body();

                            if(shopsResponse !=null)EventBus.getDefault().post(shopsResponse);
                        }

                        @Override
                        public void onFailure(Call<List<ShopsResponse>> call, Throwable t) {
                            EventBus.getDefault().post(t);
                        }
                    };
                    allShopsCall.enqueue(allShopsCallback);
                    break;
                case "manager":
                    Call<List<ShopsResponse>> managerShopsCall =
                            client.getManagerShops(username, password);
                    Callback<List<ShopsResponse>> managerShopsCallback = new Callback<List<ShopsResponse>>() {
                        @Override
                        public void onResponse(Call<List<ShopsResponse>> call, Response<List<ShopsResponse>> response) {
                            List<ShopsResponse> shopsResponse = response.body();
                            EventBus.getDefault().post(shopsResponse);
                        }

                        @Override
                        public void onFailure(Call<List<ShopsResponse>> call, Throwable t) {
                            EventBus.getDefault().post(t);
                        }
                    };
                    managerShopsCall.enqueue(managerShopsCallback);
                    break;
                case "activation":

                        EventBus.getDefault().post(loginResponse);

                    break;
            }
        }
        EventBus.getDefault().unregister(this);
    }

    public void activateUser(String username, String password, String newPassword){
        client = ClientGenerator.createService();
        Call<LoginResponse> activateUserCall =
                client.activateUser(username, password, newPassword);
        Callback<LoginResponse> activateUserCallCallback = new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = new LoginResponse(null,false);
                if(response.isSuccessful()){
                    loginResponse = response.body();
                }
                EventBus.getDefault().post(loginResponse);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                EventBus.getDefault().post(t);
            }
        };

        activateUserCall.enqueue(activateUserCallCallback);
    }
}

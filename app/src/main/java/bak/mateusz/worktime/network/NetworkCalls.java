package bak.mateusz.worktime.network;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import bak.mateusz.worktime.models.FinishRecordStatus;
import bak.mateusz.worktime.models.LoginResponse;
import bak.mateusz.worktime.models.RecordStatus;
import bak.mateusz.worktime.models.RecordsResponse;
import bak.mateusz.worktime.models.ShopsResponse;
import retrofit2.Call;
import retrofit2.Callback;
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
                LoginResponse loginResponse = new LoginResponse(null, false);
                if (response.isSuccessful()) {
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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onLoginResponse(LoginResponse loginResponse) {
        if (loginResponse.status == true) {
            switch (loginResponse.role) {
                case "worker":
                    Call<ArrayList<RecordsResponse>> recordsCall =
                            client.getRecords(username, password);
                    Callback<ArrayList<RecordsResponse>> recordsCallback = new Callback<ArrayList<RecordsResponse>>() {
                        @Override
                        public void onResponse(Call<ArrayList<RecordsResponse>> call, Response<ArrayList<RecordsResponse>> response) {
                            ArrayList<RecordsResponse> recordsResponse = response.body();
                            EventBus.getDefault().post(recordsResponse);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<RecordsResponse>> call, Throwable t) {
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

                            if (shopsResponse != null) EventBus.getDefault().post(shopsResponse);
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

    public void activateUser(String username, String password, String newPassword) {
        client = ClientGenerator.createService();
        Call<LoginResponse> activateUserCall =
                client.activateUser(username, password, newPassword);
        Callback<LoginResponse> activateUserCallCallback = new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = new LoginResponse(null, false);
                if (response.isSuccessful()) {
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

    public void getUserDetails(String username, String password) {
        client = ClientGenerator.createService();
        Call<String> userDetailsCall =
                client.getUserDetails(username, password);
        Callback<String> userDetailsCallCallback = new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> HTMLResponse) {
                EventBus.getDefault().post(HTMLResponse.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                EventBus.getDefault().post(t);
            }
        };

        userDetailsCall.enqueue(userDetailsCallCallback);
    }

    public void addRecord(String username, String password, String recordType) {
        client = ClientGenerator.createService();
        Call<RecordStatus> addRecordCall =
                client.addRecord(username, password, recordType);
        Callback<RecordStatus> recordStatusCallback = new Callback<RecordStatus>() {
            @Override
            public void onResponse(Call<RecordStatus> call, Response<RecordStatus> status) {
                EventBus.getDefault().post(status.body());
            }

            @Override
            public void onFailure(Call<RecordStatus> call, Throwable t) {
                EventBus.getDefault().post(t);
            }
        };

        addRecordCall.enqueue(recordStatusCallback);
    }

    public void finishRecord(String username, String password, Integer recordId) {
        client = ClientGenerator.createService();
        Call<FinishRecordStatus> finishRecordStatusCall =
                client.finishRecord(username, password, recordId);
        Callback<FinishRecordStatus> finishRecordStatusCallback = new Callback<FinishRecordStatus>() {
            @Override
            public void onResponse(Call<FinishRecordStatus> call, Response<FinishRecordStatus> status) {
                EventBus.getDefault().post(status.body());
            }

            @Override
            public void onFailure(Call<FinishRecordStatus> call, Throwable t) {
                EventBus.getDefault().post(t);
            }
        };

        finishRecordStatusCall.enqueue(finishRecordStatusCallback);
    }
}

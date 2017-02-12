package bak.mateusz.worktime.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import bak.mateusz.worktime.R;
import bak.mateusz.worktime.activities.dialogs.ShopDialogFragment;

import bak.mateusz.worktime.models.ShopsResponse;
import bak.mateusz.worktime.network.NetworkCalls;

import butterknife.BindView;
import butterknife.ButterKnife;


import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class LoginActivity extends AppCompatActivity {
    boolean IS_MANAGER_LOGIN = false;
    String setShop;
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("preferences", 0);
        setShop = settings.getString("registered_shop", getString(R.string.app_name));
        this.setTitle(setShop);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void changeManagerLoginMode(MenuItem item) {
        login.setText("");
        password.setText("");
        if (!IS_MANAGER_LOGIN) {
            IS_MANAGER_LOGIN = true;
            login.setHint("Manager username");
            login.setInputType(TYPE_CLASS_TEXT);
            password.setHint("Password");
            password.setInputType(TYPE_CLASS_TEXT|TYPE_TEXT_VARIATION_PASSWORD);
            item.setTitle("Worker login");
        } else {
            IS_MANAGER_LOGIN = false;
            login.setHint("ID");
            login.setInputType(TYPE_CLASS_NUMBER);
            password.setHint("PIN");
            password.setInputType(TYPE_CLASS_NUMBER|TYPE_NUMBER_VARIATION_PASSWORD);
            item.setTitle("Change shop");
        }
    }

    public void sendLoginRequest(View view){
        progressBar.setVisibility(View.VISIBLE);
        NetworkCalls networkCalls = new NetworkCalls();
        networkCalls.login(login.getText().toString(),password.getText().toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShopsResponse(List<ShopsResponse> shopsResponse) {
        String[] shopsAddresses = new String[shopsResponse.size()];
        for (int i = 0; i < shopsResponse.size(); i++) {
            shopsAddresses[i] = shopsResponse.get(i).address;
        }

        DialogFragment shopDialog = new ShopDialogFragment();
        FragmentManager manager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putStringArray("shops_addresses",shopsAddresses);
        shopDialog.setArguments(bundle);
        progressBar.setVisibility(View.INVISIBLE);
        shopDialog.show(manager,"shop_list");
    }
}

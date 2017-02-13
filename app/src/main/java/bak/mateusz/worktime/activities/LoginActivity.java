package bak.mateusz.worktime.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import bak.mateusz.worktime.R;
import bak.mateusz.worktime.activities.dialogs.ShopDialogFragment;

import bak.mateusz.worktime.models.LoginResponse;
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
    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    MenuItem changeShop;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.login_menu,menu);
        changeShop = menu.findItem(R.id.change_shop);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferences settings = getSharedPreferences("preferences", 0);
        setShop = settings.getString("registered_shop", getString(R.string.app_name));
        this.setTitle(setShop);

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

    @Override
    protected void onResume() {
        password.setText("");
        super.onResume();
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
            item.setTitle(R.string.change_shop);
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        if(IS_MANAGER_LOGIN) {
            IS_MANAGER_LOGIN = false;
            login.setHint("ID");
            login.setInputType(TYPE_CLASS_NUMBER);
            login.setText("");
            password.setHint("PIN");
            password.setInputType(TYPE_CLASS_NUMBER|TYPE_NUMBER_VARIATION_PASSWORD);
            password.setText("");

            changeShop.setTitle(R.string.change_shop);
        }
        super.onTitleChanged(title, color);
    }

    public void sendLoginRequest(View view){
        if(password.length() >= 4) {
            login.setEnabled(false);
            password.setEnabled(false);
            view.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            NetworkCalls networkCalls = new NetworkCalls();
            networkCalls.login(login.getText().toString(),password.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(),"Password is too short",Toast.LENGTH_SHORT).show();
        }


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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginResponse(LoginResponse loginResponse) {
        progressBar.setVisibility(View.INVISIBLE);
        login.setEnabled(true);
        password.setEnabled(true);
        loginButton.setEnabled(true);
        if(loginResponse.role.equals("null") && loginResponse.status==false){
            Toast.makeText(getApplicationContext(),"Bad credentials",Toast.LENGTH_LONG).show();
        }
        else if(loginResponse.role.equals("worker") && loginResponse.status==false) {
            Toast.makeText(getApplicationContext(),"You are not activated, please provide a new password",Toast.LENGTH_LONG).show();
            Intent setPasswordIntent = new Intent(getApplicationContext(), SetPasswordActivity.class);
            setPasswordIntent.putExtra("USERNAME", login.getText().toString());
            setPasswordIntent.putExtra("PASSWORD", password.getText().toString());
            startActivity(setPasswordIntent);
        }
    }

}

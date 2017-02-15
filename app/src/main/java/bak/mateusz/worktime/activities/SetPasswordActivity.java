package bak.mateusz.worktime.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bak.mateusz.worktime.R;
import bak.mateusz.worktime.models.LoginResponse;
import bak.mateusz.worktime.network.NetworkCalls;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetPasswordActivity extends AppCompatActivity {
    @BindView(R.id.editTextPassword1) EditText password1;
    @BindView(R.id.editTextPassword2) EditText password2;
    @BindView(R.id.buttonSetPassword) Button button;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    NetworkCalls networkCalls;
    String passwordString1;
    String passwordString2;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username=getIntent().getStringExtra("USERNAME");
        password=getIntent().getStringExtra("PASSWORD");
        setTitle("ID: " + username);
        setContentView(R.layout.set_password_activity);
        ButterKnife.bind(this);
        networkCalls = new NetworkCalls();
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

    private boolean checkPasswords(){
        passwordString1 = this.password1.getText().toString();
        passwordString2 = this.password2.getText().toString();
        if (!passwordString1.isEmpty() && !passwordString2.isEmpty()) {
                if(passwordString1.equals(passwordString2)){
                    if(passwordString1.length() >=4) {
                        return true;
                    } else {
                        this.password1.setText("");
                        this.password2.setText("");
                        Toast.makeText(getApplicationContext(),"Password must be at least 4 digits long",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    this.password1.setText("");
                    this.password2.setText("");
                    Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_SHORT).show();
                return false;
            }

    }

    public void sendSetPasswordRequest(View view) {
        if (checkPasswords()) {
            password1.setEnabled(false);
            password2.setEnabled(false);
            view.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            networkCalls.activateUser(username,password,passwordString1);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginResponse(LoginResponse loginResponse) {
        if(loginResponse.role.equals("activation") && loginResponse.status==false){
            Toast.makeText(getApplicationContext(),"Activation failed",Toast.LENGTH_LONG).show();
        }
        else if(loginResponse.role.equals("activation") && loginResponse.status==true) {
            Toast.makeText(getApplicationContext(),"You are activated, you can login now",Toast.LENGTH_LONG).show();
        }
        finish();
    }
}

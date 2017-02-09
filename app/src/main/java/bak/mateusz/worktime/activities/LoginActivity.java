package bak.mateusz.worktime.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import bak.mateusz.worktime.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

public class LoginActivity extends AppCompatActivity {
    boolean IS_MANAGER_LOGIN = false;
    @BindView(R.id.login) EditText login;
    @BindView(R.id.password) EditText password;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
}

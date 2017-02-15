package bak.mateusz.worktime.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import bak.mateusz.worktime.R;
import bak.mateusz.worktime.models.FinishRecordStatus;
import bak.mateusz.worktime.models.RecordStatus;
import bak.mateusz.worktime.models.RecordsResponse;
import bak.mateusz.worktime.network.NetworkCalls;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private String username;
    private String password;

    private final Integer WORK = 2;
    private final Integer BREAK = 1;


    private Boolean isWorkStarted = false;
    private Boolean isBreakStarted = false;

    private Integer idWorkStarted;
    private Integer idBreakStarted;

    @BindView(R.id.workButton)
    Button workButton;
    @BindView(R.id.breakButton)
    Button breakButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.webView)
    WebView webView;
    ArrayList<RecordsResponse> records;
    NetworkCalls networkCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        username = getIntent().getStringExtra("USERNAME");
        password = getIntent().getStringExtra("PASSWORD");
        setTitle("ID: " + username);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        networkCalls = new NetworkCalls();


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                progressBar.setVisibility(View.INVISIBLE);
            }

        });
    }

    public void sendBreakRequest(View view) {
        if (isBreakStarted) {
            networkCalls.finishRecord(username, password, idBreakStarted);
        } else {
            networkCalls.addRecord(username, password, "BREAK");
        }

    }

    public void sendWorkRequest(View view) {
        if (isWorkStarted) {
            networkCalls.finishRecord(username, password, idWorkStarted);
        } else {
            networkCalls.addRecord(username, password, "WORK");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        networkCalls.getUserDetails(username, password);

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRecordsResponse(ArrayList<RecordsResponse> records) {
        for (RecordsResponse record : records) {
            if (!record.getFinished()) {
                if (record.actionId.equals(WORK)) {
                    isWorkStarted = true;
                    idWorkStarted = record.id;
                    ButtonThread workThread = new ButtonThread(record.getCreatedAt(), workButton);
                    workThread.updateTime();
                } else if (record.actionId.equals(BREAK)) {
                    isBreakStarted = true;
                    idBreakStarted = record.id;
                    ButtonThread breakThread = new ButtonThread(record.getCreatedAt(), breakButton);
                    breakThread.updateTime();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddRecordResponse(RecordStatus status) {
        if (status.status) {
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            Toast.makeText(getApplicationContext(), "Record successfully started", Toast.LENGTH_SHORT).show();
            startActivity(loginActivity);
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishRecordResponse(FinishRecordStatus status) {
        if (status.status) {
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            Toast.makeText(getApplicationContext(), "Record successfully finished", Toast.LENGTH_SHORT).show();
            startActivity(loginActivity);
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHTMLResponse(String HTMLwebPage) {
        webView.loadData(HTMLwebPage, "text/html; charset=UTF-8", null);

    }

    public void logout(View view) {
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
        startActivity(loginActivity);
        finish();
    }

    public class ButtonThread {
        private LocalDateTime startTime;
        private Button button;

        ButtonThread(LocalDateTime startTime, Button button) {
            // store parameter for later user
            this.startTime = startTime;
            this.button = button;
        }

        public void updateTime() {
            Timer t = new Timer();
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int hours = Hours.hoursBetween(startTime, LocalDateTime.now(DateTimeZone.UTC)).getHours();
                            Period period = new Period(startTime, LocalDateTime.now(DateTimeZone.UTC));
                            button.setText("Stop " + button.getTag() + " (" + hours + ":" + period.getMinutes() + ":" + period.getSeconds() + ")");

                        }
                    });
                }
            }, 0, 1000);
        }
    }
}



package bak.mateusz.worktime.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.sql.Time;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bak.mateusz.worktime.R;
import bak.mateusz.worktime.models.LoginResponse;
import bak.mateusz.worktime.models.RecordsResponse;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private String username;
    private String password;

    private final Integer WORK = 1;
    private final Integer BREAK = 2;

    @BindView(R.id.workButton)
    Button workButton;
    @BindView(R.id.breakButton)
    Button breakButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    ArrayList<RecordsResponse> records;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        username=getIntent().getStringExtra("USERNAME");
        password=getIntent().getStringExtra("PASSWORD");
    }

    public void sendLoginRequest(View view) {
    }

    public void sendWorkRequest(View view) {
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRecordsResponse(ArrayList<RecordsResponse> records) {
        for(RecordsResponse record : records) {
            if(!record.getFinished()) {
                if(record.actionId == WORK) {
                    ButtonThread workThread = new ButtonThread(record.getCreatedAt(),workButton);
                    workThread.updateTime();
                } else if(record.actionId == BREAK) {
                    ButtonThread breakThread = new ButtonThread(record.getCreatedAt(),breakButton);
                    breakThread.updateTime();
                }
            }
        }

    }


    public class ButtonThread {

        private DateTime startTime;
        Runnable updater;
        private Button button;

        ButtonThread(DateTime startTime, Button button) {
            // store parameter for later user
            this.startTime = startTime;
            this.button = button;
        }


        public void updateTime(){
            Timer t = new Timer();

            //schedule a timer
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int hours = Hours.hoursBetween(startTime, DateTime.now()).getHours();
                            Period period = new Period(startTime, DateTime.now());
                            button.setText("Stop "+button.getTag()+" ("+hours+":"+period.getMinutes()+":"+period.getSeconds()+")");

                        }
                    });
                }
            },0,1000);
        }

    }
}



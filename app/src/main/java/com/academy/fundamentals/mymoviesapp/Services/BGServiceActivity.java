package com.academy.fundamentals.mymoviesapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.academy.fundamentals.mymoviesapp.R;

public class BGServiceActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PROGRESS_UPDATE_ACTION = "PROGRESS_UPDATE_ACTION";

    private Button serviceBtn;
    private Button intentServiceBtn;
    private TextView progressValue;
    private boolean isServiceRunning;
    private boolean isIntentServiceRunning;

    private BackgroundProgressReceiver mBackgroundProgressReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgservice);

        serviceBtn = findViewById(R.id.service_button);
        intentServiceBtn = findViewById(R.id.intent_button);

        progressValue = findViewById(R.id.percents_text);

        serviceBtn.setOnClickListener(this);
        intentServiceBtn.setOnClickListener(this);
    }

    /**
     * When the activity enters the Resumed state, it comes to the foreground,
     * and then the system invokes the onResume() callback.
     * This is the state in which the app interacts with the user.
     * The app stays in this state until something happens to take focus away from the app.
     * <p>
     * You should implement onResume() to initialize components that you release during onPause(),
     * and perform any other initializations that must occur each time the activity enters the Resumed state.
     */
    @Override
    protected void onResume() {
        super.onResume();

        subscribeForProgressUpdates();
    }

    @Override
    protected void onPause() {

        if (mBackgroundProgressReceiver != null) {
            unregisterReceiver(mBackgroundProgressReceiver);
        }

        super.onPause();
    }

    private void subscribeForProgressUpdates() {
        if (mBackgroundProgressReceiver == null) {
            mBackgroundProgressReceiver = new BackgroundProgressReceiver();
        }

        IntentFilter progressUpdateActionFilter = new IntentFilter(PROGRESS_UPDATE_ACTION);
        registerReceiver(mBackgroundProgressReceiver, progressUpdateActionFilter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.service_button):
                if (isIntentServiceRunning) {
                    stopService(new Intent(this, HardJobIntentService.class));
                    isIntentServiceRunning = false;
                } else if (isServiceRunning) {
                    stopService(new Intent(this, HardJobService.class));
                    isServiceRunning = false;
                }
                startService(new Intent(this, HardJobService.class));
                isServiceRunning = true;
                break;
            case (R.id.intent_button):
                if (isIntentServiceRunning) {
                    stopService(new Intent(this, HardJobIntentService.class));
                    isIntentServiceRunning = false;
                } else if (isServiceRunning) {
                    stopService(new Intent(this, HardJobService.class));
                    isServiceRunning = false;
                }
                startService(new Intent(this, HardJobIntentService.class));
                isIntentServiceRunning = true;
                break;
        }
    }


    /**
     * A broadcast receiver is a component which allows you to register for system or application events.
     * All registered receivers for an event are notified by the Android runtime once this event happens.
     */
    private Toast mToast;

    public class BackgroundProgressReceiver extends BroadcastReceiver {

        public static final String PROGRESS_VALUE_KEY = "PROGRESS_VALUE_KEY";

        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        @Override
        public void onReceive(Context context, Intent intent) {

            mToast = Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG);
            mToast.show();

            int progress = intent.getIntExtra(PROGRESS_VALUE_KEY, -1);

            if (100 == progress) {
                progressValue.setText("Done!");
                isServiceRunning = false;
                isIntentServiceRunning = false;
            }

            else if (progressValue != null) {
                progressValue.setText(progress + "%");
            }

        }

    }

    @Override
    protected void onDestroy() {
        // Check to see whether this activity is in the process of finishing,
        // either because you called finish() on it or someone else has requested that it finished.
        // This is often used in onPause() to determine whether the activity is simply pausing or completely finishing.
        if (isFinishing()) {
            if (isIntentServiceRunning) {
                stopService(new Intent(this, HardJobIntentService.class));
            }
            if (isServiceRunning) {
                stopService(new Intent(this, HardJobService.class));
            }

            if (mToast != null) {
                mToast.cancel();
            }
        }
        super.onDestroy();
    }
}

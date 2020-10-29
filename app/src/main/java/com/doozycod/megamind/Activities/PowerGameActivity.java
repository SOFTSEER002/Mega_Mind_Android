package com.doozycod.megamind.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.doozycod.megamind.R;
import com.doozycod.megamind.Service.BroadcastService;

import java.util.concurrent.TimeUnit;

public class PowerGameActivity extends AppCompatActivity {
    TextView timer, numberPowerGame;
    final int delay = 1000; // delay in count down
    int count;// seconds
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };
    Button exitButton;
    CountDownTimer countDownTimer;

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            Log.i("TAG", "Countdown seconds remaining: " + millisUntilFinished / 1000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_game);
        startService(new Intent(this, BroadcastService.class));
//        Log.e("TAG", "Started service");
        timer = findViewById(R.id.timer);
        numberPowerGame = findViewById(R.id.numberPowerGame);
        exitButton = findViewById(R.id.exitButton);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);


        int time = getIntent().getIntExtra("seconds", 60);
        String temp = "";
        if (time == 60) {
            temp = 1 + " minute";
        }
        if (time == 120) {
            temp = 2 + " minutes";
        }
        @SuppressLint("InvalidWakeLockTag") final PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");

        if (getIntent().getStringExtra("powerType").equals("Addition")) {
            numberPowerGame.setText("Starting from " + getIntent().getStringExtra("startForm")
                    + ", keep adding " + getIntent().getIntExtra("random", 0) + " For "
                    + temp);
        }
        if (getIntent().getStringExtra("powerType").equals("Subtraction")) {
//            numberPowerGame.setText("-" + getIntent().getIntExtra("random", 0));
            numberPowerGame.setText("Starting from " + getIntent().getStringExtra("startForm")
                    + ", keep on subtracting " + getIntent().getIntExtra("random", 0) + " For "
                    + temp);
        }
        if (getIntent().getIntExtra("seconds", 60) == 60) {
            count = 60000;
            wl.acquire(count);

        }
        if (getIntent().getIntExtra("seconds", 60) == 120) {
            count = 120000;
            wl.acquire(count);

        }
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                Intent intent = new Intent
                        (PowerGameActivity.this,
                                MainNavigation.class);

                intent.putExtra("practice", "answer");
                startActivity(intent);
                finishAffinity();
            }
        });
        countDownTimer = new CountDownTimer(count, delay) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                timer.setText("" + String.format("%d:%d\n sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                wl.release();
                Intent intent = new Intent(PowerGameActivity.this, EnterAnswer.class);
                intent.putExtra("random", getIntent().getIntExtra("random", 0));
                intent.putExtra("startForm", getIntent().getStringExtra("startForm"));
                intent.putExtra("powerType", getIntent().getStringExtra("powerType"));
                intent.putExtra("seconds", getIntent().getIntExtra("seconds", 0));
                Log.e("TAG", "onFinish: done");
                startActivity(intent);
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
        Log.e("TAG", "Registered broacast receiver");
    }

    @Override
    public void onPause() {
        super.onPause();
//        unregisterReceiver(br);
//        Log.e("TAG", "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
        Log.e("TAG", "Stopped service");
        super.onDestroy();
    }
}

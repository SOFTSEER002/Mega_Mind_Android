package com.doozycod.megamind.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
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
        Log.e("TAG", "Started service");
        timer = findViewById(R.id.timer);
        numberPowerGame = findViewById(R.id.numberPowerGame);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        if (getIntent().getStringExtra("powerType").equals("Addition")) {
            numberPowerGame.setText("+" + getIntent().getIntExtra("random", 0) + "");
        }
        if (getIntent().getStringExtra("powerType").equals("Subtraction")) {
            numberPowerGame.setText("-" + getIntent().getIntExtra("random", 0));
        }
        if (getIntent().getIntExtra("seconds", 60) == 60) {
            count = 60000;
            wl.acquire(count);

        }
        if (getIntent().getIntExtra("seconds", 60) == 120) {
            count = 120000;
            wl.acquire(count);

        }
        new CountDownTimer(count, delay) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                timer.setText("" + String.format("%d min : %d sec",

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

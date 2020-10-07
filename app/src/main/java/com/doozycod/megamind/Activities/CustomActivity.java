package com.doozycod.megamind.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.megamind.R;

public class CustomActivity extends AppCompatActivity {

    int noOfDigits=5,flickeringSpeed=500,subtraction=0;
    int digitSize =1;
    Button level1,level2,level3,feedback,start,customButton;
    ImageView plus, minus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_custom);

        Button selectLevel1 = findViewById(R.id.selectLevel1);
        selectLevel1.setText("Select skill");

        String[] arraySpinner = new String[] {
                "Single Digit", "Double Digit", "Triple Digit"
        };
        final Spinner digitSizeSpinner = findViewById(R.id.digitSizeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        digitSizeSpinner.setAdapter(adapter);

        String[] noOfDigitsString = new String[] {
                "5", "10", "15"
        };
        final Spinner noOfDigitsSpinner = findViewById(R.id.noOfDigitsSpinner);
        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, noOfDigitsString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);

        start = findViewById(R.id.startCustomGamebtn);
         plus = findViewById(R.id.plusButton);
         minus = findViewById(R.id.minusButton);
        customButton = findViewById(R.id.customButton);

        customButton.setSelected(true);


       feedback = findViewById(R.id.feedbackButton);

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent
                        (CustomActivity.this,
                                FeedbackActivity.class);
                startActivity(intent);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView flickeringSpeedTextView = findViewById(R.id.flickeringSpeedTextView);
                int speed = Integer.parseInt
                        (flickeringSpeedTextView.getText().toString());
                speed=speed+50;
                flickeringSpeedTextView.setText(String.valueOf(speed));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView flickeringSpeedTextView = findViewById(R.id.flickeringSpeedTextView);
                int speed = Integer.parseInt
                        (flickeringSpeedTextView.getText().toString());
                if(speed==50){
                    Toast.makeText(CustomActivity.this, "You can set minimum 50ms speed.", Toast.LENGTH_SHORT).show();
                }else{
                    speed=speed-50;
                }
                flickeringSpeedTextView.setText(String.valueOf(speed));
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView flickeringSpeedTextView =
                        findViewById(R.id.flickeringSpeedTextView);

                String digitSizeString =
                        digitSizeSpinner.getSelectedItem().toString();
                String noOfDigitsString =
                        noOfDigitsSpinner.getSelectedItem().toString();
                String flickeringSpeedTextViewString = flickeringSpeedTextView.getText().toString();
                if(digitSizeString.equals("Single Digit")){
                    digitSize = 1;
                }
                if(digitSizeString.equals("Double Digit")){
                    digitSize = 2;
                }
                if(digitSizeString.equals("Triple Digit")){
                    digitSize = 3;
                }
                noOfDigits = Integer.parseInt(noOfDigitsString);
                flickeringSpeed =
                        Integer.parseInt(flickeringSpeedTextViewString);
                subtraction = 0;

                Switch syncSwitch =  findViewById(R.id.subtractionSwitch);
                if(syncSwitch.isChecked()){
                    subtraction =1;
                }

                Intent intent = new Intent
                        (CustomActivity.this,
                                GameActivity.class);
                intent.putExtra("digitSize",digitSize);
                intent.putExtra("noOfDigits",noOfDigits);
                intent.putExtra("flickeringSpeed",flickeringSpeed);
                intent.putExtra("subtraction",subtraction);
                intent.putExtra("levelNumber",digitSize);
                startActivity(intent);

            }
        });

        level1 = findViewById(R.id.level1btn);
        level2 = findViewById(R.id.level2btn);
        level3 = findViewById(R.id.level3btn);

        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });

        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(CustomActivity.this, MainActivity.class);
        startActivity(setIntent);
    }

    public void startMainActivity(){
        Intent setIntent = new Intent(CustomActivity.this, MainActivity.class);
        startActivity(setIntent);
    }
}

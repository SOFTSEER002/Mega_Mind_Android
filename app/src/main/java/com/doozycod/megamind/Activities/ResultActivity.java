package com.doozycod.megamind.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.doozycod.megamind.R;

public class ResultActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    Button startNewGame, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);

        TextView totalQuestionsTextView =
                findViewById(R.id.totalQuestionsTextView);
        TextView correctAnswerTextview =
                findViewById(R.id.correctAnswerTextview);
        TextView wrongAnswerTextView =
                findViewById(R.id.wrongAnswerTextView);
        TextView percentageTextView = findViewById(R.id.percentageTextView);
        TextView percentageTopLabel = findViewById(R.id.percentageTopLabel);
        TextView levelNoTextView3 =
                findViewById(R.id.levelNoTextView3);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int totalCorrect = prefs.getInt("result", -22);
        int questionNo = prefs.getInt("questions", -22);
        int wrongAnswers = prefs.getInt("wrongAnswers", -22);
        int levelNumber = prefs.getInt("levelNumber", -22);

        if (levelNumber == -22) {
            levelNoTextView3.setText("Level 0");
        } else {

            if (levelNumber == 1)
                levelNoTextView3.setText("Single Digit");

            if (levelNumber == 2)
                levelNoTextView3.setText("Double Digit");
            if (levelNumber == 3)
                levelNoTextView3.setText("Triple Digit");
        }

        if (questionNo == -22) {
            totalQuestionsTextView.setText("0");
        } else {
            totalQuestionsTextView.setText(String.valueOf(questionNo));
        }

        if (totalCorrect == -22) {
            correctAnswerTextview.setText("0D");

        } else {
            correctAnswerTextview.setText(String.valueOf(totalCorrect));
            float percentD = (float) totalCorrect / questionNo;
            percentD = percentD * 100;
            int p = Math.round(percentD);

            percentageTextView.setText
                    (p + "%");
            percentageTopLabel.setText
                    (p + "%");
        }

        if (wrongAnswers == -22) {
            wrongAnswerTextView.setText("0D");
        } else {
            wrongAnswerTextView.setText(String.valueOf(wrongAnswers));
        }


        startNewGame = findViewById(R.id.nextQuestionResults);
        back = findViewById(R.id.back);

        startNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent
                        (ResultActivity.this,
                                MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(ResultActivity.this, getIntent().getIntExtra("int",0), Toast.LENGTH_SHORT).show();

               onBackPressed();


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

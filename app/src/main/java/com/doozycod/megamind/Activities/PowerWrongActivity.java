package com.doozycod.megamind.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.megamind.Interface.ApiService;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Service.ApiUtils;
import com.doozycod.megamind.Utils.CustomProgressBar;
import com.doozycod.megamind.Utils.DatabaseHelper;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.doozycod.megamind.Utils.DatabaseHelper.ASSIGNMENT_ID;
import static com.doozycod.megamind.Utils.DatabaseHelper.QUESTION_FOR_STUDENT;

public class PowerWrongActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferenceMethod sharedPreferenceMethod;
    DatabaseHelper dbHelper;
    TextView numbersArrayTextView, WrongAnswerTv, usersAnswer, result, questionNoTextView;
    Button seeresults, nextQuestion, quitButton, tryAgainWrongbutton;
    ApiService apiService;
    CustomProgressBar progressBar;
    private boolean isSubmitted = false;
    TextView powerTypeWrongTxt, powerWrongTxt, powerWrongStartFromTxt, powerWrongTimeTxt,
            answerPowerWrongTxt, countWrongTxt;
    String powerExerciseType, count, answer, startForm, time, randomNumber;

    private void initUI() {
        seeresults = findViewById(R.id.seeresults);
        nextQuestion = findViewById(R.id.nextQuestionResults);
        quitButton = findViewById(R.id.quitToMainBtnWrong);
        numbersArrayTextView = findViewById(R.id.numbersArrayTextView);
//        WrongAnswerTv = findViewById(R.id.WrongAnswerTextView);
        usersAnswer = findViewById(R.id.usersAnswer);
        result = findViewById(R.id.calculateResultTV);
        questionNoTextView = findViewById(R.id.questionNoTextView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_power_wrong);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        progressBar = new CustomProgressBar(this);

        apiService = ApiUtils.getAPIService();
        dbHelper = new DatabaseHelper(this);

//          typcasting
        tryAgainWrongbutton = findViewById(R.id.tryAgainWrongButton);
        powerTypeWrongTxt = findViewById(R.id.powerTypeWrongTxt);
        powerWrongTxt = findViewById(R.id.powerWrongTxt);
        powerWrongStartFromTxt = findViewById(R.id.powerWrongStartFromTxt);
        powerWrongTimeTxt = findViewById(R.id.powerWrongTimeTxt);
        answerPowerWrongTxt = findViewById(R.id.answerPowerWrongTxt);
        countWrongTxt = findViewById(R.id.countWrongTxt);


        powerExerciseType = getIntent().getStringExtra("exercisePowerType");
        randomNumber = getIntent().getStringExtra("randomNumber");
        time = getIntent().getStringExtra("time");
        startForm = getIntent().getStringExtra("startForm");
        answer = getIntent().getStringExtra("answer");
        count = getIntent().getStringExtra("count");

//        set data
        powerTypeWrongTxt.setText(sharedPreferenceMethod.getExerciseType());
        if (sharedPreferenceMethod.getExerciseType().equals("Subtraction")) {
            powerWrongTxt.setText("-" + sharedPreferenceMethod.getPowerRandom());
        } else {
            powerWrongTxt.setText("+" +sharedPreferenceMethod.getPowerRandom());
        }
//        powerWrongTxt.setText(sharedPreferenceMethod.getPowerRandom());
        powerWrongStartFromTxt.setText(sharedPreferenceMethod.getStartFrom());
        answerPowerWrongTxt.setText(sharedPreferenceMethod.getPowerAnswer());
        powerWrongTimeTxt.setText(sharedPreferenceMethod.getSeconds() + " seconds");
        countWrongTxt.setText("-");
        tryAgainWrongbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PowerWrongActivity.this, MainNavigation.class);
                intent.putExtra("power", "answer");
                startActivity(intent);
                finishAffinity();
            }
        });
    }


//    backup
/*        apiService.updateAssignment(Integer.parseInt(sharedPreferenceMethod.getID()), Integer.parseInt(sharedPreferenceMethod.getAssignmentId()), model).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.e("UploadfinalResult", "onnnnn: " + response.code());

                Log.e(TAG, "onResponse: " + response.toString());
                progressBar.hideProgress();
                if (response.code() == 502) {
                    Log.e("UploadfinalResult", "onResponse: " + sharedPreferenceMethod.getID() + " & " + Integer.parseInt(sharedPreferenceMethod.getAssignmentId()));
                    Toast.makeText(WrongActivity.this, "Something went wrong! response code " + response.code(), Toast.LENGTH_SHORT).show();

                }
                if (response.code() == 500) {
                    Log.e("Update Assignment", "onResponse: Invalid Student Id or Assignment Id");
                }
                if (response.code() == 200) {
                    Log.e("UploadfinalResult", "onResponse: " + sharedPreferenceMethod.getID() + " ha " + Integer.parseInt(sharedPreferenceMethod.getAssignmentId()));
                    Toast.makeText(WrongActivity.this, "Result submitted!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("UploadfinalResult", "onFailure: " + t.getMessage());
                progressBar.hideProgress();
            }
        });*/


    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(PowerWrongActivity.this, MainNavigation.class);
        setIntent.putExtra("power", "answer");

        startActivity(setIntent);
    }
}

package com.doozycod.megamind.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

public class PowerCorrectActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferenceMethod sharedPreferenceMethod;
    DatabaseHelper dbHelper;
    ApiService apiService;
    CustomProgressBar progressBar;
    private boolean isSubmitted = false;
    TextView powerTypeCorrectTxt, powerCorrectTxt, powerCorrectStartFromTxt, powerCorrectTimeTxt,
            answerPowerCorrectTxt, countCorrectTxt;
    String powerExerciseType, count, answer, startForm, time, randomNumber;
    Button tryAgainCorrectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_power_correct);
        tryAgainCorrectButton = findViewById(R.id.tryAgainCorrectButton);
        powerTypeCorrectTxt = findViewById(R.id.powerTypeCorrectTxt);
        powerCorrectTxt = findViewById(R.id.powerCorrectTxt);
        powerCorrectStartFromTxt = findViewById(R.id.powerCorrectStartFromTxt);
        powerCorrectTimeTxt = findViewById(R.id.powerCorrectTimeTxt);
        answerPowerCorrectTxt = findViewById(R.id.answerPowerCorrectTxt);
        countCorrectTxt = findViewById(R.id.countCorrectTxt);
//        shared prefernces for result
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        progressBar = new CustomProgressBar(this);
        dbHelper = new DatabaseHelper(this);
        apiService = ApiUtils.getAPIService();
        powerExerciseType = getIntent().getStringExtra("exercisePowerType");
        randomNumber = getIntent().getStringExtra("randomNumber");
        time = getIntent().getStringExtra("time");
        startForm = getIntent().getStringExtra("startForm");
        answer = getIntent().getStringExtra("answer");
        count = getIntent().getStringExtra("count");


        powerTypeCorrectTxt.setText(sharedPreferenceMethod.getExerciseType());
        if (sharedPreferenceMethod.getExerciseType().equals("Subtraction")) {
            powerCorrectTxt.setText("-" + sharedPreferenceMethod.getPowerRandom());

        } else {
            powerCorrectTxt.setText("+" +sharedPreferenceMethod.getPowerRandom());

        }
        powerCorrectStartFromTxt.setText(sharedPreferenceMethod.getStartFrom());
        answerPowerCorrectTxt.setText(sharedPreferenceMethod.getPowerAnswer());
        powerCorrectTimeTxt.setText(sharedPreferenceMethod.getSeconds() + " seconds");
        countCorrectTxt.setText(sharedPreferenceMethod.getCount());

        tryAgainCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PowerCorrectActivity.this, MainNavigation.class);
                intent.putExtra("power", "answer");
                startActivity(intent);
                finishAffinity();
            }
        });
    }


    public void uploadFinalResult(JSONObject model) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, model.toString());
        Request request = new Request.Builder()
                .url("https://glneayr1ic.execute-api.ca-central-1.amazonaws.com/Development/student/" + sharedPreferenceMethod.getID() + "/assignment/" + sharedPreferenceMethod.getAssignmentId())
                .patch(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "629ee071-2dc9-a8a1-e526-4fefab35127e")
                .build();

        try {
            okhttp3.Response response = client.newCall(request).execute();
            Log.e(TAG, "OK HTTP  " + response.code());
            if (response.code() == 502) {
                Log.e("UploadfinalResult", "onResponse: " + sharedPreferenceMethod.getID() + " & " + Integer.parseInt(sharedPreferenceMethod.getAssignmentId()));
                Toast.makeText(PowerCorrectActivity.this, "Something went wrong! 0% result, Error code = " + response.code(), Toast.LENGTH_SHORT).show();
                progressBar.hideProgress();
            }
            if (response.code() == 500) {
                Log.e("Update Assignment", "onResponse: Invalid Student Id or Assignment Id");
                Toast.makeText(PowerCorrectActivity.this, "Invalid Student Id or Assignment Id", Toast.LENGTH_SHORT).show();
                progressBar.hideProgress();
            }
            if (response.code() == 200) {
                Toast.makeText(PowerCorrectActivity.this, "Result submitted!", Toast.LENGTH_SHORT).show();
                isSubmitted = true;
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(QUESTION_FOR_STUDENT, ASSIGNMENT_ID + " = ?", new String[]{sharedPreferenceMethod.getAssignmentId()});
//                db.close();
//                startActivity(intent);
                progressBar.hideProgress();

            }
            if (response.code() == 404) {
                Log.e(TAG, "uploadFinalResult: " + response.toString());
                progressBar.hideProgress();

            }

        } catch (IOException e) {
            e.printStackTrace();
            progressBar.hideProgress();

        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(PowerCorrectActivity.this, MainNavigation.class);
        setIntent.putExtra("power", "answer");
        startActivity(setIntent);

    }
}

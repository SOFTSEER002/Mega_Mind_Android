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

public class WrongActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferenceMethod sharedPreferenceMethod;
    DatabaseHelper dbHelper;
    TextView numbersArrayTextView, correctAnswerTv, usersAnswer, result, questionNoTextView;
    Button seeresults, nextQuestion, quitButton;
    ApiService apiService;
    CustomProgressBar progressBar;
    private boolean isSubmitted = false;

    private void initUI() {
        seeresults = findViewById(R.id.seeresults);
        nextQuestion = findViewById(R.id.nextQuestionResults);
        quitButton = findViewById(R.id.quitToMainBtnWrong);
        numbersArrayTextView = findViewById(R.id.numbersArrayTextView);
        correctAnswerTv = findViewById(R.id.correctAnswerTextView);
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
        setContentView(R.layout.activity_wrong);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        progressBar = new CustomProgressBar(this);

        apiService = ApiUtils.getAPIService();
        dbHelper = new DatabaseHelper(this);
        Bundle extras = getIntent().getExtras();
        int[] numbersArray = extras.getIntArray("numbersArray");

//        typecasting
        initUI();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
//        float correctAns = extras.getFloat("correctAns");
        long userAns = extras.getLong("userAns");

        int questinNum = 0;
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        int totalCorrect = prefs.getInt("result", -22);
        final int questionNo = prefs.getInt("questions", -22);

        if (getIntent().hasExtra("assignmentAnswer")) {
            int totalCorrect = 0;
            for (int i = 0; i < dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size(); i++) {
                if (dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).isResult()) {
                    totalCorrect += 1;
                }
            }
            questinNum = dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size() + 1;

            Log.e("WRONG ACTIVITY", "onCreate: assignmentIntent");
            quitButton.setVisibility(View.VISIBLE);
//            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) nextQuestion.getLayoutParams();
//            // position on right bottom
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
//            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//
//            nextQuestion.setLayoutParams(rlp);
//            rlp.setMargins(0, 0, 0, 100);
            if (questinNum == 0) {
                questinNum = 1;
                questionNoTextView.setText("Question " + questinNum);
            } else {

                questionNoTextView.setText("Question " + questinNum);
            }

            if (totalCorrect == 0) {
                result.setText("0/" + sharedPreferenceMethod.getQuestions());
            } else {
                result.setText((totalCorrect) + "/" + sharedPreferenceMethod.getQuestions());
            }
            if (questinNum == Integer.parseInt(sharedPreferenceMethod.getQuestions())) {
                seeresults.setVisibility(View.VISIBLE);
                nextQuestion.setVisibility(View.GONE);
                quitButton.setVisibility(View.GONE);
                prefs.edit().remove("questions").apply();

            }

            for (int i = 0; i < dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size(); i++) {
                Log.e("Assignment Finish", "onClick: " + dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getQuestionNum());
            }
        } else {
            int totalCorrect1 = prefs.getInt("result", -22);

            if (questionNo == -22) {
                questionNoTextView.setText("Question 1");
            } else {
                questionNoTextView.setText("Question " + questionNo);
            }
            if (totalCorrect1 == -22) {
                result.setText("0/" + sharedPreferenceMethod.getQuestions());
            } else {
                result.setText((totalCorrect1) + "/" + sharedPreferenceMethod.getQuestions());
            }
            if (getIntent().hasExtra("assignmentAnswer")) {
                if (questionNo == Integer.parseInt(sharedPreferenceMethod.getQuestions()) && questionNo != 1) {
                    seeresults.setVisibility(View.VISIBLE);
                    nextQuestion.setVisibility(View.GONE);
//                    quitButton.setVisibility(View.GONE);
                    prefs.edit().remove("questions").apply();

                }
            } else {
                if (50 == Integer.parseInt(sharedPreferenceMethod.getQuestions())) {
                    seeresults.setVisibility(View.VISIBLE);
                    nextQuestion.setVisibility(View.GONE);

//                    quitButton.setVisibility(View.VISIBLE);
                    prefs.edit().remove("questions").apply();

                }
            }

        }


        String numbersString = String.valueOf(numbersArray[0]);

        for (int i = 1; i < numbersArray.length; i++) {
            if ((numbersArray[i] * (-1)) <= 0) {
                if (sharedPreferenceMethod.getType().equals("Addition")) {

                    numbersString += "+" + numbersArray[i];

                }
                if (sharedPreferenceMethod.getType().equals("Multiplication")) {
                    numbersString += "x" + numbersArray[i];
                }
                if (sharedPreferenceMethod.getType().equals("Division")) {
                    numbersString += "÷" + numbersArray[i];
                }
                if (sharedPreferenceMethod.getType().equals("Subtraction")) {
                    if ((numbersArray[i] * (-1)) <= 0) {
                        //positive integer or zero
                        numbersString += "+" + numbersArray[i];
                    } else {
                        numbersString += numbersArray[i];
                    }
                }
            } else {
                numbersString += numbersArray[i];
            }
//            if((numbersArray[i]*(-1))>0)
        }

        numbersArrayTextView.setText(numbersString + "=");
//        correctAnswerTv.setText(String.valueOf(correctAns));
        /*   if (sharedPreferenceMethod.getType().equals("Divide")) {
            float correctAnss = extras.getFloat("correctAns");
            correctAnswerTv.setText(String.valueOf(correctAnss));
            usersAnswer.setText(String.valueOf(correctAns));
            dbHelper.insertQuestionAnswer("Question " + questionNo,sharedPreferenceMethod.getQuestions(), numbersString + "=",
                    sharedPreferenceMethod.getType(), String.valueOf(userAns), String.valueOf(correctAnss),"wrong");

        } else {
            int correctAnss = (int) extras.getFloat("correctAns");
            correctAnswerTv.setText(String.valueOf(correctAns));
            int userAnss = (int) userAns;

            usersAnswer.setText(String.valueOf(userAnss));
            dbHelper.insertQuestionAnswer("Question " + questionNo,sharedPreferenceMethod.getQuestions(), numbersString + "=",
                    sharedPreferenceMethod.getType(), String.valueOf(userAns), String.valueOf(correctAnss),"wrong");
        }*/
        long correctAns = extras.getLong("correctAns");

        long UserAns = userAns;
        if (sharedPreferenceMethod.getType().equals("Division")) {
            correctAnswerTv.setText(String.valueOf(correctAns));
            usersAnswer.setText(String.valueOf(userAns));
            if (getIntent().hasExtra("assignmentAnswer")) {
                dbHelper.insertAssignmentQuestionAnswer(sharedPreferenceMethod.getID(), sharedPreferenceMethod.getAssignmentId(), questinNum + "",
                        sharedPreferenceMethod.getQuestions(), numbersString + "=", sharedPreferenceMethod.getType(), String.valueOf(userAns),
                        String.valueOf(correctAns), "false");
//                dbHelper.insertQuestionAssignmentAnswer("" + questionNo,"101","5","400", numbersString + "=",
//                        sharedPreferenceMethod.getType(), String.valueOf(userAns), String.valueOf(correctAns),"07/12/2019","wrong");

            } else {
                dbHelper.insertQuestionAnswer(" " + questionNo, sharedPreferenceMethod.getQuestions(), numbersString + "=",
                        sharedPreferenceMethod.getType(), String.valueOf(userAns), String.valueOf(correctAns), "false");
            }
        } else {

            correctAnswerTv.setText(String.valueOf(correctAns));
            usersAnswer.setText(String.valueOf(UserAns));
            if (getIntent().hasExtra("assignmentAnswer")) {
                dbHelper.insertAssignmentQuestionAnswer(sharedPreferenceMethod.getID(), sharedPreferenceMethod.getAssignmentId(), questinNum + "",
                        sharedPreferenceMethod.getQuestions(), numbersString + "=", sharedPreferenceMethod.getType(), String.valueOf(userAns),
                        String.valueOf(correctAns), "false");
            } else {
                dbHelper.insertQuestionAnswer(" " + questionNo, sharedPreferenceMethod.getQuestions(), numbersString + "=",
                        sharedPreferenceMethod.getType(), String.valueOf(UserAns), String.valueOf(correctAns), "false");
            }
        }
        final Intent intent = new Intent
                (WrongActivity.this,
                        MainNavigation.class);
        if (questinNum == Integer.parseInt(sharedPreferenceMethod.getQuestions())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int totalCorrect = 0;
                    for (int i = 0; i < dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size(); i++) {
                        if (dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).isResult()) {
                            totalCorrect += 1;
                        }
                    }
                    float percentD = (float) totalCorrect / dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size();
                    percentD = percentD * 100;
                    int p = Math.round(percentD);
                    sharedPreferenceMethod.setPercentage(p + "");
//                    UpdateQuestionModel model = new UpdateQuestionModel();
//                    model.setPercentage(p);
//                    model.setAssignmentStatus("completed");
//                    model.setActualCompletionDate(checkAssignmentDay());
//                    model.setQuestions(dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())));


//                    json request for questions submission
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("assignmentStatus", "Completed");
                        jsonObject.put("percentage", p);
                        jsonObject.put("actualCompletionDate", checkAssignmentDay());
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size(); i++) {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("questionNum", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getQuestionNum());
                            jsonObject1.put("question", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getQuestion());
                            jsonObject1.put("answer", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getAnswer());
                            jsonObject1.put("studentAnswer", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getStudentAnswer());
                            jsonObject1.put("result", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).isResult());
                            jsonArray.put(jsonObject1);
                        }

                        jsonObject.put("questions", jsonArray);
                        Log.e("JSON OBJECT", "JSON OBJECT: " + jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(WrongActivity.this, "Please wait, Submitting your result!", Toast.LENGTH_SHORT).show();

                    progressBar.showProgress();
                    uploadFinalResult(jsonObject);
                }
            }, 500);
        }

        final int finalQuestinNum = questinNum;
        seeresults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "uploadFinalResult: student ID" + Integer.parseInt(sharedPreferenceMethod.getID()));
                sharedPreferenceMethod.saveOnResultClick("");

                for (int i = 0; i < dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size(); i++) {
                    Log.e("SEE RESULTS", "onClick: " + dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getQuestion() + " = " + dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getAnswer());
                }
                intent.putExtra("result", "seeResult");
                if (getIntent().hasExtra("assignmentAnswer")) {
                    intent.putExtra("assignment", "assignment");
                    /*int totalCorrect = 0;
                    for (int i = 0; i < dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size(); i++) {
                        if (dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).isResult()) {
                            totalCorrect += 1;
                        }
                    }
                    float percentD = (float) totalCorrect / dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size();
                    percentD = percentD * 100;
                    int p = Math.round(percentD);
                    Log.e(" Percentage", "onClick: percentage " + p);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("assignmentStatus", "Completed");
                        jsonObject.put("percentage", p);
                        jsonObject.put("actualCompletionDate", checkAssignmentDay());
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).size(); i++) {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("questionNum", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getQuestionNum());
                            jsonObject1.put("question", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getQuestion());
                            jsonObject1.put("answer", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getAnswer());
                            jsonObject1.put("studentAnswer", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).getStudentAnswer());
                            jsonObject1.put("result", dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())).get(i).isResult());
                            jsonArray.put(jsonObject1);
                        }

                        jsonObject.put("questions", jsonArray);
                        Log.e("JSON OBJECT", "JSON OBJECT: " + jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
//                    UpdateQuestionModel model = new UpdateQuestionModel();
//                    model.setPercentage(p);
//                    model.setAssignmentStatus("Completed");
//                    model.setActualCompletionDate(checkAssignmentDay());
//
//                    model.setQuestions(dbHelper.getQuestionResults(Integer.parseInt(sharedPreferenceMethod.getAssignmentId())));
//                    progressBar.showProgress();


                    assignmentProperity(intent);
                    if (isSubmitted) {
                        startActivity(intent);
                    }

                }
                if (finalQuestinNum == Integer.parseInt(sharedPreferenceMethod.getQuestions())) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    assignmentProperity(intent);
                    startActivity(intent);
                }
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent
                        (WrongActivity.this,
                                GameActivity.class);
                if (sharedPreferenceMethod.getType().equals("Division")) {
                    if (getIntent().hasExtra("assignmentAnswer")) {
                        intent.putExtra("assignment", "assignment");
                       /* if(sharedPreferenceMethod.getType().equals("Subtraction") ||
                                sharedPreferenceMethod.getType().equals("Addition") ){
                        }*/
                        if (sharedPreferenceMethod.getSubtraction()) {
                            intent.putExtra("subtraction", 1);
                        } else {
                            intent.putExtra("subtraction", 0);
                        }
                        sharedPreferenceMethod.insertProperties(sharedPreferenceMethod.getType(), sharedPreferenceMethod.getFlickerSpeed(), sharedPreferenceMethod.getDigitSize(), sharedPreferenceMethod.getNoOfDigits() + "",
                                sharedPreferenceMethod.getQuestions());
                    } else {
                        sharedPreferenceMethod.insertProperties(sharedPreferenceMethod.getType(), sharedPreferenceMethod.getFlickerSpeed(), sharedPreferenceMethod.getDigitSize(), sharedPreferenceMethod.getNoOfDigits() + "",
                                "" + (Integer.parseInt(sharedPreferenceMethod.getQuestions()) + 1));
//                        int qNo = Integer.parseInt(sharedPreferenceMethod.getQuestions()) + 1;
//                        sharedPreferenceMethod.insertQuestions(qNo + "");
                    }
                    intent.putExtra("digitSize", sharedPreferenceMethod.getDigitSize());
                    intent.putExtra("noOfDigits", sharedPreferenceMethod.getNoOfDigits());
                    intent.putExtra("flickeringSpeed", sharedPreferenceMethod.getFlickerSpeed());

                   /* if (sharedPreferenceMethod.getSubtraction()) {
                        intent.putExtra("subtraction", 1);
                    } else {
                        intent.putExtra("subtraction", 0);
                    }*/

                    intent.putExtra("levelNumber", 1);
                    intent.putExtra("calc_type", sharedPreferenceMethod.getType());

                } else {
                    if (getIntent().hasExtra("assignmentAnswer")) {
                        intent.putExtra("assignment", "assignment");
                        if (sharedPreferenceMethod.getSubtraction()) {
                            intent.putExtra("subtraction", 1);
                        } else {
                            intent.putExtra("subtraction", 0);
                        }
                    } else {
                        sharedPreferenceMethod.insertProperties(sharedPreferenceMethod.getType(), sharedPreferenceMethod.getFlickerSpeed(), sharedPreferenceMethod.getDigitSize(), sharedPreferenceMethod.getNoOfDigits() + "",
                                "" + (Integer.parseInt(sharedPreferenceMethod.getQuestions()) + 1));
//                        int noQuestion = Integer.parseInt(sharedPreferenceMethod.getQuestions()) + 1;
//                        sharedPreferenceMethod.insertQuestions(noQuestion + "");
                    }
                    intent.putExtra("digitSize", sharedPreferenceMethod.getDigitSize());
                    intent.putExtra("noOfDigits", sharedPreferenceMethod.getNoOfDigits());
                    intent.putExtra("flickeringSpeed", sharedPreferenceMethod.getFlickerSpeed());

                    intent.putExtra("levelNumber", 1);
                    intent.putExtra("calc_type", sharedPreferenceMethod.getType());
                }
                startActivity(intent);
            }
        });

        final int finalQuestinNum1 = questinNum;
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setIntent = new Intent(WrongActivity.this, MainNavigation.class);
                if (finalQuestinNum1 == Integer.parseInt(sharedPreferenceMethod.getQuestions())) {
                    setIntent.putExtra("int", 0);
                    setIntent.putExtra("result", "seeResults");
                    startActivity(setIntent);
                } else {
                    setIntent.putExtra("practice", "answer");
                    startActivity(setIntent);
                    finishAffinity();
                }


//                Intent intent = new Intent
//                        (WrongActivity.this,
//                                MainNavigation.class);
//                intent.putExtra("activity_name","incorrect");

//                sharedPreferenceMethod.saveOnResultClick("");
            }
        });
    }

    void assignmentProperity(Intent intent) {
        if (sharedPreferenceMethod.getType().equals("Division")) {
//                    sharedPreferenceMethod.insertProperties(sharedPreferenceMethod.getType(),sharedPreferenceMethod.getFlickerSpeed(), sharedPreferenceMethod.getDigitSize(), sharedPreferenceMethod.getNoOfDigits() + "", sharedPreferenceMethod.getQuestions());

            intent.putExtra("digitSize", sharedPreferenceMethod.getDigitSize());
            intent.putExtra("noOfDigits", sharedPreferenceMethod.getNoOfDigits());
            intent.putExtra("flickeringSpeed", sharedPreferenceMethod.getFlickerSpeed());
            if (sharedPreferenceMethod.getSubtraction()) {
                intent.putExtra("subtraction", 1);
            } else {
                intent.putExtra("subtraction", 0);
            }
            intent.putExtra("levelNumber", 1);
            intent.putExtra("calc_type", sharedPreferenceMethod.getType());

        } else {
            intent.putExtra("digitSize", sharedPreferenceMethod.getDigitSize());
            intent.putExtra("noOfDigits", sharedPreferenceMethod.getNoOfDigits());
            intent.putExtra("flickeringSpeed", sharedPreferenceMethod.getFlickerSpeed());
            if (sharedPreferenceMethod.getSubtraction()) {
                intent.putExtra("subtraction", 1);
            } else {
                intent.putExtra("subtraction", 0);
            }
            intent.putExtra("levelNumber", 1);
            intent.putExtra("calc_type", sharedPreferenceMethod.getType());
        }
    }

    public String checkAssignmentDay() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();

        String dayOfTheWeek = sdf.format(d);

        Log.e(TAG, "checkAssignmentDay: " + dayOfTheWeek + "  ");
        return dayOfTheWeek;
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
//                .addHeader("postman-token", "629ee071-2dc9-a8a1-e526-4fefab35127e")
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            Log.e(TAG, "OK HTTP  " + response.code() + response.toString() + " " + response.message());
            if (response.code() == 502) {
                Log.e("UploadfinalResult", "onResponse: " + sharedPreferenceMethod.getID() + " & " + Integer.parseInt(sharedPreferenceMethod.getAssignmentId()));
                Toast.makeText(WrongActivity.this, "Something went wrong! 0% result, Error code = " + response.code(), Toast.LENGTH_SHORT).show();

                progressBar.hideProgress();

            }
            if (response.code() == 500) {
                Log.e("Update Assignment", "onResponse: Invalid Student Id or Assignment Id");
                progressBar.hideProgress();

            }
            if (response.code() == 200) {
                isSubmitted = true;
                Log.e("UploadfinalResult", "onResponse: " + sharedPreferenceMethod.getID() + " ha " + Integer.parseInt(sharedPreferenceMethod.getAssignmentId()));
                Toast.makeText(WrongActivity.this, "Result submitted!", Toast.LENGTH_SHORT).show();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete(QUESTION_FOR_STUDENT, ASSIGNMENT_ID + " = ?", new String[]{sharedPreferenceMethod.getAssignmentId()});
//                db.close();
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
        Intent setIntent = new Intent(WrongActivity.this, MainNavigation.class);
        setIntent.putExtra("answer", "answer");
        setIntent.putExtra("assignment", "assignment");

        startActivity(setIntent);
    }
}

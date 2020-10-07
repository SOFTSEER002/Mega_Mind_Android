package com.doozycod.megamind.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.megamind.R;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import java.text.DecimalFormat;

public class EnterAnswer extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    long total = 0;
    int noOfDigits;
    int[] numbersArray;
    SharedPreferenceMethod sharedPreferenceMethod;
    DecimalFormat df;
    int count, startform, random, seconds;
    EditText answerET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_enter_answer);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        df = new DecimalFormat("#.##");

        int backPressed = prefs.getInt("backPressed", -22);

        if (backPressed == 1) {
            Toast.makeText(EnterAnswer.this,
                    String.valueOf(backPressed),
                    Toast.LENGTH_LONG).show();
            Intent setIntent = new Intent(EnterAnswer.this, MainNavigation.class);
            setIntent.putExtra("answer", "answer");
            startActivity(setIntent);
        }

        Bundle extras = getIntent().getExtras();
        numbersArray = extras.getIntArray("numbersArray");
        noOfDigits = extras.getInt("noOfDigits");

        Button submit = findViewById(R.id.submitResult2);
        EditText editTextAnswer = findViewById(R.id.editTextAnswer);
        answerET = findViewById(R.id.editTextAnswer);
        showKeyboard(editTextAnswer);
        editTextAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    EditText result =
                            findViewById(R.id.editTextAnswer);
                    String resultString = result.getText().toString();
                    if (resultString.matches("")) {
                        Toast.makeText(EnterAnswer.this,
                                "Enter a Value!",
                                Toast.LENGTH_LONG).show();
                    } else {
//                        if ()
                        result();
                    }
                }
                return false;
            }
        });

        answerET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 2 && s.toString().startsWith("00")) {
                    Log.e("MEGAMIND", "afterTextChanged: " + s);
//                    answerET.setText("");

                } else {
                    if (s.toString().length() == 1)
                        Log.e("MEGAMIND", "afterTextChanged: go ahead " + s);

                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText result = findViewById(R.id.editTextAnswer);
                String resultString = result.getText().toString();
                if (resultString.matches("")) {
                    Toast.makeText(EnterAnswer.this,
                            "Enter a Value!",
                            Toast.LENGTH_LONG).show();
                } else {
//                    if (Integer.parseInt(answerET.getText().toString()) == 0) {
//                        Toast.makeText(EnterAnswer.this, "Please enter answer greater than 0", Toast.LENGTH_SHORT).show();
//                        answerET.setText("");
//                    } else

                    if (answerET.getText().toString().length() >= 2 && Integer.parseInt(answerET.getText().toString()) == 0) {
                        Log.e("MEGAMIND", "clicked: " + answerET.getText().toString().length());
                        answerET.setText("");
                        Toast.makeText(EnterAnswer.this, "You can not enter multiple Zeros!", Toast.LENGTH_SHORT).show();

                    } else {
                        result();

                    }
                }
            }
        });

    }

    public static void showKeyboard(final EditText editText) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public void result() {
        if (getIntent().hasExtra("startForm")) {
            if (answerET.getText().toString() == null) {
                Toast.makeText(this, "Please enter answer greater than 0", Toast.LENGTH_SHORT).show();
                answerET.setText("");
            } else {
                startform = Integer.parseInt(getIntent().getStringExtra("startForm"));
                random = getIntent().getIntExtra("random", 0);
                seconds = getIntent().getIntExtra("seconds", 0);
                if (getIntent().getStringExtra("powerType").equals("Addition")) {
                    count = (Integer.parseInt(answerET.getText().toString()) - startform) / random;
                    Log.e("TAG", "result: " + count);
                    if (answerET.getText().toString().contains("-")) {
                        Intent intent = new Intent(EnterAnswer.this, PowerWrongActivity.class);
                        sharedPreferenceMethod.savePowerAttributes(random + "", seconds + "", startform + "",
                                getIntent().getStringExtra("powerType"), answerET.getText().toString(), "-");

                        startActivity(intent);

                    } else {
                        if ((Integer.parseInt(answerET.getText().toString()) - startform) % random == 0) {
                            Log.e("TAG", "modolus: " + count);
                            Intent intent = new Intent(EnterAnswer.this, PowerCorrectActivity.class);
//                    intent.putExtra("randomNumber", random + "");
//                    intent.putExtra("time", seconds + "");
//                    intent.putExtra("startForm", startform + "");
//                    intent.putExtra("answer", answerET.getText().toString());
//                    intent.putExtra("exercisePowerType", getIntent().getStringExtra("powerType"));
//                    intent.putExtra("count", count + "");
                            sharedPreferenceMethod.savePowerAttributes(random + "", seconds + "", startform + "",
                                    getIntent().getStringExtra("powerType"), answerET.getText().toString(), count + "");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(EnterAnswer.this, PowerWrongActivity.class);
//                    intent.putExtra("randomNumber", random + "");
//                    intent.putExtra("time", seconds + "");
//                    intent.putExtra("startForm", startform + "");
//                    intent.putExtra("answer", answerET.getText().toString());
//                    intent.putExtra("exercisePowerType", getIntent().getStringExtra("powerType"));
//                    intent.putExtra("count", count + "");
                            sharedPreferenceMethod.savePowerAttributes(random + "", seconds + "", startform + "",
                                    getIntent().getStringExtra("powerType"), answerET.getText().toString(), "-");

                            startActivity(intent);
                        }
                    }

                }
                if (getIntent().getStringExtra("powerType").equals("Subtraction")) {
//                startform = random * seconds;
                    count = (startform - Integer.parseInt(answerET.getText().toString())) / random;
                    Log.e("Subtraction", "result: " + count);
                    if ((startform - (Integer.parseInt(answerET.getText().toString()))) % random == 0) {
                        Log.e("TAG", "modolus: " + count);
                        Intent intent = new Intent(EnterAnswer.this, PowerCorrectActivity.class);
                        intent.putExtra("randomNumber", random + "");
                        intent.putExtra("time", seconds + "");
                        intent.putExtra("startForm", startform + "");
                        intent.putExtra("answer", answerET.getText().toString());
                        intent.putExtra("exercisePowerType", getIntent().getStringExtra("powerType"));
                        intent.putExtra("count", count + "");
                        if (answerET.getText().toString().contains("-")) {
                            sharedPreferenceMethod.savePowerAttributes(random + "", seconds + "", startform + "",
                                    getIntent().getStringExtra("powerType"), answerET.getText().toString(), count + "".replaceAll("-", ""));

                        } else {
                            sharedPreferenceMethod.savePowerAttributes(random + "", seconds + "", startform + "",
                                    getIntent().getStringExtra("powerType"), answerET.getText().toString(), count + "");

                        }

                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(EnterAnswer.this, PowerWrongActivity.class);
                        intent.putExtra("randomNumber", random + "");
                        intent.putExtra("time", seconds + "");
                        intent.putExtra("startForm", startform + "");
                        intent.putExtra("answer", answerET.getText().toString());
                        intent.putExtra("exercisePowerType", getIntent().getStringExtra("powerType"));
                        intent.putExtra("count", count + "");

                        sharedPreferenceMethod.savePowerAttributes(random + "", seconds + "", startform + "",
                                getIntent().getStringExtra("powerType"), answerET.getText().toString(), count + "");

                        startActivity(intent);
                    }
                }

            }
        } else {

            if (sharedPreferenceMethod.getType().equals("Addition")) {
                total = 0;
                for (int i = 0; i < noOfDigits; i++) {
                    total += numbersArray[i];
                    Log.e("", "result: Adddtion" + numbersArray[i] + "\n" + total);

                }
            }
            if (sharedPreferenceMethod.getType().equals("Multiplication")) {
                total = 1;
                for (int i = 0; i < noOfDigits; i++) {
                    total *= numbersArray[i];
                    Log.e("", "result: Multiple" + numbersArray[i] + "\n" + total);

                }
            }
            if (sharedPreferenceMethod.getType().equals("Division")) {
                total = numbersArray[0];
                Log.e("Division", "result: Division" + numbersArray[0] + "\n" + total);

                for (int i = 1; i < noOfDigits; i++) {
                    total = total / numbersArray[i];
                }
                Log.e("Division", "result: Division Final" + total);

            }
            if (sharedPreferenceMethod.getType().equals("Subtraction")) {
                total = 0;
                for (int i = 0; i < noOfDigits; i++) {
                    total += numbersArray[i];
                    Log.e("", "result: Adddtion" + numbersArray[i] + "\n" + total);

                }
            }
//        String numbersArrayString ="";

            EditText result = findViewById(R.id.editTextAnswer);
            String userAnswerString = result.getText().toString();
            long userAnswer = Long.parseLong(userAnswerString);

            if (total == userAnswer) {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int totalRightAnswers = prefs.getInt("result", -22);
                int totalQuestions = prefs.getInt("questions", -22);

                SharedPreferences settings =
                        getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                if (totalQuestions == -22) {
                    editor.putInt("questions", 1);
                    editor.apply();
                } else {
                    editor.putInt("questions", totalQuestions + 1);
                    editor.apply();
                }

                if (totalRightAnswers == -22) {
                    editor.putInt("result", 1);
                    editor.apply();
                }
                if (totalRightAnswers != -22) {

                    editor.putInt("result", totalRightAnswers + 1);
                    editor.apply();
                }


                Intent intent = new Intent(EnterAnswer.this,
                        CorrectActivity.class);
                if (getIntent().hasExtra("assign")) {
                    Log.e("Enter Answer", "result: ");
                    intent.putExtra("numbersArray", numbersArray);
                    intent.putExtra("correctAns", total);
                    intent.putExtra("assignmentAnswer", "assignmentAnswer");
                } else {
                    intent.putExtra("numbersArray", numbersArray);
                    intent.putExtra("correctAns", total);
                }


                startActivity(intent);
            } else {

                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

                int totalQuestions = prefs.getInt("questions", -22);
                int totalWrongAnswers = prefs.getInt("wrongAnswers", -22);
                int totalRightAnswers = prefs.getInt("result", -22);

                SharedPreferences settings =
                        getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();

                if (totalWrongAnswers == -22) {
                    editor.putInt("wrongAnswers", 1);
                    editor.apply();
                } else {
                    editor.putInt("wrongAnswers", totalWrongAnswers + 1);
                    editor.apply();
                }

                if (totalQuestions == -22) {
                    editor.putInt("questions", 1);
                    editor.apply();
                } else {
                    editor.putInt("questions", totalQuestions + 1);
                    editor.apply();
                }
                if (totalRightAnswers == -22) {
                    editor.putInt("result", 0);
                    // Apply the edits!
                    editor.apply();
                }
                Intent intent = new Intent
                        (EnterAnswer.this,
                                WrongActivity.class);
                if (getIntent().hasExtra("assign")) {
                    Log.e("Enter Answer", "result: ");

                    intent.putExtra("numbersArray", numbersArray);
                    intent.putExtra("correctAns", total);
                    intent.putExtra("userAns", userAnswer);
                    intent.putExtra("assignmentAnswer", "assignmentAnswer");
                } else {
                    intent.putExtra("numbersArray", numbersArray);
                    intent.putExtra("correctAns", total);
                    intent.putExtra("userAns", userAnswer);
                }

                startActivity(intent);

            }
            total = 0;
        }

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(EnterAnswer.this, MainNavigation.class);
        setIntent.putExtra("answer", "answer");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setIntent);
        finish();
    }
}

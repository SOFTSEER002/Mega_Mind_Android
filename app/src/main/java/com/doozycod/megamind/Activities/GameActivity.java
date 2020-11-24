package com.doozycod.megamind.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.doozycod.megamind.R;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    int count = 0;
    int numbersArray[];
    String digitSize;
    int noOfDigits;
    int flickeringSpeed;
    int subtraction;
    int levelNumber;
    SharedPreferenceMethod sharedPreferenceMethod;
    public static final String PREFS_NAME = "MyPrefsFile";
    int sum = 0;
    List<Integer> divisorList = new ArrayList<>();
    int dividend = 0, finalDivisor = 0;
    List<Integer> divList = new ArrayList<>();
    int num, rand, counter = 0;
    Random random;
    int digits = 0;
    TextView numberType;
    Button exitButton;
    Handler handler;
    Runnable runnable;
    //    MyRunnable runnable;
    private boolean killMe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_game);
        sharedPreferenceMethod = new SharedPreferenceMethod(this);
        TextView number = findViewById(R.id.numberDisplayGame);
        numberType = findViewById(R.id.numberType);
        exitButton = findViewById(R.id.exitButton);

        Intent intent = getIntent();
        random = new Random();
//        int dSize = intent.getIntExtra("digitSize", 0);
//        int noOfD = intent.getIntExtra("noOfDigits", 0);
//        int fSpeed = intent.getIntExtra("flickeringSpeed", 0);
//        int sub = intent.getIntExtra("subtraction", 0);
//        int levelN = intent.getIntExtra("levelNumber", 0);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

//        digitSize = prefs.getInt("digitSize", -22);
        noOfDigits = Integer.parseInt(sharedPreferenceMethod.getNoOfDigits());
        flickeringSpeed = Integer.parseInt(sharedPreferenceMethod.getFlickerSpeed());
        subtraction = getIntent().getIntExtra("subtraction", 0);
        levelNumber = prefs.getInt("levelNumber", -22);
        count = 0;

        SharedPreferences settings =
                getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        digitSize = sharedPreferenceMethod.getDigitSize();
        noOfDigits = Integer.parseInt(sharedPreferenceMethod.getNoOfDigits());
        flickeringSpeed = Integer.parseInt(sharedPreferenceMethod.getFlickerSpeed());
        Log.e("TAG", "digitSize: " + digitSize);
        if (/*(digitSize==-22)||*/(noOfDigits == -22)
                || (flickeringSpeed == -22) || (subtraction == -22) || (levelNumber == -22)) {

        }
//        new Handler(this.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {

        /*Log.e("RUN", "run: " + sharedPreferenceMethod.getDivisorSize());
        numbersArray = getDivisionQuestion(digitSize, sharedPreferenceMethod.getDivisorSize());
        Log.e("RUN", "run11: " + num + "\n" + rand);*/
        if (sharedPreferenceMethod.getType().equals("Subtraction")) {
            if (getIntent().hasExtra("assignment")) {
                numberType.setText(sharedPreferenceMethod.getType());

            } else {
                numberType.setText("Addition");

            }

        } else {
            numberType.setText(sharedPreferenceMethod.getType() + "");

        }

        if (sharedPreferenceMethod.getType().equals("Division")) {
            numbersArray = new int[2];
            for (int i = 0; i < numbersArray.length; i++) {
                Log.e("TAG", "New numbersArray: " + numbersArray[i]);
            }

            String divisorSize = "1";
            divisorSize = sharedPreferenceMethod.getDivisorSize();
            numbersArray = getDivisionQuestion(digitSize, sharedPreferenceMethod.getDivisorSize());

        } else {
            numbersArray = new int[noOfDigits];
        }
        calculationMethod();
//            }
//        }, 1500);

        int secondsDelayed = 1;
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            public void run() {
                if (killMe)
                    return;
                else
                    Game();
            }
        };
//        runnable;
        handler.postDelayed(runnable, secondsDelayed * 500);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                killMe = true;
//                handler.removeCallbacks(null);
                handler.removeCallbacks(runnable);
                finish();
            }
        });
    }


    private void calculationMethod() {

        if (subtraction == 1) {
            if (sharedPreferenceMethod.getType().equals("Addition")
                    || sharedPreferenceMethod.getType().equals("Subtraction")) {
                int sum = 0;
                for (int i = 0; i < noOfDigits; i++) {
                    if (digitSize.equals("Single")) {
                        int oddEven = random.nextInt(2);
                        int singleRandom = random.nextInt(10);

                        if (singleRandom != 0) {
                            if (oddEven == 0 && sum > singleRandom)
                                numbersArray[i] = singleRandom * -1;
                            else
                                numbersArray[i] = singleRandom;
                        } else {
                            if (oddEven == 0 && sum > singleRandom)
                                numbersArray[i] = -1;
                            else
                                numbersArray[i] = 1;
                        }
                        sum = sum + numbersArray[i];
                    }

                    if (digitSize.equals("Double")) {
                        int oddEven = random.nextInt(2);
                        int doubleRandom =
                                (random.nextInt(89) + 10);
                        if (doubleRandom != 0) {
                            if (oddEven == 0 && sum > doubleRandom)
                                numbersArray[i] = doubleRandom * -1;
                            else
                                numbersArray[i] = doubleRandom;
                        } else {
                            if (oddEven == 0 && sum > doubleRandom)
                                numbersArray[i] = -11;
                            else
                                numbersArray[i] = 11;
                        }
                        sum = sum + numbersArray[i];
                    }
                    if (digitSize.equals("Triple")) {
                        int oddEvenDecider = random.nextInt(2);
                        int tripleRandom =
                                (random.nextInt(900) + 100);

                        if (tripleRandom != 0) {
                            if (oddEvenDecider == 0 && sum > tripleRandom)
                                numbersArray[i] = tripleRandom * -1;
                            else
                                numbersArray[i] =
                                        tripleRandom;
                        } else {
                            if (oddEvenDecider == 0 && sum > tripleRandom)
                                numbersArray[i] = -100;
                            else
                                numbersArray[i] = 101;
                        }
                        sum = sum + numbersArray[i];
                    }
                    if (digitSize.equals("Quad")) {
                        int oddEvenDecider = random.nextInt(2);
                        int tripleRandom =
                                (random.nextInt(9000) + 1000);

                        if (tripleRandom != 0) {
                            if (oddEvenDecider == 0 && sum > tripleRandom)
                                numbersArray[i] = tripleRandom * -1;
                            else
                                numbersArray[i] =
                                        tripleRandom;
                        } else {
                            if (oddEvenDecider == 0 && sum > tripleRandom)
                                numbersArray[i] = -1000;
                            else
                                numbersArray[i] = 1001;
                        }
                        sum = sum + numbersArray[i];
                    }

                }
            } else {
                if (sharedPreferenceMethod.getType().equals("Multiplication")) {
                    for (int i = 0; i < noOfDigits; i++) {
                        if (digitSize.equals("Single")) {
                            int singleRandom = random.nextInt(10);
                            if (singleRandom > 1)
                                numbersArray[i] = singleRandom;
                            else
                                numbersArray[i] = 2;

                        }
                        if (digitSize.equals("Double")) {
                            int doubleRandom = random.nextInt(100);
                            if (doubleRandom > 1)
                                numbersArray[i] = doubleRandom;
                            else
                                numbersArray[i] = 11;
                        }
                        if (digitSize.equals("Triple")) {
                            int tripleRandom = random.nextInt(1000);
                            if (tripleRandom > 1)
                                numbersArray[i] = tripleRandom;
                            else
                                numbersArray[i] = 101;
                        }
                        if (digitSize.equals("Quad")) {
                            int tripleRandom = random.nextInt(10000);
                            if (tripleRandom > 1)
                                numbersArray[i] = tripleRandom;
                            else
                                numbersArray[i] = 1001;
                        }
                    }
                }
            }

        } else {

            Log.e("", "onCreate: " + getIntent().getStringExtra("calc_type"));

            if (sharedPreferenceMethod.getType().equals("Addition")) {
                for (int i = 0; i < noOfDigits; i++) {
                    if (digitSize.equals("Single")) {
                        int singleRandom = random.nextInt(10);
                        if (singleRandom != 0)
                            numbersArray[i] = singleRandom;
                        else
                            numbersArray[i] = 1;


                    }
                    Log.e("", "onCreate: Single " + numbersArray[i]);

                    if (digitSize.equals("Double")) {
                        int doubleRandom = random.nextInt(90) + 10;
                        if (doubleRandom != 0)
                            numbersArray[i] = doubleRandom;
                        else
                            numbersArray[i] = 11;
                    }
                    if (digitSize.equals("Triple")) {
                        int tripleRandom = random.nextInt(900) + 100;
                        if (tripleRandom != 0)
                            numbersArray[i] = tripleRandom;
                        else
                            numbersArray[i] = 101;
                    }
                    if (digitSize.equals("Quad")) {
                        int tripleRandom = random.nextInt(9000) + 1000;
                        if (tripleRandom != 0)
                            numbersArray[i] = tripleRandom;
                        else
                            numbersArray[i] = 1001;
                    }
                }
            }
            if (sharedPreferenceMethod.getType().equals("Multiplication")) {
                for (int i = 0; i < noOfDigits; i++) {
                    if (digitSize.equals("Single")) {
                        int singleRandom = random.nextInt(10);
                        if (singleRandom > 1)
                            numbersArray[i] = singleRandom;
                        else
                            numbersArray[i] = 2;

                    }
                    if (digitSize.equals("Double")) {
                        int doubleRandom = random.nextInt(100);
                        if (doubleRandom > 1)
                            numbersArray[i] = doubleRandom;
                        else
                            numbersArray[i] = 11;
                    }
                    if (digitSize.equals("Triple")) {
                        int tripleRandom = random.nextInt(1000);
                        if (tripleRandom > 1)
                            numbersArray[i] = tripleRandom;
                        else
                            numbersArray[i] = 101;
                    }
                    if (digitSize.equals("Quad")) {
                        int tripleRandom = random.nextInt(10000);
                        if (tripleRandom > 1)
                            numbersArray[i] = tripleRandom;
                        else
                            numbersArray[i] = 1001;
                    }
                }
            }
            if (sharedPreferenceMethod.getType().equals("Subtraction")) {

                for (int i = 0; i < noOfDigits; i++) {
                    if (digitSize.equals("Single")) {
                        int oddEven = random.nextInt(2);
                        int singleRandom = random.nextInt(10);

                        if (singleRandom != 0) {
                            if (oddEven == 0 && sum > singleRandom)
                                numbersArray[i] = singleRandom * -1;
                            else
                                numbersArray[i] = singleRandom;
                        } else {
                            if (oddEven == 0 && sum > singleRandom)
                                numbersArray[i] = -1;
                            else
                                numbersArray[i] = 1;
                        }
                        sum = sum + numbersArray[i];
                    }

                    if (digitSize.equals("Double")) {
                        int oddEven = random.nextInt(2);
                        int doubleRandom =
                                (random.nextInt(89) + 10);
                        if (doubleRandom != 0) {
                            if (oddEven == 0 && sum > doubleRandom)
                                numbersArray[i] = doubleRandom * -1;
                            else
                                numbersArray[i] = doubleRandom;
                        } else {
                            if (oddEven == 0 && sum > doubleRandom)
                                numbersArray[i] = -11;
                            else
                                numbersArray[i] = 11;
                        }
                        sum = sum + numbersArray[i];
                    }
                    if (digitSize.equals("Triple")) {
                        int oddEvenDecider = random.nextInt(2);
                        int tripleRandom =
                                (random.nextInt(900) + 100);

                        if (tripleRandom != 0) {
                            if (oddEvenDecider == 0 && sum > tripleRandom)
                                numbersArray[i] = tripleRandom * -1;
                            else
                                numbersArray[i] =
                                        tripleRandom;
                        } else {
                            if (oddEvenDecider == 0 && sum > tripleRandom)
                                numbersArray[i] = -100;
                            else
                                numbersArray[i] = 101;
                        }
                        sum = sum + numbersArray[i];
                    }
                    if (digitSize.equals("Quad")) {
                        int oddEvenDecider = random.nextInt(2);
                        int tripleRandom =
                                (random.nextInt(9000) + 1000);

                        if (tripleRandom != 0) {
                            if (oddEvenDecider == 0 && sum > tripleRandom)
                                numbersArray[i] = tripleRandom * -1;
                            else
                                numbersArray[i] =
                                        tripleRandom;
                        } else {
                            if (oddEvenDecider == 0 && sum > tripleRandom)
                                numbersArray[i] = -1000;
                            else
                                numbersArray[i] = 1001;
                        }
                        sum = sum + numbersArray[i];
                    }

                }
            }
            //Siva: Commenting existing division code for backup
            /*if (sharedPreferenceMethod.getType().equals("Division")) {
                noOfDigits = 2;
                for (int i = 0; i < noOfDigits; i++) {
                    if (digitSize.equals("2")) {
//                        divideCalculations(i, random, 10, 1);
                        dividend = getRandomNumberInRange(10, 100);
                        if (checkPrime(dividend,100)) {
                            dividend = 0;
                            Log.e("Check Prime", "Random is Prime number!");
                            dividend = getRandomNumberInRange(10, 100);

                            division(dividend, 10, 2, random);

                        } else {
                            division(dividend, 10, 2, random);
                        }
                    }
                    if (digitSize.equals("3")) {

//                        dividend = random.nextInt(999);
                        dividend = getRandomNumberInRange(100, 1000);

                        if (checkPrime(dividend,1000)) {
                            dividend = 0;
                            Log.e("Check Prime", "Random is Prime number!");
//                            dividend = random.nextInt(999);
                            dividend = getRandomNumberInRange(100, 1000);

                            division(dividend, 10, 2, random);
                        } else {
                            if (sharedPreferenceMethod.getNoOfDigits().equals("1")) {
                                division(dividend, 10, 2, random);

                            }
                            if (sharedPreferenceMethod.getNoOfDigits().equals("2")) {
                                division(dividend, 100, 10, random);

                            }
                        }
                    }
                    if (digitSize.equals("4")) {
//                        divideCalculations(i, random, 1000, 101);
//                        dividend = random.nextInt(9999);
                        dividend = getRandomNumberInRange(1000, 10000);

                        if (checkPrime(dividend,10000)) {
                            dividend = 0;
                            Log.e("Check Prime", "Random is Prime number!");
//                            dividend = random.nextInt(9999);
                            dividend = getRandomNumberInRange(1000, 10000);

                            division(dividend, 10, 2, random);
                        } else {
                            if (sharedPreferenceMethod.getNoOfDigits().equals("1")) {
                                division(dividend, 10, 2, random);

                            }
                            if (sharedPreferenceMethod.getNoOfDigits().equals("2")) {
                                division(dividend, 100, 10, random);

                            }
                            if (sharedPreferenceMethod.getNoOfDigits().equals("3")) {
                                division(dividend, 1000, 100, random);

                            }
                        }


                    }
                    if (digitSize.equals("5")) {
//                        divideCalculations(i, random, 10000, 1001);
//                        dividend = random.nextInt(99999);
                        dividend = getRandomNumberInRange(10000, 100000);

                        if (checkPrime(dividend,100000)) {
                            dividend = 0;
                            Log.e("Check Prime", "Random is Prime number!");
//                            dividend = random.nextInt(99999);
                            dividend = getRandomNumberInRange(10000, 100000);

                            division(dividend, 10, 2, random);
                        } else {
                            if (sharedPreferenceMethod.getNoOfDigits().equals("1")) {
                                division(dividend, 10, 2, random);

                            }
                            if (sharedPreferenceMethod.getNoOfDigits().equals("2")) {
                                division(dividend, 100, 10, random);

                            }
                            if (sharedPreferenceMethod.getNoOfDigits().equals("3")) {
                                division(dividend, 1000, 100, random);

                            }
                            if (sharedPreferenceMethod.getNoOfDigits().equals("4")) {
                                division(dividend, 10000, 1000, random);
                            }
                        }


                    }


                    */
            /*for (int q = 0; q < divList.size(); q++) {
                        numbersArray[q] = divList.get(q);
                    }*/
            /*
                    if (divisorList.size() > 0) {
                        divisorList.clear();
                    }
                    Random random = new Random();


                }

            }*/

            //Siva: New Method for division
         /*   if (sharedPreferenceMethod.getType().equals("Division")) {


                //Siva: Check below 2 lines of code to get Divisor size, it is very important.
//                String divisorSize = "1";
//                divisorSize = sharedPreferenceMethod.getDivisorSize();
//                numbersArray = getDivisionQuestion(digitSize, sharedPreferenceMethod.getDivisorSize());
//                for (int i = 0; i < numbersArray.length; i++) {
//                    Log.e("TAG", "calculationMethod: " + numbersArray[i]);
//
//
//                }
                //Siva: I guess once we have numberArray automatically
                // UI will show the quesiton as numbersArray[0] / numbersArray[1]

            }*/

        }
    }

    //Siva: new method
    private int[] getDivisionQuestion(String digitSize, String divisorSize) {
        int numbersArray[] = new int[2];
        int dividend = 0;
        int divisorTopRange = 0;
        int finalDivisor = 0;
        List<Integer> divisorList = new ArrayList<>();

        dividend = primeCheck(digitSize);


/*if (divisorSize.equals("1")) {
            divisorTopRange = 9;
        }
        if (divisorSize.equals("2")) {
            divisorTopRange = 99;
        }
        if (divisorSize.equals("3")) {
            divisorTopRange = 999;
        }
        if (divisorSize.equals("4")) {
            divisorTopRange = 9999;
        }*/

        //Siva: by doing the square root the loop will be smaller and faster

        finalDivisor = setDivisor(divisorSize, dividend, divisorList);
        num = dividend;
        rand = finalDivisor;
        numbersArray[0] = num;
        numbersArray[1] = rand;

        if (rand == dividend) {
            getDivisionQuestion(digitSize, sharedPreferenceMethod.getDivisorSize());
        }

        return numbersArray;
    }

    private int primeCheck(String digitSize) {
        do {
            if (digitSize.equals("2")) {
                dividend = getRandomNumberInRange(10, 100);
                digits = 100;
                if (isCompositeNumber(dividend)) {
                    Log.e("getDivision", "getDivisionQuestion: composite " + dividend);
                } else {
                    dividend = 2;
                    Log.e("getDivision", "getDivisionQuestion: not composite " + dividend);
                }
            } else if (digitSize.equals("3")) {
                dividend = getRandomNumberInRange(100, 1000);
                digits = 1000;
                if (isCompositeNumber(dividend)) {
                    Log.e("getDivision", "getDivisionQuestion: composite " + dividend);
                } else {
                    dividend = 2;
                    Log.e("getDivision", "getDivisionQuestion: not composite " + dividend);
                }
            } else if (digitSize.equals("4")) {
                dividend = getRandomNumberInRange(1000, 10000);
                digits = 10000;
                if (isCompositeNumber(dividend)) {
                    Log.e("getDivision", "getDivisionQuestion: composite " + dividend);
                } else {
                    dividend = 2;
                    Log.e("getDivision", "getDivisionQuestion: not composite " + dividend);
                }
            } else if (digitSize.equals("5")) {
                dividend = getRandomNumberInRange(10000, 100000);
                digits = 100000;
                if (isCompositeNumber(dividend)) {
                    Log.e("getDivision", "getDivisionQuestion: composite " + dividend);
                } else {
                    dividend = 2;
                    Log.e("getDivision", "getDivisionQuestion: not composite " + dividend);
                }
            }

            setDivisor(sharedPreferenceMethod.getDivisorSize(), dividend, divisorList);

            if (divisorList.size() == 0) {
                dividend = 2;
            }

        } while (isPrime(dividend));
        return dividend;
    }

    /*private int primeCheck(String digitSize) {
        int dividend;

        return dividend;
    }*/

    int setDivisor(String divisorSize, int dividend, List<Integer> divisorList) {
        int div_min = 2;
        int divisorTopRange = 2;

        if (divisorList.size() > 0) {
            divisorList.clear();
        }

        if (divisorSize.equals("1")) {
            div_min = 2;
            divisorTopRange = 9;

        }
        if (divisorSize.equals("2")) {
            div_min = 10;
            divisorTopRange = 99;

        }
        if (divisorSize.equals("3")) {
            div_min = 100;
            divisorTopRange = 999;

        }
        if (divisorSize.equals("4")) {
            div_min = 1000;
            divisorTopRange = 9999;

        }
        List<Integer> tempList = new ArrayList<>();
        for (int i = div_min; i <= dividend; i++) {
            if (dividend % i == 0) {

                int q = dividend / i;
                if (i == q) {
                    tempList.add(i);
                    Log.e("DIVISOR", "TEMP ADD For: " + i);

                } else {
                    if (i <= divisorTopRange)
                        tempList.add(i);
//                    Log.e("Divisor list", "setDivisor: " + i);
                    Log.e("DIVISOR", "TEMP ADD For: " + i);


                    if (q <= divisorTopRange)
                        tempList.add(q);
//                    Log.e("Divisor list", "setDivisor: " + i);
                    Log.e("DIVISOR", "TEMP ADD For: " + q);


                }

            }

        }
        for (int i = 0; i < tempList.size(); i++) {
            int length = String.valueOf(tempList.get(i)).length();
            if (length == Integer.parseInt(divisorSize)) {
                if (tempList.get(i) != 1)
                    divisorList.add(tempList.get(i));
                Log.e("DIVISOR", "TEMP ADD: " + tempList.get(i));
            } /*else {
                dividend = primeCheck(digitSize);
                Log.e("dividend", "dividend: ");

                break;
            }*/
        }

        int size = divisorList.size();
        if (size > 0) {
            random = new Random();
            int randomNumber = random.nextInt(size);
            finalDivisor = divisorList.get(randomNumber);
            if (finalDivisor == 1) {
                randomNumber = random.nextInt(size);
                finalDivisor = divisorList.get(randomNumber);
            }
        } else {
            getDivisionQuestion(digitSize, divisorSize);
//            primeCheck(digitSize);
//            finalDivisor = dividend;
        }
//        Log.e("finalDivisor", "final Divided by: " + finalDivisor);

        num = dividend;
        return finalDivisor;
    }

    //Siva: new method
    boolean isPrime(int number) {
        for (int i = 2; i < number; i++) {
            if (number % i == 0) {
                counter++;
                break;
            }
        }
        if (counter == 0) {
            System.out.print("This is a Prime Number");
            return true;
        } else {
            System.out.print("This is not a Prime Number");
            return false;
        }
    }

    boolean isCompositeNumber(int num) {
        int i = 1, count = 0;
        while (i <= num) {
            if (num % i == 0) {
                count++;
                if (count > 2)
                    return true;
            }
        }
        return false;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /*boolean checkPrime(int number, int range) {
        for (int i = 2; i < range; i++) {
            if (number % i == 0) {
                counter++;
                break;
            }
        }
        if (counter == 0) {
            System.out.print("This is a Prime Number");
            return true;
        } else {
            System.out.print("This is not a Prime Number");
            return false;
        }
    }*/
   /* void division(int num, int divisorTopRange, int div_min, Random random) {
        for (int as = 0; as < 2; as++) {
            if (isCompositeNumber(num)) {
                Log.e("Division", "onCreate: composite Num " + num);
                if (as == 0) {
                    numbersArray[0] = num;
                    divList.add(num);
                }
                for (int i = div_min; i < divisorTopRange; i++) {
                    if (num % i == 0) {
                        divisorList.add(i);
//                        Log.e("Division", "onCreate: divisor " + i);
                        int q = num / i;
                        if (q == 2 || (q == 3 && num % 2 != 0)) {
                            break;
                        }
                    }
                }

                int size = divisorList.size();
                if (size > 0) {
                    random = new Random();
                    int randomNumber = random.nextInt(size);
                    Log.e("Division", "randomNumber " + randomNumber);

                    finalDivisor = divisorList.get(randomNumber);
                    Log.e("Division", "onCreate: new finalll " + finalDivisor);
                    if (as == 1) {
                        divList.add(finalDivisor);
                        numbersArray[1] = finalDivisor;

                    }
                    divisorList.clear();
                } else {
                    finalDivisor = dividend;
                    numbersArray[1] = finalDivisor;
                    Log.e("Division", "onCreate: new final " + finalDivisor);
                }

            } else {
                int dividend = random.nextInt(num);
                Log.e("Division", "onCreate: new " + dividend);
                division(dividend, divisorTopRange, div_min, random);
            }
        }
    }*/


    public void Game() {   //Handler handler = new Handler();
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                //final int i=0;
                displayNumber();
            }
        }, flickeringSpeed);
    }

    public void clearT() {

        if (count < noOfDigits) {

            TextView number = findViewById(R.id.numberDisplayGame);
            if (sharedPreferenceMethod.getType().equals("Multiplication")) {
                number.setText("X");

            }
            if (sharedPreferenceMethod.getType().equals("Division")) {
                number.setText("÷");
            }
            if (sharedPreferenceMethod.getType().equals("Addition")) {
                number.setText(" ");
            }
            if (sharedPreferenceMethod.getType().equals("Subtraction")) {
                number.setText(" ");
            }

            Game();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void displayNumber() {
        TextView number = findViewById(R.id.numberDisplayGame);
        number.setText(String.valueOf(numbersArray[count]));

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressTintList(ColorStateList.valueOf(getColor(R.color.progress_bar_tint)));

        count++;
        if (count < noOfDigits) {
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //final int i=0;
                    clearT();
                }
            }, flickeringSpeed);
            progressBar.setProgress(count * (100 / noOfDigits));
        }
        if (count == noOfDigits) {

            progressBar.setProgress(100);

            int secondsDelayed = flickeringSpeed;
            if (!killMe) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Intent intent =
                                new Intent(GameActivity.this,
                                        EnterAnswer.class);

                        if (getIntent().hasExtra("assignment")) {

                            Log.e("GAME ACTIVITY", "run: ");
                            intent.putExtra("numbersArray", numbersArray);
                            intent.putExtra("noOfDigits", noOfDigits);
                            intent.putExtra("cal_result", getIntent().getStringExtra("calc_type"));
                            intent.putExtra("assign", "assign");
                        } else {
                            intent.putExtra("numbersArray", numbersArray);
                            intent.putExtra("noOfDigits", noOfDigits);
                            intent.putExtra("cal_result", getIntent().getStringExtra("calc_type"));
                        }


                        startActivity(intent);
                    }
                }, secondsDelayed);
            }

        }

    }

    @Override
    public void onBackPressed() {
    }
}

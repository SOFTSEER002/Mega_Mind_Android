package com.doozycod.megamind.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import com.doozycod.megamind.Activities.GameActivity;
import com.doozycod.megamind.Activities.MainNavigation;
import com.doozycod.megamind.Interface.OnFragmentInteractionListener;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Utils.DatabaseHelper;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.doozycod.megamind.Activities.WrongActivity.PREFS_NAME;

public class PracticeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    Button additionButton, substractButton, getAssignmentButton, multipleButton, divisionButton, minusBtn, multipleBtn, divideBtn, startButton;
    ImageView speedUpbtn, speedDownBtn, addButtonPower, speedDownNoDigitBtn, speedDownBtnPower, addDigitButton, plusBtn, addButtonDivisor, speedDownBtnDivisor;
    TextView flickeringSpeedTextView, noOfDigitText, flickeringSpeedTextViewPower, levelNoTextView, digitSizeTxtDivisor,
            flickeringSpeedTextViewDivisor, digitNoTxtDivisor;
    AppCompatTextView digitNoTxt, digitSizetxt;
    String[] digitSize = {"Single", "Double", "Triple", "Quad"};
    String[] powerDigitSize = {"Single", "Double", "Triple"};
    //    String[] dividendSize = {"2", "3", "4", "5"};
//    String[] divisorSize = {"1", "2", "3", "4"};
    String[] noOfQuestionsString = {"5", "10", "15", "20", "25"};
    ArrayList noOfDigitsString = new ArrayList<>();
    ArrayList noOfDigitsMultiply = new ArrayList<>();
    ArrayList dividendSize = new ArrayList<>();
    ArrayList exerciseType = new ArrayList<>();
    ArrayList timeType = new ArrayList<>();
    ArrayList divisorSize = new ArrayList<>();
    Spinner noOfQuestions, noOfDigitsSpinner, digitSizeSpinner, digitSizeSpinnerDivisor, powerDigitSizeSpinner, exceriseSpinner, timeSpinner;
    private int noOfDigitsInt, flickeringSpeed, levelNumber;
    String digitSizeSelected;
    String calculationType = "";
    SharedPreferenceMethod sharedPreferenceMethod;
    DatabaseHelper dbHelper;
    LinearLayout powerDialogLayout, digitSizeDialogLayout, digitSizeDivsorDialogLayout;
    OnNavigation navigation;
    private String digitSizeDivisor = "";

    public void setmListener(OnNavigation mListener) {
        this.navigation = mListener;
    }

    public interface OnNavigation {
        void OnNavigationChange(String navigation);
    }

    private void initUI(View root) {
        speedDownBtnPower = root.findViewById(R.id.speedDownBtnPower);
        flickeringSpeedTextViewPower = root.findViewById(R.id.flickeringSpeedTextViewPower);
        addButtonPower = root.findViewById(R.id.addButtonPower);
        digitNoTxtDivisor = root.findViewById(R.id.digitNoTxtDivisor);
        flickeringSpeedTextViewDivisor = root.findViewById(R.id.flickeringSpeedTextViewDivisor);
        digitSizeTxtDivisor = root.findViewById(R.id.digitSizeTxtDivisor);
        addButtonDivisor = root.findViewById(R.id.addButtonDivisor);
        speedDownBtnDivisor = root.findViewById(R.id.speedDownBtnDivisor);
        digitSizeSpinnerDivisor = root.findViewById(R.id.digitSizeSpinnerDivisor);
        digitSizeDivsorDialogLayout = root.findViewById(R.id.digitSizeDialogLayoutDivisor);
        speedDownNoDigitBtn = root.findViewById(R.id.speedDownNoDigitBtn);
        addDigitButton = root.findViewById(R.id.addDigitButton);
        noOfDigitText = root.findViewById(R.id.noOfDigitText);
        getAssignmentButton = root.findViewById(R.id.getAssignmentButton);
        digitSizeDialogLayout = root.findViewById(R.id.digitSizeDialogLayout);
        powerDialogLayout = root.findViewById(R.id.powerDialogLayout);
        timeSpinner = root.findViewById(R.id.timeSpinner);
        exceriseSpinner = root.findViewById(R.id.exceriseSpinner);
        powerDigitSizeSpinner = root.findViewById(R.id.powerDigitSizeSpinner);
        additionButton = root.findViewById(R.id.level1btn);
        substractButton = root.findViewById(R.id.level2btn);
        multipleButton = root.findViewById(R.id.level3btn);
        divisionButton = root.findViewById(R.id.divisionButton);
        plusBtn = root.findViewById(R.id.plus);
        minusBtn = root.findViewById(R.id.minus);
        multipleBtn = root.findViewById(R.id.multiplyb);
        divideBtn = root.findViewById(R.id.divide);
        flickeringSpeedTextView = root.findViewById(R.id.flickeringSpeedTextView);
        speedUpbtn = root.findViewById(R.id.addButton);
        speedDownBtn = root.findViewById(R.id.speedDownBtn);
        levelNoTextView = root.findViewById(R.id.levelNoTextView);
        digitNoTxt = root.findViewById(R.id.digitNoTxt);
        digitSizetxt = root.findViewById(R.id.digitSizeTxt);
        startButton = root.findViewById(R.id.startButton);
//      No of Digits Spinner
        noOfDigitsSpinner = root.findViewById(R.id.noOfDigitsSpinner);
//        Digit size Spinner
        digitSizeSpinner = root.findViewById(R.id.digitSizeSpinner);
//        DB


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_practice, container, false);

        initUI(root);
//          set title
        if (mListener != null) {
            mListener.onFragmentInteraction("Megamind\nAbacus Practice");
        }
        sharedPreferenceMethod = new SharedPreferenceMethod(getContext());
        dbHelper = new DatabaseHelper(getActivity());
//        time selection
        timeType.add("1 Minute");
        timeType.add("2 Minutes");
        exerciseType.add("Addition");
        exerciseType.add("Subtraction");
//       noOfDigitStrings
        addInList(26, noOfDigitsString);

//        Multiplication no of Digits list
        addInList(4, noOfDigitsMultiply);

//        Division Dividend Size
        addInList(6, dividendSize);


        // Division no of digits
        divisorSize.add(1);
        digitSizeSpinnerDivisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                digitSizeDivisor = adapterView.getItemAtPosition(position).toString();
                String digitSize = digitSizeDivisor;
                Log.e(TAG, "onItemSelected: " + digitSize);
                if (Integer.parseInt(digitSize) == 2) {
                    if (divisorSize.size() > 0) {
                        divisorSize.clear();
                    }
                    divisorSize.add(1);
                        /*for (int index = 1; index < 2; index++) {
                            divisorSize.add(index);
                        }*/
                    ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                            R.layout.my_spinner, divisorSize);
                    noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);

                    Log.e(TAG, "onItemSelected: 2");

                }
                if (Integer.parseInt(digitSize) == 3) {
                    divisorSize.clear();
                    for (int index = 1; index < 3; index++) {
                        divisorSize.add(index);
                    }
                    ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                            R.layout.my_spinner, divisorSize);
                    noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
                    Log.e(TAG, "onItemSelected: 3");

                }
                if (Integer.parseInt(digitSize) == 4) {
                    divisorSize.clear();

                    for (int index = 1; index < 4; index++) {
                        divisorSize.add(index);
                    }
                    ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                            R.layout.my_spinner, divisorSize);
                    noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
                    Log.e(TAG, "onItemSelected: 4");

                }
                if (Integer.parseInt(digitSize) == 5) {
                    divisorSize.clear();

                    for (int index = 1; index < 5; index++) {
                        divisorSize.add(index);
                    }
                    ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                            R.layout.my_spinner, divisorSize);
                    noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);

                    Log.e(TAG, "onItemSelected: 5");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), MainNavigation.class);
//                intent.putExtra("assignmentReceived","assignmentReceived");
//                startActivity(intent);
//                navigation.OnNavigationChange("assignment");
                mListener.onFragmentInteraction("assignment");
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out,
                        android.R.animator.fade_in,
                        android.R.animator.fade_out).replace(R.id.nav_host_fragment, new AssignmentsFragment()).addToBackStack("FragmentAssignment").commit();
            }
        });

        speedDownNoDigitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calculationType.equals("Multiplication")) {

                    int speed = Integer.parseInt
                            (noOfDigitText.getText().toString());
                    if (speed == 2) {
                        Toast.makeText(getActivity(), "You can set minimum 2 digits.", Toast.LENGTH_SHORT).show();
                    } else {
                        speed = speed - 1;
                    }
                    noOfDigitText.setText(String.valueOf(speed));
                }
                if (calculationType.equals("Subtraction")) {

                    int speed = Integer.parseInt
                            (noOfDigitText.getText().toString());
                    if (speed == 2) {
                        Toast.makeText(getActivity(), "You can set minimum 2 digits.", Toast.LENGTH_SHORT).show();
                    } else {
                        speed = speed - 1;
                    }
                    noOfDigitText.setText(String.valueOf(speed));
                }

            }
        });
        addDigitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calculationType.equals("Multiplication")) {
                    int speed = Integer.parseInt
                            (noOfDigitText.getText().toString());
                    if (speed == 3) {
                        Toast.makeText(getActivity(), "You can set maximum 3 digits.", Toast.LENGTH_SHORT).show();
                    } else {
                        speed = speed + 1;
                    }
                    noOfDigitText.setText(String.valueOf(speed));
                }
                if (calculationType.equals("Subtraction")) {
                    int speed = Integer.parseInt
                            (noOfDigitText.getText().toString());
                    if (speed == 25) {
                        Toast.makeText(getActivity(), "You can set maximum 25 digits.", Toast.LENGTH_SHORT).show();
                    } else {
                        speed = speed + 1;
                    }
                    noOfDigitText.setText(String.valueOf(speed));
                }
            }
        });
        speedDownBtnPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (flickeringSpeedTextViewPower.getText().toString().equals("1 Minute")) {
                    Toast.makeText(getActivity(), "You can set minimum 1 minute.", Toast.LENGTH_SHORT).show();
                } else {
                    flickeringSpeedTextViewPower.setText("1 minute");
                }
            }
        });
        addButtonPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flickeringSpeedTextViewPower.getText().toString().equals("2 Minutes")) {
                    Toast.makeText(getActivity(), "You can set maximum 2 minutes", Toast.LENGTH_SHORT).show();
                } else {
                    flickeringSpeedTextViewPower.setText("2 Minutes");
                }
            }
        });
//        digitSize Spinner for multiplication Cases
        digitSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (calculationType.equals("Multiplication")) {

                    if (digitSizeSpinner.getSelectedItem().equals("Single")) {
                        if (noOfDigitsMultiply.size() > 0) {
                            noOfDigitsMultiply.clear();
                        }
                        for (int index = 2; index < 4; index++) {
                            noOfDigitsMultiply.add(index);
                        }
                        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                                R.layout.my_spinner, noOfDigitsMultiply);
                        noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
                    }
                    if (digitSizeSpinner.
                            getSelectedItem().equals("Double")) {
                        noOfDigitsMultiply.clear();
                        for (int index = 2; index < 4; index++) {
                            noOfDigitsMultiply.add(index);
                        }
                        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                                R.layout.my_spinner, noOfDigitsMultiply);
                        noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
                    }
                    if (digitSizeSpinner.getSelectedItem().equals("Triple") || digitSizeSpinner.getSelectedItem().equals("Quad")) {
                        noOfDigitsMultiply.clear();

                        for (int index = 2; index < 4; index++) {
                            noOfDigitsMultiply.add(index);
                        }
                        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                                R.layout.my_spinner, noOfDigitsMultiply);
                        noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
                    }
                }

//               if Calculation is type Division
//                if (calculationType.equals("Division")) {
//                    if (Integer.parseInt(digitSizeSpinnerDivisor.getSelectedItem().toString()) == 2) {
//                        if (divisorSize.size() > 0) {
//                            divisorSize.clear();
//                        }
//                        divisorSize.add(1);
//                        /*for (int index = 1; index < 2; index++) {
//                            divisorSize.add(index);
//                        }*/
//                        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
//                                R.layout.my_spinner, divisorSize);
//                        noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
//
//                        Log.e(TAG, "onItemSelected: 2");
//
//                    }
//                    if (Integer.parseInt(digitSizeSpinnerDivisor.getSelectedItem().toString()) == 3) {
//                        divisorSize.clear();
//                        for (int index = 1; index < 3; index++) {
//                            divisorSize.add(index);
//                        }
//                        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
//                                R.layout.my_spinner, divisorSize);
//                        noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
//
//                        Log.e(TAG, "onItemSelected: 3");
//
//                    }
//                    if (Integer.parseInt(digitSizeSpinnerDivisor.getSelectedItem().toString()) == 4) {
//                        divisorSize.clear();
//
//                        for (int index = 1; index < 4; index++) {
//                            divisorSize.add(index);
//                        }
//                        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
//                                R.layout.my_spinner, divisorSize);
//                        noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
//                        Log.e(TAG, "onItemSelected: 4");
//
//                    }
//                    if (Integer.parseInt(digitSizeSpinnerDivisor.getSelectedItem().toString()) == 5) {
//                        divisorSize.clear();
//
//                        for (int index = 1; index < 5; index++) {
//                            divisorSize.add(index);
//                        }
//                        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
//                                R.layout.my_spinner, divisorSize);
//                        noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
//
//                        Log.e(TAG, "onItemSelected: 5");
//
//                    }
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        onClickListeners();

        return root;
    }

    void addInList(int size, ArrayList list) {
        for (int index = 2; index < size; index++) {
            list.add(index);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//       default selection is Addition
        defaultAttributes();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, digitSize);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        digitSizeSpinner.setAdapter(adapter);
        ArrayAdapter<String> power = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, digitSize);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        digitSizeSpinner.setAdapter(adapter);


        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(noOfDigitsSpinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, noOfDigitsString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);


//        Questions Spinner
        noOfQuestions = view.findViewById(R.id.noOfQuestions);
        try {
            Field popupq = Spinner.class.getDeclaredField("mPopup");
            popupq.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popupq.get(noOfQuestions);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        ArrayAdapter<String> noOfQuestionsArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, noOfQuestionsString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noOfQuestions.setAdapter(noOfQuestionsArrayAdapter);
//        Power Exercise Spinner
        powerDigitSizeSpinner = view.findViewById(R.id.powerDigitSizeSpinner);
        try {
            Field popupq = Spinner.class.getDeclaredField("mPopup");
            popupq.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popupq.get(powerDigitSizeSpinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        ArrayAdapter<String> poweExerciseArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, powerDigitSize);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        powerDigitSizeSpinner.setAdapter(poweExerciseArrayAdapter);

//        Power Exercise Spinner
        timeSpinner = view.findViewById(R.id.timeSpinner);
        try {
            Field popupq = Spinner.class.getDeclaredField("mPopup");
            popupq.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popupq.get(timeSpinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        ArrayAdapter<String> timeSpinnerArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, timeType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeSpinnerArrayAdapter);

//        Power Exercise Spinner
        exceriseSpinner = view.findViewById(R.id.exceriseSpinner);
        try {
            Field popupq = Spinner.class.getDeclaredField("mPopup");
            popupq.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popupq.get(exceriseSpinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        ArrayAdapter<String> exceriseSpinnerArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, exerciseType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exceriseSpinner.setAdapter(exceriseSpinnerArrayAdapter);
    }

    private void onClickListeners() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calculationType.equals("Division")) {
                } else {

                }

                if (sharedPreferenceMethod != null) {
                    if (dbHelper != null) {
                        dbHelper.deleteTable();
                    }
                    Log.e(TAG, "onCreateView: NOT NULL");
                    SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    int questionNo = prefs.getInt("questions", -22);
//                if (questionNo >= Integer.parseInt(sharedPreferenceMethod.getQuestions())) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("Megamind_PREF", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    SharedPreferences.Editor editor1 = prefs.edit();
                    prefs.edit().remove("questions").apply();
                    preferences.edit().remove("digitSize").apply();
                    preferences.edit().remove("noOfDigits").apply();
                    preferences.edit().remove("type").apply();
                    preferences.edit().remove("flickerSpeed").apply();
                    editor.apply();
                    editor1.clear();
                    editor1.apply();
//                }
                }
                if (calculationType.equals("Addition")) {
                    PowerFragment powerFragment = new PowerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("powerExerciseType", exceriseSpinner.getSelectedItem().toString());
                    bundle.putString("selectedTimeTxt", flickeringSpeedTextViewPower.getText().toString());
                    bundle.putString("powerDigitSize", powerDigitSizeSpinner.getSelectedItem().toString());
                    powerFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, powerFragment, "").addToBackStack("powerfragment").commit();
                } else {
                    digitSizeSelected = String.valueOf(digitSizeSpinnerDivisor.getSelectedItem());
                    String dSpinner = String.valueOf(noOfDigitsSpinner.getSelectedItem());
                    String qSpinner = 1 + "";

                    noOfDigitsInt = Integer.parseInt(dSpinner);
                    flickeringSpeed = Integer.parseInt(flickeringSpeedTextView.getText().toString());
                    String temp = (String) noOfQuestions.getSelectedItem();
                    levelNumber = Integer.parseInt(temp);

                    Log.e(TAG, "onClick: " + digitSizeSelected + "\n" + sharedPreferenceMethod.getNoOfDigits() + "\n" + flickeringSpeed + "\n" + levelNumber);

                    Intent intent = new Intent(getActivity(), GameActivity.class);


                    if (calculationType.equals("Division")) {
                        sharedPreferenceMethod.insertProperties(calculationType, flickeringSpeedTextView.getText().toString(), digitSizeSelected, 2 + "", qSpinner);
                        sharedPreferenceMethod.insertDivision(noOfDigitsInt + "");
                        intent.putExtra("digitSize", digitSizeSelected);
                        intent.putExtra("noOfDigits", 2);
                        intent.putExtra("flickeringSpeed", flickeringSpeed);
                        intent.putExtra("subtraction", 0);
                        intent.putExtra("levelNumber", 1);
                        intent.putExtra("calc_type", calculationType);

                    } else {
                        digitSizeSelected = String.valueOf(digitSizeSpinner.getSelectedItem());

                        sharedPreferenceMethod.insertProperties(calculationType, flickeringSpeedTextView.getText().toString(),
                                digitSizeSelected, noOfDigitText.getText().toString(), qSpinner);

                        intent.putExtra("digitSize", digitSizeSelected);
                        intent.putExtra("noOfDigits", noOfDigitText.getText().toString());
                        intent.putExtra("flickeringSpeed", flickeringSpeed);
                        intent.putExtra("subtraction", 0);
                        intent.putExtra("levelNumber", 1);
                        intent.putExtra("calc_type", calculationType);
                    }
                    startActivity(intent);
                }


            }
        });
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //        CalculationType
                calculationType = "Addition";
                startButton.setText("Next");
//      Sppiner properties change
                powerSpinner();

                noOfDigitText.setText("2");

                additionButton.setSelected(true);
                substractButton.setSelected(false);
                multipleButton.setSelected(false);
                divisionButton.setSelected(false);
                additionButton.setTextColor(getResources().getColor(R.color.spalsh_background));
                substractButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                multipleButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                divisionButton.setTextColor(getResources().getColor(R.color.grayishbrown));

//                plusBtn.setSelected(true);
                minusBtn.setSelected(false);
                multipleBtn.setSelected(false);
                divideBtn.setSelected(false);

                plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_blue));
                minusBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                multipleBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                divideBtn.setTextColor(getResources().getColor(R.color.greyFontColor));

//              Level Type
                levelNoTextView.setText("Addition");

            }
        });
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //        CalculationType
                calculationType = "Subtraction";
                startButton.setText("Start");
                noOfDigitText.setText("2");
//      Sppiner properties change
                defaultSpinner();


                additionButton.setSelected(false);
                substractButton.setSelected(true);
                multipleButton.setSelected(false);
                divisionButton.setSelected(false);


                additionButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                substractButton.setTextColor(getResources().getColor(R.color.spalsh_background));
                multipleButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                divisionButton.setTextColor(getResources().getColor(R.color.grayishbrown));

//                plusBtn.setSelected(false);
                minusBtn.setSelected(true);
                multipleBtn.setSelected(false);
                divideBtn.setSelected(false);
                plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_grey));
                minusBtn.setTextColor(getResources().getColor(R.color.cobalt));
                multipleBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                divideBtn.setTextColor(getResources().getColor(R.color.greyFontColor));

                //              Level Type
                levelNoTextView.setText("Addition");

            }
        });
        multipleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //        CalculationType
                calculationType = "Multiplication";
                startButton.setText("Start");
                noOfDigitText.setText("2");

                digitSizetxt.setText("Digit size");
                digitNoTxt.setText("Number of Digits");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        R.layout.my_spinner, digitSize);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                digitSizeSpinner.setAdapter(adapter);
                //      Sppiner properties change
                ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.my_spinner, noOfDigitsMultiply);
                noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
                additionButton.setSelected(false);
                substractButton.setSelected(false);
                multipleButton.setSelected(true);
                divisionButton.setSelected(false);

                additionButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                substractButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                multipleButton.setTextColor(getResources().getColor(R.color.spalsh_background));
                divisionButton.setTextColor(getResources().getColor(R.color.grayishbrown));


//                plusBtn.setSelected(false);
                minusBtn.setSelected(false);
                multipleBtn.setSelected(true);
                divideBtn.setSelected(false);

                plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_grey));
                minusBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                multipleBtn.setTextColor(getResources().getColor(R.color.cobalt));
                divideBtn.setTextColor(getResources().getColor(R.color.greyFontColor));

                //              Level Type
                levelNoTextView.setText("Multiplication");

            }
        });
        divideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              CalculationType
                calculationType = "Division";
                startButton.setText("Start");
                divisorSpinner();
                digitSizeTxtDivisor.setText("Dividend size");
                digitNoTxtDivisor.setText("Divisor size");

//              Change Spinner Properties
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        R.layout.my_spinner, dividendSize);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                digitSizeSpinnerDivisor.setAdapter(adapter);

                ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                        R.layout.my_spinner, divisorSize);
                noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);

//                resetOtherButtonLayout();
//                additionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cobalt)));
//                additionButton.setBackgroundResource(R.drawable.blue_btn);
                additionButton.setSelected(false);
                substractButton.setSelected(false);
                multipleButton.setSelected(false);
                divisionButton.setSelected(true);
                additionButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                substractButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                multipleButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                divisionButton.setTextColor(getResources().getColor(R.color.spalsh_background));


//                plusBtn.setSelected(false);
                minusBtn.setSelected(false);
                multipleBtn.setSelected(false);
                divideBtn.setSelected(true);

                plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_grey));
                minusBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                multipleBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                divideBtn.setTextColor(getResources().getColor(R.color.cobalt));

//              Level Type
                levelNoTextView.setText("Division");


            }
        });
        additionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              CalculationType
                calculationType = "Addition";
                startButton.setText("Next");
                noOfDigitText.setText("2");
//              Sppiner properties change
                powerSpinner();

                additionButton.setSelected(true);
                substractButton.setSelected(false);
                multipleButton.setSelected(false);
                divisionButton.setSelected(false);
                additionButton.setTextColor(getResources().getColor(R.color.spalsh_background));
                substractButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                multipleButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                divisionButton.setTextColor(getResources().getColor(R.color.grayishbrown));


//                plusBtn.setSelected(true);
                minusBtn.setSelected(false);
                multipleBtn.setSelected(false);
                divideBtn.setSelected(false);

                plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_blue));
                minusBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                multipleBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                divideBtn.setTextColor(getResources().getColor(R.color.greyFontColor));

                //              Level Type
                levelNoTextView.setText("Power exercise");

            }
        });


        substractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //        CalculationType
                calculationType = "Subtraction";
                startButton.setText("Start");
                noOfDigitText.setText("2");

//      Sppiner properties change
                defaultSpinner();

                additionButton.setSelected(false);
                substractButton.setSelected(true);
                multipleButton.setSelected(false);
                divisionButton.setSelected(false);

                additionButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                substractButton.setTextColor(getResources().getColor(R.color.spalsh_background));
                multipleButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                divisionButton.setTextColor(getResources().getColor(R.color.grayishbrown));


//                plusBtn.setSelected(false);
                minusBtn.setSelected(true);
                multipleBtn.setSelected(false);
                divideBtn.setSelected(false);

                plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_grey));
                minusBtn.setTextColor(getResources().getColor(R.color.cobalt));
                multipleBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                divideBtn.setTextColor(getResources().getColor(R.color.greyFontColor));

                //              Level Type
                levelNoTextView.setText("Addition");


            }
        });


        multipleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //        CalculationType
                calculationType = "Multiplication";
                startButton.setText("Start");
                noOfDigitText.setText("2");
                defaultSpinner();

                digitSizetxt.setText("Digit size");
                digitNoTxt.setText("Number of Digits");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        R.layout.my_spinner, digitSize);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                digitSizeSpinner.setAdapter(adapter);
//      Spinner properties change
                ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                        R.layout.my_spinner, noOfDigitsMultiply);
                noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);


                additionButton.setSelected(false);
                substractButton.setSelected(false);
                multipleButton.setSelected(true);
                divisionButton.setSelected(false);
//
                additionButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                substractButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                multipleButton.setTextColor(getResources().getColor(R.color.spalsh_background));
                divisionButton.setTextColor(getResources().getColor(R.color.grayishbrown));


//                plusBtn.setSelected(false);
                minusBtn.setSelected(false);
                multipleBtn.setSelected(true);
                divideBtn.setSelected(false);

                plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_grey));
                minusBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                multipleBtn.setTextColor(getResources().getColor(R.color.cobalt));
                divideBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                //              Level Type
                levelNoTextView.setText("Multiplication");
            }
        });
        divisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        CalculationType
                calculationType = "Division";
                startButton.setText("Start");
                divisorSpinner();

                digitSizeTxtDivisor.setText("Dividend size");
                digitNoTxtDivisor.setText("Divisor size");
//                Change Spinner Properties
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        R.layout.my_spinner, dividendSize);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                digitSizeSpinnerDivisor.setAdapter(adapter);

                ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                        R.layout.my_spinner, divisorSize);
                noOfDigitsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);

                digitSizeSpinner.setAdapter(adapter);
                additionButton.setSelected(false);
                substractButton.setSelected(false);
                multipleButton.setSelected(false);
                divisionButton.setSelected(true);
//
                additionButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                substractButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                multipleButton.setTextColor(getResources().getColor(R.color.grayishbrown));
                divisionButton.setTextColor(getResources().getColor(R.color.spalsh_background));


//                plusBtn.setSelected(false);
                minusBtn.setSelected(false);
                multipleBtn.setSelected(false);
                divideBtn.setSelected(true);

                plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_grey));
                minusBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                multipleBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
                divideBtn.setTextColor(getResources().getColor(R.color.cobalt));

                //              Level Type
                levelNoTextView.setText("Division");
            }
        });
        speedUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int speed = Integer.parseInt
                        (flickeringSpeedTextView.getText().toString());

                if (speed == 1500) {
                    Toast.makeText(getActivity(), "You can set maximum 1500ms speed.", Toast.LENGTH_SHORT).show();
                } else {
                    speed = speed + 50;
                }
                flickeringSpeedTextView.setText(String.valueOf(speed));
            }
        });

        speedDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int speed = Integer.parseInt
                        (flickeringSpeedTextView.getText().toString());
                if (speed == 200) {
                    Toast.makeText(getActivity(), "You can set minimum 200ms speed.", Toast.LENGTH_SHORT).show();
                } else {
                    speed = speed - 50;
                }
                flickeringSpeedTextView.setText(String.valueOf(speed));
            }
        });
        addButtonDivisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int speed = Integer.parseInt
                        (flickeringSpeedTextViewDivisor.getText().toString());

                if (speed == 1500) {
                    Toast.makeText(getActivity(), "You can set maximum 1500ms speed.", Toast.LENGTH_SHORT).show();
                } else {
                    speed = speed + 50;
                }
                flickeringSpeedTextViewDivisor.setText(String.valueOf(speed));
            }
        });

        speedDownBtnDivisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int speed = Integer.parseInt
                        (flickeringSpeedTextViewDivisor.getText().toString());
                if (speed == 200) {
                    Toast.makeText(getActivity(), "You can set minimum 200ms speed.", Toast.LENGTH_SHORT).show();
                } else {
                    speed = speed - 50;
                }
                flickeringSpeedTextViewDivisor.setText(String.valueOf(speed));
            }
        });
    }

    void powerSpinner() {
        powerDialogLayout.setVisibility(View.VISIBLE);
        digitSizeDialogLayout.setVisibility(View.GONE);
        digitSizeDivsorDialogLayout.setVisibility(View.GONE);
        //      Sppiner properties change
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, powerDigitSize);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        powerDigitSizeSpinner.setAdapter(adapter);

        ArrayAdapter<String> exceriseAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, exerciseType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exceriseSpinner.setAdapter(exceriseAdapter);
        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, timeType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(noOfDigitsArrayAdapter);
    }

    void defaultSpinner() {

        digitSizetxt.setText("Digit size");
        digitNoTxt.setText("Number of Digits");
        powerDialogLayout.setVisibility(View.GONE);
        digitSizeDialogLayout.setVisibility(View.VISIBLE);
        digitSizeDivsorDialogLayout.setVisibility(View.GONE);
        //      Sppiner properties change
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, digitSize);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        digitSizeSpinner.setAdapter(adapter);

        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, noOfDigitsString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
    }

    void divisorSpinner() {

        digitSizeTxtDivisor.setText("Digit size");
        digitNoTxtDivisor.setText("Number of Digits");
        powerDialogLayout.setVisibility(View.GONE);
        digitSizeDialogLayout.setVisibility(View.GONE);
        digitSizeDivsorDialogLayout.setVisibility(View.VISIBLE);
        //      Sppiner properties change
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner, digitSize);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        digitSizeSpinnerDivisor.setAdapter(adapter);

//        ArrayAdapter<String> noOfDigitsArrayAdapter = new ArrayAdapter<>(getActivity(),
//                R.layout.my_spinner, noOfDigitsString);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        noOfDigitsSpinner.setAdapter(noOfDigitsArrayAdapter);
    }

    private void defaultAttributes() {

//        CalculationType
        calculationType = "Addition";

        powerDialogLayout.setVisibility(View.VISIBLE);
        digitSizeDialogLayout.setVisibility(View.GONE);
        digitSizeDivsorDialogLayout.setVisibility(View.GONE);
//        Buttons
        additionButton.setSelected(true);
        additionButton.setTextColor(getResources().getColor(R.color.spalsh_background));
        substractButton.setSelected(false);
        substractButton.setTextColor(getResources().getColor(R.color.grayishbrown));
        multipleButton.setSelected(false);
        multipleButton.setTextColor(getResources().getColor(R.color.grayishbrown));
        divisionButton.setSelected(false);
        divisionButton.setTextColor(getResources().getColor(R.color.grayishbrown));

//      Button Icons
//        plusBtn.setSelected(true);
        plusBtn.setImageDrawable(getResources().getDrawable(R.drawable.light_blue));
        minusBtn.setSelected(false);
        minusBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
        multipleBtn.setSelected(false);
        multipleBtn.setTextColor(getResources().getColor(R.color.greyFontColor));
        divideBtn.setSelected(false);
        divideBtn.setTextColor(getResources().getColor(R.color.greyFontColor));

    }
}
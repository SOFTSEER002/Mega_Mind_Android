package com.doozycod.megamind.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.megamind.Activities.PowerGameActivity;
import com.doozycod.megamind.R;

import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class PowerFragment extends Fragment {
    TextView powerExerciseType, powerRandomTxt, selectedTimeTxt, powerTypeTxt, objectiveTxt;
    int random, seconds;
    Button startPowerAssignmentButton;
    int startForm;
    EditText startFromTxt;

    public PowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_power, container, false);
        objectiveTxt = view.findViewById(R.id.objectiveTxt);
        powerTypeTxt = view.findViewById(R.id.powerTypeTxt);
        powerExerciseType = view.findViewById(R.id.powerExerciseType);
        powerRandomTxt = view.findViewById(R.id.powerRandomTxt);
        startFromTxt = view.findViewById(R.id.startFromTxt);
        selectedTimeTxt = view.findViewById(R.id.selectedTimeTxt);
        startPowerAssignmentButton = view.findViewById(R.id.startPowerAssignmentButton);
        Log.e(TAG, "onCreateView: " + getArguments().getString("powerDigitSize"));
        powerExerciseType.setText(getArguments().getString("powerExerciseType"));
        if (getArguments().getString("powerDigitSize").contains("Single")) {
            final int min = 2;
            final int max = 9;
            random = new Random().nextInt((max - min) + 1) + min;
        }
        if (getArguments().getString("powerDigitSize").equals("Double")) {
            final int min = 10;
            final int max = 99;
            random = new Random().nextInt((max - min) + 1) + min;
        }
        if (getArguments().getString("powerDigitSize").equals("Triple")) {
            final int min = 100;
            final int max = 999;
            random = new Random().nextInt((max - min) + 1) + min;
        }
        if (getArguments().getString("selectedTimeTxt").contains("1")) {
            selectedTimeTxt.setText("60 seconds");
            seconds = 60;
        }
        if (getArguments().getString("selectedTimeTxt").contains("2")) {
            selectedTimeTxt.setText("120 seconds");
            seconds = 120;
        }

        if (getArguments().getString("powerExerciseType").equals("Addition")) {
            powerTypeTxt.setText("Keep Adding");
            powerRandomTxt.setText("" + random);
            objectiveTxt.setText("Start from " + startForm + ",\n Add +" + random
                    + " for " + selectedTimeTxt.getText().toString());
        }
        if (getArguments().getString("powerExerciseType").equals("Subtraction")) {
            startForm = random * seconds;
            powerTypeTxt.setText("Keep Subtracting");

            objectiveTxt.setText("Start from " + startForm + ",\n Subtract " + random
                    + " for " + selectedTimeTxt.getText().toString());
            startFromTxt.setText(startForm + "");
            powerRandomTxt.setText("" + random);
        }
        startFromTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (getArguments().getString("powerExerciseType").equals("Addition")) {

                    objectiveTxt.setText("Start from " + charSequence + "\n Add +" + random
                            + " for " + selectedTimeTxt.getText().toString());
                }
                if (getArguments().getString("powerExerciseType").equals("Subtraction")) {

                    objectiveTxt.setText("Start from " + charSequence + "\n Subtract -" + random
                            + " for " + selectedTimeTxt.getText().toString());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        startPowerAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startFromTxt.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter in 'start from'", Toast.LENGTH_SHORT).show();
                } else {
                    if (getArguments().getString("selectedTimeTxt").contains("1")) {
                        Intent intent = new Intent(getActivity(), PowerGameActivity.class);
                        intent.putExtra("seconds", 60);
                        intent.putExtra("random", random);
                        intent.putExtra("powerType", getArguments().getString("powerExerciseType"));
                        intent.putExtra("startForm", startFromTxt.getText().toString());
                        startActivity(intent);
                    }
                    if (getArguments().getString("selectedTimeTxt").contains("2")) {
                        Intent intent = new Intent(getActivity(), PowerGameActivity.class);
                        intent.putExtra("seconds", 120);
                        intent.putExtra("random", random);
                        intent.putExtra("powerType", getArguments().getString("powerExerciseType"));
                        intent.putExtra("startForm", startFromTxt.getText().toString());
                        startActivity(intent);
                    }
                }

            }
        });
        return view;
    }
}

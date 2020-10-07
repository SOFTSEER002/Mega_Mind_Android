package com.doozycod.megamind.Fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.doozycod.megamind.Activities.GameActivity;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Utils.DatabaseHelper;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentGet extends Fragment {

    Button startButtonAssignment;
    SharedPreferenceMethod sharedPreferenceMethod;
    String Type;
    TextView assignmentType;
    TextView questions;
    TextView flickeringSpeedTextView;
    TextView digitSizeTxt;
    TextView noOfDigitsTxt;
    DatabaseHelper dbHelper;

    public AssignmentGet() {
        // Required empty public constructor
    }

    Intent intent;

    void initUI(View v) {
        startButtonAssignment = v.findViewById(R.id.startButtonAssignment);
        assignmentType = v.findViewById(R.id.assignmentType);
        questions = v.findViewById(R.id.noOfQuestionsAssignment);
        noOfDigitsTxt = v.findViewById(R.id.noOfDigitsAssignment);
        digitSizeTxt = v.findViewById(R.id.digitSizeTxt);
        flickeringSpeedTextView = v.findViewById(R.id.flickeringSpeedTextView);
        assignmentType = v.findViewById(R.id.assignmentType);
        questions = v.findViewById(R.id.noOfQuestionsAssignment);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_assgnment_get, container, false);
        // Inflate the layout for this fragment
        initUI(v);

        sharedPreferenceMethod = new SharedPreferenceMethod(getContext());
        dbHelper = new DatabaseHelper(getActivity());

//        setting up property of assignment
        assignmentType.setText(sharedPreferenceMethod.getType());
        questions.setText(sharedPreferenceMethod.getQuestions());
        digitSizeTxt.setText(sharedPreferenceMethod.getDigitSize());
        noOfDigitsTxt.setText(sharedPreferenceMethod.getNoOfDigits());
        flickeringSpeedTextView.setText(sharedPreferenceMethod.getFlickerSpeed() + "ms");

//        intent
        intent = new Intent(getActivity(), GameActivity.class);

        if (sharedPreferenceMethod.getDigitSize().equals("1")) {
            Type = "Single";
        }
        if (sharedPreferenceMethod.getDigitSize().equals("2")) {
            Type = "Double";
        }
        if (sharedPreferenceMethod.getDigitSize().equals("3")) {
            Type = "Triple";

        }
        if (sharedPreferenceMethod.getDigitSize().equals("4")) {
            Type = "Quad";
        }
//        sharedPreferenceMethod.insertProperties("Addition", 500+"", "Single","5", "5");

        int assignmentId =Integer.parseInt(sharedPreferenceMethod.getAssignmentId());
        if(dbHelper.uncompletedAssignments(assignmentId)!= Integer.parseInt(sharedPreferenceMethod.getQuestions()) && dbHelper.uncompletedAssignments(assignmentId) ==0){
                startButtonAssignment.setText("Start");
            }else{
                startButtonAssignment.setText("Resume");
            }
        intent.putExtra("digitSize", Type);
        intent.putExtra("noOfDigits", sharedPreferenceMethod.getNoOfDigits());
        intent.putExtra("flickeringSpeed", sharedPreferenceMethod.getFlickerSpeed());

        if (sharedPreferenceMethod.getSubtraction()) {
            intent.putExtra("subtraction", 1);
        } else {
            intent.putExtra("subtraction", 0);
        }
        intent.putExtra("levelNumber", sharedPreferenceMethod.getLevel());
        intent.putExtra("calc_type", sharedPreferenceMethod.getType());
        intent.putExtra("assignment", sharedPreferenceMethod.getType());
        onClickListener();
        return v;
    }

//    onClick Listener
    private void onClickListener() {
        startButtonAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(intent);
            }
        });
    }


}

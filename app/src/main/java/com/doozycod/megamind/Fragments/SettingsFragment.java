package com.doozycod.megamind.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.megamind.Interface.ApiService;
import com.doozycod.megamind.Interface.OnFragmentInteractionListener;
import com.doozycod.megamind.Model.StudentModel;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Service.ApiUtils;
import com.doozycod.megamind.Utils.AutoResizeTextView;
import com.doozycod.megamind.Utils.CustomProgressBar;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    ApiService apiService;
    StudentModel student;
    EditText screenName;
    TextView assignmentDayTxt;
    Button updateScreenNameButton;
    AutoResizeTextView studentName;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar progressBar;
    private OnFragmentInteractionListener mListener;
    Boolean connected = false;

    public SettingsFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        apiService = ApiUtils.getAPIService();
        screenName = v.findViewById(R.id.s_name);
        updateScreenNameButton = v.findViewById(R.id.updateButton);
        studentName = v.findViewById(R.id.studentName);
        assignmentDayTxt = v.findViewById(R.id.assignmentDayTxt);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        if (mListener != null) {
            mListener.onFragmentInteraction("Megamind\nAbacus Practice");
        }

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        student = new StudentModel();

        screenName.setText(sharedPreferenceMethod.getScreenName());
        progressBar = new CustomProgressBar(getActivity());
        studentName.setText(sharedPreferenceMethod.getName());
        assignmentDayTxt.setText(sharedPreferenceMethod.getAssignmentDay());


        updateScreenNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (screenName.getText().toString().equals("") || screenName.getText().toString().startsWith(" ")) {
                    Toast.makeText(getActivity(), "Please enter valid screen name!", Toast.LENGTH_SHORT).show();
                } else {

                    student.setScreenName(screenName.getText().toString());
                    progressBar.showProgress();
                    updateProfile(sharedPreferenceMethod.getmCode(), Integer.parseInt(sharedPreferenceMethod.getID()), student);
                }

            }
        });
    }

    //    update profile method
    void updateProfile(String mCode, int id, StudentModel student) {
        apiService.updateStudentDetails(id, mCode, student).enqueue(new Callback<StudentModel>() {
            @Override
            public void onResponse(Call<StudentModel> call, Response<StudentModel> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Log.e(TAG, "onResponse: " + response.body().getId() + "\n" + response.body().getScreenName() + "\n" + response.body().getName() + "\n"
                                + response.body().getLevel() + "\n" + response.body().getAssignmentDay() + "\n");
                        assignmentDayTxt.setText(response.body().getAssignmentDay());

                        Toast.makeText(getActivity(), "Your new Screen name has been updated successfully.", Toast.LENGTH_SHORT).show();
                        sharedPreferenceMethod.updateScreenName(screenName.getText().toString());
                    }
                    if (response.code() == 400) {
                        Toast.makeText(getActivity(), "Invalid student supplied", Toast.LENGTH_SHORT).show();
                    }
                    if (response.code() == 404) {
                        Toast.makeText(getActivity(), "Student not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StudentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                progressBar.hideProgress();
                showAlertDialog();
            }
        });
    }

    public boolean isConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;

        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void showAlertDialog() {
        if(isConnected() == false) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());

            // Set title
            alertDialogBuilder.setTitle("Can't reach to the Server");

            // Set dialog message
            alertDialogBuilder
                    .setMessage("Please check your Wifi or Mobile Data")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            // Create Alert Dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // Display it
            alertDialog.show();

        }
    }

}

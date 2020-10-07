package com.doozycod.megamind.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.megamind.Activities.MainNavigation;
import com.doozycod.megamind.Adapter.AssignmentResultAdapter;
import com.doozycod.megamind.Interface.ApiService;
import com.doozycod.megamind.Model.NewAssignmentResponse;
import com.doozycod.megamind.Model.QuestionModel;
import com.doozycod.megamind.Model.StudentAssignmentModel;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Service.ApiUtils;
import com.doozycod.megamind.Utils.CustomProgressBar;
import com.doozycod.megamind.Utils.DatabaseHelper;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.doozycod.megamind.R.drawable.result_red;
import static com.doozycod.megamind.R.drawable.resultpercentage;
import static com.doozycod.megamind.R.drawable.resultspercentage_bg_image;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedAssignmentResultFragment extends Fragment {

    DatabaseHelper databaseHelper;
    AssignmentResultAdapter resultAdapter;
    RecyclerView recyclerView;
    TextView percentageTopLabel;
    Button tryAgainButton, getNewAssignmentButton, nextAssignmentButton;
    Callback mCallbackListener;
    ApiService apiService;
    List<QuestionModel.Question> questionList;
    List<StudentAssignmentModel.AssignmentArray> assignments;
    SharedPreferenceMethod sharedPreferenceMethod;
    int arraySize = 0;
    CustomProgressBar progressBar;
    Boolean connected = false;
    TextView doneAssignmentTxt;

    public SelectedAssignmentResultFragment() {
        // Required empty public constructor
    }

    public interface Callback {
        void mCallbackListener(boolean result);
    }

    public void setListener(Callback callbackListener) {
        this.mCallbackListener = callbackListener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallbackListener = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyInterface ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_assignment_result, container, false);

        recyclerView = view.findViewById(R.id.result_recyclerView);
        progressBar = new CustomProgressBar(getActivity());
        percentageTopLabel = view.findViewById(R.id.percentageTopLabel);
        nextAssignmentButton = view.findViewById(R.id.nextAssignmentButton);
        getNewAssignmentButton = view.findViewById(R.id.getNewAssignmentButton);
        doneAssignmentTxt = view.findViewById(R.id.doneAssignmentTxt);
        tryAgainButton = view.findViewById(R.id.tryAgainButton);
        apiService = ApiUtils.getAPIService();
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());

        int percentage = Integer.parseInt(sharedPreferenceMethod.getPercentage());

        if (percentage > 75) {
            percentageTopLabel.setBackground(getResources().getDrawable(resultspercentage_bg_image));
        }
        if (percentage <= 75 && percentage >= 60) {
            percentageTopLabel.setBackground(getResources().getDrawable(resultpercentage));
        }
        if (percentage < 60) {
            percentageTopLabel.setBackground(getResources().getDrawable(result_red));
        }

        percentageTopLabel.setText(sharedPreferenceMethod.getPercentage() + "%");
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().finish();
                if (databaseHelper != null) {
                    databaseHelper.deleteTable();
                }
                mCallbackListener.mCallbackListener(true);
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new PracticeFragment()).commit();

            }
        });
        nextAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbackListener.mCallbackListener(true);
                int position = Integer.parseInt(sharedPreferenceMethod.getPosition());
                Log.e(TAG, "onClick: Item" + position + " arraysize " + arraySize);
                if (position < arraySize - 1) {
                    position++;
                    final String upperString = assignments.get(position).getAssignmentType().substring(0, 1).toUpperCase() + assignments.get(position).getAssignmentType().substring(1);
                    sharedPreferenceMethod.setAssignmentId(assignments.get(position).getStudentAssignmentId());
                    sharedPreferenceMethod.saveSubtraction(assignments.get(position).getSubtraction());

                    if (assignments.get(position).getDigitSize() == 1) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Single",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 2) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Double",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 3) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Triple",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 4) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Quad",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getSubtraction() != null) {
                        sharedPreferenceMethod.saveSubtraction(assignments.get(position).getSubtraction());
                    } else {
                        sharedPreferenceMethod.saveSubtraction(false);
                    }
                    Fragment fragment = new AssignmentGet();
                    getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                            android.R.animator.fade_out,
                            android.R.animator.fade_in,
                            android.R.animator.fade_out)
                            .replace(R.id.nav_host_fragment, fragment).addToBackStack("Assignment").commit();

                }
                if (position == arraySize - 1) {
                    position = 0;
                    final String upperString = assignments.get(position).getAssignmentType().substring(0, 1).toUpperCase() + assignments.get(position).getAssignmentType().substring(1);
                    sharedPreferenceMethod.setAssignmentId(assignments.get(position).getStudentAssignmentId());
                    sharedPreferenceMethod.saveSubtraction(assignments.get(position).getSubtraction());

                    /*if (databaseHelper.upcompletedAssignments() == assignments.get(position).getQuestionsQty()) {*/
                    if (assignments.get(position).getDigitSize() == 1) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Single",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 2) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Double",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 3) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Triple",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 4) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Quad",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getSubtraction() != null) {
                        sharedPreferenceMethod.saveSubtraction(assignments.get(position).getSubtraction());
                    } else {
                        sharedPreferenceMethod.saveSubtraction(false);
                    }
                    Fragment fragment = new AssignmentGet();
                    getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                            android.R.animator.fade_out,
                            android.R.animator.fade_in,
                            android.R.animator.fade_out)
                            .replace(R.id.nav_host_fragment, fragment).addToBackStack("Assignment").commit();
                }
                if (position == arraySize ) {
                    position = 0;
                    final String upperString = assignments.get(position).getAssignmentType().substring(0, 1).toUpperCase() + assignments.get(position).getAssignmentType().substring(1);
                    sharedPreferenceMethod.setAssignmentId(assignments.get(position).getStudentAssignmentId());
                    sharedPreferenceMethod.saveSubtraction(assignments.get(position).getSubtraction());

                    /*if (databaseHelper.upcompletedAssignments() == assignments.get(position).getQuestionsQty()) {*/
                    if (assignments.get(position).getDigitSize() == 1) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Single",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 2) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Double",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 3) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Triple",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getDigitSize() == 4) {
                        sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpeed()), "Quad",
                                String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                    }
                    if (assignments.get(position).getSubtraction() != null) {
                        sharedPreferenceMethod.saveSubtraction(assignments.get(position).getSubtraction());
                    } else {
                        sharedPreferenceMethod.saveSubtraction(false);
                    }
                    Fragment fragment = new AssignmentGet();
                    getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                            android.R.animator.fade_out,
                            android.R.animator.fade_in,
                            android.R.animator.fade_out)
                            .replace(R.id.nav_host_fragment, fragment).addToBackStack("Assignment").commit();
                }

            }
        });
        getNewAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAssignment();
            }
        });

        progressBar.showProgress();
        getQuestions(Integer.parseInt(sharedPreferenceMethod.getID()), sharedPreferenceMethod.getAssignmentId());
        getAssignments(Integer.parseInt(sharedPreferenceMethod.getID()), "pending");

        for (int q = 0; q < databaseHelper.getAssignmentResults().size(); q++) {
            Log.e(TAG, "onCreateView: " + databaseHelper.getAssignmentResults().get(q).getCheckAnswer());
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    //    check left assignments
    void getAssignments(int id, String status) {
        apiService.getAssignmentById(id, status).enqueue(new retrofit2.Callback<StudentAssignmentModel>() {
            @Override
            public void onResponse(Call<StudentAssignmentModel> call, Response<StudentAssignmentModel> response) {

                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        assignments = response.body().getAssignments();
                        arraySize = response.body().getAssignments().size();
                        Log.e(TAG, "onResponse: Assignments " + assignments.size());
//                        get response in model (local)
                        if(sharedPreferenceMethod.getOnResultClick().equals("saved")){
                            Log.e(TAG, "onResponse: main" );
                        }else{
                            if (response.body().getAssignments().size() == 0) {
                                doneAssignmentTxt.setVisibility(View.VISIBLE);
                                getNewAssignmentButton.setVisibility(View.VISIBLE);
                            } else {
                                nextAssignmentButton.setVisibility(View.VISIBLE);
                            }
                        }
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
            public void onFailure(Call<StudentAssignmentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                showAlertDialog();
            }
        });
    }

    void createNewAssignment() {
        progressBar.showProgress();
        apiService.createAssignmentById(Integer.parseInt(sharedPreferenceMethod.getID()), "pending").enqueue(new retrofit2.Callback<NewAssignmentResponse>() {
            @Override
            public void onResponse(Call<NewAssignmentResponse> call, Response<NewAssignmentResponse> response) {
                progressBar.hideProgress();

                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        getAssignments(Integer.parseInt(sharedPreferenceMethod.getID()), "pending");
                        Intent intent = new Intent(getActivity(), MainNavigation.class);
                        intent.putExtra("assignmentReceived", "assignmentReceived");
                        try {
                            getNewAssignmentButton.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "New assignments created!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Something went wrong please try again!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                    }
                }

                if (response.code() == 400) {
                    Toast.makeText(getActivity(), "Invalid student supplied", Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 404) {
                    Toast.makeText(getActivity(), "Student not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewAssignmentResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                progressBar.hideProgress();
                showAlertDialog();
            }
        });
    }

    void getQuestions(int studentId, String assignmentId) {
        apiService.getDetailOfQuestion(studentId, assignmentId).enqueue(new retrofit2.Callback<QuestionModel>() {
            @Override
            public void onResponse(Call<QuestionModel> call, Response<QuestionModel> response) {
                progressBar.hideProgress();
                Log.e(TAG, "onResponse: Questions " + response.body());
                for (int i = 0; i < response.body().getQuestions().size(); i++) {
                    Log.e(TAG, "onResponse: Questions " + response.body());
                }
                questionList = response.body().getQuestions();
                resultAdapter = new AssignmentResultAdapter(getActivity(), questionList);
                recyclerView.setAdapter(resultAdapter);
            }

            @Override
            public void onFailure(Call<QuestionModel> call, Throwable t) {
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
        if (isConnected() == false) {
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

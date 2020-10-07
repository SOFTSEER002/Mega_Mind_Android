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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.megamind.Adapter.AssignmentsAdapter;
import com.doozycod.megamind.Interface.ApiService;
import com.doozycod.megamind.Interface.OnFragmentInteractionListener;
import com.doozycod.megamind.Model.NewAssignmentResponse;
import com.doozycod.megamind.Model.StudentAssignmentModel;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Service.ApiUtils;
import com.doozycod.megamind.Utils.CustomProgressBar;
import com.doozycod.megamind.Utils.DatabaseHelper;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ApiService apiService;
    List<StudentAssignmentModel.AssignmentArray> studentAssignmentModels;
    AssignmentsAdapter assignmentsAdapter;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    TextView txtPendingAssignments, staticText;
    Boolean connected = false;
    Button getNewAssignmentsFragButton;
    LinearLayout mainsLay;

    public AssignmentsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_assignments, container, false);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());

        recyclerView = v.findViewById(R.id.recycler_view);
        getNewAssignmentsFragButton = v.findViewById(R.id.getNewAssignmentsFragButton);
        txtPendingAssignments = v.findViewById(R.id.txtPendingAssignments);
        staticText = v.findViewById(R.id.staticText);
        mainsLay = v.findViewById(R.id.mainsLay);

        databaseHelper = new DatabaseHelper(getActivity());
        apiService = ApiUtils.getAPIService();
        if (mListener != null) {
            mListener.onFragmentInteraction("Megamind Abacus");
        }

        getNewAssignmentsFragButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAssignment();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        databaseHelper.insertQuestionAssignmentAnswer();
        if (sharedPreferenceMethod.checkLogin()) {
            getAssignments(Integer.parseInt(sharedPreferenceMethod.getID()), "pending");
            staticText.setVisibility(View.GONE);
        } else {
            mainsLay.setVisibility(View.GONE);
        }

    }


    // get pending assignments
    void getAssignments(int id, String status) {
        customProgressBar.showProgress();
        apiService.getAssignmentById(id, status).enqueue(new Callback<StudentAssignmentModel>() {
            @Override
            public void onResponse(Call<StudentAssignmentModel> call, Response<StudentAssignmentModel> response) {
                customProgressBar.hideProgress();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
//                        get response in model (local)
                        studentAssignmentModels = response.body().getAssignments();
                        if (studentAssignmentModels != null && studentAssignmentModels.size() > 0) {
                            txtPendingAssignments.setVisibility(View.GONE);
                            getNewAssignmentsFragButton.setVisibility(View.GONE);
                            Log.e(TAG, "onResponse: " + response.body().getAssignments().size());
//                            Toast.makeText(getActivity(), "S ID " + response.body().getStudentId(), Toast.LENGTH_SHORT).show();
//                            set assignment to recyclerview
                            assignmentsAdapter = new AssignmentsAdapter(getActivity(), studentAssignmentModels, sharedPreferenceMethod, "assignment", databaseHelper);
                            recyclerView.setAdapter(assignmentsAdapter);
                            for (int i = 0; i < studentAssignmentModels.size(); i++) {
                                if (databaseHelper.columnExists(String.valueOf(response.body().getStudentId()), response.body().getAssignments().get(i).getStudentAssignmentId())) {
                                    Log.e(TAG, "DB: Already Exists!!");
                                } else {

//                                    insert assignment to db
                                    databaseHelper.insertAssignment(response.body().getStudentId(), response.body().getAssignments().get(i).getStudentAssignmentId(),
                                            studentAssignmentModels.get(i).getQuestionsQty(), studentAssignmentModels.get(i).getSpeed(),
                                            studentAssignmentModels.get(i).getAssignmentType(), studentAssignmentModels.get(i).getDigitSize(), studentAssignmentModels.get(i).getDigitCount(),
                                            studentAssignmentModels.get(i).getPercentage(), String.valueOf(studentAssignmentModels.get(i).getSubtraction()),
                                            studentAssignmentModels.get(i).getExpectedCompletionDate(), studentAssignmentModels.get(i).getAssignmentStatus(),
                                            studentAssignmentModels.get(i).getAssignmentDate());
                                }
                            }

                        } else {
                            txtPendingAssignments.setVisibility(View.VISIBLE);
                            getNewAssignmentsFragButton.setVisibility(View.VISIBLE);
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
                customProgressBar.hideProgress();
                showAlertDialog();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void createNewAssignment() {
        customProgressBar.showProgress();
        apiService.createAssignmentById(Integer.parseInt(sharedPreferenceMethod.getID()), "pending").enqueue(new Callback<NewAssignmentResponse>() {
            @Override
            public void onResponse(Call<NewAssignmentResponse> call, Response<NewAssignmentResponse> response) {
                customProgressBar.hideProgress();

                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        getAssignments(Integer.parseInt(sharedPreferenceMethod.getID()), "pending");
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
                customProgressBar.hideProgress();
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

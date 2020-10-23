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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.megamind.Adapter.AllResultsAdapter;
import com.doozycod.megamind.Adapter.AssignmentsAdapter;
import com.doozycod.megamind.Interface.ApiService;
import com.doozycod.megamind.Interface.OnFragmentInteractionListener;
import com.doozycod.megamind.Model.ResultModel;
import com.doozycod.megamind.Model.StudentAssignmentModel;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Service.ApiUtils;
import com.doozycod.megamind.Utils.CustomProgressBar;
import com.doozycod.megamind.Utils.DatabaseHelper;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.doozycod.megamind.R.drawable.result_red;
import static com.doozycod.megamind.R.drawable.resultpercentage;
import static com.doozycod.megamind.R.drawable.resultspercentage_bg_image;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllResultFragment extends Fragment {
    DatabaseHelper databaseHelper;
    ArrayList<ResultModel> listByType = new ArrayList<>();
    AllResultsAdapter allResultsAdapter;
    RecyclerView recyclerView;
    ArrayList<String> percentage;
    TextView percentageTopLabel, txtNoResults;
    private OnFragmentInteractionListener mListener;
    Boolean connected = false;

    SharedPreferenceMethod sharedPreferenceMethod;
    List<StudentAssignmentModel.AssignmentArray> studentAssignmentModels;
    ApiService apiService;
    CustomProgressBar progressBar;
    AssignmentsAdapter assignmentsAdapter;

    public AllResultFragment() {
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
        View view = inflater.inflate(R.layout.fragment_all_result, container, false);

        recyclerView = view.findViewById(R.id.rv_all_results);
        percentageTopLabel = view.findViewById(R.id.percentageTopLabelAll);
        txtNoResults = view.findViewById(R.id.txtNoResults);
        recyclerView.setNestedScrollingEnabled(false);
        databaseHelper = new DatabaseHelper(getActivity());
        apiService = ApiUtils.getAPIService();
        progressBar = new CustomProgressBar(getActivity());
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        if (mListener != null) {
            mListener.onFragmentInteraction("Megamind\nAbacus Practice");
        }
        // Inflate the layout for this fragment
        int totalCorrect = 0;
        for (int i = 0; i < databaseHelper.getAssignmentResults().size(); i++) {
            if (databaseHelper.getAssignmentResults().get(i).getCheckAnswer().equals("true")) {
                totalCorrect += 1;
            }
        }
        float percentD = (float) totalCorrect / databaseHelper.getAssignmentResults().size();
        percentD = percentD * 100;
        int p = Math.round(percentD);
        if (p > 75) {
            percentageTopLabel.setBackground(getResources().getDrawable(resultspercentage_bg_image));
        }
        if (p <= 75 && p >= 60) {
            percentageTopLabel.setBackground(getResources().getDrawable(resultpercentage));
        }
        if (p < 60) {
            percentageTopLabel.setBackground(getResources().getDrawable(result_red));
        }
        percentageTopLabel.setText(p + "%");

        progressBar.showProgress();
//        get completed assignments
        getAssignments(Integer.parseInt(sharedPreferenceMethod.getID()), "completed");
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public class StringDateComparator implements Comparator<StudentAssignmentModel.AssignmentArray> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int i = 0;


        @Override
        public int compare(StudentAssignmentModel.AssignmentArray lhs, StudentAssignmentModel.AssignmentArray rhs) {
            try {
                return dateFormat.parse(rhs.getActualCompletionDate().substring(0, 10)).compareTo(dateFormat.parse(lhs.getActualCompletionDate().substring(0, 10)));
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    //    get completed assignment
    void getAssignments(int id, String status) {
        apiService.getAssignmentById(id, status).enqueue(new Callback<StudentAssignmentModel>() {
            @Override
            public void onResponse(Call<StudentAssignmentModel> call, Response<StudentAssignmentModel> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        studentAssignmentModels = response.body().getAssignments();
                        if (studentAssignmentModels != null) {
                            txtNoResults.setVisibility(View.GONE);
                            Log.e("CHECK SORTING", " " + studentAssignmentModels.size());

                            Collections.sort(studentAssignmentModels, new StringDateComparator());
                            for (int i = 0; i < studentAssignmentModels.size(); i++) {
                                Log.e("CHECK SORTING", " " + studentAssignmentModels.get(i).getActualCompletionDate());
                            }
//                        Toast.makeText(getActivity(), "" + response.body().getStudentId(), Toast.LENGTH_SHORT).show();
                            assignmentsAdapter = new AssignmentsAdapter(getActivity(), studentAssignmentModels, sharedPreferenceMethod, "result", databaseHelper);
                            recyclerView.setAdapter(assignmentsAdapter);
                            for (int i = 0; i < studentAssignmentModels.size(); i++) {
                                if (databaseHelper.columnExists(String.valueOf(response.body().getStudentId()), response.body().getAssignments().get(i).getStudentAssignmentId())) {
                                    Log.e(TAG, "DB: Already Exists!!");
                                } else {
                                    databaseHelper.insertAssignment(response.body().getStudentId(), response.body().getAssignments().get(i).getStudentAssignmentId(),
                                            studentAssignmentModels.get(i).getQuestionsQty(), studentAssignmentModels.get(i).getSpeed(),
                                            studentAssignmentModels.get(i).getAssignmentType(), studentAssignmentModels.get(i).getDigitSize(), studentAssignmentModels.get(i).getDigitCount(),
                                            studentAssignmentModels.get(i).getPercentage(), String.valueOf(studentAssignmentModels.get(i).getSubtraction()),
                                            studentAssignmentModels.get(i).getExpectedCompletionDate(), studentAssignmentModels.get(i).getAssignmentStatus(),
                                            studentAssignmentModels.get(i).getAssignmentDate());
                                }
                            }

                        } else {
                            txtNoResults.setVisibility(VISIBLE);
                            Toast.makeText(getActivity(), "No Results to display", Toast.LENGTH_SHORT).show();
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
                progressBar.hideProgress();
                showAlertDialog();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

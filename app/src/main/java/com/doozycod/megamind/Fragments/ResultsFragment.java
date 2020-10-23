package com.doozycod.megamind.Fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.megamind.Activities.MainNavigation;
import com.doozycod.megamind.Activities.PowerCorrectActivity;
import com.doozycod.megamind.Adapter.ResultAdapter;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Utils.DatabaseHelper;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.doozycod.megamind.R.drawable.result_red;
import static com.doozycod.megamind.R.drawable.resultpercentage;
import static com.doozycod.megamind.R.drawable.resultspercentage_bg_image;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment {

    DatabaseHelper databaseHelper;
    ResultAdapter resultAdapter;
    RecyclerView recyclerView;
    TextView percentageTopLabel;
    Button tryAgainButton;
    Callback mCallbackListener;

    public ResultsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        recyclerView = view.findViewById(R.id.result_recyclerView);
        percentageTopLabel = view.findViewById(R.id.percentageTopLabel);
        tryAgainButton = view.findViewById(R.id.tryAgainButton);

        databaseHelper = new DatabaseHelper(getActivity());
        resultAdapter = new ResultAdapter(getActivity(), databaseHelper.getResults());

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().finish();
                if (databaseHelper != null) {
                    databaseHelper.deleteTable();
                }
                Intent setIntent = new Intent(getActivity(), MainNavigation.class);
                setIntent.putExtra("practice", "answer");
                startActivity(setIntent);
                getActivity().finishAffinity();
                mCallbackListener.mCallbackListener(true);

            }
        });
        int totalCorrect = 0;
        for (int i = 0; i < databaseHelper.getResults().size(); i++) {
            if (databaseHelper.getResults().get(i).getCheckAnswer().equals("true")) {
                totalCorrect += 1;
            }
        }
        float percentD = (float) totalCorrect / databaseHelper.getResults().size();
        percentD = percentD * 100;
        int p = Math.round(percentD);
        if (p >= 100) {
            percentageTopLabel.setBackground(getResources().getDrawable(resultspercentage_bg_image));
        }
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

        for (int q = 0; q < databaseHelper.getResults().size(); q++) {
            Log.e(TAG, "onCreateView: " + databaseHelper.getResults().get(q).getCheckAnswer());
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(resultAdapter);
    }
}

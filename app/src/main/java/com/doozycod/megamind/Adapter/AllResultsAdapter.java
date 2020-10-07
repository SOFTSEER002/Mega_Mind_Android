package com.doozycod.megamind.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.megamind.Model.AssignmentResultModel;
import com.doozycod.megamind.Model.ResultModel;
import com.doozycod.megamind.R;

import java.util.ArrayList;
import java.util.List;

public class AllResultsAdapter extends RecyclerView.Adapter<AllResultsAdapter.ResultHolder> {
    private static final String TAG = "";
    Context context;
    ArrayList<AssignmentResultModel> results;

    public AllResultsAdapter(Context context, ArrayList<AssignmentResultModel> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public AllResultsAdapter.ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_recycler_view, parent, false);

        return new AllResultsAdapter.ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {
        holder.resultsNumbersArray.setText(results.get(position).getNumberArray());
        holder.resultUsersAnswer.setText(results.get(position).getUserAnswer());
        holder.resultCorrectAnswer.setText(results.get(position).getCorrectAnswer());
        holder.resultQuestionNo.setText("Question "+results.get(position).getQuestionNo());

        if (results.get(position).getCheckAnswer().equals("false")) {
            holder.rightAnswer.setVisibility(View.GONE);
            holder.wrongAnswer.setVisibility(View.VISIBLE);
            Log.e(TAG, "onBindViewHolder: "+results.get(position).getCheckAnswer() );

        }
        if (results.get(position).getCheckAnswer().equals("true")) {
            holder.rightAnswer.setVisibility(View.VISIBLE);
            holder.wrongAnswer.setVisibility(View.GONE);
            Log.e(TAG, "onBindViewHolder: "+results.get(position).getCheckAnswer() );
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ResultHolder extends RecyclerView.ViewHolder {
        TextView resultsNumbersArray, resultCorrectAnswer, resultUsersAnswer, resultQuestionNo, rightAnswer, wrongAnswer;

        public ResultHolder(@NonNull View itemView) {
            super(itemView);
            resultsNumbersArray = itemView.findViewById(R.id.resultsNumbersArray);
            resultCorrectAnswer = itemView.findViewById(R.id.resultCorrectAnswer);
            resultUsersAnswer = itemView.findViewById(R.id.resultUsersAnswer);
            resultQuestionNo = itemView.findViewById(R.id.resultQuestionNo);
            rightAnswer = itemView.findViewById(R.id.right);
            wrongAnswer = itemView.findViewById(R.id.wrong);
        }
    }
}

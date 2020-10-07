package com.doozycod.megamind.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.megamind.Model.AssignmentResultModel;
import com.doozycod.megamind.Model.QuestionModel;
import com.doozycod.megamind.Model.ResultModel;
import com.doozycod.megamind.Model.StudentAssignmentModel;
import com.doozycod.megamind.R;

import java.util.ArrayList;
import java.util.List;

public class AssignmentResultAdapter extends RecyclerView.Adapter<AssignmentResultAdapter.ResultHolder> {
    private static final String TAG = "RESULT";
    Context context;
    List<QuestionModel.Question> results;

    public AssignmentResultAdapter(Context context, List<QuestionModel.Question> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public AssignmentResultAdapter.ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_recycler_view, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentResultAdapter.ResultHolder holder, int position) {
        holder.resultsNumbersArray.setText(results.get(position).getQuestion());
        holder.resultUsersAnswer.setText(results.get(position).getStudentAnswer()+"");
        holder.resultCorrectAnswer.setText(results.get(position).getAnswer()+"");
        holder.resultQuestionNo.setText("Question "+results.get(position).getQuestionNum());

        if (!results.get(position).getResult()) {
            holder.rightAnswer.setVisibility(View.GONE);
            holder.wrongAnswer.setVisibility(View.VISIBLE);
            Log.e(TAG, "onBindViewHolder: "+results.get(position).getAnswer() );
        }
        if (results.get(position).getResult()) {
            holder.rightAnswer.setVisibility(View.VISIBLE);
            holder.wrongAnswer.setVisibility(View.GONE);
            Log.e(TAG, "onBindViewHolder: "+results.get(position).getAnswer() );
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

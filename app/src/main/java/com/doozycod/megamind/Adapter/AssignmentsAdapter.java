package com.doozycod.megamind.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.megamind.Fragments.AssignmentGet;
import com.doozycod.megamind.Fragments.PracticeFragment;
import com.doozycod.megamind.Fragments.SelectedAssignmentResultFragment;
import com.doozycod.megamind.Model.StudentAssignmentModel;
import com.doozycod.megamind.R;
import com.doozycod.megamind.Utils.DatabaseHelper;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.doozycod.megamind.R.drawable.result_red;
import static com.doozycod.megamind.R.drawable.resultpercentage;
import static com.doozycod.megamind.R.drawable.resultspercentage_bg_image;


public class AssignmentsAdapter extends RecyclerView.Adapter<AssignmentsAdapter.AssignmentHolder> {
    Context context;
    List<StudentAssignmentModel.AssignmentArray> assignments;
    SharedPreferenceMethod sharedPreferenceMethod;
    String type;
    DatabaseHelper databaseHelper;

    public AssignmentsAdapter(Context context, List<StudentAssignmentModel.AssignmentArray> assignments, SharedPreferenceMethod sharedPreferenceMethod, String type, DatabaseHelper databaseHelper) {
        this.context = context;
        this.assignments = assignments;
        this.sharedPreferenceMethod = sharedPreferenceMethod;
        this.type = type;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public AssignmentsAdapter.AssignmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.assignments_listview, parent, false);
        return new AssignmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AssignmentsAdapter.AssignmentHolder holder, final int position) {
        final String upperString = assignments.get(position).getAssignmentType().substring(0, 1).toUpperCase() + assignments.get(position).getAssignmentType().substring(1);

        if (upperString.equals("Addition")) {
            holder.cal_image.setImageResource(R.drawable.plus_img);
        }
        if (upperString.equals("Multiplication")) {
            holder.cal_image.setImageResource(R.drawable.unchecked);
        }
        if (upperString.equals("Division")) {
            holder.cal_image.setImageResource(R.drawable.division);
        }
        if (upperString.equals("Subtraction")) {
            holder.cal_image.setImageResource(R.drawable.subtraction);
        }
        holder.assignmentType.setText(upperString);
        int p = assignments.get(position).getPercentage();
        String assignmentDate = assignments.get(position).getAssignmentDate().substring(0, 10);
        String expectedCompletionDate = assignments.get(position).getExpectedCompletionDate().substring(0, 10);
        if (type.equals("result")) {
            if (p > 75) {
                holder.percentageResult.setBackground(context.getResources().getDrawable(resultspercentage_bg_image));
            }
            if (p <= 75 && p >= 60) {
                holder.percentageResult.setBackground(context.getResources().getDrawable(resultpercentage));
            }
            if (p < 60) {
                holder.percentageResult.setBackground(context.getResources().getDrawable(result_red));
            }
            holder.percentageResult.setText(p + "%");
            String actualCompletionDate;

//           //** Working temporarily
//            if(assignments.get(position).getActualCompletionDate() == null){
//                actualCompletionDate = "00-00-0000";
//
//            }else{
            //ACTUAL COMPLETION DATE NULL  ---> APP IS CRASHING  **
            if (assignments.get(position).getActualCompletionDate() != null) {
                actualCompletionDate = assignments.get(position).getActualCompletionDate().substring(0, 10);
                holder.assignmentDate.setText("Completed on: " + actualCompletionDate);
            }

//            }

        } else {
            holder.assignmentDate.setText(assignmentDate + " to " + expectedCompletionDate);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferenceMethod.setPosition("" + position);
                Log.e(TAG, "onClick: " + position);
                sharedPreferenceMethod.setPercentage(String.valueOf(assignments.get(position).getPercentage()));
                sharedPreferenceMethod.setAssignmentId(assignments.get(position).getStudentAssignmentId());
                if (type.equals("assignment")) {
                    /*if(Integer.parseInt(databaseHelper.getAssignmentResults().get(position).getQuestionNo()) == assignments.get(position).getQuestionsQty()){

                    }*/
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
                    ((Activity) context).getFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,
                            android.R.animator.fade_out,
                            android.R.animator.fade_in,
                            android.R.animator.fade_out)
                            .replace(R.id.nav_host_fragment, fragment).addToBackStack("Assignment").commit();
                    /*} else {
                        if (assignments.get(position).getDigitSize() == 1) {
                            sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpped()), "Single",
                                    String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                        }
                        if (assignments.get(position).getDigitSize() == 2) {
                            sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpped()), "Double",
                                    String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                        }
                        if (assignments.get(position).getDigitSize() == 3) {
                            sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpped()), "Triple",
                                    String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                        }
                        if (assignments.get(position).getDigitSize() == 4) {
                            sharedPreferenceMethod.insertProperties(upperString, String.valueOf(assignments.get(position).getSpped()), "Quad",
                                    String.valueOf(assignments.get(position).getDigitCount()), String.valueOf(assignments.get(position).getQuestionsQty()));
                        }
                        if (assignments.get(position).getSubtraction() != null) {
                            sharedPreferenceMethod.saveSubtraction(assignments.get(position).getSubtraction());
                            Log.e(TAG, "onBindViewHolder: " + sharedPreferenceMethod.getType() + " " + sharedPreferenceMethod.getFlickerSpeed() + " " +
                                    sharedPreferenceMethod.getDigitSize() + " " + sharedPreferenceMethod.getNoOfDigits() + " "
                                    + sharedPreferenceMethod.getQuestions()
                            );
                        } else {
                            sharedPreferenceMethod.saveSubtraction(false);
                        }
                        showDialog();
//                        Fragment fragment = new AssignmentGet();
//                        ((Activity) context).getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("").commit();
                    }*/
                }
                if (type.equals("result")) {
                    Fragment fragment = new SelectedAssignmentResultFragment();
                    ((Activity) context).getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in,
                                    android.R.animator.fade_out,
                                    android.R.animator.fade_in,
                                    android.R.animator.fade_out)
                            .replace(R.id.nav_host_fragment, fragment).addToBackStack("SelectedAssignment").commit();
                }

            }
        });

        if (position % 2 == 1) {
            holder.linearLayout.setBackground(holder.linearLayout.getResources().getDrawable(R.color.recycler_items));
        } else {

            holder.linearLayout.setBackground(holder.linearLayout.getResources().getDrawable(R.color.white));

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
        return assignments.size();
    }

  /*  public void showDialog() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.resume_assignment);
        ImageView continueBtn = dialog.findViewById(R.id.continueAssignmentBtn);
        ImageView closeAssignment = dialog.findViewById(R.id.noAssignmentBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AssignmentGet();
                ((Activity) context).getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in,
                                android.R.animator.fade_out,
                                android.R.animator.fade_in,
                                android.R.animator.fade_out)
                        .replace(R.id.nav_host_fragment, fragment).commit();
            }
        });
        closeAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PracticeFragment();
                ((Activity) context).getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in,
                                android.R.animator.fade_out,
                                android.R.animator.fade_in,
                                android.R.animator.fade_out)
                        .replace(R.id.nav_host_fragment, fragment).commit();
            }
        });
    }*/

    public class AssignmentHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView assignmentType, assignmentDate, percentageResult;
        ImageView cal_image;

        public AssignmentHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_layout_assign);
            assignmentDate = itemView.findViewById(R.id.assignmentDate);
            assignmentType = itemView.findViewById(R.id.assignmentType);
            percentageResult = itemView.findViewById(R.id.percentageResult);
            cal_image = itemView.findViewById(R.id.cal_image);
        }

    }
}

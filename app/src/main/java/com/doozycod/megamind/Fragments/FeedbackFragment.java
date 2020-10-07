package com.doozycod.megamind.Fragments;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.doozycod.megamind.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {
    Button submitFeedbackBtn;
    EditText nameET, emailET, phoneET, descriptionET;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        phoneET = view.findViewById(R.id.phoneET);
        descriptionET = view.findViewById(R.id.descriptionET);
        submitFeedbackBtn = view.findViewById(R.id.submitFeedbackBtn);
        // Inflate the layout for this fragment
        return view;
    }

    //      Email Validation
    boolean emailValidate() {
        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +

                "\\@" +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                "(" +

                "\\." +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                ")+";
        String email = emailET.getText().toString();

//          check/match pattern that  email is valid
        Matcher matcher = Pattern.compile(validemail).matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(getActivity(), "Please Enter A  Valid Email", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameET.getText().toString().equals("")) {
                    nameET.setError("Name is required!");
                    return;
                }
                if (!emailValidate()) {
                    Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (emailET.getText().toString().equals("")) {
                    emailET.setError("Email is required!");
                    return;
                }
                if (phoneET.getText().toString().equals("")) {
                    phoneET.setError("Please enter your phone no.");
                    return;
                }
                if (descriptionET.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please share your thoughts in description!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Pattern p = Pattern.compile("[^\\p{L}+]");
                    Matcher phoneNo = p.matcher(phoneET.getText().toString());
                    if (nameET.getText().toString().substring(0, 1).startsWith(" ")) {
                        Toast.makeText(getActivity(), "Space is not allowed in starting!", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    if (!phoneNo.find() || phoneET.getText().toString().length() < 10) {
                        Toast.makeText(getActivity(), "Please enter a valid phone no!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {

//                        show submit email dialog
                        showSubmitDialog();

//                        start intent for gmail to send mail
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@techdoquest.com"});
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MegaMind-Abacus:Feedback");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, descriptionET.getText().toString().trim() + "\n\n" + "Name : - " + nameET.getText() + "\n Email:- " + emailET.getText() + " \n Phone: - " + phoneET.getText());
//                        sendIntent.addCategory("android.intent.category.APP_EMAIL");
                        sendIntent.setType("text/html");
                        sendIntent.setPackage("com.google.android.gm");
                        try {
                            startActivity(sendIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                        nameET.setText("");
                        emailET.setText("");
                        phoneET.setText("");
                        descriptionET.setText("");
                    }
                }
            }

        });
    }

    private void showSubmitDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.submission_dialog);
        dialog.show();
    }
}

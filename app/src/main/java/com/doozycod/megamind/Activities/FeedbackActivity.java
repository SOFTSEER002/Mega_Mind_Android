package com.doozycod.megamind.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doozycod.megamind.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Button submit = findViewById(R.id.submitFeedbackBtn);
        Button backButtonFeedback = findViewById(R.id.backButtonFeedback);

        backButtonFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(FeedbackActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText nameET = findViewById(R.id.nameET);
                EditText emailET= findViewById(R.id.emailET);
                EditText phoneET = findViewById(R.id.phoneET);
                EditText descriptionET = findViewById(R.id.descriptionET);

                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String phone = phoneET.getText().toString();
                String description = descriptionET.getText().toString();


                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"feedback@techdoquest.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "MegaMind-Abacus:Feedback");
                i.putExtra(Intent.EXTRA_TEXT   , description
                                                    +"\n\n"+"Name: "+name+"\n"+"Phone: "+phone
                        +"\n"+"Email: "+email);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FeedbackActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(FeedbackActivity.this, MainActivity.class);
        startActivity(setIntent);
    }
}

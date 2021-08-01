package com.example.MatrixCalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FeedbackActivity extends AppCompatActivity {

    TextInputEditText Content;
    TextInputLayout contentLayout;

    RadioButton bugRadio,featureRadio;

    Boolean feedbackFields=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Initialize();
    }

    public void Initialize(){

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Content.getText().toString().trim().equals(""))
                new AlertDialog.Builder(FeedbackActivity.this)
                        .setTitle("")
                        .setMessage("Discard Feedback")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                back();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                else back();
            }
        });

        bugRadio=findViewById(R.id.radioButton1);
        featureRadio=findViewById(R.id.radioButton2);

        Content=findViewById(R.id.feedbackContentField);


        contentLayout=findViewById(R.id.contentLayout);

        findViewById(R.id.joinButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://t.me/joinchat/vL6zyE3Lig8yODVl"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    public void SendFeedback(View v){



        if(Content.getText().toString().equals(""))
            contentLayout.setError("*Required");
        else
            contentLayout.setError(null);

        feedbackFields=Content.getText().toString().equals("");
        if(!feedbackFields) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);

            intent.setData(Uri.parse("mailto:")); // only email apps should handle this

            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ijlalahmad845@gmail.com"});

            if(bugRadio.isChecked())
                intent.putExtra(Intent.EXTRA_SUBJECT, "Bug Encountered");
            else if(featureRadio.isChecked())
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feature Request");

            intent.putExtra(Intent.EXTRA_TEXT,Content.getText().toString());
            startActivity(intent);

        }

    }

    private void back() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {

        if(!Content.getText().toString().equals(""))
        new AlertDialog.Builder(FeedbackActivity.this)
                .setTitle("")
                .setMessage("Discard Feedback")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        back();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        else back();
    }
}
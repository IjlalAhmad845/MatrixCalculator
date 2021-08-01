package com.example.navigation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView link1,link2, TermsOfService, privacyPolicy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        link1=findViewById(R.id.link1);
        link2=findViewById(R.id.link2);

        link1.setMovementMethod(LinkMovementMethod.getInstance());
        link2.setMovementMethod(LinkMovementMethod.getInstance());

        TermsOfService =findViewById(R.id.TOS);
        privacyPolicy =findViewById(R.id.privacyPolicy);

        TermsOfService.setMovementMethod(LinkMovementMethod.getInstance());
        privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void back() {
        super.onBackPressed();
    }
}
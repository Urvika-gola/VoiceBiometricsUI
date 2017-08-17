package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AuthenticationSuccess extends AppCompatActivity {
    TextView myTextView1, myTextView2;
    String finalusername = SignIn.userName;
    Button startOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_success);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        myTextView1 = (TextView) findViewById(R.id.text2);
        myTextView2 = (TextView) findViewById(R.id.text1);
        startOver=(Button)findViewById(R.id.startover);

        myTextView2.setTypeface(typeface1);
        myTextView1.setTypeface(typeface1);
        startOver.setTypeface(typeface1);

        myTextView1.setText("Hi " + finalusername + ", Welcome Back!");
        startOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AuthenticationSuccess.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}

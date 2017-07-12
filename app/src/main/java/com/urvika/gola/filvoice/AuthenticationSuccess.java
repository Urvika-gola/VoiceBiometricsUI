package com.urvika.gola.filvoice;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AuthenticationSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_success);

        TextView myTextView1 = (TextView) findViewById(R.id.text2);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTextView1.setTypeface(typeface1);


        TextView myTextView2 = (TextView) findViewById(R.id.text1);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTextView2.setTypeface(typeface2);
    }
}

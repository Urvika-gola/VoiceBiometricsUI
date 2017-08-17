package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import com.andexert.library.RippleView;

import java.io.File;

public class EnrollmentSuccess extends Activity {
    String username=record.username.getText().toString();
    TextView myTextView1,myTextView2;
    String OUTPUT_FILE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enrollment_success);
        for(int x=1;x<=3;x++)
        {
            OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/attempt"+x+".wav";
            File outfile = new File(OUTPUT_FILE);
            if(outfile.exists())
                outfile.delete();
        }
        myTextView1 = (TextView) findViewById(R.id.text1);
        myTextView2 = (TextView) findViewById(R.id.text2);
        myTextView1.setText("Hi "+username);
        final RippleView rippleView=(RippleView)findViewById(R.id.more);
        Button button = (Button) findViewById(R.id.submit);

        final RippleView rippleView1=(RippleView)findViewById(R.id.more1);
        Button button1 = (Button) findViewById(R.id.enrollmore);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTextView1.setTypeface(typeface);
        myTextView2.setTypeface(typeface);

        button.setTypeface(typeface);
        button1.setTypeface(typeface);

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(EnrollmentSuccess.this,SignIn.class);
                startActivity(i);
            }
        });

        rippleView1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(EnrollmentSuccess.this,record.class);
                startActivity(i);
            }
        });
    }}
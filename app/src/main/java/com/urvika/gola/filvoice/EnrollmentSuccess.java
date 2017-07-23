package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.andexert.library.RippleView;

public class EnrollmentSuccess extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enrollment_success1);

        TextView myTextView1 = (TextView) findViewById(R.id.text2);
        final RippleView rippleView=(RippleView)findViewById(R.id.more);
        Button button = (Button) findViewById(R.id.submit);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTextView1.setTypeface(typeface);
        button.setTypeface(typeface);

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(EnrollmentSuccess.this,SignIn.class);
                startActivity(i);
            }
        });
    }}
package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img = (ImageView)findViewById(R.id.imageviewhandler);
        img.setBackgroundResource(R.drawable.spin_animation);

        RippleView rippleView=(RippleView)findViewById(R.id.more);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();

        TextView myTextView = (TextView) findViewById(R.id.text1);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTextView.setTypeface(typeface);
        TextView myTextView1 = (TextView) findViewById(R.id.text2);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTextView1.setTypeface(typeface1);

        Button button = (Button) findViewById(R.id.button);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        button.setTypeface(typeface2);

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
               Intent i = new Intent(MainActivity.this,PhraseListView.class);
              //   Intent i = new Intent(MainActivity.this,RecyclerViewActivity.class);
                startActivity(i);
            }

        });
       /* button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
              Intent i = new Intent(MainActivity.this,PhraseListView.class);
               // Intent i = new Intent(MainActivity.this,RecyclerViewActivity.class);
                startActivity(i);
            }
        });*/
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });


        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

                    Intent i = new Intent(MainActivity.this, SignIn.class);
                    startActivity(i);

            }

        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
     t1.speak("Welcome to Fidelity Voice Authentication System. Please start the authentication process by speaking your pass phrase. ",TextToSpeech.QUEUE_FLUSH, null, "aa");
            }
        }, 2000);
    }

}

package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.andexert.library.RippleView;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class record extends AppCompatActivity {
    private String OUTPUT_FILE;
    static  int attempt;
    final GetPhrase gp1=new GetPhrase();
    String s=gp1.getphrase();
    TextToSpeech t1;
    private WavAudioRecorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ///storage/emulated/0
      //  OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/urvika.mp4";
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        ImageView speaker=(ImageView)findViewById(R.id.speakerimageview);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(s,TextToSpeech.QUEUE_FLUSH, null, "aa");
            }
        });

        final TextView mTextView = (TextView) findViewById(R.id.tvphrase);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        EditText username=(EditText)findViewById(R.id.entername);  //Take input
        username.setTypeface(typeface2);

        Button button = (Button) findViewById(R.id.submit);
        button.setTypeface(typeface2);

        mTextView.setText(s);
        mTextView.setTypeface(typeface2);

        final RippleView rippleView=(RippleView)findViewById(R.id.more);

        final ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebut);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // The toggle is DIS - enabled
                   stopRecord();
                    toggle.clearAnimation();


                }
                else {
                    // The toggle is ENABLE
                    toggle.startAnimation(myAnim);

                    ++attempt;
                    try {
                        startRecord();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }});

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(record.this,EnrollmentSuccess.class);
                startActivity(i);
            }
        });
    }


    private void stopRecord() {
        mRecorder.stop();
        mRecorder.reset();
    }
private void startRecord() throws IOException{
    mRecorder = WavAudioRecorder.getInstanse();
    if(attempt==1)
        {
            OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/attempt1.wav";
        }
        if(attempt==2)
        {
            OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/attempt2.mp4";
        }
        if(attempt==3)
        {
            OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/attempt3.mp4";
            attempt=0; //reset.
        }
        mRecorder.setOutputFile(OUTPUT_FILE);

    File outfile = new File(OUTPUT_FILE);
    if(outfile.exists())
        outfile.delete();

    if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {
        mRecorder.prepare();
        mRecorder.start();
    }
    else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
        mRecorder.release();
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(OUTPUT_FILE);
    }}
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecorder) {
            mRecorder.release();
        }
    }
    }


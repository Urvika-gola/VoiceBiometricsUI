package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andexert.library.RippleView;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class record extends AppCompatActivity {
    private MediaRecorder recorder;
    private String OUTPUT_FILE;
    static  int attempt;
    final GetPhrase gp1=new GetPhrase();
    String s=gp1.getphrase();
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ///storage/emulated/0
        OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/urvika.wav";
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        ImageView speaker=(ImageView)findViewById(R.id.speakerimageview);

        final TextView mTextView = (TextView) findViewById(R.id.tvphrase);
        final TextInputLayout username=(TextInputLayout)findViewById(R.id.text_input_layout);
        username.setHint("Username");

        final RippleView rippleView=(RippleView)findViewById(R.id.more);


        ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebut);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // The toggle is DIS - enabled

                    stopRecord();
                }
                else {
                    // The toggle is ENABLE
                    try {
                    ++attempt;
                    startRecord();

                } catch (IOException e) {
                    e.printStackTrace();
                }



                }
            }
        });

        ///////TEXT
        mTextView.setText(s);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        mTextView.setTypeface(typeface2);
        ///////////
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(s,TextToSpeech.QUEUE_FLUSH, null, "aa");
            }
        });
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(record.this,EnrollmentSuccess.class);
                startActivity(i);
            }
        });
    }


    private void stopRecord() {
        if(recorder!=null)
            recorder.stop();
    }
private void startRecord() throws IOException{
clearRecord();


    if(attempt==1)
        {
            OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/urvika1.wav";
        }
        if(attempt==2)
        {
            OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/urvika2.wav";
        }
        if(attempt==3)
        {
            OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/urvika3.wav";
            attempt=0; //reset.
        }
        File outfile=new File(OUTPUT_FILE);
           if(outfile.exists())
            outfile.delete();

        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      /*  recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);*/
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      recorder.setOutputFile(OUTPUT_FILE);
        recorder.prepare();
        recorder.start();
    }

    private void clearRecord() {
        if(recorder!=null)
            recorder.release();
    }

}

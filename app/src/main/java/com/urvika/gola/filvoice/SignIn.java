package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.andexert.library.RippleView;

import java.io.File;
import java.io.IOException;

public class SignIn extends AppCompatActivity {
    private MediaRecorder recorder;
    private String OUTPUT_FILE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        ///storage/emulated/0
        OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/urvika.wav";
       final ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebut);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final Button accessgranted = (Button) findViewById(R.id.accessgranted);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    toggle.startAnimation(myAnim);
                    try {
                        startRecord();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // The toggle is disabled
                    stopRecord();
                    toggle.clearAnimation();
                }
            }
        });

        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        accessgranted.setTypeface(typeface2);
        final RippleView rippleView=(RippleView)findViewById(R.id.more);

        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent i = new Intent(SignIn.this,AuthenticationSuccess.class);
                startActivity(i);
            }
        });
    }

    private void stopRecord() {
        if(recorder!=null)
            recorder.stop();
    }

    private void startRecord() throws IOException {
        clearRecord();
        OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/final.wav";
        File outfile=new File(OUTPUT_FILE);
        if(outfile.exists())
            outfile.delete();

        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      /*  recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);*/
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(OUTPUT_FILE);
        recorder.prepare();
        recorder.start();


    }

    private void clearRecord() {
        if(recorder!=null)
            recorder.release();
    }
/*    public void didTapButton(View view) {
        Button button = (Button)findViewById(R.id.button);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        button.startAnimation(myAnim);
    }*/
/*    public void hi(View view) {
        ToggleButton dummyaccessgranted=(ToggleButton)findViewById(R.id.togglebut);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        dummyaccessgranted.startAnimation(myAnim);
    }*/
}

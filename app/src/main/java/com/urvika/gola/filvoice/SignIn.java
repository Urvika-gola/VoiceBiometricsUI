package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.RecoverySystem;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.andexert.library.RippleView;
import com.wooplr.spotlight.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

public class SignIn extends AppCompatActivity {
    private String OUTPUT_FILE;
    private WavAudioRecorder mRecorder;
    File outfile ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        ///storage/emulated/0
      OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/final.wav";
        outfile = new File(OUTPUT_FILE);
       final ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebut);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final Button accessgranted = (Button) findViewById(R.id.accessgranted);

        final TextView mTextView = (TextView) findViewById(R.id.loginphrase);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        mTextView.setTypeface(typeface);

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
        mRecorder.stop();
        mRecorder.reset();
    }
    private void startRecord() throws IOException{
        mRecorder = WavAudioRecorder.getInstanse();
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
    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(outfile);
    }
}
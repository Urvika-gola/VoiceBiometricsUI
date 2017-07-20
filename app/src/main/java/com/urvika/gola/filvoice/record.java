package com.urvika.gola.filvoice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.andexert.library.RippleView;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.wooplr.spotlight.SpotlightConfig;
import com.wooplr.spotlight.SpotlightView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Random;

public class record extends AppCompatActivity {
    private String OUTPUT_FILE;
    static  int attempt=0;
    final GetPhrase gp1=new GetPhrase();
    String s=gp1.getphrase();
    TextToSpeech t1;
    private WavAudioRecorder mRecorder;
    String[] descriptionData = {};
    HttpClient httpclient = HttpClients.createDefault();
    HttpPost request;

    public record() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
///storage/emulated/0
        final Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        Random r=new Random();
        //  String rstring=r.toString();
      //  Log.i("som",rstring);
        final ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebut);
        new SpotlightView.Builder(this)
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(32)
                .headingTvText("Tap the Mic")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("To Start/Stop Recording")
                .maskColor(Color.parseColor("#dc000000"))
                .target(findViewById(R.id.togglebut))
                .lineAnimDuration(400)
                .setTypeface(typeface2)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId("e") //UNIQUE ID
                .show();

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

        EditText username=(EditText)findViewById(R.id.entername);  //Take input
        username.setTypeface(typeface2);
      //  username.setHint("Enter Name");

        Button button = (Button) findViewById(R.id.submit);
        button.setTypeface(typeface2);

        mTextView.setText(s);
        mTextView.setTypeface(typeface2);



        final       StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        final RippleView rippleView=(RippleView)findViewById(R.id.more);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // The toggle is DIS - enabled
                   stopRecord();

                    toggle.clearAnimation();

                    if(attempt==1)
                    {
                        stateProgressBar.enableAnimationToCurrentState(true);
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        stateProgressBar.setAnimationDuration(2000);

                    }
                    if(attempt==2)
                    {
                        stateProgressBar.enableAnimationToCurrentState(true);
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        stateProgressBar.setAnimationDuration(2000);
                    }
                    if(attempt==3)
                    {
                      stateProgressBar.setAllStatesCompleted(true);

                    }
                }
                else {
                    // The toggle is ENABLE
                    if(attempt>=3)
                    {
                        attempt=0;
                        stateProgressBar.setAllStatesCompleted(false);

                      stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                        stateProgressBar.enableAnimationToCurrentState(true);
                        stateProgressBar.setAnimationDuration(2000);

                    }
                    toggle.startAnimation(myAnim);
                    ++attempt;
                    Log.i("picu",""+attempt);

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
    /*

    void send() throws URISyntaxException {
        URIBuilder builder=new URIBuilder("http://localhost:8080/voiceAuthenticationService/rest/profile/register");
        URI uri=builder.build();
         request = new HttpPost(uri);
        request.setEntity(new FileEntity(new File(OUTPUT_FILE), ContentType.APPLICATION_OCTET_STREAM));    }
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        Log.i("somyu", entity);
        if(entity!=null)
    {
        //System.out.println(EntityUtils.toString(entity));
        try {
            JSONObject obj=new JSONObject(EntityUtils.toString(entity));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Profile successfully created!\nYour ProfileID is: " + identificationProfileId);

    }
*/
     }


package com.urvika.gola.filvoice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.os.AsyncTask;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.wooplr.spotlight.SpotlightConfig;
import com.wooplr.spotlight.SpotlightView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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
    ProgressDialog progressDialog;
    long totalSize = 0;
     EditText username;
    public record() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
///storage/emulated/0
        final Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        Random r=new Random();
        progressDialog = new ProgressDialog(this);

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
                .usageId("b") //UNIQUE ID
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

        username=(EditText)findViewById(R.id.entername);  //Take input
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
//                    Log.i("picu",""+attempt);

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
                new SubmitPostHandler().execute("");
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

    private class SubmitPostHandler extends AsyncTask<String, Integer, String> {

        private String postMessage;
        @Override
        protected void onPreExecute() {
            //progressBar.setProgress(0);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            /*progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);*/
        }

        @Override
        protected String doInBackground(String... params) {
            postMessage = params[0];
            if(null==postMessage){
                postMessage = "";
            }
            return submitPost();
        }

        @SuppressWarnings("deprecation")
        private String submitPost() {

            String responseString = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.43.68:8080/voiceAuthenticationService/rest/profile/register");
            HttpContext localContext = new BasicHttpContext();
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                entity.addPart("file1", new FileBody(new File(Environment.getExternalStorageDirectory()+"/attempt1.wav")));
                entity.addPart("file2", new FileBody(new File(Environment.getExternalStorageDirectory()+"/attempt2.wav")));
                entity.addPart("file3", new FileBody(new File(Environment.getExternalStorageDirectory()+"/attempt3.wav")));
                entity.addPart("username", new StringBody(username.getText().toString()));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost, localContext);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Problem occurred while submitting the post.";
                    System.out.println("response is 2 :: "+EntityUtils.toString(r_entity));
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Response from server: " + result);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (result != null && !result.equalsIgnoreCase("")) {
                try {
                    JSONObject response = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {

            }
            Intent i = new Intent(record.this,EnrollmentSuccess.class);
            startActivity(i);
            super.onPostExecute(result);
        }
    }
}


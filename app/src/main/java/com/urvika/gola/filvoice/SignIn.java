package com.urvika.gola.filvoice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.prefs.PreferencesManager;
import com.wooplr.spotlight.utils.Utils;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class SignIn extends AppCompatActivity {
    private String OUTPUT_FILE;
    private WavAudioRecorder mRecorder;
    File outfile ;
    ProgressDialog progressDialog;
    long totalSize = 0;
   static public String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        ///storage/emulated/0
        OUTPUT_FILE= Environment.getExternalStorageDirectory()+"/final.wav";
        outfile = new File(OUTPUT_FILE);
        final TextView mTextView = (TextView) findViewById(R.id.loginphrase);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        mTextView.setTypeface(typeface);

        //for resetting the spotlight!
        Context context=getApplicationContext();
        PreferencesManager p=new PreferencesManager(context);
        p.resetAll();
        progressDialog = new ProgressDialog(this);

        final ToggleButton toggle = (ToggleButton) findViewById(R.id.togglebut);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);

        new SpotlightView.Builder(this)
                .introAnimationDuration(400)
                .enableRevealAnimation(true)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(26)
                .headingTvText("Say PassPhrase")
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(16)
                .subHeadingTvText("You Used For Enrollment")
                .maskColor(Color.parseColor("#dc000000"))
                .target(findViewById(R.id.togglebut))
                .lineAnimDuration(400)
                .setTypeface(typeface)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(false)
                .enableDismissAfterShown(false)
                .usageId("hint2") //UNIQUE ID
                .show();




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
    }
    private void stopRecord() {
        mRecorder.stop();
        mRecorder.reset();
        new SubmitPostHandler().execute("");
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
            HttpPost httppost = new HttpPost("http://192.168.43.68:8080/voiceAuthenticationService/rest/profile/login");
            HttpContext localContext = new BasicHttpContext();
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                entity.addPart("file", new FileBody(new File(OUTPUT_FILE)));
            //    entity.addPart("userName", new StringBody(postMessage));
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
                     userName = (String) response.get("username");
                    System.out.println("userName :: "+userName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

            }
             if(userName.equals(""))
             {
                 Intent i = new Intent(SignIn.this, AccessDenied.class);
                 startActivity(i);
             }

            else {
                 Intent i = new Intent(SignIn.this, AuthenticationSuccess.class);
                 startActivity(i);
                 }
                super.onPostExecute(result);
            }
        }
}
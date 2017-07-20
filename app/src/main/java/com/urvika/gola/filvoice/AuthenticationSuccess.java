package com.urvika.gola.filvoice;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticationSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_success);

        TextView myTextView1 = (TextView) findViewById(R.id.text2);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTextView1.setTypeface(typeface1);


        TextView myTextView2 = (TextView) findViewById(R.id.text1);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTextView2.setTypeface(typeface2);

        final TextView mTextView = (TextView) findViewById(R.id.text);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.43.68:8060/voiceAuthenticationService/rest/profile/login";

        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject mainObject1= new JSONObject(response);
              /*  mainObject1.getString("username");
                            Log.i("somyu",mainObject1.getString("username");
              */         /* JSONArray mainObject=new JSONArray(response);

                          mainObject.get(1).toString();
                           mainObject1.toString();*/

                            //mainObject.getString("username");
                            //Log.i("resp",mainObject.getString("username"));
                         //   mTextView.setText("Response is "+ mainObject+"JSON OBJ"+mainObject1+"FiELD"+mainObject.get(0) );
                                 Log.i("SOMYU",mainObject1.toString());
                                  mTextView.setText(""+mainObject1.toString());

                           // mTextView.setText(mainObject1.getString("username"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                      //  mTextView.setText("Response is: "+ response.substring(0,response.length()));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}

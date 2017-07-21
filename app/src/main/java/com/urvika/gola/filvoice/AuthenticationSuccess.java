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
    TextView myTextView1, myTextView2;
    String finalusername=SignIn.userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTextView1 = (TextView) findViewById(R.id.text2);
        myTextView2 = (TextView) findViewById(R.id.text1);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        myTextView2.setTypeface(typeface1);
        myTextView1.setTypeface(typeface1);
        setContentView(R.layout.authentication_success);
        myTextView1.setText("Hi " + finalusername + ", Welcome Back!");

    }

}

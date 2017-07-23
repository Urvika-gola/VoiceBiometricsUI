package com.urvika.gola.filvoice;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class
PhraseListView extends AppCompatActivity {
    ListView listView;
    TextView selectpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final GetPhrase gp=new GetPhrase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.phrase_list_view);

        listView= (ListView) findViewById(R.id.list);
        selectpass=(TextView) findViewById(R.id.selectpass);

        // Defined Array values to show in ListView
        String[] values = new String[] { "I am going to make him an offer he cannot refuse",
                "Houston we have had a problem",
                "My voice is my passport verify me",
                "Apple juice tastes funny after toothpaste",
                "You can get in without your password",
                "You can activate security system now",
                "My voice is stronger than passwords",
                "My password is not your business",
                "My name is unknown to you",
                "Be yourself everyone else is already taken"
        };
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
       selectpass.setTypeface(typeface2);
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                gp.setphrase(itemValue);

                ColorDrawable[] color = {
                        new ColorDrawable(Color.parseColor("#C21F9F")),
                        new ColorDrawable(Color.parseColor("#781FC2"))
                };
                TransitionDrawable trans = new TransitionDrawable(color);
                view.setBackground(trans);
                trans.startTransition(2000); // duration 2 seconds

                // Go back to the default background color of Item
                ColorDrawable[] color2 = {
                        new ColorDrawable(Color.parseColor("#781FC2")),
                        new ColorDrawable(Color.parseColor("#0f3b69"))
                };
                TransitionDrawable trans2 = new TransitionDrawable(color2);
                view.setBackground(trans2);
                trans2.startTransition(2000);
                new java.util.Timer().schedule(
                        new java.util.TimerTask(){
                            public void run(){
                                Intent i = new Intent(PhraseListView.this,record.class);
                                startActivity(i);

                            }
                        },1000
                );
            }});
    }
}

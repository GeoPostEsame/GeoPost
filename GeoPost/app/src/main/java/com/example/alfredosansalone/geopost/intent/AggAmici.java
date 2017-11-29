package com.example.alfredosansalone.geopost.intent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;

public class AggAmici extends AppCompatActivity {

    EditText username;
    String user;
    String risp;
    String url;
    String idsession;
    RequestQueue queue;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agg_amici);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void Aggiungi(View v) {
        Log.d("Sono nella MainActivity", "myTap");
        Intent intent = new Intent(this, AmiciSeguiti.class);
        startActivity(intent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agg_amici);
        username = (EditText)findViewById(R.id.autoCompleteTextView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        idsession = MyModel.getInstance().getIdsession();
    }

    public void Follow(View v) {

        user = username.getText().toString();
        Log.d("Follow", "Id session = "+ idsession);
        Log.d("Follow", "User = "+ user);

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/follow?session_id=" + idsession + "&username=" + user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Follow", "response is " + response);

                        Intent intent = new Intent(AggAmici.this, AmiciSeguiti.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Follow", "response is " + error);
                    }
                });

        queue.add(stringRequest);


    }
}

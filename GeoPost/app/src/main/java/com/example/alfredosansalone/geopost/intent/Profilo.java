package com.example.alfredosansalone.geopost.intent;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;

public class Profilo extends AppCompatActivity {

    MyModel myModel;
    String idsession;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        idsession = myModel.getInstance().getIdsession();
    }

    public void Logout(View v) {

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/logout?session_id=" + idsession;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Logout", "response is " + response);
                        Log.d("Logout", "Logout");
                        Intent intent = new Intent(Profilo.this, Login.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Logout", "That didn't work!");
            }
        });
        queue.add(stringRequest);

    }
}

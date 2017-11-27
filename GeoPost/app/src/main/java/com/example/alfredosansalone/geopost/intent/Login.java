package com.example.alfredosansalone.geopost.intent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.alfredosansalone.geopost.LoginRequest;
import com.example.alfredosansalone.geopost.R;

public class Login extends AppCompatActivity {

    RequestQueue queue;
    String risp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        queue = Volley.newRequestQueue(this);

        //queue.add(LoginRequest);
    }

    public void Login(View v){
        Log.d("Sono nella MainActivity", "myTap");
        Intent intent = new Intent(this, AmiciSeguiti.class);
        startActivity(intent);

    }
}

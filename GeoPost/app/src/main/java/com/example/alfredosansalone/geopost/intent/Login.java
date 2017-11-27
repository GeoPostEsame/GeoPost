package com.example.alfredosansalone.geopost.intent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.alfredosansalone.geopost.LoginRequest;
import com.example.alfredosansalone.geopost.LoginRequest;
import com.example.alfredosansalone.geopost.R;

public class Login extends AppCompatActivity {

    String risp;
    //MySingleton id_se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);
        String user = username.getText().toString();
        String passw = password.getText().toString();


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://ewserver.di.unimi.it/mobicomp/geopost/login";

        //queue.add(LoginRequest);
        LoginRequest loginRequest= new LoginRequest(user, passw, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        risp = response;
                        // Display the first 500 characters of the response string.
                        Log.d("Login", "Response is: "+ risp);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                risp = "That didn't work!";
                Log.d("Login", "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(loginRequest);
    }


    public void Login(View v){
        Log.d("Sono nella MainActivity", "myTap");
        Intent intent = new Intent(this, AmiciSeguiti.class);
        startActivity(intent);

    }
}

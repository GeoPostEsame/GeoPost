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
//import com.example.alfredosansalone.geopost.LoginRequest;
import com.example.alfredosansalone.geopost.LoginRequest;
import com.example.alfredosansalone.geopost.R;

public class Login extends AppCompatActivity {

    EditText username;
    EditText password;
    String user;
    String passw;
    String risp;
    String url;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);




        queue = Volley.newRequestQueue(this);
        url ="https://ewserver.di.unimi.it/mobicomp/geopost/login";

        //queue.add(LoginRequest);

        // Add the request to the RequestQueue.

    }


    public void Login(View v){

        user = username.getText().toString();
        passw = password.getText().toString();
        Log.d("Login", "User = "+ user);
        Log.d("Login", "Password = "+ passw);

        LoginRequest loginRequest= new LoginRequest(user, passw, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        risp = response;
                        // Display the first 500 characters of the response string.
                        Log.d("Login", "Response is: "+ response);
                        Log.d("Login ", "risp = "+ risp);

                        MyModel.getInstance().setIdsession(risp);
                        Log.d("Login", MyModel.getInstance().getIdsession());

                        if(risp != "") {
                            Intent intent = new Intent(Login.this, AmiciSeguiti.class);
                            startActivity(intent);
                        }else{
                            Log.d("Login ", "user o password errati");
                            //alert user o password errati
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                risp = "That didn't work!";
                Log.d("Login", "That didn't work!");
            }
        });

        queue.add(loginRequest);


    }


}

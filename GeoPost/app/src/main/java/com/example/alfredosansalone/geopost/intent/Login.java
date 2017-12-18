package com.example.alfredosansalone.geopost.intent;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
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
    String idsession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        queue = Volley.newRequestQueue(this);
        url ="https://ewserver.di.unimi.it/mobicomp/geopost/login";

    }

    @Override
    protected void onStart() {
        super.onStart();
        idsession = MyModel.getInstance().getIdsession();
        Log.d("Login", "session id "+idsession);
        if(idsession!=null){
            Intent intent = new Intent(Login.this, AmiciSeguiti.class);
            startActivity(intent);
        }
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
                        Log.d("Login", "Response is: "+ response);
                        Log.d("Login ", "risp = "+ risp);

                        MyModel.getInstance().setIdsession(risp);
                        Log.d("Login ass myModel", MyModel.getInstance().getIdsession());

                            Intent intent = new Intent(Login.this, AmiciSeguiti.class);
                            startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                risp = "Nomeutente o Password errate";
                Log.d("Login", risp);

                //Alert per la creazione di messagio per errore di accesso
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setMessage(risp).setTitle("Login Error");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        password.setText("");
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        queue.add(loginRequest);


    }


}

package com.example.alfredosansalone.geopost.intent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        queue = Volley.newRequestQueue(this);
        url ="https://ewserver.di.unimi.it/mobicomp/geopost/login";

        //SharedPreferences per salvare idsession sul dispositivo
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /*SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ID_SESSION", null);
        editor.commit();*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        idsession = MyModel.getInstance().getIdsession();
        Log.d("GeoPost Login", "session id "+idsession);
        String idSharedPref = sharedPref.getString("ID_SESSION", null);
        Log.d("GeoPost Login", "sharedPref = "+ idSharedPref);
        if(idSharedPref != null){
            MyModel.getInstance().setIdsession(idSharedPref);
            Log.d("GeoPost Login", "session id setted with sharedPref = " + MyModel.getInstance().getIdsession());
            Intent intent = new Intent(Login.this, AmiciSeguiti.class);
            startActivity(intent);
        }

        if(idsession!=null){
            Intent intent = new Intent(Login.this, AmiciSeguiti.class);
            startActivity(intent);
        }
    }


    public void Login(View v){

        user = username.getText().toString();
        passw = password.getText().toString();
        Log.d("GeoPost Login", "User = "+ user);
        Log.d("GeoPost Login", "Password = "+ passw);

        LoginRequest loginRequest= new LoginRequest(user, passw, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        risp = response;
                        Log.d("GeoPost Login ", "risp = "+ risp);

                        MyModel.getInstance().setUsername(user);

                        MyModel.getInstance().setIdsession(risp);
                        Log.d("GeoPost Login myModel", MyModel.getInstance().getIdsession());

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("ID_SESSION", risp);
                        editor.commit();

                            Intent intent = new Intent(Login.this, AmiciSeguiti.class);
                            startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("GeoPost Login", error.toString());
                risp = "Nomeutente o Password errate";
                Log.d("GeoPost Login", risp);

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

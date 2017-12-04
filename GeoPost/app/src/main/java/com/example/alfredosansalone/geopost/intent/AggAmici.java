package com.example.alfredosansalone.geopost.intent;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;

public class AggAmici extends AppCompatActivity {

    AutoCompleteTextView username;
    String user;
    String risp;
    String idsession;
    RequestQueue queue;
    //private static String[] USER;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agg_amici);
        username = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);
        /* USER = new String[]{""};
        adapter = new ArrayAdapter<String>(AggAmici.this, android.R.layout.simple_list_item_1, USER);
        username.setAdapter(adapter);*/

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user = username.getText().toString();
                Log.d("user", user);

                String url = "https://ewserver.di.unimi.it/mobicomp/geopost/users?session_id=" + idsession+"&usernamestart="+user+"&limit=7";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.d("user", "response is " + response);
                                String s = response;
                                s=s.substring(14,s.length()-2);
                                Log.d("substring", s);
                                s= s.replace("\"", "");
                                final String[] USER = s.split(",");
                                Log.d("substring", USER.toString());
                                adapter = new ArrayAdapter<String>(AggAmici.this, android.R.layout.simple_list_item_1, USER);
                                username.setAdapter(adapter);
                                //adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("user", "That didn't work!");
                    }
                });

                queue.add(stringRequest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



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
                        Toast.makeText(getApplicationContext(), "Utente seguito", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AggAmici.this, AmiciSeguiti.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Follow", "on error response is " + volleyError);
                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                            volleyError = error;
                            Log.d("Follow", "volleyError is " + volleyError);
                            String errore = volleyError.toString().replace("com.android.volley.VolleyError: ", "");
                            Log.d("Follow", "stringa di errore " + errore);

                            if(errore.equals("ALREADY FOLLOWING USER")){
                                //alert
                                AlertDialog.Builder builder = new AlertDialog.Builder(AggAmici.this);
                                builder.setMessage("Utente gia seguito").setTitle("Follow Error");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        username.setText("");
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            if(errore.equals("CANNOT FOLLOW YOURSELF")){
                                //alert
                                AlertDialog.Builder builder = new AlertDialog.Builder(AggAmici.this);
                                builder.setMessage("Non puoi seguire te stesso").setTitle("Follow Error");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        username.setText("");
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            if(errore.equals("USERNAME NOT FOUND")){
                                //alert
                                AlertDialog.Builder builder = new AlertDialog.Builder(AggAmici.this);
                                builder.setMessage("Utente non trovato").setTitle("Follow Error");

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        username.setText("");
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    }
                });

        queue.add(stringRequest);


    }
}

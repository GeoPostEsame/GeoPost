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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AggAmici extends AppCompatActivity {

    AutoCompleteTextView username;
    String user;
    String myusername;
    String idsession;
    RequestQueue queue;
    ArrayAdapter<String> adapter;
    JSONArray results;
    ArrayList<String> listdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agg_amici);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myusername = MyModel.getInstance().getUsername();

        listdata = new ArrayList<>();
        username = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        adapter = new ArrayAdapter<String>(AggAmici.this, android.R.layout.simple_list_item_1, listdata);
        //username.setAdapter(adapter);


        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user = s.toString();
                String url;
                queue = Volley.newRequestQueue(AggAmici.this);
                Log.d("GeoPost User", user);
                if(user.equals("")) {
                    url = "https://ewserver.di.unimi.it/mobicomp/geopost/users?session_id=" + idsession;
                    Log.d("GeoPost User", "user nullo: "+url);
                }else{
                    url = "https://ewserver.di.unimi.it/mobicomp/geopost/users?session_id=" + idsession + "&usernamestart=" + user + "&limit=7";
                    Log.d("GeoPost User", url);
                }
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d("GeoPost User", "response is " + response);
                                try {

                                    JSONObject risp = new JSONObject(response);
                                    results = risp.getJSONArray("usernames");
                                    Log.d("GeoPost User", "results: " + results);
                                    if(results != null){
                                        listdata.clear();
                                        for(int i = 0; i < results.length(); i++){
                                            String s = results.get(i).toString();
                                            Log.d("GeoPost User", "s= " + s);
                                            if(!s.equals(myusername)) {
                                                listdata.add(s);
                                            }
                                        }
                                    }
                                    adapter = new ArrayAdapter<String>(AggAmici.this, android.R.layout.simple_list_item_1, listdata);
                                    username.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("GeoPost user", "That didn't work!");
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
        Log.d("GeoPost Follow", "Id session = "+ idsession);
        Log.d("GeoPost Follow", "User = "+ user);

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/follow?session_id=" + idsession + "&username=" + user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    //Toast fa uscire piccola text momentanea per feedback
                    public void onResponse(String response) {
                        Log.d("GeoPost Follow", "response is " + response);
                        Toast.makeText(getApplicationContext(), "Utente seguito", Toast.LENGTH_LONG).show();
                        username.setText("");

                        //Intent intent = new Intent(AggAmici.this, AmiciSeguiti.class);
                        //startActivity(intent);
                    }
                    //Gestione errori (Non trovato, Gia amico, Non puoi seguire te stesso)
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Gestione errori Follow
                        Log.d("GeoPost Follow", "on error response is " + volleyError);
                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                            volleyError = error;
                            Log.d("GeoPost Follow", "volleyError is " + volleyError);
                            String errore = volleyError.toString().replace("com.android.volley.VolleyError: ", "");
                            Log.d("GeoPost Follow", "stringa di errore " + errore);

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

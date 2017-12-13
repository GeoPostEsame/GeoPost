package com.example.alfredosansalone.geopost.intent;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;
import com.example.alfredosansalone.geopost.fragment.ListFragment;
import com.example.alfredosansalone.geopost.fragment.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class AmiciSeguiti extends AppCompatActivity {
    String idsession;
    RequestQueue queue;

    //PULSANTE GESTIONE VISTA
    public void Vista(View v){
        ImageButton mappa = (ImageButton) findViewById(R.id.mappa);
        ImageButton lista = (ImageButton) findViewById(R.id.lista);
        if(lista.getVisibility() == View.VISIBLE){
            mappa.setVisibility(View.VISIBLE);
            lista.setVisibility(View.GONE);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content, new ListFragment());
            transaction.commit();
        } else {
            mappa.setVisibility(View.GONE);
            lista.setVisibility(View.VISIBLE);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content, new MapFragment());
            transaction.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amici_seguiti);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new MapFragment());
        transaction.commit();

        queue = Volley.newRequestQueue(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        idsession = MyModel.getInstance().getIdsession();

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/followed?session_id=" + idsession;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Amiciseguiti", "response is " + response);

                        try {
                            JSONObject risp = new JSONObject(response);
                            Log.d("Amiciseguiti", risp.toString());



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Gestione errori
                Log.d("Amiciseguiti", "on error response is " + volleyError);
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                    Log.d("Amiciseguiti", "volleyError is " + volleyError);
                    String errore = volleyError.toString().replace("com.android.volley.VolleyError: ", "");
                    Log.d("Amiciseguiti", "stringa di errore " + errore);
                }
            }
        });
        queue.add(stringRequest);
    }

    // PULSANTE MENU A SCOMPARSA
    public void Menu(View v){
        ImageButton profilo = (ImageButton) findViewById(R.id.profilo);
        ImageButton aggamico = (ImageButton) findViewById(R.id.aggamici);
        if(profilo.getVisibility() == View.INVISIBLE) {
            Log.d("Button", "VISIBLE");
            profilo.setVisibility(View.VISIBLE);
            aggamico.setVisibility(View.VISIBLE);
        }else{
            Log.d("Button", "INVISIBLE");
            profilo.setVisibility(View.INVISIBLE);
            aggamico.setVisibility(View.INVISIBLE);
        }

    }

    // INTENT PER APRIRE NUOVE ACTIVITY
    public void Stato(View v) {
        Intent intent = new Intent(this, AggStato.class);
        startActivity(intent);
    }

    public void Profilo(View v) {

        Intent intent = new Intent(this, Profilo.class);
        startActivity(intent);
    }

    public void AggAmici(View v) {

        Intent intent = new Intent(this, AggAmici.class);
        startActivity(intent);
    }
}

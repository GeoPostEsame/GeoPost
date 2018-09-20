package com.example.alfredosansalone.geopost.intent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class Profilo extends AppCompatActivity implements OnMapReadyCallback {

    MyModel myModel;
    String idsession;
    RequestQueue queue;
    TextView username;
    TextView messaggio;
    double latitudine;
    double longitudine;
    LatLng myPosition;
    GoogleMap mMap = null;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = (TextView)findViewById(R.id.nomeutente);
        messaggio = (TextView)findViewById(R.id.messaggio);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(Profilo.this);

        queue = Volley.newRequestQueue(this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        idsession = myModel.getInstance().getIdsession();
    }

    @Override
    public void onMapReady(GoogleMap map){
        Log.d("GeoPost Location", "Map is ready!");
        mMap = map;

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/profile?session_id=" + idsession;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("GeoPost Profilo", "response is " + response);

                        try {
                            JSONObject risp = new JSONObject(response);
                            Log.d("GeoPost Profilo", risp.toString());

                            Log.d("GeoPost Profilo", "username: "+risp.get("username").toString());

                            String user = risp.get("username").toString();
                            username.setText(user);

                            Log.d("GeoPost Profilo", "messaggio: "+risp.get("msg").toString());
                            String msg = risp.get("msg").toString();
                            if(!(msg.equals("null"))) {
                                messaggio.setText(msg);
                            }else{
                                messaggio.setText("Non hai ancora pubblicato uno stato e non viene visualizzata la posizione");
                            }
                            String lati = risp.get("lat").toString();
                            if(!lati.equals("null")) {
                                Log.d("GeoPost Profilo", "lat: " + risp.get("lat").toString());
                                latitudine = Double.parseDouble(lati);
                                Log.d("GeoPost Profilo", "lon: " + risp.get("lon").toString());
                                longitudine = Double.parseDouble(risp.get("lon").toString());

                                //AGGIUNTO GET E SET POSITION IN MYMODEL
                                myPosition = new LatLng(latitudine, longitudine);

                                MarkerOptions mp1 = new MarkerOptions();
                                mp1.position(myPosition).title("La tua ultima posizione!");

                                mp1.draggable(true);
                                mp1.icon(BitmapDescriptorFactory.fromResource(R.drawable.new_blue_dot50));

                                mMap.addMarker(mp1);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("GeoPost Logout", "That didn't work!");
            }
        });
        queue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                String url = "https://ewserver.di.unimi.it/mobicomp/geopost/logout?session_id=" + idsession;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                Log.d("GeoPost Logout", "response is " + response);
                                Log.d("GeoPost Logout", "Logout");
                                Intent intent = new Intent(Profilo.this, Login.class);
                                startActivity(intent);
                                MyModel.getInstance().setIdsession(null);

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("ID_SESSION", null);
                                editor.commit();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("GeoPost Logout", "That didn't work!");
                    }
                });
                queue.add(stringRequest);
                return true;
        }
        return false;
    }

}

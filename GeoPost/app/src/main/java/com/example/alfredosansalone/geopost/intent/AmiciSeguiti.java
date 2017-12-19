package com.example.alfredosansalone.geopost.intent;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;
import com.example.alfredosansalone.geopost.fragment.ListFragment;
import com.example.alfredosansalone.geopost.fragment.MapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class AmiciSeguiti extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    String idsession;
    RequestQueue queue;

    private static final int LOCATION_PERMISSION = 1;
    private GoogleApiClient mGoogleApiClient = null;
    private boolean googleApiClientReady = false;
    private boolean permissionGranted = false;


    //PULSANTE GESTIONE VISTA
    public void Vista(View v){
        ImageButton mappa = (ImageButton) findViewById(R.id.mappa);
        ImageButton lista = (ImageButton) findViewById(R.id.lista);
        if(lista.getVisibility() == View.VISIBLE){
            mappa.setVisibility(View.VISIBLE);
            lista.setVisibility(View.INVISIBLE);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content, new ListFragment());
            transaction.commit();
        } else {
            mappa.setVisibility(View.INVISIBLE);
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

        //posizione esercizio prof
        //next line checks if user has granted permission to use fine location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)  {
            Log.d("GeoPost Location", "Permission granted");
            permissionGranted = true;
        } else {
            Log.d("GeoPost Location", "Permission NOT granted");
            // we request the permission. When done,
            // the onRequestPermissionsResult method is called
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        idsession = MyModel.getInstance().getIdsession();

        /* String url = "https://ewserver.di.unimi.it/mobicomp/geopost/followed?session_id=" + idsession;
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
        queue.add(stringRequest); */

        // posizione esercizio prof
        //Check if GooglePlayServices are available
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(status == ConnectionResult.SUCCESS) {
            Log.d("GeoPost Location", "GooglePlayServices available");
        } else {
            Log.d("GeoPost Location", "GooglePlayServices UNAVAILABLE");
            if(googleApiAvailability.isUserResolvableError(status)) {
                Log.d("GeoPost Location", "Ask the user to fix the problem");
                //If the user accepts to install the google play services,
                //a new app will open. When the user gets back to this activity,
                //the onStart method is invoked again.
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            } else {
                Log.d("GeoPost Location", "The problem cannot be fixed");
            }
        }

        // Instantiate and connect GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    //override per posizione esercizio prof
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("GeoPost Location", "GoogleApiClient connected");
        googleApiClientReady = true;
        checkAndStartLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GeoPost Location", "GoogleApiClient suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GeoPost Location", "GoogleApiClient failed");
        Toast.makeText(getApplicationContext(), "Unable to start GooglePlayServices.", Toast.LENGTH_LONG).show();
    }

    //The next method is called after the user grants (or not) a permission
    //In our case we only have the fine_location
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        //In our example we only have one permission, but we could have more
        //we use the requestCode to distinguish them
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    checkAndStartLocationUpdate();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AlertDialog.Builder builder = new AlertDialog.Builder(AmiciSeguiti.this);
                    builder.setMessage("Questa app necessita dei permessi di locazione").setTitle("Login Error");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void checkAndStartLocationUpdate() {
        if (permissionGranted && googleApiClientReady) {
            Log.d("GeoPost Location", "Start updating location");
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            } catch (SecurityException e) {
                // this should not happen because the exception fires when the user has not
                // granted permission to use location, but we already checked this
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("GeoPost Location", "Location update received: " + location.toString());


    }

    // PULSANTE MENU A SCOMPARSA
    public void Menu(View v){
        ImageButton profilo = (ImageButton) findViewById(R.id.profilo);
        ImageButton aggamico = (ImageButton) findViewById(R.id.aggamici);
        if(profilo.getVisibility() == View.INVISIBLE) {
            Log.d("GeoPost Button", "VISIBLE");
            profilo.setVisibility(View.VISIBLE);
            aggamico.setVisibility(View.VISIBLE);
        }else{
            Log.d("GeoPost Button", "INVISIBLE");
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

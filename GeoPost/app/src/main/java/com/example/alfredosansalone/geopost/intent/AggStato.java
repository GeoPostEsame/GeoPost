package com.example.alfredosansalone.geopost.intent;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AggStato extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    String idsession;
    RequestQueue queue;
    EditText mex;
    double latitudine;
    double longitudine;
    LatLng myPosition;
    GoogleMap mMap = null;

    private static final int LOCATION_PERMISSION = 1;
    private GoogleApiClient mGoogleApiClient = null;
    private boolean googleApiClientReady = false;
    private boolean permissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agg_stato);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(AggStato.this);

        mex = (EditText)findViewById(R.id.messaggio);
        queue = Volley.newRequestQueue(this);

        //posizione esercizio prof
        //next line checks if user has granted permission to use fine location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)  {
            Log.d("Location", "Permission granted");
            permissionGranted = true;
        } else {
            Log.d("Location", "Permission NOT granted");
            // we request the permission. When done,
            // the onRequestPermissionsResult method is called
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
        }


    }

    @Override
    public void onMapReady(GoogleMap map){
        Log.d("Location", "Map is ready!");
        mMap = map;
    }


    @Override
    protected void onStart() {
        super.onStart();
        idsession = MyModel.getInstance().getIdsession();

        // posizione esercizio prof
        //Check if GooglePlayServices are available
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(status == ConnectionResult.SUCCESS) {
            Log.d("Location", "GooglePlayServices available");
        } else {
            Log.d("Location", "GooglePlayServices UNAVAILABLE");
            if(googleApiAvailability.isUserResolvableError(status)) {
                Log.d("Location", "Ask the user to fix the problem");
                //If the user accepts to install the google play services,
                //a new app will open. When the user gets back to this activity,
                //the onStart method is invoked again.
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            } else {
                Log.d("Location", "The problem cannot be fixed");
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


    public void Pubblica(View v) {
        String messaggio = mex.getText().toString();
        Log.d("Location", "messaggio = "+messaggio);

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/status_update?session_id="+idsession+"&message="+messaggio+"&lat="+latitudine+"&lon="+longitudine;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("AggStato", "response is " + response);
                        Toast.makeText(getApplicationContext(), "Stato aggiornato con successo", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AggStato.this, AmiciSeguiti.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AggStato", "That didn't work!");
                Toast.makeText(getApplicationContext(), "Impossibile aggiornare lo stato", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);

    }

    //override per posizione esercizio prof
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Location", "GoogleApiClient connected");
        googleApiClientReady = true;
        checkAndStartLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Location", "GoogleApiClient suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Location", "GoogleApiClient failed");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(AggStato.this);
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
            Log.d("Location", "Start updating location");
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
        Log.d("Location", "Location update received: " + location.toString());

        latitudine = location.getLatitude();
        longitudine = location.getLongitude();
        myPosition = new LatLng(latitudine, longitudine);
        mMap.addMarker(new MarkerOptions().position(myPosition).title("Marker in myPosition"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));
        Log.d("Location", "lat: "+latitudine+" longi: "+longitudine);

    }
}


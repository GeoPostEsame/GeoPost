package com.example.alfredosansalone.geopost.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;
import com.example.alfredosansalone.geopost.intent.AmiciSeguiti;
import com.example.alfredosansalone.geopost.intent.MyModel;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGMap;
    MapView mMView;
    View mView;
    JSONObject risp;
    String idsession;
    RequestQueue queue;
    Location myLoc;

    //valori per il la visualizzazione dei marker nella mappa
    double maxLat = 0d;
    double maxLon = 0d;
    double minLat = 0d;
    double minLon = 0d;
    double lat = 0d;
    double lon = 0d;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        //Creazione della view mappa
        super.onViewCreated(view, saveInstanceState);

        mMView = mView.findViewById(R.id.map);
        if (mMView != null) {
            mMView.onCreate(null);
            mMView.onResume();
            mMView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap gmap) {
        //Avviso di mappa quando è caricata
        mGMap = gmap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/followed?session_id=" + idsession;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("GeoPost Amiciseguiti", "response is " + response);

                        try {
                            risp = new JSONObject(response);
                            Log.d("GeoPost Amiciseguiti", risp.toString());

                            JSONArray array = risp.getJSONArray("followed");
                            for(int i = 0; i<array.length(); i++) {
                                JSONObject followed = array.getJSONObject(i);
                                if (!followed.get("lat").toString().equals("null")) {
                                    lat = Double.parseDouble(followed.get("lat").toString());
                                    lon = Double.parseDouble(followed.get("lon").toString());
                                    LatLng position = new LatLng(lat, lon);
                                    mGMap.addMarker(new MarkerOptions().position(position).title(followed.get("username").toString()).snippet(followed.get("msg").toString()));

                                    if(minLat == 0 | lat < minLat){
                                        minLat = lat;
                                    }
                                    if(maxLat == 0 | lat > maxLat){
                                        maxLat = lat;
                                    }
                                    if(minLon == 0 | lon < minLon){
                                        minLon = lon;
                                    }
                                    if(maxLon == 0 | lon > maxLon){
                                        maxLon = lon;
                                    }

                                }

                            }
                            Log.d("Valori Min Max", maxLon + " " + maxLat);
                            //Movimento telecamera per centrare utenti!
                            LatLngBounds bounds = new LatLngBounds(new LatLng(minLat-0.5, minLon-0.5), new LatLng(maxLat+0.5, maxLon+0.5));
                            mGMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
                            Log.d("Valori Min Max", "Passo");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Gestione errori
                Log.d("GeoPost Amiciseguiti", "on error response is " + volleyError);
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                    Log.d("GeoPost Amiciseguiti", "volleyError is " + volleyError);
                    String errore = volleyError.toString().replace("com.android.volley.VolleyError: ", "");
                    Log.d("GeoPost Amiciseguiti", "stringa di errore " + errore);
                }
            }
        });
        queue.add(stringRequest);

    }

    @Override
    public void onStart() {
        super.onStart();
        idsession = MyModel.getInstance().getIdsession();
    }
}

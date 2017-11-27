package com.example.alfredosansalone.geopost.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alfredosansalone.geopost.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends Fragment /*implements OnMapReadyCallback*/ {

    GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*View rootView =*/ return inflater.inflate(R.layout.fragment_map, container, false);

        /*SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapFragment.this);*/

        //return rootView;
    }

    /*@Override
    public void onMapReady(GoogleMap map){
        Log.d("Main Activity", "Map is ready!");
        mMap = map;

    }*/
}
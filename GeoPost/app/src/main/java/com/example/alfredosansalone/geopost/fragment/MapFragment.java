package com.example.alfredosansalone.geopost.fragment;

import android.app.FragmentManager;
import android.content.Context;
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
import android.widget.TextView;

import com.example.alfredosansalone.geopost.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGMap;
    MapView mMView;
    View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        Log.d("MainActivity", "Map ready");
        mGMap = gmap;
        gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

}

package com.example.alfredosansalone.geopost.fragment;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by alfredosansalone on 13/12/17.
 */

class Contact implements LocationListener {
    private String user = null;
    private String messaggio = null;
    private String latitudine = null;
    private String longitudine = null;
    double latit;
    double longi;

    public Contact(String user, String messaggio, String latitudine, String longitudine) {
        this.user = user;
        this.messaggio = messaggio;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public String getUser() {
        return user;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public double getLatitudine() {
        return Double.parseDouble(latitudine);
    }

    public double getLongitudine() {
        return Double.parseDouble(longitudine);
    }

    @Override
    public void onLocationChanged(Location location) {
        latit = location.getLatitude();
        Log.d("Contact", " onLocationChanged lat: "+latit);
        longi = location.getLongitude();
        Log.d("Contact", " onLocationChanged lon: "+longi);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public float getDistanza(double lat, double lon){
        float distanza = 0f;
        return distanza;
    }
}

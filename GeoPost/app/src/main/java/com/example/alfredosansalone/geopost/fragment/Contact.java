package com.example.alfredosansalone.geopost.fragment;

import android.location.Location;
import android.util.Log;

import com.example.alfredosansalone.geopost.intent.MyModel;

/**
 * Created by alfredosansalone on 13/12/17.
 */

class Contact implements Comparable<Contact> {
    private String user = null;
    private String messaggio = null;
    private float distance = 0f;


    public Contact(String user, String messaggio, Location l) {
        this.user = user;
        this.messaggio = messaggio;
        Log.d("Distanza", l+"");
        //this.distance = (MyModel.getInstance().getPosition().distanceTo(l))/1000;
        Log.d("Distance", ""+distance);
    }

    public Contact(String u, String m){
        user = u;
        messaggio = m;
        distance = 0;

    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getUser() {
        return user;
    }

    public String getMessaggio() {
        return messaggio;
    }

    @Override
    public int compareTo(Contact c){
        return Float.compare(distance, c.distance);
    }


}

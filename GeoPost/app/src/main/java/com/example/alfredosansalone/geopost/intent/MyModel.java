package com.example.alfredosansalone.geopost.intent;

import android.location.Location;

/**
 * Created by alfredosansalone on 27/11/17.
 */

public class MyModel {
    private static final MyModel ourInstance = new MyModel();

    private String idsession;
    private String username;
    private Location position;

    public static MyModel getInstance() {
        return ourInstance;
    }

    private MyModel() {
        idsession=null;
    }

    public void setIdsession(String value){
        idsession = value;
    }

    public void setUsername(String user) {
        username = user;
    }

    public void setPosition(Location p){
        position = p;
    }

    public Location getPosition(){
        return position;
    }

    public String getIdsession(){
        return idsession;
    }
    public String getUsername(){
        return username;
    }


}

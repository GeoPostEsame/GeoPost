package com.example.alfredosansalone.geopost.intent;

/**
 * Created by alfredosansalone on 27/11/17.
 */

public class MyModel {
    private static final MyModel ourInstance = new MyModel();

    private String idsession;
    private double latid;
    private double longi;

    public static MyModel getInstance() {
        return ourInstance;
    }

    private MyModel() {
        idsession=null;
    }

    public void setIdsession(String value){
        idsession = value;
    }

    public void setLatidMe(double lat) { latid = lat; }

    public void setLongiMe(double lon) { longi = lon; }

    public String getIdsession(){
        return idsession;
    }

    public double getLatidMe() { return latid;}

    public double getLongiMe() { return longi;}

}

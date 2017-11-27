package com.example.alfredosansalone.geopost.intent;

/**
 * Created by alfredosansalone on 27/11/17.
 */

class MyModel {
    private static final MyModel ourInstance = new MyModel();

    private String idsession;

    static MyModel getInstance() {
        return ourInstance;
    }

    private MyModel() {
        idsession=null;
    }

    public void setIdsession(String value){
        idsession = value;
    }

    public String getIdsession(){
        return idsession;
    }

}

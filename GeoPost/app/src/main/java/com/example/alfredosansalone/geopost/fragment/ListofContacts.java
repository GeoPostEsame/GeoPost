package com.example.alfredosansalone.geopost.fragment;

/**
 * Created by alfredosansalone on 13/12/17.
 */

import java.util.ArrayList;


public class ListofContacts extends ArrayList<Contact> {

    public ListofContacts() {
        super();
        this.add(new Contact("stefano", "frecc","45.473968", "9.200301"));
        this.add(new Contact("Stefano", "Ciao Alfredo tutto bene? Salutami Leo","45.4720467", "9.1929367"));
        this.add(new Contact("carlo", "bestiaaa","45.454384", "9.2147808"));

    }
}
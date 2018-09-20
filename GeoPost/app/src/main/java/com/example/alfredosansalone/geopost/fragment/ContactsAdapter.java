package com.example.alfredosansalone.geopost.fragment;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alfredosansalone.geopost.R;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {

    //devo definire questi due costruttori
    public ContactsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ContactsAdapter(Context context, int resource, List<Contact> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_element, null);
        }

        Contact p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.user);
            TextView tt2 = (TextView) v.findViewById(R.id.messaggio);
            TextView tt3 = (TextView) v.findViewById(R.id.distanza);

            if (tt1 != null) {
                tt1.setText(p.getUser());
            }

            if (tt2 != null) {
                if(p.getMessaggio() != null){
                    tt2.setText(p.getMessaggio());
                }else{
                    tt2.setText("non è stato ancora inserito alcun messaggio");
                }

            }

            if (tt3 != null) {
                if(p.getDistance() != 0){

                    tt3.setText(String.valueOf(p.getDistance())+" Km");
                }else{
                    tt3.setText("posizione sconosciuta");
                }
            }
        }
        return v;
    }

}
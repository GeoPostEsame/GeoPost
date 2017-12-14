package com.example.alfredosansalone.geopost.fragment;

/**
 * Created by alfredosansalone on 13/12/17.
 */

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
        View v = convertView;  if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_element, null);
        }
        Contact p = getItem(position);
        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.user);
            TextView tt2 = (TextView) v.findViewById(R.id.distanza);
            TextView tt3 = (TextView) v.findViewById(R.id.messaggio);
            tt1.setText(p.getUser());
            float distanza = p.getDistanza(p.getLatitudine(), p.getLongitudine());
            String s = distanza+"";
            tt2.setText(s);
            tt3.setText(p.getMessaggio());
        }
        return v;
    }

}
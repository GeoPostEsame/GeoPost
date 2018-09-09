package com.example.alfredosansalone.geopost.fragment;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alfredosansalone.geopost.R;
import com.example.alfredosansalone.geopost.intent.MyModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ListFragment extends Fragment {

    RequestQueue queue;
    ArrayList<Contact> users;
    JSONArray follarr;
    View mView;
    ContactsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, container, false);
        users = new ArrayList<>();

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());


        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/followed?session_id=" + MyModel.getInstance().getIdsession();

        StringRequest followed = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject followed = new JSONObject(response);
                            follarr = followed.getJSONArray("followed");
                            if(follarr != null){
                                for(int i = 0; i < follarr.length(); i++) {
                                    JSONObject obj = new JSONObject(follarr.get(i).toString());
                                    Log.d("utente", obj.toString());
                                    Location l = new Location(LocationManager.GPS_PROVIDER);
                                    String m = null;
                                    String u = obj.get("username").toString();
                                    if (!obj.get("msg").toString().equals("null")) {
                                        m = obj.get("msg").toString();
                                    }
                                    if (!obj.get("lat").toString().equals("null")) {
                                        l.setLatitude(Double.parseDouble(obj.get("lat").toString()));
                                        l.setLongitude(Double.parseDouble(obj.get("lon").toString()));
                                        Contact user = new Contact(u, m, l);
                                        users.add(user);
                                    }
                                    else{
                                        Contact user = new Contact(u, m);
                                        users.add(user);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Collections.sort(users);
                        ListView userList = (ListView) mView.findViewById(R.id.list);
                        adapter = new ContactsAdapter(getActivity(), android.R.layout.simple_list_item_1, users);
                        userList.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MainActivity", "That didn't work!");
            }
        });

        queue.add(followed);

        return mView;

    }

}

package com.example.alfredosansalone.geopost.fragment;


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

public class ListFragment extends Fragment {

    JSONObject risp;
    String idsession;
    View mView;
    RequestQueue queue;
    String user;
    String msg;
    double lat = 0d;
    double lon = 0d;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, container, false);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        idsession = MyModel.getInstance().getIdsession();

        final ListView userList = (ListView) mView.findViewById(R.id.list);

        final ListofContacts contacts = new ListofContacts();

        String url = "https://ewserver.di.unimi.it/mobicomp/geopost/followed?session_id=" + idsession;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("GeoPost ListFragment", "response is " + response);

                        try {
                            risp = new JSONObject(response);
                            Log.d("GeoPost ListFragment", risp.toString());

                            JSONArray array = risp.getJSONArray("followed");
                            for(int i = 0; i<array.length(); i++) {
                                JSONObject followed = array.getJSONObject(i);
                                if (followed.get("lat").toString() != "null") {
                                    user = followed.get("username").toString();
                                    Log.d("GeoPost ListFragment", user);
                                    msg = followed.get("msg").toString();
                                    lat = Double.parseDouble(followed.get("lat").toString());
                                    lon = Double.parseDouble(followed.get("lon").toString());
                                    contacts.add(new Contact(user, msg, lat+"", lon+""));
                                    Log.d("GeoPost ListFragment", "contacts "+contacts.toString());

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ContactsAdapter myAdapter = new ContactsAdapter(getActivity(), android.R.layout.simple_list_item_1, contacts);

                        userList.setAdapter(myAdapter);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Gestione errori
                Log.d("GeoPost ListFragment", "on error response is " + volleyError);
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                    Log.d("GeoPost ListFragment", "volleyError is " + volleyError);
                    String errore = volleyError.toString().replace("com.android.volley.VolleyError: ", "");
                    Log.d("GeoPost ListFragment", "stringa di errore " + errore);
                }
            }
        });
        queue.add(stringRequest);
        //String[] users = {"Anna", "Beatrice", "Carmine"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, users);
        //userList.setAdapter(adapter);


        Log.d("GeoPost ListFragment", "contacts "+contacts.toString());
        //ContactsAdapter myAdapter = new ContactsAdapter(getActivity(), android.R.layout.simple_list_item_1, contacts);

        //userList.setAdapter(myAdapter);
    }
}

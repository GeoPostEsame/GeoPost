package com.example.alfredosansalone.geopost;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alfredosansalone on 20/11/17.
 */

public class LoginRequest extends StringRequest {
    private Map<String, String> mParams;

    public LoginRequest(String username, String password, String url, Response.Listener<String> listener, Response.ErrorListener errorListener ) {
        super(Request.Method.POST, url, listener, errorListener);
        mParams = new HashMap<String, String>();
        mParams.put("username", username);
        mParams.put("password", password);
    }

    @Override
    public Map<String, String> getParams(){
        return mParams;
    }
}

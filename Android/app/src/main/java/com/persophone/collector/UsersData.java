package com.persophone.collector;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by Michael Goldenberg on 18/08/2017.
 */

public class UsersData {

    public UsersData(){

    }

    public void SaveUserPreferences(JSONObject data, final Response.Listener<JSONObject> callback){
        int userid = 10;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/users/userPreferences");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, baseURL, data, callback, null);

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }



    public void GetUserPreferences(final Response.Listener<JSONArray> callback) throws MalformedURLException {
        // Create URL
        int userid = 10;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/users/userPreferences");
        String parameters = new String("user=" + userid);

        URL reqUrl = new URL(baseURL + "?" + parameters);

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, reqUrl.toString(), null, callback, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }

}
package com.persophone.collector;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by Michael Goldenberg on 18/08/2017.
 */

public class PhonesData {

    public PhonesData(){

    }

    public void GetAllPhonesData(final Response.Listener<JSONArray> callback){

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, RequestHandler.URL_APP_SERVER + "/phonesList", null, callback, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }

    public void GetPhonesImage(String url, final ImageLoader.ImageListener listener){
        RequestHandler.getInstance(null).getImageLoader().get(url, listener);
    }

    public void GetRecomendedPhones(final Response.Listener<JSONArray> callback){

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, RequestHandler.URL_APP_SERVER + "/recommendedPhones", null, callback, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }

    public JSONObject CalculateImprovmentFromCurrentPhone(Bundle args){
        JSONObject improvement =  new JSONObject();

        try {
            improvement.put("ram_improvement",10);
            improvement.put("storage_improvement",10 + new Random().nextInt(30));
            improvement.put("battery_improvement",10 + new Random().nextInt(50));
            improvement.put("screen_size_improvement",10 + new Random().nextInt(90));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return improvement;
    }
}

package com.persophone.collector;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Michael Goldenberg on 18/08/2017.
 */

public class PhonesData {

    public PhonesData(){
        int deb = 2;
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
}

package com.persophone.app;

import android.icu.lang.UScript;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.persophone.app.RequestHandler;

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

        Uri uri = new Uri.Builder()
                .scheme(RequestHandler.SERVER_SCHEME)
                .authority(RequestHandler.SERVER_AUTH)
                .path("phones/phonesList")
                .build();

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, uri.toString(), null, callback, new Response.ErrorListener() {

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

        Uri uri = new Uri.Builder()
                .scheme(RequestHandler.SERVER_SCHEME)
                .authority(RequestHandler.SERVER_AUTH)
                .path("phones/recommendedPhones")
                .appendQueryParameter("user", Integer.toString(UsersData.CurrentUserId))
                .appendQueryParameter("phone_name", UsersData.CurrentUserDevDetails.GetDeviceName())
                .build();

        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, uri.toString(), null, callback, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }

    public void GetPhoneRates(String phone_name, final Response.Listener<JSONObject> callback){

        Uri uri = new Uri.Builder()
                .scheme(RequestHandler.SERVER_SCHEME)
                .authority(RequestHandler.SERVER_AUTH)
                .path("phones/phoneRates")
                .appendQueryParameter("phone_name", phone_name)
                .build();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, callback, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }



    private int calcParamImprovment(double param1, double param2){
        double improvement = param1 / param2;
        if (improvement > 1){
            improvement = (int)((improvement - 1) * 100);
        }
        else {
            improvement = 0;
        }

        return (int)improvement;
    }

    public JSONObject CalculateImprovmentFromCurrentPhone(Bundle args){
        JSONObject improvement =  new JSONObject();

        try {
            improvement.put("ram_improvement",calcParamImprovment(Double.parseDouble(args.getString("ram")),UsersData.CurrentUserDevDetails.GetRamValue()));
            improvement.put("cpu_cores_improvement",calcParamImprovment(Double.parseDouble(args.getString("cpu_cores")),UsersData.CurrentUserDevDetails.GetCpuCoresAmount()));
            improvement.put("storage_improvement",calcParamImprovment(Double.parseDouble(args.getString("storage")),UsersData.CurrentUserDevDetails.GetTotalStorageValue()));
            String battertFromArgs = args.getString("battery");
            double batterCapacityFromArgs = Double.parseDouble(battertFromArgs.substring(0,battertFromArgs.indexOf("m")));
            improvement.put("battery_improvement",calcParamImprovment(batterCapacityFromArgs,UsersData.CurrentUserDevDetails.GetBatteryCapacityValue()));
            improvement.put("screen_size_improvement",calcParamImprovment(Double.parseDouble(args.getString("screen_size")),UsersData.CurrentUserDevDetails.GetScreenInches()));

            //improvement.put("screen_res_improvement",calcParamImprovment(Double.parseDouble(args.getString("screen_resolution")),UsersData.CurrentUserDevDetails.GetScreenInches()));
            improvement.put("screen_res_improvement",15);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return improvement;
    }
}

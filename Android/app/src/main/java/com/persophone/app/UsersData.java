package com.persophone.app;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Michael Goldenberg on 18/08/2017.
 */

public class UsersData {

    public static int CurrentUserId;
    public static DevDetails CurrentUserDevDetails;

    public UsersData(){

    }

    public void SaveUserPreferences(JSONObject data, final Response.Listener<JSONObject> callback){
        int userid = CurrentUserId;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/users/userPreferences");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, baseURL, data, callback, null);

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }



    public void GetUserPreferences(final Response.Listener<JSONArray> callback) throws MalformedURLException {
        // Create URL
        int userid = CurrentUserId;
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

    public  void LogCurrentUserCameraUse(){
        int userid = CurrentUserId;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/phone_usage/logCameraUse");
        JSONObject data = new JSONObject();

        try {
            data.put("user",CurrentUserId);
            data.put("phone_name",CurrentUserDevDetails.getDeviceName());
            data.put("camera",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, baseURL, data, null, null);

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }


    public void SaveUserRates(JSONObject data, final Response.Listener<JSONObject> callback){
        int userid = CurrentUserId;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/users/userRates");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, baseURL, data, callback, null);

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }

    public void GetUserRates(final Response.Listener<JSONArray> callback) throws MalformedURLException {
        // Create URL
        int userid = CurrentUserId;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/users/userRates");
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

    public void GetUserBatteryGrade(final  Response.Listener<JSONObject> callback) throws MalformedURLException {
        // Create URL
        int userid = UsersData.CurrentUserId;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/users/batteryGrade");
        String parameters = new String("user=" + userid + "&phone_name=" + "Unknown");

        URL reqUrl = new URL(baseURL + "?" + parameters);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
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
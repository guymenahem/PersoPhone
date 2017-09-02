package com.persophone.app;

import android.net.Uri;

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
        Uri uri = new Uri.Builder()
                .scheme(RequestHandler.SERVER_SCHEME)
                .authority(RequestHandler.SERVER_AUTH)
                .path("users/userPreferences")
                .appendQueryParameter("user", Integer.toString(UsersData.CurrentUserId))
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

    public  void LogCurrentUserCameraUse(int numOfPhotos){
        int userid = CurrentUserId;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/phones_usage/logCameraUse");
        JSONObject data = new JSONObject();

        try {
            data.put("user",CurrentUserId);
            data.put("phone_name",CurrentUserDevDetails.GetDeviceName());
            data.put("camera",numOfPhotos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, baseURL, data, null, null);

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }

    public  void NewUser(final Response.Listener<JSONObject> callback){
        int userid = CurrentUserId;
        String baseURL = new String(RequestHandler.URL_APP_SERVER + "/users/newUser");

        JSONObject data = new JSONObject();

        try {
            data.put("user_name","MMM");
            data.put("phone_name",CurrentUserDevDetails.GetDeviceName());
            data.put("email","somemail@gmail.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, baseURL, data, callback, null);

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
        Uri uri = new Uri.Builder()
                .scheme(RequestHandler.SERVER_SCHEME)
                .authority(RequestHandler.SERVER_AUTH)
                .path("users/userRates")
                .appendQueryParameter("user", Integer.toString(UsersData.CurrentUserId))
                .appendQueryParameter("phone", UsersData.CurrentUserDevDetails.GetDeviceName())
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

    public void GetUserBatteryGrade(final  Response.Listener<JSONObject> callback) throws MalformedURLException {
        Uri uri = new Uri.Builder()
                .scheme(RequestHandler.SERVER_SCHEME)
                .authority(RequestHandler.SERVER_AUTH)
                .path("users/batteryUsageGrade")
                .appendQueryParameter("user", Integer.toString(UsersData.CurrentUserId))
                .appendQueryParameter("phone_name", UsersData.CurrentUserDevDetails.GetDeviceName())
                .build();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, callback, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }

    public void GetAllGrades(final  Response.Listener<JSONObject> callback) throws MalformedURLException {
        // Create URL
        Uri uri = new Uri.Builder()
                .scheme(RequestHandler.SERVER_SCHEME)
                .authority(RequestHandler.SERVER_AUTH)
                .path("users/getAllGrades")
                .appendQueryParameter("user", Integer.toString(UsersData.CurrentUserId))
                .appendQueryParameter("phone_name", UsersData.CurrentUserDevDetails.GetDeviceName())
                .build();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, callback, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }

    public void GetBatteryUsageGraph(final  Response.Listener<JSONObject> callback) throws MalformedURLException {
        // Create URL
        Uri uri = new Uri.Builder()
                .scheme(RequestHandler.SERVER_SCHEME)
                .authority(RequestHandler.SERVER_AUTH)
                .path("users/batteryUsageGraph")
                .appendQueryParameter("user", Integer.toString(UsersData.CurrentUserId))
                .appendQueryParameter("phone_name", UsersData.CurrentUserDevDetails.GetDeviceName())
                .build();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, callback, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestHandler.getInstance(null).addToRequestQueue(jsObjRequest);
    }
}
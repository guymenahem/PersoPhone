package com.persophone.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Response;
import com.persophone.collector.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Guy on 8/19/2017.
 */

public class UserPref {

    // Files and keys
    private String PREF_FILE_NAME = "UserPrefFile";
    private String USER_ID_KEY = "USERID";

    // Values
    private int userid = -1;

    // Activity
    private SharedPreferences settings;

    public UserPref(Context con){
        this.settings = con.getSharedPreferences(PREF_FILE_NAME, 0);

        this.userid = this.settings.getInt(USER_ID_KEY, -999);

        if (this.userid == -999){
            Logger.writeToErrorLog("Get New USER ID");
            getNewUserID();
        }else{
            UsersData.CurrentUserId = this.userid;
        }
    }

    private void getNewUserID(){
        final UserPref _this = this;
        // TODO : ADD query from API
        new UsersData().NewUser(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    _this.userid = response.getInt("user_id");
                    UsersData.CurrentUserId = _this.userid;
                    SharedPreferences.Editor editor = _this.settings.edit();
                    editor.putInt(USER_ID_KEY, _this.userid);
                    editor.commit();
                    Logger.writeToErrorLog("USER ID : " + _this.userid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getUserID(){
        return this.userid;
    }

}

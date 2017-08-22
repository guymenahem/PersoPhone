package com.persophone.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.persophone.collector.Logger;

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
            this.userid = this.getNewUserID();
            SharedPreferences.Editor editor = this.settings.edit();
            editor.putInt(USER_ID_KEY, this.userid);
            editor.commit();

        }
    }

    private int getNewUserID(){
        // TODO : ADD query from API
        return 10;
    }

    public int getUserID(){
        return this.userid;
    }

}

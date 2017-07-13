package com.persophone.collector;

import android.util.Log;

/**
 * Created by Guy on 14/03/2017.
 */

public class Logger {

    public static void writeToErrorLog(String str) {

        Log.e("PERSO-PHONE",str);
    }

    public static void writeToDebugLog(String str) {

        Log.d("PERSO-PHONE",str);
    }
}

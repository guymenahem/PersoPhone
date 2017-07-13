package com.persophone.collector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.persophone.app.MainActivity;

/**
 * Created by Guy on 09/06/2017.
 */



public class ProcessStarter extends BroadcastReceiver {

    public static final int PROC_INTERVAL = 120000;
    public static boolean alarmSet = false;

    public ProcessStarter(){
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        /*Intent i = new Intent(context,CollectorService.class);
        context.startService(i);*/


        Logger.writeToDebugLog("Try to set alarm after boot");

        if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {

            Logger.writeToDebugLog("Alarm set after boot");
            Intent i = new Intent(context, CollectorService.class);
            AlarmManager alm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
            alm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), PROC_INTERVAL, pi);

            alarmSet = true;
        }
    }

}

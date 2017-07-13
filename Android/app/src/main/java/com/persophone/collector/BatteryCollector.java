package com.persophone.collector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Guy on 14/03/2017.
 */

public class BatteryCollector {

    private int lastBattery;

    public BatteryCollector(){
        this.lastBattery = -1;
    }

    protected void collectBatteryStatues(Context context) {

        // get Intent
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // get battery precent
        int level = batteryStatus.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;

        //return value
        this.lastBattery = (int)(batteryPct*100);
    }

    protected int getBatteryStatues(){
        return this.lastBattery;
    }
}

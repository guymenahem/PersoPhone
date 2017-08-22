package com.persophone.collector;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;

/**
 * Created by m on 22/08/2017.
 */

public class CameraCollector {

        private int lastBattery;

        public CameraCollector(){
            this.lastBattery = -1;
        }

        protected void collectCameraStatus(Context context) {

            Camera cam = new Camera();
            //CameraManager.AvailabilityCallback
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

        protected int getCameraStatues(){
            return this.lastBattery;
        }

}

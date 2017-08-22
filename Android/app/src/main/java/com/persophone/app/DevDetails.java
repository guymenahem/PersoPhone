package com.persophone.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

import java.io.RandomAccessFile;

/**
 * Created by Guy on 8/21/2017.
 */
public class DevDetails {

    // Device details
    private String devName;
    private String mainName;
    private double batteryCap;
    private int devHeight;
    private int devWidth;
    private int cpuCores;
    private int cpuFreq;

    private Activity actContext;

    public DevDetails(Activity actContext){
        this.actContext = actContext;
        this.getUserDeviceDetails();
    }

    private void getUserDeviceDetails(){
        // Name
        String dev = this.getDeviceName();
        this.mainName = dev.substring(0, dev.indexOf(" "));
        this.devName = dev.substring(dev.indexOf(" "));

        // Battery
        this.batteryCap = this.getBatteryCapacity();

        // Num of cores
        this.cpuCores = Runtime.getRuntime().availableProcessors();

        // Screen size
        this.fillScreenSize();

        // CPU
        this.fillCPUDetails();
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public double getBatteryCapacity() {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        double batteryCapacity = -1f;

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this.actContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;
    }

    private void fillScreenSize(){
        // Screen size
        Display display = this.actContext.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.devHeight = size.y;
        this.devWidth = size.x;
    }

    private void fillCPUDetails(){
        // Proc freq
        int tempFreq = 0;
        try {
            RandomAccessFile reader = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
            tempFreq = Integer.parseInt(reader.readLine());
            reader.close();
        }
        catch (Exception e){}

        this.cpuFreq = tempFreq/1000;
    }
}

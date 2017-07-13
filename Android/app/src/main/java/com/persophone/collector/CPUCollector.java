package com.persophone.collector;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * Created by Guy on 14/03/2017.
 */

public class CPUCollector {

    // hist
    float usage = 0;
    long histTotal = 0;
    long histIdle = 0;
    String saveUsage;

    int currentIdlePrc = 100;

    public CPUCollector(){
    }

    protected void collectIntervalCpuStats(){

        // interval + sample variables
        long intTotal = 0;
        long intIdle = 0;
        long sampTotal = 0;
        long sampIdle = 0;

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();

            String[] toks = load.split(" ");


            sampTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]) + Long.parseLong(toks[5]);
            sampIdle = Long.parseLong(toks[5]);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        // calculate interval values
        intTotal = sampTotal - histTotal;
        intIdle = sampIdle - histIdle;

        // save history variables
        histTotal = sampTotal;
        histIdle = sampIdle;

        double idle = ((double)intIdle/(double)intTotal)*100.0f;
        DecimalFormat df = new DecimalFormat("##.##");
        Logger.writeToDebugLog("CPU - interval total : " + intTotal + " idle presentage:" + df.format(idle));
        this.currentIdlePrc =  (int)idle;
    }

    protected int getCurrentIdlePrc(){
        return this.currentIdlePrc;
    }


}

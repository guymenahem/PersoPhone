package com.persophone.collector;

import java.text.DecimalFormat;

/**
 * Created by Guy on 14/03/2017.
 */

public class ApplicationData {
    protected String applicationName;
    protected long usage;
    protected float percUsage;

    public static DecimalFormat dfPerc = new DecimalFormat("##.##");

    public ApplicationData(String applicationName, long usage, float preUsage){
        this.applicationName = applicationName;
        this.usage = usage;
        this.percUsage = preUsage;
    }
    public ApplicationData(String compactString){
        String arr[] = compactString.split("\\|");

        this.applicationName = arr[0].split(":")[1];
        this.usage = Integer.parseInt(arr[1].split(":")[1]);
        this.percUsage = Float.parseFloat(arr[2].split(":")[1]);
    }

    public void setName(String name){
        this.applicationName = name;
    }
    public String getName(){
        return this.applicationName;
    }
    public void setUsage(int usage){
        this.usage = usage;
    }
    public long getUsage() {
        return this.usage;
    }
    public void setPreUsage(float preUsage){
        this.percUsage = preUsage;
    }
    public float getPreUsage() {
        return this.percUsage;
    }

    public String getCompactString(){
        return "app:" + this.applicationName+ "|time:" + this.usage + "|userP:" + dfPerc.format(this.percUsage) + "+";
    }

    public String getUserViewString(){

        return "app: " + String.format("%-15s",this.applicationName) + " time: " +
                String.format("%-8s",this.usage) +
                " USER%: " + String.format("%-6s",dfPerc.format(this.percUsage)) + ",";
    }
}

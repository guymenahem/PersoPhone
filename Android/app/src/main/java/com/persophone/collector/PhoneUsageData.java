package com.persophone.collector;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Guy on 14/03/2017.
 */

public class PhoneUsageData {

    // battery stats
    protected int battery;

    // CPU stats
    protected int idleTime;

    // Storage stats
    protected long freeStorage;
    protected long totalStorage;

    // Apps stats
    protected ArrayList<ApplicationData> applications = new ArrayList<ApplicationData>();

    // battery Methods
    public void setBattey(int battery){
        this.battery = battery;
    }
    public int getBattery(){
        return this.getBattery();
    }

    // CPU Methods
    public void setIdleTime(int idleTime){
        this.idleTime = idleTime;
    }
    public int getIdleTime(){
        return this.idleTime;
    }

    // Storage Methods
    public void setFreeStorage(long freeStorage){this.freeStorage = freeStorage;}
    public long getFreeStorage(){return this.freeStorage;}
    public void setTotalStorage(long totalStorage){this.totalStorage = totalStorage;};
    public long getTotalStorage(){return totalStorage;}

    // Apps methods
    public ArrayList<ApplicationData> getApplicationData(){
        return this.applications;
    }
    public void setAppsByString(String appString){
        String arr[];
        arr = appString.split(" ");
        this.applications.clear();

        for(String str : arr){
            this.applications.add(new ApplicationData(str));
        }
    }


    public String getURLData(){

        String parameters = new String("");

        parameters += "&battery=" + this.battery;
        parameters += "&idle=" + this.idleTime;
        parameters += "&appsuse=";

        for(ApplicationData app : this.applications) {
            parameters += app.getCompactString();
        }

        return parameters;
    }

    public String getUserView() {
        String userData = new String("Battery :" + this.battery+ "\nidle=" + this.idleTime+"\n" + "Storage: free" + this.freeStorage + "GB from " + this.totalStorage + "GB \n");

        for(ApplicationData app : this.applications){
            userData += app.getUserViewString() + "\n";
        }

        return userData;
    }

    public JSONObject getJSONObgect(){
        JSONObject obj = new JSONObject();

        try{
            obj.put(DBUpdater.USER_PARAM,DBUpdater.USER_ID);
            obj.put(DBUpdater.BATTERY_PARAM,Integer.toString(this.battery));
            obj.put(DBUpdater.IDLE_PARAM,Integer.toString(this.idleTime));
            obj.put(DBUpdater.STOR_PARAM,Integer.toString((int)this.totalStorage));
            obj.put(DBUpdater.APP_PARAM,this.getApplicationCompact());
        }catch (Exception e){}

        return obj;
    }

    public void updateMapOfValues(Map<String,String> map){
        map.clear();

        map.put(DBUpdater.USER_PARAM,"555");
        map.put(DBUpdater.BATTERY_PARAM,Integer.toString(this.battery));
        map.put(DBUpdater.IDLE_PARAM,Integer.toString(this.idleTime));
        map.put(DBUpdater.STOR_PARAM,Integer.toString((int)this.totalStorage));
        map.put(DBUpdater.APP_PARAM,this.getApplicationCompact());
    }

    private String getApplicationCompact(){
        String s = new String();

        for (int i = 0;i<this.applications.size();i++){
            s += this.applications.get(i).getCompactString();
        }

        return s;
    }
}

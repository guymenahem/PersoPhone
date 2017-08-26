package com.persophone.collector;

import com.persophone.app.DevDetails;
import com.persophone.app.MainActivity;
import com.persophone.app.UsageFragment;
import com.persophone.app.UsersData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

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

    protected  int camera;
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
        parameters += "&free_storage=" + this.freeStorage;
        parameters += "&total_storage=" + this.totalStorage;
        parameters += "&camera=" + new Random().nextInt(2);

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
            obj.put(DBUpdater.USER_PARAM,UsersData.CurrentUserId);
            obj.put(DBUpdater.PHONE_NAME_PARAM, UsersData.CurrentUserDevDetails.GetDeviceName());
            obj.put(DBUpdater.BATTERY_PARAM,Integer.toString(this.battery));
            obj.put(DBUpdater.IDLE_PARAM,Integer.toString(this.idleTime));
            obj.put(DBUpdater.FREE_STOR_PARAM,Integer.toString((int)this.freeStorage));
            obj.put(DBUpdater.STOR_PARAM,Integer.toString((int)this.totalStorage));
            obj.put(DBUpdater.APP_PARAM,this.getApplicationCompact());
            obj.put(DBUpdater.CAMERA_PARAM, 2);
        }catch (Exception e){}

        return obj;
    }

    public void updateMapOfValues(Map<String,String> map){
        map.clear();

        map.put(DBUpdater.USER_PARAM,Integer.toString(UsersData.CurrentUserId));
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

package com.persophone.collector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Guy on 09/06/2017.
 */

public class CollectorService extends Service {

    CPUCollector cpuCollector = new CPUCollector();
    BatteryCollector batteryCollector = new BatteryCollector();
    PhoneUsageData phoneUsageData = new PhoneUsageData();
    ApplicationCollector applicationCollector = new ApplicationCollector(this.phoneUsageData.getApplicationData());
    StorageCollector storageCollector = new StorageCollector();

    DBUpdater DBUpdater;

    public CollectorService(){
        // for setting values to default before start
        this.cpuCollector.collectIntervalCpuStats();
        Logger.writeToErrorLog("CTOR");
    }

    public int onStartCommand (Intent intent, int flags, int startId) {

        // android.os.Debug.waitForDebugger();

        this.collect();
        this.send();

        this.stopSelf();
        return START_NOT_STICKY;
    }

    private void send(){

        if(!DBUpdater.getInstance().isConnectionSet()){
            DBUpdater.getInstance().setQueueAndConnection(this);
        }

        DBUpdater.getInstance().saveData1();
    }

    /*private void send(){

        Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        // Create URL
                        int userid = 100;
                        String baseURL = new String("http://ec2-35-157-26-139.eu-central-1.compute.amazonaws.com/logSignal");

                        // setting URL data
                        String parameters = new String("user=" + userid);
                        URL persoNode = new URL(baseURL + "?" + parameters);

                        // Create connection
                        HttpURLConnection myConnection =
                                (HttpURLConnection) persoNode.openConnection();


                        myConnection.setRequestMethod("POST");

                        if (myConnection.getResponseCode() == 200) {
                            Log.e("HTTP", "Save data to DB");
                        } else {
                            Log.e("HTTP", "ERROR save toDB");
                        }

                    } catch (Exception e) {
                        Log.e("HTTP:", e.getMessage());
                    }
                }
            });

        Logger.writeToErrorLog("write signal");
        thread.start();
    }*/

    private void collect() {

        // set Battery
        this.batteryCollector.collectBatteryStatues(getBaseContext());
        this.phoneUsageData.setBattey(this.batteryCollector.getBatteryStatues());

        // set CPU
        this.cpuCollector.collectIntervalCpuStats();
        this.phoneUsageData.setIdleTime(this.cpuCollector.getCurrentIdlePrc());

        // set Application
        this.applicationCollector.start();
        try{this.applicationCollector.join();}catch(Exception e){Logger.writeToErrorLog(e.getMessage() + e.toString());}

        // set Storage
        this.storageCollector.collectStorageStatus();
        this.phoneUsageData.setTotalStorage(this.storageCollector.getTotalStorage());
        this.phoneUsageData.setFreeStorage(this.storageCollector.getFreeStorage());

        DBUpdater.getInstance().setPhoneUsage(this.phoneUsageData);
    }

    private void checkCache(){

    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Log.e("SERV","Service on remove");
        Intent res = new Intent(getApplicationContext(),this.getClass());
        res.setPackage(getPackageName());
        PendingIntent pi = PendingIntent.getService(getApplicationContext(),1, res,PendingIntent.FLAG_ONE_SHOT);

        AlarmManager as = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        as.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100,pi);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("SERV","Start on bind");
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
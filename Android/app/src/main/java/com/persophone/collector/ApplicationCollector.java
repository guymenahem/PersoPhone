package com.persophone.collector;

import com.persophone.shell.ProcessManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guy on 14/03/2017.
 */



public class ApplicationCollector extends Thread {

    public ArrayList<ApplicationData> appData;

    public ApplicationCollector(ArrayList<ApplicationData> appList){
        this.appData = appList;
    }


    protected void writeApplicationUsage(){

        long sumUse = 0;
        double curPer = 0.0f;
        String appNameArr[];
        String showUsage = new String();
        String curAppName = new String("");


        // Moves the current Thread into the background
        List<ProcessManager.Process> lst = null;
        try{
            lst = ProcessManager.getRunningApps();
        }catch (Exception e){
            Logger.writeToErrorLog(e.getMessage() + e.getMessage());
        }

        for (int i=0; i < lst.size(); i++){
            sumUse += lst.get(i).userTime;
        }

        for (int i=0; i < lst.size(); i++){

            curAppName = lst.get(i).name.substring(4);
            curPer = (double)lst.get(i).userTime/(double)sumUse*100;

            // add aplication to data
            if(!(curAppName.contains("android") || curAppName.contains("oneplus"))) {

                // format application name and take name
                appNameArr = curAppName.split("\\.");
                if(appNameArr.length >= 2) curAppName = appNameArr[1];

                // create application data object
                ApplicationData curApp = new ApplicationData(curAppName, lst.get(i).userTime, (float)curPer);
                this.appData.add(curApp);

                /*this.loggerActivity.phoneUsageData.getApplicationData().add(curApp);*/

                /*loggerActivity.showUsage += curApp.getUserViewString();
                loggerActivity.saveUsage += curApp.getCompactString();*/
            }
        }
    }

    @Override
    public void run() {
        writeApplicationUsage();
    }
}

package com.persophone.collector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;

/**
 * Created by Guy on 8/24/2017.
 */

public class CameraCollector {

    int lastSampPics;
    int newPics;

    public CameraCollector(){
        this.CollectCameraStat();
        this.newPics = 0;
    }

    public void CollectCameraStat(){
        int numOfPhotos = 0;

        // File representing the folder that you select using a FileChooser
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM + "/CAMERA").getAbsolutePath());

        if(dir.exists() && dir.isDirectory() && dir.canRead()){
            try{numOfPhotos = dir.listFiles().length;}
            catch(Exception e){Logger.writeToErrorLog("Error to read number of photos");}
        }else{
            Logger.writeToErrorLog("File NOT exist or not directory or not readable");
        }

        this.newPics = numOfPhotos - this.lastSampPics;
        this.lastSampPics = numOfPhotos;
    }

    public int getNumOfNew(){
        return this.newPics;
    }
}

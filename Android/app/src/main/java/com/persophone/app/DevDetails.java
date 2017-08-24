package com.persophone.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private double ramValue;
    private String ramSize;
    private double screenInches;
    private double totalStorageValue;
    private String totalStorageText;
    private double cameraInMp;

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
        this.ramSize = this.getUsableRAM();
        this.ramValue = Double.parseDouble(ramSize.substring(0, ramSize.indexOf(" ")));

        this.totalStorageValue = this.getTotalInternalStorageInGB();
        this.totalStorageText = new DecimalFormat("#.##").format(totalStorageValue).concat(" GB");

        //this.cameraInMp = this.getBackCameraResolutionInMp();

        // Screen size
        this.fillScreenSize();

        this.screenInches = this.getScreenInches();

        // CPU
        this.fillCPUDetails();
    }

    public String GetDeviceName() {
        return this.devName;
    }

    public double GetScreenInches(){
        return this.screenInches;
    }

    public String GetScreenResolution(){
        return  this.devWidth + "x" + this.devHeight;
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public String GetBatteryCapacity(){
        return  (int)this.batteryCap + " mAh";
    }

    public int GetBatteryCapacityValue(){
        return  (int)this.batteryCap;
    }

    public double GetRamValue(){
        return  this.ramValue;
    }

    public String GetRamSize(){
        return this.ramSize;
    }

    public double GetCpuCoresAmount(){
        return this.cpuCores;
    }

    public double GetTotalStorageValue(){
        return this.totalStorageValue;
    }

    public String GetTotalStorageText(){
        return this.totalStorageText;
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

    private double getBatteryCapacity() {
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

    private double getTotalInternalStorageInGB(){
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long blockSize = statFs.getBlockSizeLong();
        long totalSize = statFs.getBlockCountLong()*blockSize;
        long availableSize = statFs.getAvailableBlocksLong()*blockSize;
        long freeSize = statFs.getFreeBlocksLong()*blockSize;

        return totalSize / 1073741824.0;
    }

    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }

    private static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public float getBackCameraResolutionInMp()
    {
        int noOfCameras = Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        for (int i = 0;i < noOfCameras;i++)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                Camera camera = Camera.open(i);;
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
                {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount)
                    {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((float)pixelCountTemp) / (1024000.0f);
                    }
                }

                camera.release();
            }
        }

        return maxResolution;
    }

    private String sizeToTextGB(long value){
        String lastValue = "";
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");

        double totRam = value;
        // totRam = totRam / 1024;

        double mb = totRam / 1024.0;
        double gb = totRam / 1048576.0;
        double tb = totRam / 1073741824.0;

        if (tb > 1) {
            lastValue = twoDecimalForm.format(tb).concat(" TB");
        } else if (gb > 1) {
            lastValue = twoDecimalForm.format(gb).concat(" GB");
        } else if (mb > 1) {
            lastValue = twoDecimalForm.format(mb).concat(" MB");
        } else {
            lastValue = twoDecimalForm.format(totRam).concat(" KB");
        }

        return lastValue;
    }

    private String getTotalRamOption2(){
        ActivityManager actManager = (ActivityManager)actContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        long totalMemory = memInfo.totalMem;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");

        String lastValue = "";
        double totRam = totalMemory;
        // totRam = totRam / 1024;

        double mb = totRam / 1024.0;
        double gb = totRam / 1048576.0;
        double tb = totRam / 1073741824.0;

        if (tb > 1) {
            lastValue = twoDecimalForm.format(tb).concat(" TB");
        } else if (gb > 1) {
            lastValue = twoDecimalForm.format(gb).concat(" GB");
        } else if (mb > 1) {
            lastValue = twoDecimalForm.format(mb).concat(" MB");
        } else {
            lastValue = twoDecimalForm.format(totRam).concat(" KB");
        }

        return lastValue;
    }

    private String getUsableRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();


            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }

    public double getScreenInches(){

            WindowManager windowManager = this.actContext.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);


            // since SDK_INT = 1;
            int mWidthPixels = displayMetrics.widthPixels;
            int mHeightPixels = displayMetrics.heightPixels;

            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            {
                try
                {
                    mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                    mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
                }
                catch (Exception ignored)
                {
                }
            }

            // includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 17)
            {
                try
                {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                    mWidthPixels = realSize.x;
                    mHeightPixels = realSize.y;
                }
                catch (Exception ignored)
                {
                }
            }

        DisplayMetrics dm = new DisplayMetrics();
        this.actContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(mWidthPixels/dm.xdpi,2);
        double y = Math.pow(mHeightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);
        return screenInches;
    }
}

package com.persophone.collector;

import android.icu.util.BuddhistCalendar;
import android.os.Bundle;
import android.os.Message;

import com.persophone.shell.Shell;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;

/**
 * Created by Guy on 8/26/2017.
 */

public class RAMCollector {

    public final String RAM_BUNDLE_PRC = "RAM_FREE_PRC";
    public static final AtomicInteger AtomFreePrc = new AtomicInteger(0);

    public RAMCollector(){
    }

    public void CollectRAMData() {

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    List<String> sts = Shell.SH.run("free -m");
                    String[] s = sts.get(1).split(" +");

                    int totalRAM = Integer.parseInt(s[1]);
                    int usedRAM = Integer.parseInt(s[2]);
                    int freeRAM = Integer.parseInt(s[3]);
                    float div = (float)usedRAM/(float)totalRAM;
                    int prc = (int)((float)div*100f);

                    RAMCollector.AtomFreePrc.set(prc);

                } catch (Exception e)
                {
                    Logger.writeToErrorLog("Error while try to collect RAM data");
                }
            }
        };


        t.start();
    }

    public int getFreePrcRAM(){
        return RAMCollector.AtomFreePrc.get();
    }

    public void setFREEPrcRAM(int prc){ RAMCollector.AtomFreePrc.set(prc);}
}

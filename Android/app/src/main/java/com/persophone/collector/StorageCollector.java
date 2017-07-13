package com.persophone.collector;

import android.os.Environment;
import android.os.StatFs;

/**
 * Created by Guy on 10/06/2017.
 */

public class StorageCollector {

    protected long freeStorage;
    protected long totalStorage;

    public StorageCollector(){}

    public  void collectStorageStatus() {
        Logger.writeToErrorLog("Start writeStorageStatus()");

        long size = 1024*1024*1024;
        StatFs internalStatFs = new StatFs( Environment.getRootDirectory().getAbsolutePath() );
        long internalTotal;
        long internalFree;

        StatFs   externalStatFs = new StatFs( Environment.getExternalStorageDirectory().getAbsolutePath() );
        long externalTotal;
        long externalFree;
        String p1 = Environment.getExternalStorageDirectory().getAbsolutePath(),p2 = Environment.getRootDirectory().getAbsolutePath();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            internalTotal = ( internalStatFs.getBlockCountLong() * internalStatFs.getBlockSizeLong() );
            internalFree = ( internalStatFs.getAvailableBlocksLong() * internalStatFs.getBlockSizeLong() );
            externalTotal = ( externalStatFs.getBlockCountLong() * externalStatFs.getBlockSizeLong() );
            externalFree = ( externalStatFs.getAvailableBlocksLong() * externalStatFs.getBlockSizeLong() );
        }
        else {
            internalTotal = ( (long) internalStatFs.getBlockCount() * (long) internalStatFs.getBlockSize() );
            internalFree = ( (long) internalStatFs.getAvailableBlocks() * (long) internalStatFs.getBlockSize() );
            externalTotal = ( (long) externalStatFs.getBlockCount() * (long) externalStatFs.getBlockSize() );
            externalFree = ( (long) externalStatFs.getAvailableBlocks() * (long) externalStatFs.getBlockSize() );
        }

        long total = (internalTotal + externalTotal) / size;
        long free = (internalFree + externalFree) / size;
        long used = (total - free) / size;

        //write to log
        Logger.writeToErrorLog("Storage status(GB): total " + total + " free: " + free + " used: " + used);
        this.freeStorage = free;
        this.totalStorage = total;
    }

    public long getFreeStorage(){return this.freeStorage;}
    public long getTotalStorage(){return this.totalStorage;}

    public String getURLStorage(){

        // write header
        String str = "Storage={";

        // write values
        str += "freeStor=" + this.totalStorage + "|";
        str += "totalStor=" + this.totalStorage +"|";

        // write footer
        str += "}";

        return str;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.sys;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.muplayer.audio.util.Time;

/**
 *
 * @author martin
 */
public class MemoryCleaner extends Thread {

    private static final int TIME_INTERVAL = 30; // SECS
    private static final long MAX_MEM_DIF = (long) (Math.pow(1024, 2)*20);
    
    private static MemoryCleaner instance;

    private final Runtime runtime;
    private final long initialUsedMem;
    
    static {
        instance = new MemoryCleaner();
    }
    
    public static void startNow() {
        instance.start();
        System.out.println("Memory Cleaner started!");
    }

    public MemoryCleaner() {
        setName("MemoryCleaner");
        runtime = Runtime.getRuntime();
        initialUsedMem = runtime.totalMemory()-runtime.freeMemory();
    }

    private boolean isLimitArraised() {
        return getCurrentUsedMemory()-initialUsedMem >= MAX_MEM_DIF;
    }
    
    private long getCurrentUsedMemory() {
        return runtime.totalMemory()-runtime.freeMemory();
    }
    
    @Override
    public void run() {
        System.out.println("Total: "+Runtime.getRuntime().totalMemory());
        System.out.println("Max: "+Runtime.getRuntime().maxMemory());
        System.out.println("Free: "+Runtime.getRuntime().freeMemory());
        final long ti = System.currentTimeMillis();
        final Runtime runtime = Runtime.getRuntime();
        
        while (true) {            
            try {
                if (isLimitArraised()) {
                    System.gc();
                    System.out.println("Memory Cleaned! at "+Clock.system(ZoneId.systemDefault()).toString());
                }
                Thread.sleep(TIME_INTERVAL*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MemoryCleaner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}

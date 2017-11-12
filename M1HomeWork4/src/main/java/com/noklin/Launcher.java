package com.noklin;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List; 

public class Launcher{
	private static List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
	public static final int TIME_TO_OUT_OF_MEMORY = 5 * 60 * 1000;
	
	public static void main(String[] args) throws SecurityException, IOException{ 
		Runtime.getRuntime().addShutdownHook(new Thread(() -> printGCInfo())); 
		Runtime.getRuntime().gc();
		long freeMemory = Runtime.getRuntime().freeMemory();
		new Thread(() -> new Memory().allocate(freeMemory, TIME_TO_OUT_OF_MEMORY, 0.2)).start();
		new Thread(() -> new Memory().allocate(freeMemory, TIME_TO_OUT_OF_MEMORY, 0.1)).start();
		new Thread(() -> new Memory().allocate(freeMemory, TIME_TO_OUT_OF_MEMORY, 0.1)).start();
		new Thread(() -> new Memory().allocate(freeMemory, TIME_TO_OUT_OF_MEMORY, 0.1)).start();
		new Thread(() -> new Memory().allocate(freeMemory, TIME_TO_OUT_OF_MEMORY, 0.1)).start();
		new Thread(() -> new Memory().allocate(freeMemory, TIME_TO_OUT_OF_MEMORY, 0.1)).start();
		new Thread(() -> new Memory().allocate(freeMemory, TIME_TO_OUT_OF_MEMORY, 0.3)).start();
		new Thread(() -> new Memory().allocate(freeMemory, TIME_TO_OUT_OF_MEMORY, 0)).start();
	}
 
	private static void printGCInfo(){
		gcbeans.forEach(gcBean ->{ 
			System.out.printf("GC name: %-20s Total Collections: %-5s Collections for minute %-5s Total Collection time: %-5s ms %n"
					,gcBean.getName(), gcBean.getCollectionCount(), gcBean.getCollectionCount() / (Launcher.TIME_TO_OUT_OF_MEMORY /1000 / 60)
					,gcBean.getCollectionTime());
		});
	} 
}
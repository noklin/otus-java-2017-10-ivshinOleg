package com.noklin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Memory {
	private List<MemoryBlock> heap = new ArrayList<>(); 
	private final static long ALLOCATE_DELAY = 200;
	
	public void allocate(long size, long duration, double leakRate){ 
		if(leakRate < 0 || leakRate > 1) throw new IllegalArgumentException("Leak rate should be between 0 and 1");
		long phaseCount = duration < ALLOCATE_DELAY ? 1 : duration / ALLOCATE_DELAY; 
		long blockSize = size / phaseCount; 
		long bytesToFree = blockSize - (long)(blockSize * leakRate);  
		while(phaseCount --> 0){
			try{ 
				allocate(blockSize); 
				free(bytesToFree);
				TimeUnit.MILLISECONDS.sleep(ALLOCATE_DELAY); 
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}catch(OutOfMemoryError ex){
				System.err.println("OutOfMemoryError : " + ex.getMessage());
				break;
			}
		}
	}

	private void allocate(long size){
		heap.add(new MemoryBlock(size));
	}

	private int releaseIndex = 0;
	private void free(long size){
		heap.get(releaseIndex++).free(size);
	}
	
	private static class MemoryBlock{
		private byte[] data;
		private MemoryBlock(long size){
			data = new byte[(int)size];
		}
		
		private void free(long size){
			int allocatedSize = data.length;
			data = null;
			data = new byte[allocatedSize - (int)size];
		}
	}
}
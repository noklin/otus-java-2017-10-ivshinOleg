package com.noklin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class FourThreadSorter {
	private static final int THREAD_COUNT = 4;
	
	private final Integer[] source;
	private final ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r , "sorter");
			thread.setDaemon(true);
			return thread;
		}
	});
	
	public FourThreadSorter(Integer[] source){
		this.source = source;
	}

	private boolean descOrder;
	public void sort(Comparator<Integer> comparator) {
		descOrder = comparator.compare(1, 2) > 0;
		int partSize = source.length / THREAD_COUNT;
		final CyclicBarrier sortBarrier = new CyclicBarrier(4, null);
		final CyclicBarrier mergeBarrier = new CyclicBarrier(2, null);
		final CyclicBarrier completeBarrier = new CyclicBarrier(2, null);
		if(partSize > 1) {
			for(int i = 0; i < THREAD_COUNT; i++){
				int fromIndex = i * partSize;
				int toIndex = i * partSize + partSize;
				toIndex = i + 1 == THREAD_COUNT ? source.length : toIndex;
				final int offset = toIndex;
				final int mergeTaskIndex = i;
				service.execute(() -> {
					Arrays.sort(source, fromIndex, offset, comparator);
					try {
						sortBarrier.await();
						if(mergeTaskIndex == 0) {
							merge(0, partSize - 1, partSize , partSize * 2 - 1);
							mergeBarrier.await();
							merge(0, partSize * 2 - 1, partSize * 2, source.length -1);
							completeBarrier.await();
						}else if(mergeTaskIndex == 1) {
							merge(partSize * 3 , partSize * 4 - 1, partSize * 4, source.length -1);
							merge(partSize * 2, partSize * 3 - 1, partSize * 3 , source.length -1);
							mergeBarrier.await();
						}
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				});
			}
			try {
				completeBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}else{
			Arrays.sort(source, 0, source.length, comparator); 
		}
	}
	
	private boolean isOrdered(Integer left, Integer right) {
		return descOrder ? left > right : right > left;
	}
	
	private void merge(int leftStart, int leftEnd, int rightStart, int rightEnd) {
		Integer[] leftPath = new Integer[leftEnd - leftStart + 1];
	    Integer[] rightPath = new Integer[rightEnd - rightStart + 1];
	    System.arraycopy(source, leftStart, leftPath, 0, leftPath.length);
	    System.arraycopy(source, rightStart, rightPath, 0, rightPath.length);
		int lIndex = 0;
		int rIndex = 0;
		for(int i = leftStart ; i <= rightEnd; i++) {
			boolean rightMiss = rIndex >= rightPath.length;
			boolean leftMiss = lIndex >= leftPath.length;
			int nextValue = 0;
			if(rightMiss || leftMiss) {
				nextValue = rightMiss ? leftPath[lIndex++] : rightPath[rIndex++];
			}else {
				nextValue = isOrdered(leftPath[lIndex], rightPath[rIndex]) 
						? leftPath[lIndex++] : rightPath[rIndex++];
			}
			source[i] = nextValue;
		}
	}
}
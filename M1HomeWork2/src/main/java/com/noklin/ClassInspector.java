package com.noklin;

public class ClassInspector{

	private Object[] heap;

	public interface InstanceSource<T> {
		T newInstance() throws InstantiationException, IllegalAccessException;
	}

	public ClassInspector() {
		this(1_000_000);
	}

	public ClassInspector(int heapSize) {
		heap = new Object[heapSize];
	}

	/**
	 * @return a size of instance of class in bytes.
	 * @param clas
	 *            - target class to inspect
	 */
	public long getInstanceSize(Class<?> clas) throws InstantiationException, IllegalAccessException {
		return getInstanceSize(() -> clas.newInstance());
	}

	/**
	 * @return a size of instance of class in bytes.
	 * @param instanceSource
	 *            - how instantiate class
	 */
	public long getInstanceSize(InstanceSource<?> instanceSource) throws InstantiationException, IllegalAccessException {
		fillHeap(instanceSource);
		Runtime.getRuntime().gc();
		long beforeClear = Runtime.getRuntime().freeMemory();
		clearHeap();
		Runtime.getRuntime().gc();
		long afterClear = Runtime.getRuntime().freeMemory();
		return (afterClear - beforeClear) / heap.length;
	}

	private void fillHeap(InstanceSource<?> instanceSource) throws InstantiationException, IllegalAccessException {
		for (int i = 0; i < heap.length; i++)
			heap[i] = instanceSource.newInstance();
	}

	private void clearHeap() {
		for (int i = 0; i < heap.length; i++)
			heap[i] = null;
	}
}
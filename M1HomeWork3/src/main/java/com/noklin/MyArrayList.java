package com.noklin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;


public class MyArrayList<T> implements List<T>{

	private Object[] heap;
	private int headIndex;
	
	private final static int INCREASE_RANGE = 32;
	
	public MyArrayList(int initCapacity){
		heap = new Object[initCapacity]; 
	} 
	
	public MyArrayList(){
		this(64);
	}
	
	@Override
	public boolean add(T item) {
		if(isfull())
			increaseHeap();
		heap[headIndex++] = item; 
		return true;
	}

	private void increaseHeap(){
		Object[] newHeap = new Object[heap.length + INCREASE_RANGE];
		System.arraycopy(heap, 0, newHeap, 0, heap.length);
		heap = newHeap;
	}
	
	private void checkIndexAccess(int index){
		if(index > headIndex - 1) throw new IndexOutOfBoundsException("" + index + " size: " + size());
	}
	
	private boolean isfull(){
		return headIndex == heap.length;
	}
	
	@Override
	public void add(int index, T item) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> items) {
		items.forEach(item -> add(item));
		return true;
	}

	@Override
	public boolean addAll(int startIndex, Collection<? extends T> items) {
		for(T item : items)
			add(startIndex++, item);
		return true;
	}

	@Override
	public void clear() {
		for(int i = 0 ; i < heap.length; i++)
			heap[i] = null;
		headIndex = 0;
	}

	@Override
	public boolean contains(Object item) {
		for(int i = 0 ; i < headIndex; i++)
			if(item.equals(heap[i])) return true;
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> items) {
		for(Object item : items)
			if(!contains(item)) return false;
		return true;			
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(int index) {
		checkIndexAccess(index);
		return (T)heap[index];
	}

	@Override
	public int indexOf(Object item) {
		for(int i = 0 ; i < headIndex; i++)
			if(heap[i].equals(item)) return i;
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return headIndex == 0;
	}

	 
	private class MyListIterator<X extends T> implements ListIterator<T>{
		private int currentPosition;
		
		private MyListIterator(){
			this(0);
		}
		
		private MyListIterator(int startIndex){
			if(currentPosition > headIndex - 1) throw new IndexOutOfBoundsException();
			currentPosition = startIndex;
		}
		
		@Override
		public boolean hasNext() { 
			return currentPosition != headIndex;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public T next() {  
			if(currentPosition > headIndex - 1) throw new NoSuchElementException();
			return (T)heap[currentPosition++];
		}
		
		@Override
		public void add(T e) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean hasPrevious() {
			throw new UnsupportedOperationException();
		}
		@Override
		public int nextIndex() {
			return currentPosition;
		}
		@Override
		public T previous() {
			throw new UnsupportedOperationException();
		}
		@Override
		public int previousIndex() {
			throw new UnsupportedOperationException();
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException(); 
		}
		@Override
		public void set(T e) {
			MyArrayList.this.set(currentPosition - 1, e);
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return new MyListIterator<T>();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new MyListIterator<T>();
	}

	@Override
	public ListIterator<T> listIterator(int start) {
		return new MyListIterator<T>(start);
	}

	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T remove(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T set(int index, T item) {
		checkIndexAccess(index);
		T old = (T)heap[index];
		heap[index] = item;
		return old;
	}

	@Override
	public int size() { 
		return headIndex;
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() { 
		return Arrays.copyOf(heap, headIndex);
	}

	@Override
	public <C> C[] toArray(C[] arg0) {
		throw new UnsupportedOperationException();
	}

}

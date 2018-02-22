package com.noklin.cache;

public interface Cache<K,V> {
	
	default void put(K key,V value){
		throw new UnsupportedOperationException();
	}
	
	default V get(K key){
		throw new UnsupportedOperationException();
	}
	
	default void setMaxIdle(long idle){
		throw new UnsupportedOperationException();
	}
	
	default void setTimeToLive(long ttl){
		throw new UnsupportedOperationException();
	}
	
	default void setMaxSize(long maxSize){
		throw new UnsupportedOperationException();
	}
	
	default long getMaxSize(){
		throw new UnsupportedOperationException();
	} 

	default long getTimeToLive(){
		throw new UnsupportedOperationException();
	} 

	default long getMaxIdle(){
		throw new UnsupportedOperationException();
	} 
	
	default void clear(){
		throw new UnsupportedOperationException();
	}
	
	default long getMissCount(){
		throw new UnsupportedOperationException();
	}

	default long getHitCount(){
		throw new UnsupportedOperationException();
	}

	default long getSize(){
		throw new UnsupportedOperationException();
	}
}

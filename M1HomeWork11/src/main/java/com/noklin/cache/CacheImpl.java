package com.noklin.cache;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CacheImpl<K,V> implements Cache<K,V>{
	
	public final static long NO_LIMIT_VALUE = 0;
	private final ConcurrentHashMap<K, CacheElement<K,V>> heap = new ConcurrentHashMap<>(); 
	
	private AtomicLong missCount = new AtomicLong();
	private AtomicLong hitCount = new AtomicLong();
	
	private volatile long timeToLive, maxIdle, maxSize;
	private CacheImpl(long timeToLive, long maxIdle, long maxSize) {
		this.timeToLive = timeToLive;
		this.maxIdle = maxIdle;
		this.maxSize = maxSize;
	}
	
	@Override
	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
		validateExpire();
	}
	
	@Override
	public void setMaxIdle(long maxIdle) {
		this.maxIdle = maxIdle;
		validateExpire();
	}

	private void validateExpire(){
		Iterator<Entry<K, CacheElement<K, V>>> it = heap.entrySet().iterator();
		while(it.hasNext()){
			Entry<K, CacheElement<K, V>> ent = it.next();
			CacheElement<K, V> el = ent.getValue();
			if(el.isExpired()){
				el.calcelDeleteService();
				it.remove();
			}
		}
	}
	
	@Override
	public long getHitCount() {
		return hitCount.get();
	}

	@Override
	public long getMissCount() {
		return missCount.get();
	}
	
	@Override
	public void setMaxSize(long maxSize) {
		long toDelete = this.maxSize - maxSize;
		if(toDelete > 0){
			Iterator<Entry<K, CacheElement<K, V>>> it = heap.entrySet().iterator();
			while(it.hasNext() && toDelete --> 0){
				it.next();
				it.remove();
			}
		}
		this.maxSize = maxSize;
	}
	
	@Override
	public void put(K key, V value) {
		if(maxSize != NO_LIMIT_VALUE && heap.size() == maxSize){
			Iterator<?> it = heap.entrySet().iterator();
			it.next();
			it.remove();
		}
		CacheElement<K,V> el = new CacheElement<>(key,value);
		heap.put(key, el);
		el.validateAccess();
	}
		
	@Override
	public V get(K key) { 
		CacheElement<K, V> el = heap.get(key);
		V result = el == null ? null : el.unWrap();
		if(result == null){
			missCount.incrementAndGet();
		}else{
			hitCount.incrementAndGet();
		}
		return result;
	}
	
	@Override
	public void clear() {
		heap.clear();
	}
	
	private class CacheElement<G,T> {
		private final SoftReference<T> ref;
		private final G key;
		private final long createTime = System.currentTimeMillis();
		private volatile long lastAccess = createTime;
		
		private CacheElement(G key, T value){
			this.key = key;
			ref = new SoftReference<T>(value);
		}
		
		private Timer deleteTimer = new Timer(true);
		private class DeleteTask extends TimerTask{
			@Override
			public void run() {
				validateAccess();
			}
		}
		
		private DeleteTask deleteTask;
		private void calcelDeleteService(){
			if(deleteTask != null){
				deleteTask.cancel();
			}
		}
		
		private boolean isIdleExpired(){
			if(maxIdle == NO_LIMIT_VALUE){
				return false;
			}else{
				long now = System.currentTimeMillis();
				long idleness = now - lastAccess; 
				return idleness >= maxIdle;
			}
		}

		private boolean isTTLExpired(){
			if(timeToLive == NO_LIMIT_VALUE){
				return false;
			}else{
				long now = System.currentTimeMillis();
				long lifeTime = now - createTime;
				return lifeTime >= timeToLive;
			}
		}
		
		private boolean isExpired(){
			return isTTLExpired() || isIdleExpired();
		} 
		
		private void validateAccess(){
			if(timeToLive == NO_LIMIT_VALUE && maxIdle == NO_LIMIT_VALUE){
				return;
			}
			calcelDeleteService();
			if(isExpired()){
				heap.remove(key);
			}else{
				deleteTask = new DeleteTask();
				long nextDeleteTimeLive = calcNextDeleteTimeLive();
				long nextDeleteTimeIdle = calcNextDeleteTimeIdle();
				long nearDeleteTime = nextDeleteTimeLive < nextDeleteTimeIdle ? nextDeleteTimeLive : nextDeleteTimeIdle;
				deleteTimer.schedule(deleteTask , nearDeleteTime);
			}
		}
		
		private long calcNextDeleteTimeLive(){
			long now = System.currentTimeMillis();
			return timeToLive == NO_LIMIT_VALUE ? Long.MAX_VALUE : timeToLive + now - createTime;
		}

		private long calcNextDeleteTimeIdle(){
			long now = System.currentTimeMillis();
			return maxIdle == NO_LIMIT_VALUE ? Long.MAX_VALUE : maxIdle - now + lastAccess;
		}
		
		private T unWrap(){
			lastAccess = System.currentTimeMillis();
			return ref.get();
		}
	}	

	public static Builder createBuilder(){
		return new Builder();
	}
	
	public static class Builder{
		private Builder(){}
		private long timeToLive = NO_LIMIT_VALUE;
		private long idle = NO_LIMIT_VALUE;
		private long maxSize = NO_LIMIT_VALUE;
		public Builder timeToLive(int ttl){
			timeToLive = ttl;
			return this;
		}

		public Builder idle(int idle){
			this.idle = idle;
			return this;
		}

		public Builder maxSize(int size){
			this.maxSize = size;
			return this;
		}
		
		public <K,V> CacheImpl<K,V> build(){
			return new CacheImpl<>(timeToLive, idle, maxSize);
		}
	}
}
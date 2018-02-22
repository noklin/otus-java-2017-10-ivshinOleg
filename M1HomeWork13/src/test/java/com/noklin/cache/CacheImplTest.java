package com.noklin.cache;


import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class CacheImplTest {
	
	private CacheImpl<String, Object> cache;
	private static final String DEFAULT_KEY = "defK";
	private Object deafaultValue = new Object();
	
	@Before
	public void initCache(){
		initCache(CacheImpl.createBuilder().build());
	}

	private void initCache(CacheImpl<String,Object> chache){
		Map<String,Object> data = new HashMap<>();
		data.put(DEFAULT_KEY, deafaultValue);
		initCache(chache, data);
	}

	private void initCache(CacheImpl<String,Object> chache, Map<String,Object> data){
		this.cache = chache;
		data.forEach(cache::put);
	}
	
	@Test
	public void shouldSupportCorrectInsert(){
		assertTrue(cache.get(DEFAULT_KEY) != null);
	}

	@Test
	public void shouldSupportCorrectClear(){
		cache.clear();
		assertTrue(cache.get(DEFAULT_KEY) == null);
	}

	@Test
	public void shouldSupportCorrectTTL() throws InterruptedException{
		int ttl = 100;
		CacheImpl<String,Object> cache = CacheImpl.createBuilder().timeToLive(ttl).build();
		initCache(cache);
		TimeUnit.MILLISECONDS.sleep(ttl / 2);
		Thread.yield();
		assertTrue(cache.get(DEFAULT_KEY) != null);
		TimeUnit.MILLISECONDS.sleep(ttl / 2);
		int tryCount = 5;
		boolean notExist = cache.get(DEFAULT_KEY) == null;
		if(!notExist){
			while(tryCount --> 0){
				Thread.yield();
				TimeUnit.MILLISECONDS.sleep(1);
				notExist = cache.get(DEFAULT_KEY) == null;
			}
		}
		assertTrue(notExist); 
	}

	@Test
	public void shouldSupportCorrectIdle() throws InterruptedException{
		CacheImpl<String,Object> cache = CacheImpl.createBuilder().idle(200).build();
		initCache(cache);
		TimeUnit.MILLISECONDS.sleep(150);
		assertTrue(cache.get(DEFAULT_KEY) != null);
		TimeUnit.MILLISECONDS.sleep(250);
		boolean notExist = cache.get(DEFAULT_KEY) == null;
		assertTrue(notExist); 
	}
	
	@Test
	public void shouldSupportCorrectSize() throws InterruptedException{
		CacheImpl<String,Object> cache = CacheImpl.createBuilder().maxSize(2).build();
		Map<String,Object> data = new HashMap<>();
		Object v1 = new Object();
		Object v2 = new Object();
		Object v3 = new Object();
		data.put("k1", v1);
		data.put("k2", v2);
		initCache(cache, data);
		cache.put("k3", v3);
		boolean v1Orv2Deleted = cache.get("k1") == null || cache.get("k2") == null;
		assertTrue(v1Orv2Deleted && cache.get("k3") != null);
	}
	
	@Test
	public void shouldSupportCorrectSetSize(){
		CacheImpl<String,Object> cache = CacheImpl.createBuilder().maxSize(1).build();
		initCache(cache);
		cache.setMaxSize(2);
		cache.put("k1", new Object());
		assertTrue(cache.get(DEFAULT_KEY) != null);
		assertTrue(cache.get("k1") != null);
		cache.setMaxSize(1);
		assertTrue(cache.get(DEFAULT_KEY) == null || cache.get("k1") == null);
	}
	
	@Test
	public void shouldSupportCorrectSetMaxTTL() throws InterruptedException{
		CacheImpl<String,Object> cache = CacheImpl.createBuilder().timeToLive(100).build();
		initCache(cache);
		TimeUnit.MILLISECONDS.sleep(90);
		cache.setTimeToLive(0);
		TimeUnit.MILLISECONDS.sleep(100);
		assertTrue(cache.get(DEFAULT_KEY) != null);
		cache.setTimeToLive(10);
		TimeUnit.MILLISECONDS.sleep(100);
		assertTrue(cache.get(DEFAULT_KEY) == null);
	}
	
	@Test
	public void shouldSupportCorrectSetMaxIdle() throws InterruptedException{
		CacheImpl<String,Object> cache = CacheImpl.createBuilder().idle(10).build();
		initCache(cache);
		cache.setMaxIdle(0);
		TimeUnit.MILLISECONDS.sleep(100);
		assertTrue(cache.get(DEFAULT_KEY) != null);
	}
	
	@Test
	public void shouldSupportCorrectGetHitCount(){
		initCache();
		int missCount = 10;
		int i = missCount;
		while(i --> 0){
			cache.get("0");
		}
		assertTrue(missCount == cache.getMissCount());
	}
	
	@Test
	public void shouldSupportCorrectGetMissCount(){
		initCache();
		int hitCount = 123;
		int i = hitCount;
		while(i --> 0){
			cache.get(DEFAULT_KEY);
		}
		assertTrue(hitCount == cache.getHitCount());
	}
	
}
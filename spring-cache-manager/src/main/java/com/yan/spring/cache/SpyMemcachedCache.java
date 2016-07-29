package com.yan.spring.cache;

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;


public class SpyMemcachedCache extends AbstractCache {

	private MemcachedClient memCachedClient;
	private long version = 0;

	protected SpyMemcachedCache(String name, int exp, MemcachedClient memcachedClient) {
		super(name, exp);
		this.memCachedClient = memcachedClient;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Object getNativeCache() {
		return memCachedClient;
	}

	@Override
	public ValueWrapper get(Object key) {
		Object o = memCachedClient.get(makeFinalKey(key));
		if(o == null)
			return null;
		return new SimpleValueWrapper(o);
	}

	@Override
	public void put(Object key, Object value) {
		if(value == null)
			return;
		memCachedClient.set(makeFinalKey(key), exp, value);
	}

	@Override
	public void evict(Object key) {
		memCachedClient.delete(makeFinalKey(key));
	}

	@Override
	public void clear() {

	}

	public void setMemCachedClient(MemcachedClient memCachedClient) {
		this.memCachedClient = memCachedClient;
	}
}

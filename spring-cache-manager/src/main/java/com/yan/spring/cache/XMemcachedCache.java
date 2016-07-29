package com.yan.spring.cache;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.concurrent.TimeoutException;

public class XMemcachedCache  extends AbstractCache {

	private MemcachedClient memcachedClient;

	protected XMemcachedCache(String name, int exp, MemcachedClient memcachedClient) {
		super(name, exp);
		this.memcachedClient = memcachedClient;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getNativeCache() {
		return memcachedClient;
	}

	@Override
	public ValueWrapper get(Object key) {
		try {
			Object o = memcachedClient.get(makeFinalKey(key));
			if(o == null)
				return null;
			return new SimpleValueWrapper(o);
		} catch (Exception e) {
			//todo 一场处理
			return null;
		}
	}

	@Override
	public void put(Object key, Object value) {
		try {
			memcachedClient.set(makeFinalKey(key), exp, value);
		}catch (Exception ex){
			//todo 一场处理
		}
	}

	@Override
	public void evict(Object key) {
		try {
			memcachedClient.delete(makeFinalKey(key));
		} catch (Exception e) {
			//todo 一场处理
		}
	}

	@Override
	public void clear() {

	}
}

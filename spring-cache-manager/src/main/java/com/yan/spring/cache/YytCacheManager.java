package com.yan.spring.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class YytCacheManager implements CacheManager {
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
	private final Collection<String> names = Collections.unmodifiableSet(caches.keySet());

	private CacheFactory cacheFactory;

	@Override
	public Cache getCache(String name) {
		int exp = 0;
		if(name.indexOf(",") > 0){
			String[] strs = name.split(",");
			name = strs[0];
			exp = Integer.valueOf(strs[1]);
		}
		name += "_"; //防止name以数字结尾，key也为数字，会造成cache的key混乱，所以默认加“_";
		Cache c = caches.get(name);
		if(c == null){
			c = cacheFactory.createCache(name, exp);
			caches.put(name, c);
		}
		return c;
	}

	@Override
	public Collection<String> getCacheNames() {
		return names;
	}

	public void setCacheFactory(CacheFactory cacheFactory) {
		this.cacheFactory = cacheFactory;
	}
}

package com.yan.spring.cache;

import org.springframework.cache.Cache;


public interface CacheFactory {
	Cache createCache(String name, int exp);
}

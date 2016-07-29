package com.yan.spring.cache;

import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;


public class DefaultCacheFactory implements CacheFactory {

	private Object cacheClientBuilder;

	@Override
	public Cache createCache(String name, int exp) {
		if (cacheClientBuilder instanceof MemcachedClient) {
			try {
				MemcachedClient memcachedClient = (MemcachedClient) cacheClientBuilder;
				return new SpyMemcachedCache(name, exp, memcachedClient);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (cacheClientBuilder instanceof XMemcachedClientBuilder) {
			try {
				net.rubyeye.xmemcached.MemcachedClient memcachedClient = ((XMemcachedClientBuilder) cacheClientBuilder).build();
				return new XMemcachedCache(name, exp, memcachedClient);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (cacheClientBuilder instanceof RedisTemplate) {
			return new RedisCache(name, exp, (RedisTemplate) cacheClientBuilder);
		}

		throw new RuntimeException("cacheClientBuilder 只支持 MemcachedClientFactoryBean, XMemcachedClientBuilder, RedisTemplate 三种类型");
	}

	public void setCacheClientBuilder(Object cacheClientBuilder) {
		if ((cacheClientBuilder instanceof MemcachedClient) ||
		    (cacheClientBuilder instanceof XMemcachedClientBuilder) ||
		    (cacheClientBuilder instanceof RedisTemplate)) {
			this.cacheClientBuilder = cacheClientBuilder;
		} else {
			throw new RuntimeException("cacheClientBuilder 只支持 MemcachedClientFactoryBean, XMemcachedClientBuilder, RedisTemplate 三种类型");
		}

	}
}

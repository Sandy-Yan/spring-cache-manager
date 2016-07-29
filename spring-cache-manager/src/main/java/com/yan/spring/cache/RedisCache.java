package com.yan.spring.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisCache  extends AbstractCache  {

	private RedisTemplate redisTemplate;

	protected RedisCache(String name, int exp, RedisTemplate redisTemplate) {
		super(name, exp);
		this.redisTemplate = redisTemplate;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getNativeCache() {
		return redisTemplate;
	}

	@Override
	public ValueWrapper get(final Object key) {
		return (ValueWrapper) redisTemplate.execute(new RedisCallback<ValueWrapper>() {
			public ValueWrapper doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] bs = connection.get(makeFinalKey(key).getBytes());
				return (bs == null ? null : new SimpleValueWrapper(redisTemplate.getValueSerializer().deserialize(bs)));
			}
		}, true);
	}

	@Override
	public void put(Object key, final Object value) {
		final byte[] k = makeFinalKey(key).getBytes();
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(k, redisTemplate.getValueSerializer().serialize(value));
				return null;
			}
		}, true);
	}

	@Override
	public void evict(Object key) {
		final byte[] k = makeFinalKey(key).getBytes();
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.del(k);
				return null;
			}
		}, true);
	}

	@Override
	public void clear() {

	}
}

package com.yan.spring.cache;

import org.springframework.cache.Cache;


public abstract class AbstractCache implements Cache{
	protected String name;
	protected int exp;

	protected AbstractCache(String name, int exp) {
		this.name = name;
		this.exp = exp;
	}

	protected String makeFinalKey(Object key){
		String stripped = "";
		if(key != null)
			stripped = key.toString().replaceAll("\\s", "__");
		return name + stripped;
	}
}

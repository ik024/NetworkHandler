package com.ik.network_handler_lib.cache;


import android.util.LruCache;

public class CacheMechanism {

    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static final int DEFAULT_CACHE_SIZE = maxMemory / 8;

    private LruCache<String, CacheValue<?>> mCache;

    public CacheMechanism(){

        mCache = new LruCache<String, CacheValue<?>>(DEFAULT_CACHE_SIZE) {
            @Override
            protected int sizeOf(String string, CacheValue<?> cacheValue) {
               return cacheValue.getSize()/1024;
            }
        };
    }

    public boolean contains(String key){
        return mCache.get(key) != null;
    }

    public CacheValue<?> getObject(String key){
        return  mCache.get(key);
    }

    public void putObject(String key, CacheValue<?> cacheValue){
        mCache.put(key, cacheValue);
    }

    public void resize(int resize){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        if (maxMemory < resize)
            throw new IllegalArgumentException("should be less than max memory");
        mCache.resize(resize);
    }

}

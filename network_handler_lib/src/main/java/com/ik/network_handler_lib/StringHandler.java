package com.ik.network_handler_lib;

import android.os.AsyncTask;

import com.ik.network_handler_lib.cache.CacheMechanism;
import com.ik.network_handler_lib.cache.CacheValue;
import com.ik.network_handler_lib.utils.HttpHelper;
import com.ik.network_handler_lib.utils.INetworkCallback;
import com.ik.network_handler_lib.utils.RequestPackage;

import java.io.IOException;
import java.util.Map;

class StringHandler extends NetworkHandler {
    private RequestPackage mRequestPackage;
    private CacheMechanism mCache;
    private AsyncTask<Void, Void, CacheValue> mTask;
    private INetworkCallback mCallback;

    StringHandler(CacheMechanism cache, RequestPackage requestPackage){
        mCache = cache;
        mRequestPackage = requestPackage;
    }

    @Override
    public void setEndPoint(String endPoint) {
        mRequestPackage.setEndPoint(endPoint);
    }

    @Override
    public void setMethod(String method) {
        mRequestPackage.setMethod(method);
    }

    @Override
    public void setParams(Map<String, String> params) {
        mRequestPackage.setParams(params);
    }

    @Override
    public void setParam(String key, String value) {
        mRequestPackage.setParam(key, value);
    }

    @Override
    public void executeAsync(INetworkCallback callback) {
        final String url = mRequestPackage.getEndpoint();
        mCallback = callback;

        mTask = new AsyncTask<Void, Void, CacheValue>() {
            @Override
            protected CacheValue doInBackground(Void... params) {
                CacheValue cacheValue = null;

                if (mCache.contains(url)) {
                    cacheValue = mCache.getObject(url);
                } else {
                    String response = null;
                    try {
                        response = HttpHelper.makeNetworkCall(mRequestPackage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    cacheValue = new CacheValue(response, String.class);
                    cacheValue.setSize(response.length());
                    mCache.putObject(url, cacheValue);
                }

                return cacheValue;
            }

            @Override
            protected void onPostExecute(CacheValue cacheValue) {
                super.onPostExecute(cacheValue);

                if (cacheValue != null){
                    if (cacheValue.getObject() != null){
                       onSuccess(cacheValue.getType().cast(cacheValue.getObject()));
                    } else {
                        onError("Null bitmap fetched");
                    }
                } else {
                    onError("Got empty response");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    @Override
    public void cancelTask() {
        if (mTask != null)
            mTask.cancel(true);
    }

    @Override
    public void onSuccess(Object object) {
        if (mCallback != null)
            mCallback.onSuccess(object);
    }

    @Override
    public void onError(String message) {
        if (mCallback != null)
            mCallback.onError(message);
    }

}

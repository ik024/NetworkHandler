package com.ik.network_handler_lib;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import com.ik.network_handler_lib.cache.CacheMechanism;
import com.ik.network_handler_lib.cache.CacheValue;
import com.ik.network_handler_lib.utils.HttpHelper;
import com.ik.network_handler_lib.utils.INetworkCallback;
import com.ik.network_handler_lib.utils.RequestPackage;

import java.util.Map;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

class ImageHandler extends NetworkHandler {

    private RequestPackage mRequestPackage;
    private INetworkCallback mCallback;
    private AsyncTask<Void, Void, CacheValue> mTask;
    private CacheMechanism mCache;

    ImageHandler(CacheMechanism cache, RequestPackage requestPackage){
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

    public void executeAsync(INetworkCallback callback) {
        validateExecuteAsyncParameters(mRequestPackage.getEndpoint(), callback);
        mCallback = callback;

        mTask = new AsyncTask<Void, Void, CacheValue>() {
            @Override
            protected CacheValue doInBackground(Void... params) {
                CacheValue cacheValue = null;

                if (mCache.contains(mRequestPackage.getEndpoint())) {
                    cacheValue = mCache.getObject(mRequestPackage.getEndpoint());
                } else {
                    Bitmap bitmap = HttpHelper.fetchBitmap(mRequestPackage);
                    cacheValue = new CacheValue(bitmap, Bitmap.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        cacheValue.setSize(bitmap.getAllocationByteCount()/1024);
                    } else {
                        cacheValue.setSize(bitmap.getByteCount()/1024);
                    }

                    mCache.putObject(mRequestPackage.getEndpoint(), cacheValue);
                }

                return cacheValue;
            }

            @Override
            protected void onPostExecute(CacheValue cacheValue) {
                super.onPostExecute(cacheValue);
                if (cacheValue != null){
                    if (cacheValue.getObject() != null){
                        mCache.putObject(mRequestPackage.getEndpoint(), cacheValue);
                        onSuccess(cacheValue.getType().cast(cacheValue.getObject()));
                    } else {
                        onError("Null bitmap fetched");
                    }
                } else {
                    onError("Got empty response");
                }
            }
        }.executeOnExecutor(THREAD_POOL_EXECUTOR, null);
    }

    @Override
    public void cancelTask(){
        if (mTask != null)
            mTask.cancel(true);
    }
    @Override
    public void onSuccess(Object object) {
        if (mCallback != null) {
            mCallback.onSuccess(object);
        }
    }

    @Override
    public void onError(String message) {
        if (mCallback != null)
            mCallback.onError(message);
    }


}

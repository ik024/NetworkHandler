package com.ik.network_handler_lib;

import android.text.TextUtils;

import com.ik.network_handler_lib.cache.CacheMechanism;
import com.ik.network_handler_lib.utils.INetworkCallback;
import com.ik.network_handler_lib.utils.NetworkResponseType;
import com.ik.network_handler_lib.utils.RequestPackage;

import java.io.InvalidClassException;
import java.util.Map;

/**
 * This class should be extended from a class which intents to handle a NetworkResponseType
 */

public abstract class NetworkHandler {

    /**
     * Used to create a single instance of the CacheMechanism object
     */

    private static class CacheInstance {
        private static final CacheMechanism mCache = new CacheMechanism();
    }

    /**
     * This methods returns the appropriate NetworkHandler instance based on the NetworkResponseType passed
     *
     * @param type Type of NetworkHandler
     * @return NetworkHandler
     */
    public static NetworkHandler getNetworkHandler(NetworkResponseType type) {
        if (type == NetworkResponseType.IMAGE) {
            NetworkHandler handler = new ImageHandler(CacheInstance.mCache, new RequestPackage());
            if (validHandlerObject(handler)) {
                return handler;
            }
        } else if (type == NetworkResponseType.STRING) {
            NetworkHandler handler = new StringHandler(CacheInstance.mCache, new RequestPackage());
            if (validHandlerObject(handler)) {
                return handler;
            }
        } else {
            throw new UnsupportedOperationException("Unsupported data type");
        }

        return null;
    }

    /**
     * Method the resize the Cache
     *
     * @param resize Size by which Cache should be resized
     */
    public void resizeCache(int resize){
        CacheInstance.mCache.resize(resize);
    }

    /**
     * This method is used to set the url fro network request
     *
     * @param endPoint url
     */
    public abstract void setEndPoint(String endPoint);

    /**
     *  This method sets the HTTP Request Methods
     *
     * @param method HTTP Request Method
     */
    public abstract void setMethod(String method);

    /**
     * This method sets the list of params in the form of Map for the network call
     *
     * @param params list of params
     */
    public abstract void setParams(Map<String, String> params);

    /**
     * This method sets a param in the form of key value for the network call
     * @param key param key
     * @param value param value
     */

    public abstract void setParam(String key, String value);

    /**
     * This method executes the network call asynchronously and returns the response via the INetworkCallback
     *
     * @param callback Callback to return the network response
     */
    public abstract void executeAsync(INetworkCallback<?> callback);

    /**
     * This method cancels the asynchronous network call
     */
    public abstract void cancelTask();

    /**
     * This method handles returning the response on successful network call
     *
     * @param object response object
     */

    public abstract void onSuccess(Object object);

    /**
     * This method handles returning the error message on network failure
     *
     * @param message failure message
     */
    public abstract void onError(String message);

    /**
     * This method is used to validate the NetworkHandler Object that will be returned in getInstance method
     *
     * @param object the NetworkHandler object to be returned
     * @return true/false
     */
    private static boolean validHandlerObject(Object object) {
        if (object instanceof NetworkHandler)
            return true;
        else
            try {
                throw new InvalidClassException(object.getClass().getSimpleName() + " should extend " + NetworkHandler.class.getSimpleName());
            } catch (InvalidClassException e) {
                e.printStackTrace();
                return false;
            }
    }

    /**
     * This method is called from executeAsync method to check its parameters
     * @param url url for the network call
     * @param callback INetworkCallback
     */
    void validateExecuteAsyncParameters(String url, INetworkCallback callback) {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url cannot be null or empty");
        }
        if (callback == null) {
            throw new IllegalArgumentException(INetworkCallback.class.getSimpleName() + " cannot be null");
        }
    }

}

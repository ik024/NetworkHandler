package com.ik.network_handler_lib.utils;

public interface INetworkCallback<T> {
    void onSuccess(T object);
    void onError(String message);
}

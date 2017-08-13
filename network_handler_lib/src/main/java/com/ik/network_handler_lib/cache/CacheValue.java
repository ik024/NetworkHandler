package com.ik.network_handler_lib.cache;

public class CacheValue<T> {

    private Object mObject;
    private Class<T> type;
    private int size;

    public CacheValue(Object mObject, Class<T> type) {
        this.mObject = mObject;
        this.type = type;
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object mObject) {
        this.mObject = mObject;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public int getSize(){
        return size;
    }

    public void setSize(int size){
        this.size = size;
    }
}

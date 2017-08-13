package com.ik.networkhandler.interfaces;

import com.ik.networkhandler.model.pojo.Post;

import java.util.List;

public interface IViewCallback {

    void postsFetched(List<Post> posts);
    void showMessage(String message);
    void showMessage(int resId);
}

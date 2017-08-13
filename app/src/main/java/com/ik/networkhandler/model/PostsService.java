package com.ik.networkhandler.model;

import android.support.test.espresso.idling.CountingIdlingResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ik.network_handler_lib.utils.NetworkResponseType;
import com.ik.network_handler_lib.utils.INetworkCallback;
import com.ik.network_handler_lib.NetworkHandler;
import com.ik.networkhandler.R;
import com.ik.networkhandler.interfaces.IViewCallback;
import com.ik.networkhandler.model.pojo.Post;

import java.util.List;

import timber.log.Timber;

public class PostsService {

    private IViewCallback mCallback;
    /*This is used for testing*/
    public static CountingIdlingResource idlingResource = new CountingIdlingResource("API_CALL");

    public PostsService(IViewCallback callback){
        mCallback = callback;
    }

    public void fetchPosts(){
            idlingResource.increment();
            NetworkHandler handler = NetworkHandler.getNetworkHandler(NetworkResponseType.STRING);
            handler.setEndPoint("http://pastebin.com/raw/wgkJgazE");
            handler.executeAsync(new INetworkCallback<String>() {
                @Override
                public void onSuccess(String object) {
                    String json = object;
                    Timber.i(json);
                    Gson gson = new Gson();
                    List<Post> posts = gson.fromJson(json, new TypeToken<List<Post>>(){}.getType());
                    mCallback.postsFetched(posts);
                    mCallback.showMessage(R.string.posts_fetched);
                    idlingResource.decrement();
                }

                @Override
                public void onError(String message) {
                    mCallback.showMessage("Error: "+message);
                    idlingResource.decrement();
                }
            });

    }
}

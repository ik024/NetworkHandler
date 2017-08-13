package com.ik.networkhandler.controller;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ik.networkhandler.R;
import com.ik.networkhandler.common.ConnectionListener;
import com.ik.networkhandler.common.Utils;
import com.ik.networkhandler.interfaces.IViewCallback;
import com.ik.networkhandler.model.PostsService;
import com.ik.networkhandler.model.pojo.Post;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IViewCallback, ConnectionListener.Callback{

    private RecyclerView recyclerView;
    private TextView tvNoInternet;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    private DataAdapter adapter;
    private PostsService postsService;
    private boolean hasPostFetched;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tvNoInternet = (TextView) findViewById(R.id.tv_no_internet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        postsService = new PostsService(this);

        if (Utils.hasNetworkAccess(this)) {
            postsService.fetchPosts();
        } else {
            showNoInternetMsg();
        }
    }

    private void refresh() {
        if (Utils.hasNetworkAccess(this)) {
            swipeRefreshLayout.setRefreshing(true);
            postsService.fetchPosts();
        }
    }

    private void showNoInternetMsg(){
        if (!hasPostFetched) {
            tvNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void hideNoInternetMsg(){
        tvNoInternet.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectionListener.registerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConnectionListener.removeListener();
    }

    @Override
    public void postsFetched(List<Post> posts) {
        hasPostFetched = true;
        swipeRefreshLayout.setRefreshing(false);
        if (adapter == null) {
            adapter = new DataAdapter(posts);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(posts);
        }
    }

    @Override
    public void showMessage(String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showMessage(int resId) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, resId, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onConnected() {
        //device connected to internet

        hideNoInternetMsg();

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.online, Snackbar.LENGTH_LONG);
        snackbar.show();

        if (!hasPostFetched) {
            postsService.fetchPosts();
        }
    }

    @Override
    public void onDisconnected() {
        //device not connected to internet

        showNoInternetMsg();

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.offline, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

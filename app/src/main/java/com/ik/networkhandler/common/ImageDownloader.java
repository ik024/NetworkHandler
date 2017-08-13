package com.ik.networkhandler.common;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.ik.network_handler_lib.NetworkHandler;
import com.ik.network_handler_lib.utils.INetworkCallback;
import com.ik.network_handler_lib.utils.NetworkResponseType;
import com.ik.networkhandler.R;

import timber.log.Timber;

public class ImageDownloader {

    public static void loadBitmap(String url, final ImageView imageView){
        NetworkHandler handler = NetworkHandler.getNetworkHandler(NetworkResponseType.IMAGE);
        handler.setEndPoint(url);
        handler.executeAsync(new INetworkCallback() {
            @Override
            public void onSuccess(Object object) {
                Bitmap bitmap = (Bitmap) object;
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onError(String message) {
                Timber.e(message);
                imageView.setImageResource(R.drawable.no_image);
            }
        });
    }
}

package com.ik.network_handler_lib;

import android.support.test.runner.AndroidJUnit4;

import com.ik.network_handler_lib.utils.NetworkResponseType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NetworkHandlerTest {

    @Test
    public void testGetNetworkHandler(){
        assertTrue(NetworkHandler.getNetworkHandler(NetworkResponseType.IMAGE) instanceof  ImageHandler);
        assertTrue(NetworkHandler.getNetworkHandler(NetworkResponseType.STRING) instanceof  StringHandler);
    }
}
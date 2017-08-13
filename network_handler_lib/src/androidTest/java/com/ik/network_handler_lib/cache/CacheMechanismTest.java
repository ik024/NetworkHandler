package com.ik.network_handler_lib.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.runner.AndroidJUnit4;

import com.ik.network_handler_lib.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CacheMechanismTest {
    CacheMechanism cacheMechanism;
    @Before
    public void setUp(){
        cacheMechanism = new CacheMechanism();
    }

    @Test
    public void testContains() throws Exception {
        cacheMechanism.putObject("test1", new CacheValue("test1", String.class));
        cacheMechanism.putObject("test2", new CacheValue("test2", String.class));

        assertTrue(cacheMechanism.contains("test1"));
    }

    @Test
    public void testGetObject() throws Exception {
        Bitmap bitmap = BitmapFactory.decodeResource(
                getInstrumentation().getContext().getResources(), R.drawable.test);

        CacheValue value1 = new CacheValue(bitmap, Bitmap.class);
        CacheValue value2 = new CacheValue("test1", String.class);


        cacheMechanism.putObject("value1", value1);
        cacheMechanism.putObject("value2", value2);

        CacheValue returnedValue1 = cacheMechanism.getObject("value1");
        CacheValue returnedValue2 = cacheMechanism.getObject("value2");

        assertTrue(returnedValue1.getObject() instanceof Bitmap);
        assertTrue(returnedValue2.getObject() instanceof String);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalResizeValue() throws Exception {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int CACHE_SIZE = maxMemory+1;
        cacheMechanism.resize(CACHE_SIZE);
    }

}
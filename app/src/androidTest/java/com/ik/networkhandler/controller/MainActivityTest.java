package com.ik.networkhandler.controller;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.ik.networkhandler.R;
import com.ik.networkhandler.model.PostsService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivity =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testRecyclerView() {
        Espresso.registerIdlingResources(PostsService.idlingResource);
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void swipeToRefresh(){
        onView(withId(R.id.swipe_refresh_layout)).perform(swipeDown()).check(matches(isDisplayed()));
        Espresso.registerIdlingResources(PostsService.idlingResource);
        onView(withId(R.id.snackbar_text))
                .check(matches(withText(R.string.posts_fetched)));
    }

}
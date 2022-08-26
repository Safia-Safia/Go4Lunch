package com.safia.go4lunch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

import com.safia.go4lunch.controller.activity.HomeActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.safia.go4lunch", appContext.getPackageName());
    }

    private HomeActivity mActivity;

    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule =
            new ActivityTestRule(HomeActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @Test
    public void selectFragment() {
        onView(ViewMatchers.withId( R.id.list_view)).perform(click());
        onView(ViewMatchers.withId( R.id.workmates)).perform(click());
        onView(ViewMatchers.withId( R.id.card_view)).perform(click());
    }

    @Test
    public void openRestaurantDetail() throws UiObjectNotFoundException {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Chez Janou"));
        marker.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addToFavorite () throws UiObjectNotFoundException {
        openRestaurantDetail();
        onView(ViewMatchers.withId( R.id.like_button)).perform(click());
    }

    @Test
    public void callRestaurant () throws UiObjectNotFoundException {
        openRestaurantDetail();
        onView(ViewMatchers.withId( R.id.phone_button)).perform(click());
    }

    @Test
    public void getWebsite () throws UiObjectNotFoundException {
        openRestaurantDetail();
        onView(ViewMatchers.withId( R.id.website_button)).perform(click());
    }
    @Test
    public void selectRestaurant () throws UiObjectNotFoundException {
        openRestaurantDetail();
        onView(ViewMatchers.withId( R.id.fav)).perform(click());
    }

    @Test
    public void openNavigation (){}
}
package com.example.equipu.profeplus;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.StartActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class StartingTest {

    private String txtUni;
    private String txtTeacher;

    @Rule
    public ActivityTestRule<StartActivity> mActivityRule = new ActivityTestRule<>(
            StartActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        txtUni = mActivityRule.getActivity()
                .getString(R.string.text_uni_education);
        txtUni = mActivityRule.getActivity()
                .getString(R.string.text_uni_education);

    }

    @Test
    public void checkModes() {
        /*
        onView(withId(R.id.btn_uni_mode))
                .perform(typeText(mStringToBetyped), closeSoftKeyboard());
        onView(withId(R.id.changeTextBt)).perform(click());

        */

        // Check that the text was changed.
        onView(withId(R.id.btn_uni_mode))
                .check(matches(withText(txtUni)));
        onView(withId(R.id.btn_school_mode))
                .check(matches(isDisplayed()));

    }

    @Test
    public void clickUniMode(){
        onView(withId(R.id.btn_uni_mode))
                .perform(click());

        onView(withId(R.id.btn_user_teacher))
                .check(matches(isDisplayed()));


    }

}
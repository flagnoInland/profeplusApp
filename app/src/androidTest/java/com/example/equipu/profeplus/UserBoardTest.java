package com.example.equipu.profeplus;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.StartActivity;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserBoardTest {

    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;
    EvaluationParcel evaluationParcel;

    private String TOKEN = "eyJpdiI6IlVXZk15Rlk1Qmg5OXpLcDl0cFZld1FVVExBS3UzV01NTXJXTW5cL003REZj" +
            "PSIsInZhbHVlIjoiOEkwRlo2MzZ1aVNRYWJjKzZiM21QZ2JFMDRVYUNJdlwvXC9zUHB2VGYrNGRCeWNOdGRn" +
            "T1JhejVERkhMQkVYYnptYnRQRUM0c0piNmZIajRCMzRTeFVtc1JSTnNNZVYrdFV1YjZwdWNOM21RZStn" +
            "NlgrZk90UWFjSVdxSlwvbXRBK3pSYnhSU0FUbndVVk0zTUNYNUFOZ256cFMxZERzakNoUm9ldG1iRk1adz" +
            "FZPSIsIm1hYyI6IjgzMmU1YWM1M2RlMTI4Y2UzMmJkZWEwMjM3MjhiOTZlNjQ3YWM4NWZjNmNhMWJiZDJkYz" +
            "E5MTAyMDRjMjg0ZTUifQ";

    @Rule
    public ActivityTestRule<StartActivity> mActivityRule = new ActivityTestRule<>(
            StartActivity.class,true,false);

    @Before
    public void initValidString() {
        // Specify a valid string.
        appStateParcel = new AppStateParcel();
        sessionParcel = new SessionParcel();
        evaluationParcel = new EvaluationParcel();
    }


    @Test
    public void evaluationButtonTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_uni_mode)).perform(click());

        onView(withId(R.id.btn_user_teacher)).perform(click());

        onView(withId(R.id.btn_start_login)).perform(click());

        onView(withId(R.id.edt_username))
                .perform(clearText(),typeText("herbertacg@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.edt_password))
                .perform(click());

        onView(withId(R.id.edt_password))
                .perform(clearText(),typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.btn_signin)).perform(click());

        onView(allOf(withId(R.id.btn_evaluation),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .check(matches(isDisplayed()));

    }

    @Test
    public void evaluationStudentCodeTest(){

        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_uni_mode)).perform(click());

        onView(withId(R.id.btn_user_student)).perform(click());

        onView(withId(R.id.btn_start_login)).perform(click());

        onView(withId(R.id.edt_username))
                .perform(clearText(),typeText("stefan@mail.com"), closeSoftKeyboard());

        onView(withId(R.id.edt_password))
                .perform(click());

        onView(withId(R.id.edt_password))
                .perform(clearText(),typeText("1234"), closeSoftKeyboard());

        onView(withId(R.id.btn_signin)).perform(click());

        onView(withId(R.id.edtinputcode))
                .perform(clearText(),typeText("7408"), closeSoftKeyboard());

        onView(allOf(withId(R.id.start_task_btn),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());


    }




}

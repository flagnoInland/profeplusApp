package com.example.equipu.profeplus;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.UserBoardActivity;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EmailTest {

    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;
    EvaluationParcel evaluationParcel;

    private String TOKEN = "eyJpdiI6ImZ3NGJPVjJWYUtUazZxR3B6K0R1WkVYN1FXak5SdUJualRlQ2ds" +
            "ZUZSY2M9IiwidmFsdWUiOiJzMjBFc1JuZFU5NDhTcFhieWl3M0VGa1BsY0FlODdZXC9EaFFFeEh" +
            "WSFhvWXNvMnkxQjVsVzA0QThVYkdpcWNZUW04U0E1Zk12a0NLNlcrallmOHhxd3hhSlwvQmx1a" +
            "EVrZ3hXN2lsSWpnbFRGa1lvcWJrdW5vVXo1YUx2eVR6NStHejFxUGU0TVFxY0puYVBjaWNrV29n" +
            "OTZGXC90cEFBcXlcL0I2WnRRcE5uSHBVPSIsIm1hYyI6IjJlNmQ3ODZkNjE5OWY2NjRiNDU0Yz" +
            "UyMzI3YmRlZThhZDgxYjhlY2IyNjBiYmZhMjU1OWU4ZGFhNjM3MWE1MmYifQ";

    @Rule
    public ActivityTestRule<UserBoardActivity> mActivityRule = new ActivityTestRule<>(
            UserBoardActivity.class,true,false);

    @Before
    public void initValidString() {
        // Specify a valid string.
        appStateParcel = new AppStateParcel();
        sessionParcel = new SessionParcel();
        evaluationParcel = new EvaluationParcel();
        appStateParcel.setAppMode(AppStateParcel.NORMAL_MODE);
        appStateParcel.setUserType(AppStateParcel.TEACHER);
        appStateParcel.setUserId("1");
        appStateParcel.setToken(TOKEN);
    }


    @Test
    public void mailTest(){

        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);


        onView(withId(R.id.btn_free_course)).perform(click());

        onView(withId(R.id.chkb_free_exercises)).perform(click());

        onView(withId(R.id.btn_results)).perform(click());

        onView(withId(R.id.btn_finish_one)).perform(click());

        onView(withId(R.id.btn_start_two)).perform(click());

        onView(withId(R.id.btn_finish_two)).perform(click());

        onView(withId(R.id.btn_get_report)).perform(click());

        onView(withId(R.id.spn_answer_key)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("B"))).perform(click());

        onView(withId(R.id.btn_simple_report)).perform(click());


    }


}

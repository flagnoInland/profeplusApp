package com.example.equipu.profeplus;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.TeacherJobActivity;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateLessonsTest {

    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;


    private String TOKEN = "eyJpdiI6IjZKb2dGMkNneDdaR0ltNjZIMjA2V21HY1pVa0FMd0EyOGpPZ1VwNUl" +
            "jaWM9IiwidmFsdWUiOiJ6QWh0bytNZFphMU9VblB2ZGZ1VlZteTVEaDRWTVJUNVhlVzVHb1lkS0VES" +
            "VpheHJ6RDZcL0NqY3BBMzZLNzhVdTJGN1lUaFcxS2g1M0U5Nmk2MVA4YXp6NzM2bmIydDJrcjRpV2ZX" +
            "N3oyVElMR1FTeUQ5RzdkazNUTjYyTnZoUlwvSWw1YmQ2MXFTM0w0eFFcL3FwNkdNWk5ldXhMaFg5UTFr" +
            "VnNhQjJydWw0UG89IiwibWFjIjoiOGQwYmQ4ZTQ2NjM2MDFiMWYxMzM3OTkyMTU3NmY1YjIyOWIzNjM0MD" +
            "I4NTA2YjBmMWU1OGFiYWM1NWJkYTQzNyJ9";

    @Rule
    public ActivityTestRule<TeacherJobActivity> mActivityRule = new ActivityTestRule<>(
            TeacherJobActivity.class,true,false);

    @Before
    public void initValidString() {
        // Specify a valid string.
        appStateParcel = new AppStateParcel();
        sessionParcel = new SessionParcel();
        appStateParcel.setUserId("1");
        appStateParcel.setToken(TOKEN);
        appStateParcel.setAppMode(AppStateParcel.NORMAL_MODE);
        appStateParcel.setUserType(AppStateParcel.TEACHER);
        sessionParcel.setQuestionType(SessionParcel.Q_NORMAL);
        sessionParcel.setCourseId(-1);
        sessionParcel.setAccesscode("0000");
        sessionParcel.setNewSession(1);
        sessionParcel.setInactive(0);

    }


    @Test
    public void newFreeQuestionType(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);


        // Choose question type
        onView(withId(R.id.chkb_free_exercises)).perform(click());

        // Lesson Code
        onView(withId(R.id.btn_results)).perform(scrollTo(), click());

        // Charts
        onView(withId(R.id.btn_finish_one)).perform(click());

        // Exit
        onView(withId(R.id.btn_start_two)).check(matches(isDisplayed()));

        onView(withId(R.id.img_exit)).perform(click());

    }


    @Test
    public void newCodedQuestionType(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);

        // Choose question type
        onView(withId(R.id.chkb_normal_exercises)).perform(click());

        // Lesson Code
        onView(withId(R.id.btn_results)).perform(scrollTo(), click());

        // Subject and exercise
        onView(withId(R.id.edt_topic_number))
                .perform(clearText(),typeText("1"), closeSoftKeyboard());

        onView(withId(R.id.edt_exercise_number))
                .perform(click(),clearText(),typeText("43"), closeSoftKeyboard());

        onView(withId(R.id.btn_results_chart)).perform(click());

        // Charts
        onView(withId(R.id.btn_finish_one)).perform(click());

        // Exit
        onView(withId(R.id.btn_start_two)).check(matches(isDisplayed()));

        onView(withId(R.id.img_exit)).perform(click());

    }

    @Test
    public void newTrueQuestionType(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);


        // Choose question type
        onView(withId(R.id.chkb_dichotomous)).perform(click());

        // Lesson Code
        onView(withId(R.id.btn_results)).perform(scrollTo(), click());

        // Charts
        onView(withId(R.id.btn_finish_one)).perform(click());

        // Exit
        onView(withId(R.id.btn_start_two)).check(matches(isDisplayed()));

        onView(withId(R.id.img_exit)).perform(click());

    }

    @Test
    public void newSurveySatisfactionType(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);


        // Choose question type
        onView(withId(R.id.chkb_opinion)).perform(click());

        // Choose opinion type
        onView(withId(R.id.chkb_opinion1)).perform(click());

        // Lesson Code
        onView(withId(R.id.btn_results)).perform(scrollTo(), click());

        // Charts
        onView(withId(R.id.btn_finish_one)).perform(click());

        // Exit
        onView(withId(R.id.btn_finish_survey)).check(matches(isDisplayed()));

        onView(withId(R.id.img_exit)).perform(click());

    }

    @Test
    public void newSurveyAgreementType(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);


        // Choose question type
        onView(withId(R.id.chkb_opinion)).perform(click());

        // Choose opinion type
        onView(withId(R.id.chkb_opinion2)).perform(click());

        // Lesson Code
        onView(withId(R.id.btn_results)).perform(scrollTo(), click());

        // Charts
        onView(withId(R.id.btn_finish_one)).perform(click());

        // Exit
        onView(withId(R.id.btn_finish_survey)).check(matches(isDisplayed()));

        onView(withId(R.id.img_exit)).perform(click());

    }

    @Test
    public void newSurveyQualificationType(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);


        // Choose question type
        onView(withId(R.id.chkb_opinion)).perform(click());

        // Choose opinion type
        onView(withId(R.id.chkb_opinion3)).perform(click());

        // Lesson Code
        onView(withId(R.id.btn_results)).perform(scrollTo(), click());

        // Charts
        onView(withId(R.id.btn_finish_one)).perform(click());

        // Exit
        onView(withId(R.id.btn_finish_survey)).check(matches(isDisplayed()));

        onView(withId(R.id.img_exit)).perform(click());

    }

    @Test
    public void newSurveySpeakerType(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);


        // Choose question type
        onView(withId(R.id.chkb_opinion)).perform(click());

        // Choose opinion type
        onView(withId(R.id.chkb_opinion4)).perform(click());

        // Lesson Code
        onView(withId(R.id.btn_results)).perform(scrollTo(), click());

        // Charts
        onView(withId(R.id.btn_finish_one)).perform(click());

        // Exit
        onView(withId(R.id.btn_finish_survey)).check(matches(isDisplayed()));

        onView(withId(R.id.img_exit)).perform(click());

    }




}

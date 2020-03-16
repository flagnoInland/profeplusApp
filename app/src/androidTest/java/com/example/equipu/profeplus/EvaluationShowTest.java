package com.example.equipu.profeplus;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
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
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EvaluationShowTest {

    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;
    EvaluationParcel evaluationParcel;

    private String TOKEN = "eyJpdiI6InRNclB6YUdOakc3S0VxcHFPdEwxMm1qbEZpNGg1YnJkbXJyUTc3YnFMRjA9" +
            "IiwidmFsdWUiOiJcL0s5MG9EN3VCODVHRGFhM2FCVlh2cFwvenpiY3BGUituTXhNa29WXC9wSVp4ZHBSa0tu" +
            "VWxIcm9ZcjBZV2E5YXN4M09FNUxcL3ZFeVpvbUVDSWFCK0xVSEtlaHV4TWxZZVV6anlNQWxnYkdPMUROdFg0R" +
            "Eg0M3cyYlJrcFZkQlozcXpnXC9La1RQN3ZMR3BqUm9RWDBQaCt0UkJrNlNFdW85bDJ0czNUclhxU0JURT0iLC" +
            "JtYWMiOiJlYzdiNzY2OTFkOTY5ODg0ZTRhNDI4Yzk1YWRiMGQzZjhkNGRkNmIwOGYxYmYwMDY3Yzg4MTI" +
            "xNTBhMGYzNGVhIn0";

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
        appStateParcel.setEvaluations(2);
    }


    @Test
    public void evaluationButtonTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);

        onView(allOf(withId(R.id.btn_evaluation),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.btn_evaluation),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());

        onView(allOf(withId(R.id.txt_evaluation),
                withText("Midterm Exam")))
                .perform(click());

    }

    @Test
    public void evaluationInactiveStatusTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);

        onView(allOf(withId(R.id.btn_evaluation),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());

        // Active Evaluation Fragment

        onView(allOf(withId(R.id.txt_evaluation),
                withText("Final Exam")))
                .perform(click());

        // Evaluation Code Fragment

        onView(withId(R.id.btn_edit)).perform(click());

        // Evaluation first step
        onView(withId(R.id.txt_number_questions))
                .check(matches(isDisplayed()));

        onView(withId(R.id.txt_overall_score))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_equal_weight))
                .check(matches(isDisplayed()));

        onView(withId(R.id.txt_number_questions))
                .perform(clearText(), typeText("4"), closeSoftKeyboard());

        onView(withId(R.id.txt_overall_score))
                .perform(clearText(), typeText("4"), closeSoftKeyboard());

        onView(withId(R.id.btn_equal_weight)).perform(click());

        // Evaluation second step
        onView(withId(R.id.spn_start_now))
                .check(matches(isDisplayed()));

        onView(withId(R.id.spn_start_now)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("SI"))).perform(click());


        onView(withId(R.id.edt_duration))
                .perform(clearText(), typeText("120"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_next),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());

        // Evaluation third step
        onView(withId(R.id.edt_course_name))
                .perform(clearText(), typeText("Zepellin"), closeSoftKeyboard());

        onView(withId(R.id.edt_speciality))
                .perform(clearText(), typeText("Space"), closeSoftKeyboard());

        onView(withId(R.id.edt_institution))
                .perform(clearText(), typeText("UCLA"), closeSoftKeyboard());

        onView(allOf(withId(R.id.edt_header), isDisplayed()))
                .perform(scrollTo(), clearText(), typeText("Midterm Exam"), closeSoftKeyboard());


        onView(allOf(withId(R.id.btn_next),withContentDescription("btnNextStepThreeEvaluation")))
                .perform(scrollTo(), click());

        // Evaluation fourth step
        onView(withId(R.id.edt_observation))
                .check(matches(isDisplayed()));

        onView(withId(R.id.edt_observation))
                .perform(clearText(), typeText("Edit"), closeSoftKeyboard());

        onView(withId(R.id.chck_calculator)).perform(click());

        onView(withId(R.id.chck_notes)).perform(click());

        onView(withId(R.id.chck_computer)).perform(click());


        onView(allOf(withId(R.id.btn_next),withContentDescription("btnNextStepFourEvaluation")))
                .perform(scrollTo(), click());

        // Evaluation fifth step
        // Must be 4 spinners
        onView(withId(R.id.spn_pos1))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.spn_pos1),withContentDescription("pos1")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());

        onView(allOf(withId(R.id.spn_pos2),withContentDescription("pos2")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("B"))).perform(click());

        onView(allOf(withId(R.id.spn_pos3),withContentDescription("pos3")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("E"))).perform(click());

        onView(allOf(withId(R.id.spn_pos4),withContentDescription("pos4")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("C"))).perform(click());

        onView(allOf(withId(R.id.btn_finish),
                withContentDescription("btnFinishStepEval5"),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(scrollTo(), click());

        // Evaluation Code

        onView(allOf(withId(R.id.btn_finish),
                withContentDescription("btnFinishSessionCode"),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(scrollTo(), click());


    }



}

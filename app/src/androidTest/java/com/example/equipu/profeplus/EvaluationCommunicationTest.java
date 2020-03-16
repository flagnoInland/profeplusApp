package com.example.equipu.profeplus;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.TeacherJobActivity;
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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EvaluationCommunicationTest {

    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;
    EvaluationParcel evaluationParcel;

    @Rule
    public ActivityTestRule<TeacherJobActivity> mActivityRule = new ActivityTestRule<>(
            TeacherJobActivity.class,true,false);

    @Before
    public void initValidString() {
        // Specify a valid string.
        appStateParcel = new AppStateParcel();
        sessionParcel = new SessionParcel();
        evaluationParcel = new EvaluationParcel();
        appStateParcel.setAppMode(AppStateParcel.NORMAL_MODE);
        appStateParcel.setUserType(AppStateParcel.TEACHER);
        sessionParcel.setQuestionType(SessionParcel.Q_NORMAL);
        sessionParcel.setCourseId(-1);
        sessionParcel.setAccesscode("0000");
        sessionParcel.setNewSession(1);
        sessionParcel.setInactive(0);

    }

    /*
    @Test
    public void chooseQuestionTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.chkb_free_exercises))
                .check(matches(isDisplayed()));

    }
    */

    @Test
    public void newEvaluatioTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);

        // Choose evaluation type
        onView(withId(R.id.chkb_eval)).perform(click());

        // Evaluation first step
        onView(withId(R.id.txt_number_questions))
                .check(matches(isDisplayed()));

        onView(withId(R.id.txt_overall_score))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_equal_weight))
                .check(matches(isDisplayed()));

        onView(withId(R.id.txt_number_questions))
                .perform(typeText("9"), closeSoftKeyboard());

        onView(withId(R.id.txt_overall_score))
                .perform(typeText("9"), closeSoftKeyboard());

        onView(withId(R.id.btn_equal_weight)).perform(click());

        // Evaluation second step
        onView(withId(R.id.spn_start_now))
                .check(matches(isDisplayed()));

        onView(withId(R.id.spn_start_now)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("SI"))).perform(click());


        onView(withId(R.id.edt_duration))
                .perform(typeText("120"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_next),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());

        // Evaluation third step
        onView(withId(R.id.edt_course_name))
                .perform(typeText("Rockets"), closeSoftKeyboard());

        onView(withId(R.id.edt_speciality))
                .perform(typeText("Space"), closeSoftKeyboard());

        onView(withId(R.id.edt_institution))
                .perform(typeText("Standford"), closeSoftKeyboard());

        onView(allOf(withId(R.id.edt_header), isDisplayed()))
                .perform(scrollTo(), typeText("Final Exam"), closeSoftKeyboard());


        onView(allOf(withId(R.id.btn_next),withContentDescription("btnNextStepThreeEvaluation")))
                .perform(scrollTo(), click());

        // Evaluation fourth step
        onView(withId(R.id.edt_observation))
                .check(matches(isDisplayed()));

        onView(withId(R.id.edt_observation))
                .perform(typeText("None"), closeSoftKeyboard());

        onView(withId(R.id.chck_calculator)).perform(click());

        onView(withId(R.id.chck_notes)).perform(click());

        onView(withId(R.id.chck_books)).perform(click());

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

        onView(allOf(withId(R.id.btn_next),withContentDescription("esp5f5")))
                .perform(scrollTo(), click());

        // Next four questions
        onView(allOf(withId(R.id.spn_pos1),withContentDescription("pos5")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("D"))).perform(click());

        onView(allOf(withId(R.id.spn_pos2),withContentDescription("pos6")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("E"))).perform(click());

        onView(allOf(withId(R.id.spn_pos3),withContentDescription("pos7")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("E"))).perform(click());

        onView(allOf(withId(R.id.spn_pos4),withContentDescription("pos8")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());

        onView(allOf(withId(R.id.btn_next),withContentDescription("esp5f9")))
                .perform(scrollTo(), click());

        // Final question
        onView(allOf(withId(R.id.spn_pos1),withContentDescription("pos9")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("C"))).perform(click());

        /*
        pressBack();

        //Again
        onView(allOf(withId(R.id.spn_pos1),withContentDescription("pos5")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("D"))).perform(click());

        onView(allOf(withId(R.id.spn_pos2),withContentDescription("pos6")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("E"))).perform(click());

        onView(allOf(withId(R.id.spn_pos3),withContentDescription("pos7")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("E"))).perform(click());

        onView(allOf(withId(R.id.spn_pos4),withContentDescription("pos8")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("A"))).perform(click());

        onView(allOf(withId(R.id.btn_next),withContentDescription("esp5f9")))
                .perform(scrollTo(), click());

        // Final question
        onView(allOf(withId(R.id.spn_pos1),withContentDescription("pos9")))
                .perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("C"))).perform(click());
        */

        onView(allOf(withId(R.id.btn_finish),
                withContentDescription("btnFinishStepEval5"),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(scrollTo(), click());






    }




}

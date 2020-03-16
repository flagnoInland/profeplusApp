package com.example.equipu.profeplus;

import android.content.Intent;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.TeacherJobActivity;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import org.hamcrest.Matchers;
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
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EvaluationCodeTest {

    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;
    EvaluationParcel evaluationParcel;

    private String TOKEN = "eyJpdiI6IlkraU5BQWZOc3BaZzFnNG1kUktNbEh0cUk0d3ZySU56K09DUjU1Y1wvUVMw" +
            "PSIsInZhbHVlIjoibWpYbDVyOVRFSDRKcFk2bjhBeWFRQmd1SjdIcTQ2MkJoQVVwMkNNT1M4c3dUWnMxSHF" +
            "PRkIxRDMrZjVVXC9NZWlQSlN4VmlvZnEwR2s0ck1ZWjVuYWhsanNITGlrU1F3SXU1ZUh6Mkc3eloxZ0UydlE" +
            "xbitCUG5OYmR1aWlLV1JoSlwvRGR6blBWc0JSYVJIckxNV2ppRVlNQnRnejJ4SUluSmo2TnhhSU5lNFE9Iiw" +
            "ibWFjIjoiMTk1MTc0ZWUxM2Y0ZDE4ZWExYTgxMTM0MmJlYzYwNTg4NzI4M2QwYTc1YzljYTQ4NGQ5Nj" +
            "M5Y2VhNGM3NzljZiJ9";

    @Rule
    public ActivityTestRule<TeacherJobActivity> mActivityRule = new ActivityTestRule<>(
            TeacherJobActivity.class,true,false);

    @Before
    public void initValidString() {
        // Specify a valid string.
        appStateParcel = new AppStateParcel();
        sessionParcel = new SessionParcel();
        evaluationParcel = new EvaluationParcel();
        appStateParcel.setUserId("1");
        appStateParcel.setToken(TOKEN);
        appStateParcel.setAppMode(AppStateParcel.NORMAL_MODE);
        appStateParcel.setUserType(AppStateParcel.TEACHER);
        appStateParcel.setSaveEvaluation(0);
        appStateParcel.setEditEvaluation(0);
        sessionParcel.setQuestionType(SessionParcel.Q_NORMAL);
        sessionParcel.setCourseId(-1);
        sessionParcel.setAccesscode("0000");
        sessionParcel.setNewSession(1);
        sessionParcel.setInactive(0);

    }


    @Test
    public void newEvaluationTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        resultData.putExtra(LearnApp.PCL_SESSION_PARCEL,sessionParcel);
        mActivityRule.launchActivity(resultData);

        // Choose evaluation type
        onView(withId(R.id.chkb_eval)).perform(scrollTo(),click());

        // Evaluation first step
        onView(withId(R.id.txt_number_questions))
                .check(matches(isDisplayed()));

        onView(withId(R.id.txt_overall_score))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btn_equal_weight))
                .check(matches(isDisplayed()));

        onView(withId(R.id.edt_number_questions))
                .perform(typeText("4"), closeSoftKeyboard());

        onView(withId(R.id.edt_overall_score))
                .perform(typeText("4"), closeSoftKeyboard());

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
                .perform(typeText("Stanford"), closeSoftKeyboard());

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

    @Test
    public void saveEvaluationTest(){
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
                .perform(typeText("4"), closeSoftKeyboard());

        onView(withId(R.id.txt_overall_score))
                .perform(typeText("4"), closeSoftKeyboard());

        onView(withId(R.id.btn_equal_weight)).perform(click());

        // Evaluation second step
        onView(withId(R.id.spn_start_now))
                .check(matches(isDisplayed()));

        onView(withId(R.id.spn_start_now)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("NO"))).perform(click());


        onView(withId(R.id.edt_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2016, 7, 14));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.edt_start)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(14, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.edt_end)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(16, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(allOf(withId(R.id.btn_next),withContentDescription("btnNextStepTwoEvaluation")))
                .perform(scrollTo(), click());

        // Evaluation third step
        onView(withId(R.id.edt_course_name))
                .perform(typeText("Rockets"), closeSoftKeyboard());

        onView(withId(R.id.edt_speciality))
                .perform(typeText("Space"), closeSoftKeyboard());

        onView(withId(R.id.edt_institution))
                .perform(typeText("Stanford"), closeSoftKeyboard());

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

        onView(allOf(withId(R.id.btn_finish),
                withContentDescription("btnFinishStepEval5"),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(scrollTo(), click());


        // Evaluation Code

        onView(allOf(withId(R.id.btn_save),
                withContentDescription("btnSaveSessionCode"),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(scrollTo(), click());


    }


}

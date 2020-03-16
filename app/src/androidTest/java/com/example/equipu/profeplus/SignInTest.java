package com.example.equipu.profeplus;

import android.content.Intent;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.StartActivity;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.equipu.profeplus.models.UserParcel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
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
public class SignInTest {

    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;
    UserParcel userParcel;

    private String TOKEN = "eyJpdiI6InNRRmI2MWZabTAzQkZ2MVwvakVzUWdcL1hFdUgwMVpzZ09iMTZ2REZp" +
            "UlwvWlk9IiwidmFsdWUiOiIxRGFqY2NjS0dtQis3WDRXTlZ4NDhJK3FaR1VXdzNCaEdZblFsd3VOM2" +
            "NRK3hobENiSk5rbTZpVkdKTTdxaElGd1R4eU83bFdQbnp5ZEtKNVlzb1wvaVAzWExkNlBiOGlZRHJtTm" +
            "90VklIeVRQdkRxY3Q5XC9PVkowNlhmeWlQTk9mUFpzdEZmb1wvdldwdkVTRmpPeitucFh2NXZjRlwvSkx" +
            "PWFZHXC9xUlNMdzBaST0iLCJtYWMiOiJkNWViNTkwYTczZGRhZTc0Nzg4OTRmZDU5OWE4YjU3M2I2ZWNl" +
            "M2EzNTY2Mjg2ZmZmODQxM2M0ZmRmN2NlMjJiIn0";

    @Rule
    public ActivityTestRule<StartActivity> mActivityRule = new ActivityTestRule<>(
            StartActivity.class,true,false);

    @Before
    public void initValidString() {
        // Specify a valid string.
        appStateParcel = new AppStateParcel();
        userParcel = new UserParcel();

    }


    @Test
    public void newSignInUniTeacherTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_uni_mode)).perform(click());

        onView(withId(R.id.btn_user_teacher)).perform(click());

        onView(withId(R.id.btn_sign_up)).perform(click());

        // Sign in first step
        onView(withId(R.id.sign_up_name))
                .perform(typeText("Mike"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_surname))
                .perform(typeText("Smith"), closeSoftKeyboard());


        onView(withId(R.id.gender_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Masculino")))
                .perform(click());

        onView(withId(R.id.sign_up_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(1988, 7, 23));
        onView(withId(android.R.id.button1)).perform(click());

        onView(allOf(withId(R.id.btn_next),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());

        // Sign in second step
        onView(withId(R.id.sign_up_dni))
                .perform(typeText("123123123"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_pass))
                .perform(typeText("1234"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_pass_again))
                .perform(typeText("1234"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_next),withContentDescription("nextsignin2")))
                .perform(click());

        // Sign in third step
        onView(withId(R.id.sign_up_phone))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_mail))
                .perform(typeText("mike@mail.com"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_city))
                .perform(typeText("Bogota"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_nationality)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Colombia")))
                .perform(click());

        onView(withId(R.id.chk_terms)).perform(click());

        onView(allOf(withId(R.id.btn_next),withContentDescription("nextsignin3")))
                .perform(click());

        // Choose user

        onView(withId(R.id.btn_user_teacher)).perform(click());

        // Click Dialog

        onView(withId(R.id.app_dlg_btn_exit)).perform(click());

        // Teacher Board
        onView(withId(R.id.btn_protocol))
                .check(matches(isDisplayed()));


    }

    @Test
    public void newSignInUniStudentTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_uni_mode)).perform(click());

        onView(withId(R.id.btn_user_student)).perform(click());

        onView(withId(R.id.btn_sign_up)).perform(click());

        // Sign in first step
        onView(withId(R.id.sign_up_name))
                .perform(typeText("Sue"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_surname))
                .perform(typeText("Smith"), closeSoftKeyboard());


        onView(withId(R.id.gender_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Femenino")))
                .perform(click());

        onView(withId(R.id.sign_up_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(1995, 7, 23));
        onView(withId(android.R.id.button1)).perform(click());

        onView(allOf(withId(R.id.btn_next),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());

        // Sign in second step
        onView(withId(R.id.sign_up_dni))
                .perform(typeText("123123123"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_pass))
                .perform(typeText("1234"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_pass_again))
                .perform(typeText("1234"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_next),withContentDescription("nextsignin2")))
                .perform(click());

        // Sign in third step
        onView(withId(R.id.sign_up_phone))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_mail))
                .perform(typeText("sue@mail.com"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_city))
                .perform(typeText("San Jose"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_nationality)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Canadá")))
                .perform(click());

        onView(withId(R.id.chk_terms)).perform(click());

        onView(allOf(withId(R.id.btn_next),withContentDescription("nextsignin3")))
                .perform(click());

        // Choose user

        onView(withId(R.id.btn_user_student)).perform(click());

        // Sign in fourth step

        onView(withId(R.id.edt_institution))
                .perform(typeText("UCLA"), closeSoftKeyboard());

        onView(withId(R.id.edt_speciality))
                .perform(typeText("Leyes"), closeSoftKeyboard());

        onView(withId(R.id.edt_student_id))
                .perform(typeText("QER1234"), closeSoftKeyboard());


        onView(allOf(withId(R.id.btn_next),withContentDescription("nextsignin4")))
                .perform(click());


        // Student Board
        onView(withId(R.id.start_task_btn))
                .check(matches(isDisplayed()));

    }

    @Test
    public void newSignInSchoolTeacherTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_school_mode)).perform(click());

        onView(withId(R.id.btn_user_teacher)).perform(click());

        onView(withId(R.id.btn_sign_up)).perform(click());

        // Sign in first step
        onView(withId(R.id.sign_up_name))
                .perform(typeText("Juan"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_surname))
                .perform(typeText("Perez"), closeSoftKeyboard());


        onView(withId(R.id.gender_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Masculino")))
                .perform(click());

        onView(withId(R.id.sign_up_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(1990, 7, 23));
        onView(withId(android.R.id.button1)).perform(click());

        onView(allOf(withId(R.id.btn_next),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());

        // Sign in second step
        onView(withId(R.id.sign_up_dni))
                .perform(typeText("123123123"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_pass))
                .perform(typeText("1234"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_pass_again))
                .perform(typeText("1234"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_next),withContentDescription("nextsignin2")))
                .perform(click());

        // Sign in third step
        onView(withId(R.id.sign_up_phone))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_mail))
                .perform(typeText("juan.perez@mail.com"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_city))
                .perform(typeText("Lima"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_nationality)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Perú")))
                .perform(click());

        onView(withId(R.id.chk_terms)).perform(click());

        onView(allOf(withId(R.id.btn_next),withContentDescription("nextsignin3")))
                .perform(click());

        // Click Dialog

        onView(withId(R.id.app_dlg_btn_exit)).perform(click());

        // Teacher Board
        onView(withId(R.id.btn_protocol))
                .check(matches(isDisplayed()));


    }

    @Test
    public void newSignInShoolStudentTest(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_school_mode)).perform(click());

        onView(withId(R.id.btn_user_student)).perform(click());

        onView(withId(R.id.btn_sign_up)).perform(click());

        // Sign in first step
        onView(withId(R.id.sign_up_name))
                .perform(typeText("Sabrina"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_surname))
                .perform(typeText("Salinas"), closeSoftKeyboard());

        onView(withId(R.id.gender_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Femenino")))
                .perform(click());

        onView(withId(R.id.sign_up_pass))
                .perform(typeText("1234"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_pass_again))
                .perform(typeText("1234"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_next),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(click());

        // Sign in second step

        onView(withId(R.id.sign_up_mail))
                .perform(typeText("sabrina@mail.com"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_school))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_nationality)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Argentina")))
                .perform(click());

        onView(withId(R.id.sign_up_city))
                .perform(typeText("Buenos Aires"), closeSoftKeyboard());

        onView(withId(R.id.chk_terms)).perform(click());

        onView(allOf(withId(R.id.btn_next),withContentDescription("nextsignin2")))
                .perform(click());

            // Student Board
        onView(withId(R.id.start_task_btn))
                .check(matches(isDisplayed()));

    }


}

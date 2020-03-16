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
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditProfileTest {

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
    public void editTeacherProfile(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_uni_mode)).perform(click());

        onView(withId(R.id.btn_user_teacher)).perform(click());

        onView(withId(R.id.btn_start_login)).perform(click());

        // SignIN

        onView(withId(R.id.edt_username))
                .perform(clearText(),typeText("herbertacg@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.edt_password))
                .perform(click(),clearText(),typeText("1234"), closeSoftKeyboard());

        onView(withId(R.id.btn_signin)).perform(click());

        // Teacher Board
        onView(withId(R.id.btn_edit_user)).perform(click());


        // Edit Profile

        onView(withId(R.id.sign_up_name))
                .perform(clearText(), typeText("Herbert"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_surname))
                .perform(clearText(), typeText("CCCC"), closeSoftKeyboard());

        onView(withId(R.id.gender_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Masculino")))
                .perform(click());

        onView(withId(R.id.sign_up_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2010, 7, 23));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.sign_up_dni))
                .perform(clearText(), typeText("321321"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_phone))
                .perform(scrollTo(),clearText(), typeText("111222333"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_nationality)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is("Canadá")))
                .perform(click());

        onView(withId(R.id.sign_up_city))
                .perform(scrollTo(), clearText(), typeText("Toronto"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_next),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(scrollTo(), click());

    }


    @Test
    public void editStudentProfile(){
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_uni_mode)).perform(click());

        onView(withId(R.id.btn_user_teacher)).perform(click());

        onView(withId(R.id.btn_start_login)).perform(click());

        // SignIN

        onView(withId(R.id.edt_username))
                .perform(clearText(),typeText("stefan@mail.com"), closeSoftKeyboard());

        onView(withId(R.id.edt_password))
                .perform(click(),clearText(),typeText("1234"), closeSoftKeyboard());

        onView(withId(R.id.btn_signin)).perform(click());

        // Teacher Board
        onView(withId(R.id.btn_edit_user)).perform(click());


        // Edit Profile

        onView(withId(R.id.sign_up_name))
                .perform(clearText(), typeText("Stefans"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_surname))
                .perform(clearText(), typeText("Mandl"), closeSoftKeyboard());

        onView(withId(R.id.gender_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Masculino")))
                .perform(click());

        onView(withId(R.id.sign_up_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(1983, 7, 21));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.sign_up_dni))
                .perform(clearText(), typeText("234432"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_phone))
                .perform(scrollTo(),clearText(), typeText("123654"), closeSoftKeyboard());

        onView(withId(R.id.sign_up_nationality)).perform(scrollTo(),click());
        onData(allOf(is(instanceOf(String.class)), is("Canadá")))
                .perform(click());

        onView(withId(R.id.sign_up_city))
                .perform(scrollTo(), clearText(), typeText("Ottawa"), closeSoftKeyboard());

        onView(withId(R.id.edt_institution))
                .perform(scrollTo(), clearText(), typeText("UCCOO"), closeSoftKeyboard());

        onView(withId(R.id.edt_speciality))
                .perform(scrollTo(), clearText(), typeText("Medicina"), closeSoftKeyboard());

        onView(withId(R.id.edt_student_id))
                .perform(scrollTo(), clearText(), typeText("MED1298"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_next),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .perform(scrollTo(), click());

    }


}

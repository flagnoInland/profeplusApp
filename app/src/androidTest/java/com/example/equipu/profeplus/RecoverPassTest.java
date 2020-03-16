package com.example.equipu.profeplus;

import android.content.Intent;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.StartActivity;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecoverPassTest {

    AppStateParcel appStateParcel;
    UserParcel userParcel;

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
    public void recoverTest() {
        Intent resultData = new Intent();
        resultData.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        mActivityRule.launchActivity(resultData);

        onView(withId(R.id.btn_uni_mode))
                .perform(click());

        onView(withId(R.id.btn_user_teacher))
                .perform(click());

        onView(withId(R.id.btn_recover))
                .perform(click());

        onView(withId(R.id.edt_dni))
                .perform(typeText("123123123"), closeSoftKeyboard());

        onView(withId(R.id.edt_birthdate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(1990, 7, 23));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.edt_email))
                .perform(typeText("juan.perez@mail.com"), closeSoftKeyboard());

        onView(withId(R.id.edt_password))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(withId(R.id.edt_confirm_pass))
                .perform(typeText("123456"), closeSoftKeyboard());

        onView(allOf(withId(R.id.btn_change)))
                .perform(click());

         // Click Dialog

        onView(withId(R.id.app_dlg_btn_exit)).perform(click());


    }


}
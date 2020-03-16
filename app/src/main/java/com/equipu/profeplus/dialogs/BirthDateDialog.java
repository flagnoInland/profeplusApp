package com.equipu.profeplus.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.fragments.board.EditProfileFragment;
import com.equipu.profeplus.fragments.teacher_job.EvaluationStepTwoFragment;
import com.equipu.profeplus.fragments.user.RecoverFragment;
import com.equipu.profeplus.fragments.user.StepOneSignUpFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by equipu on 30/11/2015.
 * Este diálogo permite ingresar datos para la fecha de nacimiento
 */
public class BirthDateDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    static Handler handler;

    public BirthDateDialog(){}

    public static BirthDateDialog newInstance(EvaluationStepTwoFragment.EvaluationStepTwoHandler mHandler) {
        handler = mHandler;
        return new BirthDateDialog();
    }

    public static BirthDateDialog newInstance(EditProfileFragment.EditProfileHandler mHandler) {
        handler = mHandler;
        return new BirthDateDialog();
    }

    public static BirthDateDialog newInstance(RecoverFragment.RecoverHandler mHandler) {
        handler = mHandler;
        return new BirthDateDialog();
    }

    public static BirthDateDialog newInstance(StepOneSignUpFragment.StepOneSignUpHandler mHandler){
        handler = mHandler;
        return new BirthDateDialog();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
        EDIT=1 permite usar la fecha de nacimiento guardada por el usuario en MDATE
         */
        super.onCreate(savedInstanceState);
        int EDIT = getArguments().getInt("EDIT");
        String mDate = getArguments().getString("MDATE");
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (EDIT==1){
            if (mDate != null && !mDate.equals("")) {
                year = Integer.parseInt(mDate.substring(6));
                month = Integer.parseInt(mDate.substring(3, 5)) - 1;
                day = Integer.parseInt(mDate.substring(0,2));
            }
        } else if (EDIT==0) {
            year = 1980;
            month = 6;
            day = 1;
        }


        String string_date = "31-12-2002";
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        Date d = null;
        try {
            d = f.parse(string_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long maxDate = d.getTime();

        // Create a new instance of DatePickerDialog and return it
        // Usar Holo Theme para usar la ruleta.
        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(),
                R.style.date_dialog,
                this, year, month, day);
        if (EDIT < 2) {
            mDatePicker.getDatePicker().setMaxDate(maxDate);
        } else {
            mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());
        }
        return mDatePicker;
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        String m = String.format("%02d", month + 1);
        String d = String.format("%02d", day);
        String result = d + '-' + m + '-' + String.valueOf(year);
        Message msg = Message.obtain();
        msg.what = LearnApp.MSG_BIRTH_DIALOG;
        Bundle bundle = new Bundle();
        bundle.putString("BIRTH",result);
        msg.setData(bundle);
        msg.setTarget(handler);
        msg.sendToTarget();
    }


}

package com.equipu.profeplus.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.fragments.teacher_job.EvaluationStepTwoFragment;

/**
 * Created by Herbert Caller on 7/6/2016.
 * Este diálogo permite escoger hora y minutos para un evento
 */
public class AppTimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    static Handler handler;
    int start;


    public AppTimeDialog() {

    }

    public static AppTimeDialog newIntance(EvaluationStepTwoFragment.EvaluationStepTwoHandler mHandler){
        handler = mHandler;
        return new AppTimeDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        start = getArguments().getInt("START");
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                R.style.date_dialog,
                this, 0, 0, true);
        return timePickerDialog;

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String h = String.format("%02d", hourOfDay);
        String m = String.format("%02d", minute);
        String result = h + ':' + m;
        Message msg = Message.obtain();
        msg.what = LearnApp.MSG_TIME_DIALOG;
        Bundle bundle = new Bundle();
        bundle.putString("TIME",result);
        bundle.putInt("START",start);
        msg.setData(bundle);
        msg.setTarget(handler);
        msg.sendToTarget();
    }
}

package com.equipu.profeplus.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;

/*
 * Created by equipu on 17/12/2015.
 * Este diálogo permite continuar o cancelar eventos o tareas.
 */
public class ConfirmTwoButtonsSupportDialog extends DialogFragment {

    OnFireEventTwoButtonDialogListener mListener;
    String title, message;
    boolean CHOICE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("TITLE");
        message = getArguments().getString("MESSAGE");

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        return dialog;

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.app_dialog_layout2, container, false);

        TextView titleDialog = (TextView) v.findViewById(R.id.app2_title_dlg);
        TextView msgDialog = (TextView) v.findViewById(R.id.app2_msg_dlg);

        Button btnDone = (Button) v.findViewById(R.id.app_dlg_btn2_done);
        Button btnExit = (Button) v.findViewById(R.id.app_dlg_btn2_exit);

        titleDialog.setTypeface(LearnApp.appFont);
        msgDialog.setTypeface(LearnApp.appFont);

        btnDone.setTypeface(LearnApp.appFont);
        btnExit.setTypeface(LearnApp.appFont);

        titleDialog.setText(title);
        msgDialog.setText(message);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHOICE = true;
                getDialog().dismiss();

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHOICE = false;
                getDialog().dismiss();

            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getTargetFragment() instanceof OnFireEventTwoButtonDialogListener) {
            mListener = (OnFireEventTwoButtonDialogListener) getTargetFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFireEventTwoButtonDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (CHOICE){
            mListener.onFireAcceptEventTwoButtonListener();
        } else {
            mListener.onFireCancelEventTwoButtonListener();
        }
    }

    public interface OnFireEventTwoButtonDialogListener {
        void onFireAcceptEventTwoButtonListener();
        void onFireCancelEventTwoButtonListener();
    }

}
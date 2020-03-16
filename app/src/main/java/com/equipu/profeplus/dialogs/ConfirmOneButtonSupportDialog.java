package com.equipu.profeplus.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;

/*
 * Created by equipu on 17/12/2015.
 * Este díalogo permite continuar eventos y verificar que el usuario ha sido notificado
 */
public class ConfirmOneButtonSupportDialog extends DialogFragment {

    OnFireEventOneButtonDialogListener mListener;

    Handler handler;
    String title, message;
    boolean CHOICE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("TITLE");
        message = getArguments().getString("MESSAGE");

    }

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

        View v = inflater.inflate(R.layout.app_dialog_layout, container, false);

        TextView titleDialog = (TextView) v.findViewById(R.id.app_title_dlg);
        TextView msgDialog = (TextView) v.findViewById(R.id.app_msg_dlg);
        Button btnExit = (Button) v.findViewById(R.id.app_dlg_btn_exit);
        LinearLayout mLayout = (LinearLayout) v.findViewById(R.id.lin_lay_app_dlg);

        titleDialog.setTypeface(LearnApp.appFont);
        msgDialog.setTypeface(LearnApp.appFont);
        btnExit.setTypeface(LearnApp.appFont);
        titleDialog.setText(title);
        msgDialog.setText(message);

        CHOICE = false;

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHOICE = true;
                getDialog().dismiss();
            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getTargetFragment() instanceof OnFireEventOneButtonDialogListener) {
            mListener = (OnFireEventOneButtonDialogListener) getTargetFragment();
        } else if (context instanceof OnFireEventOneButtonDialogListener) {
            mListener = (OnFireEventOneButtonDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFireEventOneButtonDialogListener");
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
            mListener.onFireAcceptEventOneButtonListener();
        } else {
            mListener.onFireCancelEventOneButtonListener();
        }
    }

    public interface OnFireEventOneButtonDialogListener {
        void onFireAcceptEventOneButtonListener();
        void onFireCancelEventOneButtonListener();
    }

}
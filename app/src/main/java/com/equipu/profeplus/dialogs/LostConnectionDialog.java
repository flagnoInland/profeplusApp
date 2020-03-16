package com.equipu.profeplus.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
 * Este diálogo muestra una notificación de la pérdida de conexión
 */
public class LostConnectionDialog extends DialogFragment {

    OnFireEventLostConnectionListener mListener;

    Handler handler;
    boolean CHOICE;

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

        titleDialog.setText(getString(R.string.text_lost_connection));
        titleDialog.setVisibility(View.GONE);
        msgDialog.setText(getString(R.string.text_lost_connection));


        btnDone.setText(getString(R.string.text_try_again));
        btnExit.setText(getString(R.string.text_exit));

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
        if (getTargetFragment() instanceof OnFireEventLostConnectionListener) {
            mListener = (OnFireEventLostConnectionListener) getTargetFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFireEventLostConnectionListener");
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
            mListener.onFireAcceptLostConnectionListener();
        } else {
            mListener.onFireCancelLostConnectionListener();
        }
        Message msg = Message.obtain();
        msg.what = LearnApp.MSG_RECONNECT_DIALOG;
        Bundle b = new Bundle();
        b.putBoolean("CHOICE",CHOICE);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    public interface OnFireEventLostConnectionListener {
        void onFireAcceptLostConnectionListener();
        void onFireCancelLostConnectionListener();
    }

}
package com.equipu.profeplus.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by herbert.caller on 3/27/2018.
 */

public class SessionCodeSupportDialog extends DialogFragment {

    @BindView(R.id.edt_rand_code_session) TextView txtRandomCOde;
    @BindView(R.id.txt_can_start) TextView txtCanStart;
    @BindView(R.id.txt_write_code) TextView txtWriteCode;
    @BindView(R.id.app_dlg_btn_exit) Button btnExit;
    private Unbinder unbinder;

    OnFireEventSessionCodeListener mListener;
    String mCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCode = getArguments().getString("CODE");

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

        View v = inflater.inflate(R.layout.app_session_code_dialog_layout, container, false);
        unbinder = ButterKnife.bind(this, v);
        txtCanStart.setTypeface(LearnApp.appFont);
        txtWriteCode.setTypeface(LearnApp.appFont);
        txtRandomCOde.setTypeface(LearnApp.appFont);
        btnExit.setTypeface(LearnApp.appFont);

        txtRandomCOde.setText(mCode);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getTargetFragment() instanceof OnFireEventSessionCodeListener) {
            mListener = (OnFireEventSessionCodeListener) getTargetFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFireEventSessionCodeListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onFireOkEventSessionCodeListener();
    }

    public interface OnFireEventSessionCodeListener {
        void onFireOkEventSessionCodeListener();
    }


}
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
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Herbert Caller on 03/05/2016.
 * Este diálogo confirma que la respuesta escogida por el estudiante va ser enviada.
 */
public class AppAnswerDialog extends DialogFragment {

    OnFireEventAnswerDialogListener mListener;

    @BindView(R.id.answer_msg_dlg) TextView txtMessage;
    @BindView(R.id.app_answer_done) Button btnDone;
    @BindView(R.id.app_answer_exit) Button btnExit;
    private Unbinder unbinder;

    AppStateParcel appStateParcel;
    TaskParcel taskParcel;
    boolean isOK = false;
    String answerText;
    String answer;



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

        View v = inflater.inflate(R.layout.app_answer_dialog, container, false);
        unbinder = ButterKnife.bind(this, v);

        answer = getArguments().getString("ANSWER");

        taskParcel = getArguments().getParcelable("TaskParcel");
        appStateParcel = getArguments().getParcelable("AppStateParcel");

        if (taskParcel.getQuestionType()==TaskParcel.Q_TRUE){
            if (answer.equals("F")){
                answerText = getString(R.string.symbol_v);
            }
            if (answer.equals("G")){
                answerText = getString(R.string.symbol_f);
            }
            if (answer.equals("H")){
                answerText = getString(R.string.text_alternative_dont_know);
            }
        } else {
            answerText = answer;
        }

        String message = String.format(getString(R.string.msg_choose_alternative_want_send_answer),answerText);
        txtMessage = (TextView) v.findViewById(R.id.answer_msg_dlg);
        txtMessage.setTypeface(LearnApp.appFont);
        txtMessage.setText(message);

        btnDone = (Button) v.findViewById(R.id.app_answer_done);
        btnExit = (Button) v.findViewById(R.id.app_answer_exit);
        btnDone.setTypeface(LearnApp.appFont);
        btnExit.setTypeface(LearnApp.appFont);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOK = true;
                getDialog().dismiss();

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOK = false;
                getDialog().dismiss();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getTargetFragment() instanceof OnFireEventAnswerDialogListener) {
            mListener = (OnFireEventAnswerDialogListener) getTargetFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFireEventAnswerDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isOK){
            mListener.onFireAcceptEventAnswerListener(answer);
        } else {
            mListener.onFireCancelEventAnswerListener();
        }
    }

    public interface OnFireEventAnswerDialogListener {
        void onFireAcceptEventAnswerListener(String answer);
        void onFireCancelEventAnswerListener();
    }


}

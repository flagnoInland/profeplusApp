package com.equipu.profeplus.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
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

/**
 * Created by Herbert Caller on 18/07/2016.
 * Este diálogo muestra los términos y condiciones
 */
public class TermsDialog extends DialogFragment {


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

        View v = inflater.inflate(R.layout.app_dialog_terms, container, false);

        TextView titleDialog = (TextView) v.findViewById(R.id.app_title_dlg);
        TextView msgDialog = (TextView) v.findViewById(R.id.app_msg_dlg);
        Button btnExit = (Button) v.findViewById(R.id.app_dlg_btn_exit);
        LinearLayout mLayout = (LinearLayout) v.findViewById(R.id.lin_lay_app_dlg);

        titleDialog.setTypeface(LearnApp.appFont);
        msgDialog.setTypeface(LearnApp.appFont);
        btnExit.setTypeface(LearnApp.appFont);

        msgDialog.setText(Html.fromHtml(getString(R.string.msg_terms_conditions)));
        //msgDialog.setText(getString(R.string.msg_terms_conditions));
        titleDialog.setText(getString(R.string.text_terms_conditions));
        btnExit.setText(getString(R.string.text_close));

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return v;
    }


}

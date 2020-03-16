package com.equipu.profeplus.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Herbert Caller on 27/04/2016.
 * Este diálogo muestra un campo para completar el DNI
 * Se utiliza para terminar la sesión de un usuario externo.
 * Este diálogo no es utilizado en esta versión
 */
public class OwnerLoginDialog extends DialogFragment {

    @BindView(R.id.txt_message_dialog) TextView msgDialog;
    @BindView(R.id.edt_dni) EditText edtDni;
    @BindView(R.id.btn_accept) Button btnAccept;
    private Unbinder unbinder;

    Handler handler;
    AppStateParcel appStateParcel;
    boolean isOK;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appStateParcel = getArguments().getParcelable("AppStateParcel");
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

        View v = inflater.inflate(R.layout.dialog_owner_login, container, false);
        unbinder = ButterKnife.bind(this, v);

        edtDni.setTypeface(LearnApp.appFont);
        btnAccept.setTypeface(LearnApp.appFont);
        msgDialog.setTypeface(LearnApp.appFont);
        edtDni.setHint(getString(R.string.text_write_dni));

        msgDialog.setText(String.format(getString(R.string.text_msg_return_owner),appStateParcel.getUsername()));
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtDni.getText().toString().equals(appStateParcel.getDocumentId())){
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtDni.getWindowToken(), 0);
                    isOK = true;
                } else {
                    isOK = false;
                }
                getDialog().dismiss();
            }
        });


        return v;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Message msg = Message.obtain();
        msg.what = LearnApp.MSG_OWNER_LOGIN_DIALOG;
        Bundle data = new Bundle();
        data.putBoolean("IS_OK",isOK);
        msg.setData(data);
        handler.sendMessage(msg);
    }


}

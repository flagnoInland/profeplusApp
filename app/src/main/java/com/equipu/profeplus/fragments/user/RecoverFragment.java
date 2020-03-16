package com.equipu.profeplus.fragments.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkStartService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStartListener;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.dialogs.BirthDateDialog;
import com.equipu.profeplus.dialogs.ConfirmOneButtonSupportDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento inicializa una vista para cambiar la contraseña.
Los campos requeridos son
EMAIL
FECHA DE NACIMIENTO
NUEVA CONTRASEÑA
Inicialmente se requirió el DNI el cual se encuentra deshabilitado en esta versión.
 */
public class RecoverFragment extends Fragment
        implements ConfirmOneButtonSupportDialog.OnFireEventOneButtonDialogListener{

    @BindView(R.id.btn_change) Button btnChange;
    @BindView(R.id.edt_dni) EditText edtDocument;
    @BindView(R.id.txt_dni) TextView txtDocument;
    @BindView(R.id.edt_email) EditText edtMail;
    @BindView(R.id.edt_birthdate) TextView edtBirth;
    @BindView(R.id.edt_password) EditText edtPass;
    @BindView(R.id.edt_confirm_pass) EditText edtPass2;
    private Unbinder unbinder;

    AppStateParcel appStateParcel;
    UserParcel userParcel;
    RecoverPassReceiver recoverPassReceiver;

    private IStartListener mListener;

    public RecoverFragment() {}


    public static RecoverFragment newInstance(AppStateParcel appStateParcel) {
        RecoverFragment fragment = new RecoverFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recover, container, false);
        unbinder = ButterKnife.bind(this, v);

        userParcel = new UserParcel();

        edtBirth.setTypeface(LearnApp.appFont);
        edtDocument.setTypeface(LearnApp.appFont);
        edtDocument.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);

        edtMail.setTypeface(LearnApp.appFont);
        edtMail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        edtPass.setTypeface(LearnApp.appFont);
        edtPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtPass2.setTypeface(LearnApp.appFont);
        edtPass2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtPass2.setText("");

        edtDocument.setVisibility(View.GONE);
        txtDocument.setVisibility(View.GONE);

        edtBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = BirthDateDialog.newInstance(new RecoverHandler());
                Bundle b = new Bundle();
                b.putInt("EDIT", appStateParcel.getEditUser());
                b.putString("MDATE",edtBirth.getText().toString());
                newFragment.setArguments(b);
                newFragment.show(getChildFragmentManager(), "birthdate");
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (checkFields()) {
                    Log.d("profeplus.dni",userParcel.getDni());
                    if (mListener != null) {
                        if (isValidEmail(userParcel.getMail()) && checkPass()) {
                            ConnectivityManager cm =
                                    (ConnectivityManager) getActivity().getApplicationContext()
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()
                                    && networkInfo.isAvailable()) {
                                Intent intent = new Intent(getActivity(), NetworkStartService.class);
                                intent.putExtra(NetworkStartService.SERVICE,
                                        NetworkStartService.SERVICE_RECOVER_PASS);
                                intent.putExtra(LearnApp.PCL_USER_PARCEL, userParcel);
                                getContext().startService(intent);
                            } else {
                                mListener.dialogLostConnection();
                            }
                        } else if (!isValidEmail(userParcel.getMail())){
                            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                            Bundle b = new Bundle();
                            b.putString("TITLE", getResources().getString(R.string.text_confirm));
                            b.putString("MESSAGE", getResources().getString(R.string.msg_email_is_invalid));
                            newFragment.setArguments(b);
                            newFragment.show(getFragmentManager(), "failure");
                        } else if (!checkPass()){
                            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                            Bundle b = new Bundle();
                            b.putString("TITLE", getResources().getString(R.string.text_confirm));
                            b.putString("MESSAGE", getResources().getString(R.string.msg_passwords_are_different));
                            newFragment.setArguments(b);
                            newFragment.show(getFragmentManager(), "failure");
                        }
                    }
                } else {
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", getResources().getString(R.string.text_alerta));
                    b.putString("MESSAGE", getResources().getString(R.string.msg_fill_all_fields));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), "failure");
                }
            }
        });




        return v;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private String formatMyDateServer(String date){
        int year = Integer.parseInt(date.substring(6));
        int month = Integer.parseInt(date.substring(3, 5)) - 1;
        int day = Integer.parseInt(date.substring(0,2));
        String m = String.format("%02d", month + 1);
        String d = String.format("%02d", day);
        String value = String.valueOf(year) + '-' + m + '-' + d;
        return value;
    }


    public void getFields(){
        //userParcel.setDni(edtDocument.getText().toString());
        userParcel.setDni("0");
        userParcel.setMail(edtMail.getText().toString().trim());
        if (edtBirth.getText().toString().length() > 0) {
            userParcel.setBirth(formatMyDateServer(edtBirth.getText().toString()));
        }
        userParcel.setPassword(edtPass.getText().toString().trim());
        userParcel.setPassword2(edtPass2.getText().toString().trim());
    }


    public boolean checkFields() {
        boolean value = true;
        /*
        if (userParcel.getDni().equals("")){
            value = value & false;
        }*/
        if (userParcel.getMail().equals("")){
            value = value & false;
        }
        if (userParcel.getBirth().equals("")){
            value = value & false;
        }
        if (userParcel.getPassword().equals("")){
            value = value & false;
        }
        return value;
    }

    public boolean checkPass(){
        if (!userParcel.getPassword().equals(userParcel.getPassword2())){
            return false;
        }
        return true;
    }

    public class RecoverPassReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int  status = intent.getIntExtra(NetworkStartService.STATUS,0);
            if (status == UserController.DONE){
                DialogFragment newFragment = new ConfirmOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", "");
                b.putString("MESSAGE", getResources().getString(R.string.text_your_have_password));
                newFragment.setArguments(b);
                newFragment.setTargetFragment(RecoverFragment.this, 1234);
                newFragment.show(getFragmentManager(), "success");
            } else {
                DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", getResources().getString(R.string.text_confirm));
                b.putString("MESSAGE", getResources().getString(R.string.msg_wrong_data));
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), "failure");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkStartService.ACTION_RECOVER_PASS);
        recoverPassReceiver = new RecoverPassReceiver();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(recoverPassReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.unregisterReceiver(recoverPassReceiver);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IStartListener) {
            mListener = (IStartListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IStartListener");
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
    public void onFireAcceptEventOneButtonListener() {
        mListener.onRecoverSuccess(appStateParcel);
    }

    @Override
    public void onFireCancelEventOneButtonListener() {
        mListener.onRecoverSuccess(appStateParcel);
    }

    public class RecoverHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LearnApp.MSG_BIRTH_DIALOG) {
                Log.d("profeplus.Msg", String.valueOf(msg.what));
                String birth = msg.getData().getString("BIRTH");
                edtBirth.setText(birth);
            }
        }
    }



}

package com.equipu.profeplus.fragments.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkStartService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStartListener;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento inicializa la vista para iniciar una sesión
Se necesita ingresar:
EMAIL
CONTRASEÑA
La cuenta de mail se valida antes de requerir el token de autorización.
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.btn_signin) TextView btnLogin;
    @BindView(R.id.edt_username) AutoCompleteTextView edtUsername;
    @BindView(R.id.edt_password) EditText edtPassword;
    @BindView(R.id.progress_bar) LinearLayout progressAni;
    @BindView(R.id.btn_recover) TextView btnRecover;
    @BindView(R.id.passwordContainer) LinearLayout passwordContainer;
    private Unbinder unbinder;

    AppStateParcel appStateParcel;
    UserParcel userParcel;
    ResponseReceiver responseReceiver;

    private IStartListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(AppStateParcel appStateParcel) {
        LoginFragment fragment = new LoginFragment();
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, v);

        userParcel = new UserParcel();

        progressAni.setVisibility(View.GONE);
        btnLogin.setTypeface(LearnApp.appFont2);
        edtPassword.setTypeface(LearnApp.appFont);
        edtUsername.setTypeface(LearnApp.appFont);
        btnRecover.setTypeface(LearnApp.appFont2);

        btnRecover.setVisibility(View.GONE);
        passwordContainer.setVisibility(View.VISIBLE);

        // Cargar emails de usuarios
        SharedPreferences settings = getActivity().getSharedPreferences("userSession", 0);
        String username = settings.getString("USERNAME", "");
        Set<String> empty = new HashSet<String>();
        Set<String> names = settings.getStringSet("EMAILS", empty);
        Log.d("profeplus.msg",names.toString());
        String password = settings.getString("PASSWORD", "");
        String[] arrayNames = names.toArray(new String[names.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, arrayNames);
        edtUsername.setAdapter(adapter);

        edtUsername.setText(username);
        edtPassword.setText(password);

        edtUsername.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ( event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                if (edtUsername.hasFocus()){
                    edtUsername.clearFocus();
                    edtPassword.requestFocus();
                }
            }
            return false;
            }
        });

        // Botón para pedir una token de autenticación
        // En caso de falla mostar mensaje
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (checkFields()) {
                    if (mListener != null) {
                        if (isValidEmail(userParcel.getMail())) {
                            ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()
                                    && networkInfo.isAvailable()) {
                                progressAni.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getActivity(), NetworkStartService.class);
                                intent.putExtra(NetworkStartService.SERVICE, NetworkStartService.SERVICE_LOGIN);
                                intent.putExtra(LearnApp.PCL_USER_PARCEL, userParcel);
                                getContext().startService(intent);
                            } else {
                                mListener.dialogLostConnection();
                            }
                        } else {
                            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                            Bundle b = new Bundle();
                            b.putString("TITLE", getResources().getString(R.string.text_confirm));
                            b.putString("MESSAGE", getResources().getString(R.string.msg_email_is_invalid));
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

        // Botón para cambiar contraseña
        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.startRecover(appStateParcel);
            }
        });

        return v;
    }

    /*
    Validar correo electronico
     */
    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void getFields(){
        userParcel.setPassword(edtPassword.getText().toString().trim());
        //userParcel.setPassword("");
        userParcel.setMail(edtUsername.getText().toString().trim());
    }

    public boolean checkFields() {
        boolean value = true;

        if (userParcel.getPassword().equals("")){
            value = value & false;
        }
        if (userParcel.getMail().equals("")){
            value = value & false;
        }
        return value;
    }

    /*
    Receiver para capturar estado de la autenticación
    Si se consigue mostar paneles a usuario
    En caso de falla mostrar mensaje
     */
    public class ResponseReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (progressAni != null) {
                progressAni.setVisibility(View.GONE);
            }
            int  status = intent.getIntExtra(NetworkStartService.STATUS,0);
            SharedPreferences settings = getActivity().getSharedPreferences("userSession", 0);
            Set<String> empty = new HashSet<String>();
            Set<String> names = settings.getStringSet("EMAILS", empty);
            SharedPreferences.Editor editor = settings.edit();
            if (status == UserController.DONE){
                appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
                appStateParcel.setStartTime(System.currentTimeMillis());
                names.add(userParcel.getMail());
                editor.putString("USERNAME", userParcel.getMail());
                editor.putStringSet("EMAILS", names);
                editor.putString("PASSWORD", userParcel.getPassword());
                editor.commit();
                if (appStateParcel.getUserType() == AppStateParcel.STUDENT) {
                    if (mListener != null) {
                        mListener.goToStudentBoard(appStateParcel);
                    }
                } else {
                    if (mListener != null) {
                        mListener.goToTeacherBoard(appStateParcel);
                    }
                }
            } else if (status == UserController.CONNECTION){
                if (mListener != null) {
                    mListener.dialogLostConnection();
                }
            } else {
                DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", getResources().getString(R.string.text_alerta));
                b.putString("MESSAGE", getResources().getString(R.string.msg_password_user_incorrect));
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), "failure");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkStartService.ACTION_LOGIN);
        responseReceiver = new ResponseReceiver();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(responseReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.unregisterReceiver(responseReceiver);
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


    public interface LoginListener {
        void goToStudentBoard(AppStateParcel appStateParcel);
        void goToTeacherBoard(AppStateParcel appStateParcel);
        void startRecover(AppStateParcel appStateParcel);
        void dialogLostConnection();
    }
}

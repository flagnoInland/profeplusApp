package com.equipu.profeplus.fragments.student_job;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkStudentService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStudentJobListener;
import com.equipu.profeplus.controllers.StudentLessonController;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;
import com.equipu.profeplus.models.UserParcel;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento se usa para visualizar una pantalla de inicio de sesión
para un estudiante que prestará un smartphone para responder una pregunta
 */
public class GuestLoginFragment extends Fragment {

    @BindView(R.id.img_back) ImageView btnback;
    @BindView(R.id.img_exit) ImageView btnexit;

    @BindView(R.id.txt_message_login) TextView msgLogin;
    @BindView(R.id.edt_username) AutoCompleteTextView edtUsername;
    @BindView(R.id.edt_password) EditText edtPassword;
    @BindView(R.id.btn_accept) Button btnAccept;
    @BindView(R.id.btn_cancel) Button btnCancel;

    @BindView(R.id.lay_password) LinearLayout layPassword;
    private Unbinder unbinder;

    TaskParcel taskParcel, taskGuestParcel;
    AppStateParcel appStateParcel, appStateGuestParcel;
    UserParcel guestParcel;
    LoginGuestReceiver loginGuestReceiver;
    IntentFilter filter;

    private IStudentJobListener mListener;

    public GuestLoginFragment() {
        // Required empty public constructor
    }


    public static GuestLoginFragment newInstance(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                                                 AppStateParcel agp) {
        GuestLoginFragment fragment = new GuestLoginFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_TASK_PARCEL, tp);
        args.putParcelable(LearnApp.PCL_TASK_GUEST_PARCEL, tgp);
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
        args.putParcelable(LearnApp.PCL_APP_STATE_GUEST,agp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskParcel = getArguments().getParcelable(LearnApp.PCL_TASK_PARCEL);
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
            taskGuestParcel = getArguments().getParcelable(LearnApp.PCL_TASK_GUEST_PARCEL);
            appStateGuestParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE_GUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guest_login, container, false);
        unbinder = ButterKnife.bind(this, v);

        appStateParcel.setOwnerId(appStateParcel.getUserId());
        appStateGuestParcel.setOwnerId(appStateParcel.getUserId());

        guestParcel = new UserParcel();
        edtPassword.setTypeface(LearnApp.appFont);
        edtUsername.setTypeface(LearnApp.appFont);
        msgLogin.setTypeface(LearnApp.appFont);

        //msgLogin.setText(String.format(getString(R.string.msg_can_participate_thanks_to),
        // appStateParcel.getUsername()));

        // Cargar mail de estudiantes que usaron el app
        SharedPreferences settings = getActivity().getSharedPreferences("userSession", 0);
        Set<String> empty = new HashSet<String>();
        Set<String> names = settings.getStringSet("EMAILS", empty);
        Log.d("profeplus.msg",names.toString());
        String[] arrayNames = names.toArray(new String[names.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, arrayNames);
        edtUsername.setAdapter(adapter);
        edtUsername.setText("");

        edtPassword.setText("");

        edtUsername.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    if (edtUsername.hasFocus()){
                        edtUsername.clearFocus();
                        //edtPassword.requestFocus();
                    }
                }
                return false;
            }
        });


        if (appStateParcel.getAppMode()== AppStateParcel.SCHOOL_MODE){
            edtPassword.setVisibility(View.GONE);
            edtPassword.setText("none");
            layPassword.setVisibility(View.GONE);
        }

        /*
        edtPassword.setVisibility(View.VISIBLE);
        edtPassword.setText("none");
        layPassword.setVisibility(View.VISIBLE);
        */


        // Registrar al nuevo usuario
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);
                getFields();
                if (checkFields()) {
                    if (mListener != null) {
                        if (isValidEmail(guestParcel.getMail())) {
                            ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()
                                    && networkInfo.isAvailable()) {
                                Intent intent = new Intent(getActivity(), NetworkStudentService.class);
                                if (appStateParcel.getAppMode()== AppStateParcel.SCHOOL_MODE){
                                    intent.putExtra(NetworkStudentService.SERVICE,
                                            NetworkStudentService.SERVICE_GUEST_SCHOOL_LOGIN);
                                } else {
                                    intent.putExtra(NetworkStudentService.SERVICE,
                                            NetworkStudentService.SERVICE_GUEST_LOGIN);
                                }
                                intent.putExtra(LearnApp.PCL_USER_PARCEL, guestParcel);
                                appStateGuestParcel.setOwnerId(appStateParcel.getUserId());
                                intent.putExtra(LearnApp.PCL_APP_STATE, appStateGuestParcel);
                                taskGuestParcel.setAccesscode(taskParcel.getAccesscode());
                                taskGuestParcel.setLastAnswer("Z");
                                intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskGuestParcel);
                                getActivity().startService(intent);
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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null) {
                    mListener.backToBoard(appStateParcel);
                }
            }
        });


        return v;

    }


    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void getFields(){
        guestParcel.setPassword(edtPassword.getText().toString());
        guestParcel.setMail(edtUsername.getText().toString());
    }

    public boolean checkFields() {
        boolean value = true;
        if (guestParcel.getPassword().equals("")){
            value = value & false;
        }
        if (guestParcel.getMail().equals("")){
            value = value & false;
        }
        return value;
    }

    /*
    Receiver para capturar si el nuevo usuario ha iniciado sesión.
    Si se consigue mostar la pregunta.
    Si falla mostrar un mensaje
     */
    public class LoginGuestReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences settings = getActivity().getSharedPreferences("userSession", 0);
            Set<String> empty = new HashSet<String>();
            Set<String> names = settings.getStringSet("EMAILS", empty);
            SharedPreferences.Editor editor = settings.edit();
            int status = intent.getIntExtra(NetworkStudentService.STATUS,0);
            if (status == StudentLessonController.DONE){
                appStateGuestParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
                taskGuestParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
                appStateParcel.setGuestMode(AppStateParcel.ACTIVE);
                if (mListener!=null) {
                    mListener.updateTaskParcel(appStateParcel, appStateGuestParcel,
                            taskParcel, taskGuestParcel);
                }
                names.add(guestParcel.getMail());
                editor.putStringSet("EMAILS", names);
                editor.commit();
                if (appStateParcel.getUserType() == AppStateParcel.STUDENT) {
                    if (taskParcel.getRun() == 1){
                        if (mListener!=null) {
                            mListener.startAnswerStepOne(taskParcel, appStateParcel, taskGuestParcel,
                                    appStateGuestParcel);
                        }
                    } else {
                        if (mListener!=null) {
                            mListener.startAnswerStepTwo(taskParcel, appStateParcel, taskGuestParcel,
                                    appStateGuestParcel);
                        }
                    }
                }
            }  else {
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
        if (appStateParcel.getAppMode()== AppStateParcel.SCHOOL_MODE){
            filter = new IntentFilter(NetworkStudentService.ACTION_GUEST_SCHOOL_LOGIN);
        } else {
            filter = new IntentFilter(NetworkStudentService.ACTION_GUEST_LOGIN);
        }

        loginGuestReceiver = new LoginGuestReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(loginGuestReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(loginGuestReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IStudentJobListener) {
            mListener = (IStudentJobListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IStudentJobListener");
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

}

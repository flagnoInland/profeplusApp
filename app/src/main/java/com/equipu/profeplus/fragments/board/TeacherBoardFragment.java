package com.equipu.profeplus.fragments.board;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkBoardService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IUserBoardListener;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.equipu.profeplus.models.UserParcel;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento se usa para presentar a los profesores
un pantalla con las opciones:
PREGUNTAR A CLASE
CAMBIAR MODO
EDITAR PERFIL
INFORMACIÓN DEL APP
VER PROTOCOLO
 */
public class TeacherBoardFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener {

    //private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.btn_free_course) Button btnFreeCourse;
    @BindView(R.id.btn_protocol) Button btnProtocol;
    @BindView(R.id.btn_evaluation) Button btnEvaluation;
    @BindView(R.id.btn_edit_user) ImageView btnEditUser;
    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.btn_becas) ImageView btnBecas;
    @BindView(R.id.btn_user_mode) ImageView btnUserMode;
    @BindView(R.id.btn_share) ImageView btnShare;
    @BindView(R.id.img_iphone) ImageView btnIPhone;
    @BindView(R.id.txt_welcome) TextView helloName;
    private Unbinder unbinder;

    SessionParcel sessionParcel;
    AppStateParcel appStateParcel;
    GetUserReceiver getUserReceiver;
    private IUserBoardListener mListener;

    public TeacherBoardFragment() {
        // Required empty public constructor
    }


    public static TeacherBoardFragment newInstance(AppStateParcel ap, SessionParcel sp) {
        TeacherBoardFragment fragment = new TeacherBoardFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
        args.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LearnApp application = (LearnApp) getActivity().getApplication();
        //mTracker = application.getDefaultTracker();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
            sessionParcel = getArguments().getParcelable(LearnApp.PCL_SESSION_PARCEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_teacher_board, container, false);
        unbinder = ButterKnife.bind(this, v);


        mListener.updateUserMode();
        sessionParcel = new SessionParcel();

        btnFreeCourse.setTypeface(LearnApp.appFont2);
        btnProtocol.setTypeface(LearnApp.appFont1);

        helloName.setTypeface(LearnApp.appFont1);

        // Mostrar nombre del profesor
        if (appStateParcel.getGender().equals("Female")
                || appStateParcel.getGender().equals("Femenino")) {
            helloName.setText(String.format(getString(R.string.msg_hello_username_f),
                    appStateParcel.getUsername()));
        } else {
            helloName.setText(String.format(getString(R.string.msg_hello_username),
                    appStateParcel.getUsername()));
        }

        // Leer protocolo de uso
        btnProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToTutorial(appStateParcel);
            }
        });


        // Botón para editar datos del usuario
        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    Intent intent = new Intent(getActivity(), NetworkBoardService.class);
                    intent.putExtra(NetworkBoardService.SERVICE,
                            NetworkBoardService.SERVICE_GET_USER);
                    intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                    getActivity().startService(intent);
                } else {
                    mListener.dialogLostConnection();
                }

            }
        });

        // Botón para inciae una lección/pregunta
        btnFreeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionParcel.setAppMode(appStateParcel.getAppMode());
                sessionParcel.setQuestionType(SessionParcel.Q_NORMAL);
                sessionParcel.setCourseId(-1);
                sessionParcel.setAccesscode("0000");
                sessionParcel.setNewSession(1);
                sessionParcel.setInactive(0);
                mListener.startTeacherSession(sessionParcel);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                //mListener.goToLogin(appStateParcel);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                /*
                DialogFragment newFragment = new ConfirmTwoButtonsSupportDialog();
                Bundle bd = new Bundle();
                bd.putString("TITLE", getString(R.string.text_confirm));
                bd.putString("MESSAGE", getString(R.string.text_want_logout));
                newFragment.setArguments(bd);
                newFragment.setTargetFragment(TeacherBoardFragment.this,1234);
                newFragment.show(getFragmentManager(), "answer");
                */

            }
        });

        // Botón para ver información del app.
        // Debería cambiarse el nombre
        btnBecas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToBecas();
            }
        });

        // Botón para cambiar el modo
        btnUserMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setUserMode(appStateParcel);
            }
        });

        //Botón compartir
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToShare();
            }
        });

        btnIPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", getResources().getString(R.string.text_iphone_users));
                b.putString("MESSAGE", getResources().getString(R.string.msg_iphone_users_hint));
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), null);
            }
        });

        if (appStateParcel.getEvaluations() == 0){
            btnEvaluation.setVisibility(View.GONE);
        } else {
            btnEvaluation.setVisibility(View.VISIBLE);
        }

        // Prototipo para el botón evaluaciones
        btnEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionParcel.setCourseId(-1);
                sessionParcel.setAccesscode("0000");
                sessionParcel.setNewSession(1);
                sessionParcel.setQuestionType(SessionParcel.Q_EVAL);
                mListener.startTeacherSession(sessionParcel);
            }
        });

        return v;
    }

    /*
    Receiver para capturar el estado de la adquisición de datos del usuario
    Si se logra mostrar pantalla para editar datos.
    En caso de falla mostar mensaje
     */
    public class GetUserReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkBoardService.STATUS,0);
            if (status == UserController.DONE) {
                UserParcel userParcel =
                        intent.getParcelableExtra(LearnApp.PCL_USER_PARCEL);
                appStateParcel.setEditUser(1);
                mListener.updateProfile(appStateParcel, userParcel);
            } else if (status == UserController.CONNECTION){
                mListener.dialogLostConnection();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mTracker.setScreenName("Panel::Profesor");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // Registrar acción en firebase
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "10");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PANEL::Profesor");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PANEL::Profesor");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        // Filtrar acción para obtener datos de usuario
        IntentFilter filter = new IntentFilter(NetworkBoardService.ACTION_GET_USER);
        getUserReceiver = new GetUserReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(getUserReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(getUserReceiver);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IUserBoardListener) {
            mListener = (IUserBoardListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IUserBoardListener");
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
    public void onFireCancelEventTwoButtonListener() {

    }

    @Override
    public void onFireAcceptEventTwoButtonListener() {
        mListener.goToLogin(appStateParcel);
    }

}

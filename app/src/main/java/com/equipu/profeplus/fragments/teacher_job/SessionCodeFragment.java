package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.LostConnectionDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento permite visualizar la clave de cuatro dígitos creada
para una pregunta. Este fragmento aparece si la pregunta pertenece a otra clase.
La vista contiene un botón para visualizar los resultados en un gráfico de barras.
 */
public class SessionCodeFragment extends Fragment
        implements LostConnectionDialog.OnFireEventLostConnectionListener {

    @BindView(R.id.edt_rand_code_session) TextView txtRandomCOde;
    @BindView(R.id.txt_can_start) TextView txtCanStart;
    @BindView(R.id.txt_write_code) TextView txtWriteCode;
    @BindView(R.id.txt_message_results_mail) TextView txtMessageMail;
    @BindView(R.id.btn_finish) Button btnFinish;
    @BindView(R.id.btn_save) Button btnSave;
    @BindView(R.id.btn_results) Button btnResult;
    @BindView(R.id.btn_handout) Button btnManual;
    @BindView(R.id.img_back) ImageView btnback;
    @BindView(R.id.img_exit) ImageView btnexit;
    private Unbinder unbinder;

    SessionParcel sessionParcel;
    AppStateParcel appStateParcel;
    NewSessionReceiver newSessionReceiver;
    UpdateSessionreceiver updateSessionreceiver;

    private ITeacherJobListener mListener;

    public SessionCodeFragment() { }



    public static SessionCodeFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        SessionCodeFragment fragment = new SessionCodeFragment();
        Bundle args = new Bundle();
        args.putParcelable("SessionParcel", sp);
        args.putParcelable("AppStateParcel", asp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sessionParcel = getArguments().getParcelable("SessionParcel");
            appStateParcel = getArguments().getParcelable("AppStateParcel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_session_code, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtCanStart.setTypeface(LearnApp.appFont);
        txtWriteCode.setTypeface(LearnApp.appFont);
        txtRandomCOde.setTypeface(LearnApp.appFont);
        txtMessageMail.setTypeface(LearnApp.appFont);


        // Botón para ver manual del profesor
        btnManual.setTypeface(LearnApp.appFont);
        btnManual.setEnabled(false);

        // Botón para ver resultados
        btnResult.setTypeface(LearnApp.appFont);
        btnResult.setEnabled(false);

        // Botón a ser usado en evaluaciones
        btnFinish.setTypeface(LearnApp.appFont);
        btnFinish.setEnabled(false);

        // Botón a ser usado en evaluaciones
        btnSave.setTypeface(LearnApp.appFont);
        btnSave.setEnabled(false);

        // Bloquear botones si no es una evaluación
        int qType = sessionParcel.getQuestionType();
        int save = appStateParcel.getSaveEvaluation();
        if ( qType == SessionParcel.Q_EVAL && save == 1){
            txtMessageMail.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            btnResult.setVisibility(View.GONE);
            btnManual.setVisibility(View.GONE);
        } else if (qType == SessionParcel.Q_EVAL && save == 0) {
            txtMessageMail.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
            //btnFinish.setVisibility(View.VISIBLE);
            //btnFinish.setText(getString(R.string.text_accept));
            btnSave.setVisibility(View.GONE);
            //btnResult.setVisibility(View.GONE);
            btnResult.setVisibility(View.VISIBLE);
            btnResult.setText(getString(R.string.text_accept));
            btnManual.setVisibility(View.GONE);
        } else {
            txtMessageMail.setVisibility(View.GONE);
            btnFinish.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            btnResult.setVisibility(View.VISIBLE);
            btnManual.setVisibility(View.VISIBLE);
        }

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("profeplus.qtype", String.valueOf(sessionParcel.getQuestionType()));
                if (mListener != null) {
                    Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
                    intent.putExtra(NetworkTeacherService.SERVICE,
                            NetworkTeacherService.SERVICE_UPDATE_SESSION);
                    intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                    intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
                    getActivity().startService(intent);
                }
            }
        });

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.readTeacherManual();
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backToUserBoard(appStateParcel);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backToUserBoard(appStateParcel);
            }
        });

        startFragmentCoded();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backToFreeQuestion();
            }
        });

        return v;
    }

    /*
    Iniciar servicio para crear una lección/pregunta con el código escogido en la pantalla previa
     */
    public void startFragmentCoded(){
        if (sessionParcel.getQuestionType()==SessionParcel.Q_EVAL) {
            sessionParcel.setInactive(0);
            Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
            intent.putExtra(NetworkTeacherService.SERVICE,
                    NetworkTeacherService.SERVICE_NEW_SESSION);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
            getActivity().startService(intent);
        } else if (sessionParcel.getInactive()==1 &&
                sessionParcel.getQuestionType() != SessionParcel.Q_EVAL ) {
            sessionParcel.setInactive(0);
            Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
            intent.putExtra(NetworkTeacherService.SERVICE,
                    NetworkTeacherService.SERVICE_NEW_SESSION);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
            getActivity().startService(intent);
        }else {
            btnResult.setEnabled(true);
            btnManual.setEnabled(true);
            btnSave.setEnabled(true);
            btnFinish.setEnabled(true);
        }
    }

    /*
    Receiver para capturar si la lección ha sido actualizada
     */
    public class UpdateSessionreceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS, 0);
            if (status == TeacherLessonController.DONE){
                if (sessionParcel.getQuestionType() != SessionParcel.Q_EVAL) {
                    mListener.showResults(sessionParcel);
                } else {
                    mListener.backToUserBoard(appStateParcel);
                }
            }
        }
    }

    /*
    Receiver para capturar si la lección ha sido creada
     */
    public class NewSessionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS, 0);
            if (status == TeacherLessonController.DONE) {
                btnResult.setEnabled(true);
                btnManual.setEnabled(true);
                btnSave.setEnabled(true);
                btnFinish.setEnabled(true);
                sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
                appStateParcel.setSessionCode(sessionParcel.getAccesscode());
                txtRandomCOde.setText(sessionParcel.getAccesscode());
                mListener.updateSessionCode(appStateParcel, sessionParcel);
            } else if (status == TeacherLessonController.CONNECTION) {
                //reconnectDialog();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_NEW_SESSION);
        IntentFilter filter1 = new IntentFilter(NetworkTeacherService.ACTION_UPDATE_SESSION);
        newSessionReceiver = new NewSessionReceiver();
        updateSessionreceiver = new UpdateSessionreceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(newSessionReceiver, filter);
        lbm.registerReceiver(updateSessionreceiver, filter1);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(newSessionReceiver);
        lbm.unregisterReceiver(updateSessionreceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ITeacherJobListener) {
            mListener = (ITeacherJobListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ITeacherJobListener");
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


    public void reconnectDialog(){
        DialogFragment newFragment = new LostConnectionDialog();
        newFragment.setTargetFragment(SessionCodeFragment.this,1234);
        newFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onFireAcceptLostConnectionListener() {
        startFragmentCoded();
    }

    @Override
    public void onFireCancelLostConnectionListener() {
        if (sessionParcel.getQuestionType() == SessionParcel.Q_EVAL){
            mListener.backToUserBoardFromEval(appStateParcel);
        } else {
            mListener.backToUserBoard(appStateParcel);
        }
    }

    public class SessionCodeHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LearnApp.MSG_CONFIRM_TWO_BUTTON_DIALOG){
                onDismissSameLesson(msg);
            }
        }
    }

    private void onDismissSameLesson(Message msg){
        if (msg.getData().getBoolean("CHOICE")){
            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
            Bundle b = new Bundle();
            b.putString("TITLE", "");
            b.putString("MESSAGE", getString(R.string.msg_will_use_same_code));
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), null);
            sessionParcel.setAccesscode(appStateParcel.getSessionCode());
            startFragmentCoded();
        } else {
            sessionParcel.setAccesscode("0000");
            startFragmentCoded();
        }
    }

}

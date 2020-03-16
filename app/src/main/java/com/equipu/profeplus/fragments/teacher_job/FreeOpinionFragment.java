package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento permite la selección de un tipo de pregunta de opinión
El profesor debe pulsar uno de los tres botones rectangulares a elegir.
EL botón checkB(nivel de acuerdo) esta deshabilitado en esta versión.
checkA : nivel de satisafacción
checkC: calificación
checkD: calificar expositor
 */
public class FreeOpinionFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener {

    //private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.chkb_opinion1) Button checkA;
    @BindView(R.id.chkb_opinion2) Button checkB;
    @BindView(R.id.chkb_opinion3) Button checkC;
    @BindView(R.id.chkb_opinion4) Button checkD;
    @BindView(R.id.img_back) ImageView btnback;
    @BindView(R.id.img_exit) ImageView btnexit;
    private Unbinder unbinder;


    private SessionParcel sessionParcel;
    private AppStateParcel appStateParcel;
    long now, elapsed;

    private ITeacherJobListener mListener;
    NewSessionReceiverO newSessionReceiver;

    public FreeOpinionFragment() {  }


    public static FreeOpinionFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        FreeOpinionFragment fragment = new FreeOpinionFragment();
        Bundle args = new Bundle();
        args.putParcelable("SessionParcel", sp);
        args.putParcelable("AppStateParcel", asp);
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
            sessionParcel = getArguments().getParcelable("SessionParcel");
            appStateParcel = getArguments().getParcelable("AppStateParcel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_free_opinion, container, false);
        unbinder = ButterKnife.bind(this, v);

        /*
        Opcion opinión acerca de satisfacción
        Registrar en firebase
         */
        checkA.setTypeface(LearnApp.appFont1);
        //checkA.setSelected(false);
        checkA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkA.setSelected(true);
                /*
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Opinion")
                        .setAction("Satisfaccion")
                        .build());
                        */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "40");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "OPINION::Satisfaccion");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "OPINION::Satisfaccion");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                sessionParcel.setSurvey(SessionParcel.S_SATISFACTION);
                sessionParcel.setQuestionMode(SessionParcel.M_INDIVIDUAL);
                //mListener.setCodeSession(sessionParcel);
                checkSameSession();
            }
        });


        /*
        Opcion opinión acerca de acuerdo
        Registrar en firebase
         */
        checkB.setVisibility(View.GONE);
        checkB.setTypeface(LearnApp.appFont1);
        //checkB.setSelected(false);
        checkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkB.setSelected(true);
                /*
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Opinion")
                        .setAction("Acuerdo")
                        .build());
                        */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "41");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "OPINION::Acuerdo");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "OPINION::Acuerdo");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                sessionParcel.setSurvey(SessionParcel.S_AGREEMENT);
                sessionParcel.setQuestionMode(SessionParcel.M_INDIVIDUAL);
                mListener.setCodeSession(sessionParcel);
            }
        });


        /*
        Opcion opinión acerca de calificación
        Registrar en firebase
         */
        checkC.setTypeface(LearnApp.appFont1);
        //checkC.setSelected(false);
        checkC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkC.setSelected(true);
                /*
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Opinion")
                        .setAction("Calificacion")
                        .build());
                        */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "42");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "OPINION::Calificacion");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "OPINION::Calificacion");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                sessionParcel.setSurvey(SessionParcel.S_QUALIFICATION);
                sessionParcel.setQuestionMode(SessionParcel.M_INDIVIDUAL);
                //mListener.setCodeSession(sessionParcel);
                checkSameSession();
            }
        });


        /*
        Opcion opinión acerca de expositor/profesor
        Registrar en firebase
         */
        checkD.setTypeface(LearnApp.appFont1);
        //checkD.setSelected(false);
        checkD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkD.setSelected(true);
                /*
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Opinion")
                        .setAction("Expositor")
                        .build());
                        */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "43");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "OPINION::Expositor");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "OPINION::Expositor");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                sessionParcel.setSurvey(SessionParcel.S_SPEAKER);
                sessionParcel.setQuestionMode(SessionParcel.M_INDIVIDUAL);
                //mListener.setCodeSession(sessionParcel);
                checkSameSession();
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
                getActivity().finish();
            }
        });

        now = System.currentTimeMillis();
        elapsed = (now - appStateParcel.getStartTime())/(60*1000);

        return v;
    }

    /*
    Iniciar servico para crear una leccion con un nuevo codigo o
    mostar diálogo de confirmación
     */
    public void checkSameSession(){
        boolean isZero = appStateParcel.getSessionCode().equals("0000");
        int keepCode = appStateParcel.getKeepCode();
        if (isZero) {
            appStateParcel.setKeepCode(1);
            mListener.setCodeSession(sessionParcel);
            /*
        } else if( elapsed > 30 && !isZero && keepCode == 1 ) {
            showDialogQuestion();
        } else if( elapsed > 30 && !isZero && keepCode == 2 ){
            sessionParcel.setInactive(0);
            startFragmentCoded();
        } else if( elapsed > 60 && !isZero && keepCode == 2 ) {
            showDialogQuestion();
        } else if( elapsed > 60 && !isZero && keepCode == 3 ){
            sessionParcel.setInactive(0);
            startFragmentCoded();
        } else if( elapsed > 90 && !isZero && keepCode == 3 ) {
            showDialogQuestion();
        } else if( elapsed > 90 && !isZero && keepCode == 4 ){
            sessionParcel.setInactive(0);
            startFragmentCoded();
            */
        } else  if (elapsed > 120 && !isZero){
            appStateParcel.setKeepCode(1);
            appStateParcel.setStartTime(System.currentTimeMillis());
            appStateParcel.setSessionCode("0000");
            mListener.setCodeSession(sessionParcel);
        } else {
            showDialogQuestion();
            //sessionParcel.setInactive(0);
            //startFragmentCoded();
        }
    }

    /*
    Dialógo para confirmar si se usa el mismo código
     */
    public void showDialogQuestion(){
        DialogFragment newFragment = new ConfirmTwoButtonsSupportDialog();
        Bundle bd = new Bundle();
        bd.putString("TITLE", "");
        bd.putString("MESSAGE", getString(R.string.text_is_same_lesson));
        newFragment.setArguments(bd);
        newFragment.setTargetFragment(FreeOpinionFragment.this,1234);
        newFragment.show(getFragmentManager(), "confirm");
    }

    /*
    Iniciar servicio para crear una lección con el mismo código
     */
    public void startFragmentCoded(){
        sessionParcel.setAccesscode(appStateParcel.getSessionCode());
        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_SURVEY_LESSON);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);
    }

    /*
    Receiver para capturar el estado del a creación de una pregunta/lección
    con el mismo código
     */
    public class NewSessionReceiverO extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS, 0);
            if (status == TeacherLessonController.DONE) {
                sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
                appStateParcel.setSessionCode(sessionParcel.getAccesscode());
                mListener.updateSessionCode(appStateParcel, sessionParcel);
                mListener.showResults(sessionParcel);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_SURVEY_LESSON);
        newSessionReceiver = new NewSessionReceiverO();;
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(newSessionReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(newSessionReceiver);
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

    @Override
    public void onFireCancelEventTwoButtonListener() {
        appStateParcel.setKeepCode(1);
        appStateParcel.setStartTime(System.currentTimeMillis());
        appStateParcel.setSessionCode("0000");
        sessionParcel.setAccesscode("0000");
        mListener.updateSessionCode(appStateParcel, sessionParcel);
        mListener.setCodeSession(sessionParcel);
    }

    // Pantalla a mostar si se mantiene el código o no
    @Override
    public void onFireAcceptEventTwoButtonListener() {
        /*
        if ( elapsed > 30 ){
            appStateParcel.setKeepCode(2);
        } else if ( elapsed > 60 ){
            appStateParcel.setKeepCode(3);
        } else if ( elapsed > 90 ){
            appStateParcel.setKeepCode(4);
        }
        */
        appStateParcel.setKeepCode(2);
        sessionParcel.setInactive(0);
        startFragmentCoded();
    }

}

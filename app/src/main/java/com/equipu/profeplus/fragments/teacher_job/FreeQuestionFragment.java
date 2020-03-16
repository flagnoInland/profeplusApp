package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
Este fragmento permite elegir un tipo de pregunta.
Las opciones se muestran con botones rectangulares.
La opción checkB(Opinión) esta deshabilitada para educación básica
La opción checkE(Evaluación) esta deshabilitada
checkA : DE CINCO ALTERNATIVAS
checkC : BANCO DE PREGUNTAS
checkD : VERDADERO Y FALSO
 */
public class FreeQuestionFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener {

    //private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.chkb_free_exercises) Button checkA;
    @BindView(R.id.chkb_opinion) Button checkB;
    @BindView(R.id.chkb_normal_exercises) Button checkC;
    @BindView(R.id.chkb_dichotomous) Button checkD;
    @BindView(R.id.chkb_eval) Button checkE;
    @BindView(R.id.img_back) ImageView btnback;
    @BindView(R.id.img_exit) ImageView btnexit;
    private Unbinder unbinder;


    private SessionParcel sessionParcel;
    private AppStateParcel appStateParcel;
    NewSessionReceiverQ newSessionReceiver;
    long now, elapsed;


    private ITeacherJobListener mListener;

    public FreeQuestionFragment() {    }


    public static FreeQuestionFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        FreeQuestionFragment fragment = new FreeQuestionFragment();
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
        View v = inflater.inflate(R.layout.fragment_free_question, container, false);
        unbinder = ButterKnife.bind(this, v);

        sessionParcel.setQuestionMode(SessionParcel.M_INDIVIDUAL);

        // Opción opinion no disponible para educación basica
        if (appStateParcel.getAppMode() == AppStateParcel.SCHOOL_MODE){
            checkB.setVisibility(View.GONE);
        }


        /*
        Opcion pregunta de 5 alternativas.
        Registar accion en firebase
         */
        checkA.setTypeface(LearnApp.appFont1);
        //checkA.setSelected(false);
        checkA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkA.setSelected(true);
                /*
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Preguntas")
                        .setAction("5Alternativas")
                        .build());
                        */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "30");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PREGUNTAS::5Alternativas");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PREGUNTAS::5Alternativas");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                sessionParcel.setQuestionType(SessionParcel.Q_NORMAL);
                sessionParcel.setSurvey(SessionParcel.S_NONE);
                sessionParcel.setInactive(1);
                //mListener.setCodeSession(sessionParcel);
                checkSameSession();
            }
        });


        /*
        Opción pregunta opinion
        Registrar preguntar en firebase
         */
        checkB.setTypeface(LearnApp.appFont1);
        //checkB.setSelected(false);
        checkB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkB.setSelected(true);
                /*
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Preguntas")
                        .setAction("Opinion")
                        .build());
                        */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "31");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PREGUNTAS::Opinion");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PREGUNTAS::Opinion");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                sessionParcel.setQuestionType(SessionParcel.Q_SURVEY);
                sessionParcel.setSurvey(SessionParcel.S_NONE);
                sessionParcel.setInactive(1);
                mListener.chooseOpinionType(sessionParcel);

            }
        });


        /*
        Opción banco de preguntas
        Registrar acción en firebase
         */
        checkC.setVisibility(View.GONE);
        checkC.setTypeface(LearnApp.appFont1);
        //checkC.setSelected(false);
        checkC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkC.setSelected(true);
                /*
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Preguntas")
                        .setAction("Banco")
                        .build());
                        */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "32");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PREGUNTAS::Banco");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PREGUNTAS::Banco");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                sessionParcel.setQuestionType(SessionParcel.Q_BANK);
                sessionParcel.setSurvey(SessionParcel.S_NONE);
                sessionParcel.setInactive(1);
                //mListener.setCodeSession(sessionParcel);
                mListener.setSubjectCourse(sessionParcel);
            }
        });


        /*
        Opcion pregunta verdadero-falso
        Registrar acción en firebase
         */
        checkD.setTypeface(LearnApp.appFont1);
        //checkD.setSelected(false);
        checkD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkD.setSelected(true);
                /*
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Preguntas")
                        .setAction("Verdadero-Falso")
                        .build());
                        */
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "33");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PREGUNTAS::Verdad~Falso");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PREGUNTAS::Verdad~Falso");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                sessionParcel.setQuestionType(SessionParcel.Q_TRUE);
                sessionParcel.setSurvey(SessionParcel.S_NONE);
                sessionParcel.setInactive(1);
                //mListener.setCodeSession(sessionParcel);
                checkSameSession();
            }
        });

        /*
        Opcion pregunta evaluacion
        Registrar accion en firebase
         */
        checkE.setTypeface(LearnApp.appFont1);
        //checkE.setSelected(false);
        checkE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkE.setSelected(true);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "34");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PREGUNTAS::Examen");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PREGUNTAS::Examen");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                //appStateParcel.setEditEvaluation(0);
                //EvaluationParcel evaluationParcel = new EvaluationParcel();
                //sessionParcel.setQuestionType(SessionParcel.Q_EVAL);
                //sessionParcel.setSurvey(SessionParcel.S_NONE);
                //mListener.startEvaluationStepOne(appStateParcel, evaluationParcel, sessionParcel);
                enterToEval();
            }
        });

        //checkE.setVisibility(View.GONE);


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        now = System.currentTimeMillis();
        elapsed = (now - appStateParcel.getStartTime())/(60*1000);

        return v;
    }

    /*
    Cargar navegador con enlace a evaluaciones
     */
    public void enterToEval(){
        String mUrl = String.format( "%s/web/exam/teacher/%s/%s",
                LearnApp.baseURL, appStateParcel.getUserId(), appStateParcel.getAppMode());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mUrl));
        startActivity(intent);
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
    Diálogo para confirmar si sigue usando el mismo codigo
     */
    public void showDialogQuestion(){
        DialogFragment newFragment = new ConfirmTwoButtonsSupportDialog();
        Bundle bd = new Bundle();
        bd.putString("TITLE", "");
        bd.putString("MESSAGE", getString(R.string.text_is_same_lesson));
        newFragment.setArguments(bd);
        newFragment.setTargetFragment(FreeQuestionFragment.this,1234);
        newFragment.show(getFragmentManager(), "confirm");
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

    // Mostrar la vista de resultados o la vista para tener un nuevo código
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

    /*
    Iniciar servicio para crear una lección con el mismo código
     */
    public void startFragmentCoded(){
        sessionParcel.setAccesscode(appStateParcel.getSessionCode());
        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_NORMAL_LESSON);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);
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

    /*
    Receiver para capturar el estado del a creación de una pregunta/lección
    con el mismo código
     */
    public class NewSessionReceiverQ extends BroadcastReceiver {

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
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_NORMAL_LESSON);
        newSessionReceiver = new NewSessionReceiverQ();;
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

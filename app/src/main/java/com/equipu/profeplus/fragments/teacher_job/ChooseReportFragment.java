package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.ConfirmOneButtonSupportDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragment permite escoger la solución de la pregunta creada
y conduce a la creación de un reporte programado apara ser enviado al final del día.
La vista contiene dos botones:
INFORME SENCILLO
INFORME DETALLADO
 */
public class ChooseReportFragment extends Fragment
        implements ConfirmOneButtonSupportDialog.OnFireEventOneButtonDialogListener {

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.spn_answer_key) Spinner spnAnswer;
    @BindView(R.id.btn_simple_report) Button btnSimple;
    @BindView(R.id.btn_complete_report) Button btnComplete;
    @BindView(R.id.txt_choose_type_report) TextView txtTitle;
    @BindView(R.id.txt_answer_key) TextView txtAnswer;

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private Unbinder unbinder;

    SessionParcel sessionParcel;
    AppStateParcel appStateParcel;
    UpdateSessionReceiver updateSessionReceiver;

    private ITeacherJobListener mListener;

    public ChooseReportFragment() {
        // Required empty public constructor
    }


    public static ChooseReportFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        ChooseReportFragment fragment = new ChooseReportFragment();
        Bundle args = new Bundle();
        args.putParcelable("SessionParcel", sp);
        args.putParcelable("AppStateParcel", asp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View v = inflater.inflate(R.layout.fragment_choose_report, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtAnswer.setTypeface(LearnApp.appFont);
        txtTitle.setTypeface(LearnApp.appFont);
        btnComplete.setTypeface(LearnApp.appFont);
        btnSimple.setTypeface(LearnApp.appFont);

        String s = getString(R.string.text_button_simple_report);
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.2f), 0, 17, 0);
        ss1.setSpan(new CustomTypefaceSpan(LearnApp.appFont1), 0, 17, 0);
        btnSimple.setText("NO");

        String s2 = getString(R.string.text_button_complete_report);
        SpannableString ss2=  new SpannableString(s2);
        ss2.setSpan(new RelativeSizeSpan(1.2f), 0, 17, 0);
        ss2.setSpan(new CustomTypefaceSpan(LearnApp.appFont1), 0, 17, 0);
        btnComplete.setText("SI");


        // Mostrar alternativas para el reporte
        ArrayAdapter adp1 = ArrayAdapter.createFromResource(getActivity(),R.array.answer_key_array,
                R.layout.app_spinner_item);
        adp1.setDropDownViewResource(R.layout.app_spinner_item);
        spnAnswer.setAdapter(adp1);

        String[] levels = getResources().getStringArray(R.array.answer_key_array);
        Log.d("profeplus.inf", String.valueOf(sessionParcel.getQuestionType()));
        if (sessionParcel.getQuestionType()==SessionParcel.Q_TRUE){
            levels = getResources().getStringArray(R.array.answer_key_true);
            adp1 = ArrayAdapter.createFromResource(getActivity(),R.array.answer_key_true,
                    R.layout.app_spinner_item);
            adp1.setDropDownViewResource(R.layout.app_spinner_item);
            spnAnswer.setAdapter(adp1);
        }

        for (int i=0; i<levels.length; i++){
            if (sessionParcel.getAnswerKey().equals(levels[i])){
                spnAnswer.setSelection(i);
            }
        }

        if (sessionParcel.getQuestionType()==SessionParcel.Q_SURVEY){
            spnAnswer.setVisibility(View.GONE);
            txtAnswer.setVisibility(View.GONE);
        }

        // Botón para enviar reporte simple
        btnSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnAnswer.getSelectedItemPosition() != 0
                        && sessionParcel.getQuestionType() != SessionParcel.Q_SURVEY) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "50");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "REPORTE::Simple");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "REPORTE::Simple");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    sessionParcel.setAnswerKey(spnAnswer.getSelectedItem().toString());
                    sessionParcel.setReport(SessionParcel.R_SIMPLE);
                    startUpdating();
                } else if (sessionParcel.getQuestionType() == SessionParcel.Q_SURVEY) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "50");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "REPORTE::Simple");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "REPORTE::Simple");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    sessionParcel.setReport(SessionParcel.R_SIMPLE);
                    startUpdating();
                } else{
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", "");
                    b.putString("MESSAGE", getString(R.string.text_type_correct_answer));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), null);
                }
            }
        });

        // Botón para enviar reporte completo
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnAnswer.getSelectedItemPosition() != 0
                        && sessionParcel.getQuestionType() != SessionParcel.Q_SURVEY) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "51");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "REPORTE::Completo");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "REPORTE::Completo");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    sessionParcel.setAnswerKey(spnAnswer.getSelectedItem().toString());
                    sessionParcel.setReport(SessionParcel.R_COMPLETE);
                    mListener.makeCompleteReport(sessionParcel, appStateParcel);
                } else if (sessionParcel.getQuestionType() == SessionParcel.Q_SURVEY) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "51");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "REPORTE::Completo");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "REPORTE::Completo");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                    sessionParcel.setAnswerKey(spnAnswer.getSelectedItem().toString());
                    sessionParcel.setReport(SessionParcel.R_COMPLETE);
                    mListener.makeCompleteReport(sessionParcel, appStateParcel);
                } else{
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", "");
                    b.putString("MESSAGE", getString(R.string.text_type_correct_answer));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), null);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return v;

    }

    /*
    Iniciar servio para actualizar lección en base dedatos
     */
    public void startUpdating(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
            intent.putExtra(NetworkTeacherService.SERVICE,
                    NetworkTeacherService.SERVICE_UPDATE_SIMPLE_REPORT);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
            getActivity().startService(intent);
        } else {
            mListener.dialogLostConnection();
        }
    }

    /*
    Receiver para capturar el estado del reporte enviado por mail
     */
    public class UpdateSessionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.INVISIBLE);
            int status = intent.getIntExtra(NetworkTeacherService.STATUS, 0);
            if (status== TeacherLessonController.DONE) {
                mListener.makeReport(appStateParcel, sessionParcel);
                DialogFragment newFragment = new ConfirmOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", "");
                b.putString("MESSAGE", getResources().getString(R.string.msg_will_be_sent_message));
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), "success");
                newFragment.setTargetFragment(ChooseReportFragment.this, 1234);
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_UPDATE_SIMPLE_REPORT);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        updateSessionReceiver = new UpdateSessionReceiver();
        lbm.registerReceiver(updateSessionReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(updateSessionReceiver);
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

    public class CustomTypefaceSpan extends MetricAffectingSpan {
        private final Typeface typeface;

        public CustomTypefaceSpan(final Typeface typeface)
        {
            this.typeface = typeface;
        }

        @Override
        public void updateDrawState(final TextPaint drawState)
        {
            apply(drawState);
        }

        @Override
        public void updateMeasureState(final TextPaint paint)
        {
            apply(paint);
        }

        private void apply(final Paint paint)
        {
            final Typeface oldTypeface = paint.getTypeface();
            final int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;
            final int fakeStyle = oldStyle & ~typeface.getStyle();

            if ((fakeStyle & Typeface.BOLD) != 0)
            {
                paint.setFakeBoldText(true);
            }

            if ((fakeStyle & Typeface.ITALIC) != 0)
            {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(typeface);
        }
    }

    @Override
    public void onFireAcceptEventOneButtonListener() {
        //SessionReportHandler
    }

    @Override
    public void onFireCancelEventOneButtonListener() {
        ////SessionReportHandler
    }


}

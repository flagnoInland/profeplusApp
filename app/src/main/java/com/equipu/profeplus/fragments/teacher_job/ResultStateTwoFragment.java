package com.equipu.profeplus.fragments.teacher_job;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.models.AnswerList;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento muestra los resultados finales del paso 1 para preguntas
de opinión, banco y cinco alternativas.
btnRefresh esta deshabilitado
Se muestra las opciones
INICIAR PASO 2
FINALIZAR
Para banco de preguntas se muestra una leyenda detallada.
 */
public class ResultStateTwoFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_finish_exit) Button btnFinish;
    @BindView(R.id.btn_finish_survey) Button btnFinishSurvey;
    @BindView(R.id.btn_start_two) Button btnStartTwo;
    @BindView(R.id.btn_refresh_result) Button btnRefresh;
    @BindView(R.id.txt_run_descrption) TextView txtRunDescription;
    //@BindView(R.id.txt_results) TextView txtResults;
    @BindView(R.id.lay_normal)  LinearLayout layNormal;
    @BindView(R.id.legend_survey) TextView txtLegendSurvey;

    private Unbinder unbinder;

    private SessionParcel sessionParcel;
    private AppStateParcel appStateParcel;
    AnswerList answerList;
    AnswerList newList;
    StartTwoReceiver startTwoReceiver;
    GetDataReceiver getDataReceiver;


    BarChart mChart;
    public final static int blue  = 0xFF74B47C;
    public final static int red  = 0xFFDAA520;
    public static final int[] BLUE_COLORS = {blue,blue,blue,blue,blue,blue};
    public static final int[] RED_COLORS = {red,red,red,red,red,red};


    private ITeacherJobListener mListener;

    public ResultStateTwoFragment() {}

    public static ResultStateTwoFragment newInstance(SessionParcel sp, AppStateParcel asp, AnswerList al) {
        ResultStateTwoFragment fragment = new ResultStateTwoFragment();
        Bundle args = new Bundle();
        args.putParcelable("SessionParcel", sp);
        args.putParcelable("AppStateParcel", asp);
        args.putParcelable("AnswerList", al);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sessionParcel = getArguments().getParcelable("SessionParcel");
            appStateParcel = getArguments().getParcelable("AppStateParcel");
            answerList = getArguments().getParcelable("AnswerList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result_state_two, container, false);
        unbinder = ButterKnife.bind(this, v);

        mChart = (BarChart) v.findViewById(com.equipu.profeplus.R.id.chart_results_task);

        //txtResults.setTypeface(LearnApp.appFont);
        btnFinish.setTypeface(LearnApp.appFont);
        btnFinishSurvey.setTypeface(LearnApp.appFont);
        btnStartTwo.setTypeface(LearnApp.appFont);
        txtRunDescription.setTypeface(LearnApp.appFont);

        String[] alternatives = new String[]{"A","B","C","D","E"};
        txtLegendSurvey.setVisibility(View.GONE);
        if (sessionParcel.getQuestionType() == SessionParcel.Q_SURVEY){
            DisplayMetrics outMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            ViewGroup.LayoutParams layoutParams = mChart.getLayoutParams();
            layoutParams.height =  5*outMetrics.heightPixels/12;
            mChart.setLayoutParams(layoutParams);
            txtLegendSurvey.setVisibility(View.VISIBLE);
            if (sessionParcel.getSurvey() == SessionParcel.S_QUALIFICATION){
                alternatives = getResources().getStringArray(R.array.question_opinion3_array);
            } else if (sessionParcel.getSurvey() == SessionParcel.S_SATISFACTION){
                alternatives = getResources().getStringArray(R.array.question_opinion1_array);
            } else if (sessionParcel.getSurvey() == SessionParcel.S_SPEAKER){
                alternatives = getResources().getStringArray(R.array.question_speaker_array);
            }
            txtLegendSurvey.setText(String.format(getString(R.string.text_legend_survey),
                    alternatives[0],alternatives[1],alternatives[2],
                    alternatives[3],alternatives[4]));
        }

        String labelBtn = String.format("%s \n %s %s",getString(R.string.text_begin),
                getString(R.string.text_step),sessionParcel.getRun()+1);
        btnStartTwo.setText(labelBtn);


        setCaption(0);

        // Para preguntas de opinion mostrar alternativas con nombre completo
        if (sessionParcel.getSurvey() > 1){
            layNormal.setVisibility(View.GONE);
            btnFinishSurvey.setVisibility(View.VISIBLE);
        } else {
            layNormal.setVisibility(View.VISIBLE);
            btnFinishSurvey.setVisibility(View.GONE);
        }





        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Ocultar botón actualizar
        btnRefresh.setVisibility(View.GONE);


        // Botón para iniciar el paso 2
        btnStartTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int runNumber = sessionParcel.getRun();
                sessionParcel.setRun(runNumber+1);
                sessionParcel.setInactive(0);
                sessionParcel.setPreviousId(sessionParcel.getSessionId());
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
                    intent.putExtra(NetworkTeacherService.SERVICE,
                            NetworkTeacherService.SERVICE_NEW_SESSION);
                    intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                    intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
                    getActivity().startService(intent);
                } else {
                    mListener.dialogLostConnection();
                }

            }
        });


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ConfirmTwoButtonsSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", "");
                b.putString("MESSAGE", getResources().getString(R.string.text_want_report_for_question));
                newFragment.setArguments(b);
                newFragment.setTargetFragment(ResultStateTwoFragment.this,1234);
                newFragment.show(getFragmentManager(), "dialog");
            }
        });

        btnFinishSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ConfirmTwoButtonsSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", "");
                b.putString("MESSAGE", getResources().getString(R.string.text_want_report_for_question));
                newFragment.setArguments(b);
                newFragment.setTargetFragment(ResultStateTwoFragment.this,1234);
                newFragment.show(getFragmentManager(), "dialog");
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
                mListener.finishLesson(sessionParcel);
            }
        });

        // Adquirir resultados al iniciar
        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_GET_DATA);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);

        return v;

    }


    /*
    Colocar detalles de la lección
     */
    private void setCaption(int students){
        String caption = String.format("%s %s: %s %s  %s=%d", getString(R.string.text_step),
                sessionParcel.getRun(),
                getString(R.string.text_code), sessionParcel.getAccesscode(),
                getString(R.string.text_students),
                students);
        txtRunDescription.setText(caption);
    }

    /*
    Adquirir resultados y mostrar gráfico
     */
    public class GetDataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS,0);
            if (status == TeacherLessonController.DONE){
                newList = intent.getParcelableExtra(NetworkTeacherService.NEW_LIST);
                setCaption(newList.getInLesson());
                generateChart(newList);
            }
        }
    }

    /*
    Mostar pantalla del segundo paso
     */
    public class StartTwoReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS,0);
            if (status == TeacherLessonController.DONE){
                if (mListener != null){
                    sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
                    mListener.startResultStateThree(sessionParcel);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_NEW_SESSION);
        startTwoReceiver = new StartTwoReceiver();
        lbm.registerReceiver(startTwoReceiver, filter);
        IntentFilter filter1 = new IntentFilter(NetworkTeacherService.ACTION_GET_DATA);
        getDataReceiver = new GetDataReceiver();
        lbm.registerReceiver(getDataReceiver, filter1);

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(startTwoReceiver);
        lbm.unregisterReceiver(getDataReceiver);
    }


    /*
    Etiquetas para el eje horizonttal
     */
    private ArrayList<String> preparePercentageValues(AnswerList al){
        int value = 0;
        ArrayList<String> label = new ArrayList<>();
        value = (int) (100f*al.getAnsA()/(1f*al.getInLesson()));
        label.add(String.format("%d%%",value));
        value = (int) (100f*al.getAnsB()/(1f*al.getInLesson()));
        label.add(String.format("%d%%",value));
        value = (int) (100f*al.getAnsC()/(1f*al.getInLesson()));
        label.add(String.format("%d%%",value));
        value = (int) (100f*al.getAnsD()/(1f*al.getInLesson()));
        label.add(String.format("%d%%",value));
        value = (int) (100f*al.getAnsE()/(1f*al.getInLesson()));
        label.add(String.format("%d%%",value));
        value = (int) (100f*al.getAnsNN()/(1f*al.getInLesson()));
        label.add(String.format("%d%%",value));
        return label;
    }

    /*
    Crear gráfico para el paso 1
     */
    private void generateChart(AnswerList answerList) {

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        BarDataSet ds;
        XAxis xAxis = mChart.getXAxis();
        YAxis leftAxis = mChart.getAxisLeft();

        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(answerList.getAnsA(),0));
        entries.add(new BarEntry(answerList.getAnsB(),1));
        entries.add(new BarEntry(answerList.getAnsC(),2));
        entries.add(new BarEntry(answerList.getAnsD(),3));
        entries.add(new BarEntry(answerList.getAnsE(),4));
        entries.add(new BarEntry(answerList.getAnsNN(),5));

        /* Chart library version > 3.0.0
        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(answerList.getAnsA(),0, "A"));
        entries.add(new BarEntry(answerList.getAnsB(),1, "B"));
        entries.add(new BarEntry(answerList.getAnsC(),2, "C"));
        entries.add(new BarEntry(answerList.getAnsD(),3, "D"));
        entries.add(new BarEntry(answerList.getAnsE(),4, "E"));
        entries.add(new BarEntry(answerList.getAnsNN(),5, "S.R."));
        */
        ds = new BarDataSet(entries, "Respuestas");
        ds.setColors(BLUE_COLORS);
        ds.setValueFormatter(new MyFormat());

        Log.d("profeplus.msg", String.format("%d %d", metrics.densityDpi, DisplayMetrics.DENSITY_360));
        if (metrics.densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            xAxis.setTextSize(10f);
            ds.setValueTextSize(12f);
            leftAxis.setTextSize(14f);
        } else {
            xAxis.setTextSize(12f);
            ds.setValueTextSize(12f);
            leftAxis.setTextSize(14f);
        }


        mChart.setScaleEnabled(false);
        mChart.setHighlightPerDragEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDrawBorders(false);
        mChart.setExtraTopOffset(24f);
        mChart.setExtraBottomOffset(40f);

        mChart.setDescription("");

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        // Remove if chart library version > 3.0.0
        xAxis.setSpaceBetweenLabels(1);

        float maxY = 0f;
        if (answerList.getInLesson()==0){
            maxY = 10f;
        }else{
            maxY = answerList.getInLesson();
        }
        if (answerList.getInLesson() < 6) {
            leftAxis.setLabelCount(answerList.getInLesson()+1, true);
        }
        if (answerList.getInLesson() == 6) {
            leftAxis.setLabelCount(4, true);
        }
        if (answerList.getInLesson() > 6) {
            if (answerList.getInLesson() > 2*(answerList.getInLesson()/2)){
                leftAxis.setLabelCount(4, true);
            } else {
                leftAxis.setLabelCount(5, true);
            }
        }
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(30f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaxValue(maxY);
        leftAxis.setValueFormatter(new MyFormat2());

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        Legend l = mChart.getLegend();
        l.setEnabled(false);


        sets.add(ds);
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<String> xAltVals = preparePercentageValues(answerList);
        xVals.add("A");
        xVals.add("B");
        xVals.add("C");
        xVals.add("D");
        xVals.add("E");
        xVals.add(getString(R.string.text_alternative_no_answer));
        BarData d = new BarData(xVals, xAltVals, sets);

        // Add if chart library version < 3.0.0
        // BarData d = new BarData(ds);
        d.setValueFormatter(new MyFormat());

        mChart.setData(d);
        mChart.invalidate();

    }

    public class MyFormat implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyFormat() {
            mFormat = new DecimalFormat("#");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }

    }

    public class MyFormat2 implements YAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyFormat2() {
            mFormat = new DecimalFormat("#");
        }

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            return mFormat.format(value);
        }
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
        mListener.finishLesson(sessionParcel);
    }

    @Override
    public void onFireAcceptEventTwoButtonListener() {
        mListener.setReportData(sessionParcel);
    }

}

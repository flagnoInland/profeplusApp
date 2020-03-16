package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.dialogs.SessionCodeSupportDialog;
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
Este fragmento muestra los resultados parciales del paso 1 para preguntas de verdadero y falso
Se muestra las opciones
ACTUALIZAR RESULTADOS
TERMINAR PASO 1
ENVIAR REPORTE
 */
public class ResultTFStateOneFragment extends Fragment
        implements SessionCodeSupportDialog.OnFireEventSessionCodeListener,
        ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener  {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_get_report) Button btnReport;
    @BindView(R.id.btn_finish_one) Button btnFinishOne;
    @BindView(R.id.btn_refresh_result) Button btnRefresh;
    @BindView(R.id.txt_run_descrption) TextView txtRunDescription;
    //@BindView(R.id.txt_results) TextView txtResults;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private Unbinder unbinder;

    private SessionParcel sessionParcel;
    private AppStateParcel appStateParcel;
    FinishOneReceiver finishOneReceiver;
    GetDataReceiver getDataReceiver;
    WebServiceReceiver webServiceReceiver;

    AnswerList newList;
    View layout;

    BarChart mChart;
    public final static int blue  = 0xFF74B47C;
    public final static int red  = 0xFFDAA520;
    public static final int[] BLUE_COLORS = {blue,blue,blue,blue,blue,blue};
    public static final int[] RED_COLORS = {red,red,red,red,red,red};

    private ITeacherJobListener mListener;
    private boolean isFragmentVisible;

    public ResultTFStateOneFragment() {

    }


    public static ResultTFStateOneFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        ResultTFStateOneFragment fragment = new ResultTFStateOneFragment();
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
        View v = inflater.inflate(R.layout.fragment_result_state_one, container, false);
        unbinder = ButterKnife.bind(this, v);
        progressBar.setVisibility(View.VISIBLE);

        layout = inflater.inflate(R.layout.code_toast_layout,
                (ViewGroup) v.findViewById(R.id.lin_code_toast));

        //txtResults.setTypeface(LearnApp.appFont);
        btnReport.setTypeface(LearnApp.appFont);
        btnFinishOne.setTypeface(LearnApp.appFont);
        txtRunDescription.setTypeface(LearnApp.appFont);

        if (appStateParcel.getKeepCode()==2){
            showNotification(getString(R.string.text_assign_same_code));
        }


        setCaption(0);


        mChart = (BarChart) v.findViewById(com.equipu.profeplus.R.id.chart_results_task);


        // Botón para Actualizar resultados del paso 1
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sessionParcel.getAccesscode().equals("0000")) {
                    retrieveChartData();
                }

            }
        });


        // Botón para Finalizar paso 1
        btnFinishOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sessionParcel.getAccesscode().equals("0000")) {
                    DialogFragment newFragment = new ConfirmTwoButtonsSupportDialog();
                    Bundle bd = new Bundle();
                    bd.putString("TITLE", getString(R.string.empty));
                    bd.putString("MESSAGE", getString(R.string.text_want_finish_step_one_start_step_two));
                    newFragment.setArguments(bd);
                    newFragment.setTargetFragment(ResultTFStateOneFragment.this, 1234);
                    newFragment.show(getFragmentManager(), "finish");
                }
            }
        });


        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && !sessionParcel.getAccesscode().equals("0000")){
                    mListener.setReportData(sessionParcel);
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
                if (!sessionParcel.getAccesscode().equals("0000")) {
                    progressBar.setVisibility(View.VISIBLE);
                    mListener.finishLesson(sessionParcel);
                } else {
                    getActivity().onBackPressed();
                }
            }
        });

        startNewSessionWithCode();
        return v;

    }

    public void showNotification(String mMessage){
        TextView toastNot = (TextView) layout.findViewById(R.id.toast_answer_msg);
        toastNot.setText(mMessage);
        Toast mNot = new Toast(getContext());
        mNot.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        mNot.setDuration(Toast.LENGTH_SHORT);
        mNot.setView(layout);
        mNot.show();
    }

    /*
    Colocar detalles del paso 1
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
    Mostrar resultados finales del paso 1
     */
    public class FinishOneReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS,0);
            if (status == TeacherLessonController.DONE){
                if (mListener != null){
                    mListener.startResultTFStateTwo(sessionParcel, newList);
                }
            }
        }
    }

    /*
    Adquirir resultados del paso 1 y mostar gráfico
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

    private void startStepTwo(){
        int runNumber = sessionParcel.getRun();
        sessionParcel.setRun(runNumber + 1);
        sessionParcel.setInactive(0);
        sessionParcel.setPreviousId(sessionParcel.getSessionId());
        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_NEW_SESSION);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        webServiceReceiver = new WebServiceReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_DISABLE_SESSION);
        IntentFilter filter1 = new IntentFilter(NetworkTeacherService.ACTION_GET_DATA);
        IntentFilter filter2 = new IntentFilter(NetworkTeacherService.ACTION_NEW_SESSION);
        IntentFilter filter3 = new IntentFilter(NetworkTeacherService.ACTION_UPDATE_SESSION);
        lbm.registerReceiver(webServiceReceiver, filter);
        lbm.registerReceiver(webServiceReceiver, filter1);
        lbm.registerReceiver(webServiceReceiver, filter2);
        lbm.registerReceiver(webServiceReceiver, filter3);

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(webServiceReceiver);
    }

    public class WebServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS,0);
            String mAction = intent.getAction();
            if (status == TeacherLessonController.DONE){
                if (mAction.equals(NetworkTeacherService.ACTION_DISABLE_SESSION)
                        && mListener != null){
                    //mListener.startResultStateTwo(sessionParcel, newList);
                    if (sessionParcel.getQuestionType() != SessionParcel.Q_SURVEY) {
                        startStepTwo();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        mListener.finishLesson(sessionParcel);
                    }
                } else if (mAction.equals(NetworkTeacherService.ACTION_GET_DATA)) {
                    newList = intent.getParcelableExtra(NetworkTeacherService.NEW_LIST);
                    setCaption(newList.getInLesson());
                    progressBar.setVisibility(View.INVISIBLE);
                    generateChart(newList);
                } else if (mAction.equals(NetworkTeacherService.ACTION_NEW_SESSION)
                        && isFragmentVisible && mListener != null){
                    sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
                    mListener.updateSessionCode(appStateParcel, sessionParcel);
                    mListener.startResultTFStateThree(sessionParcel);
                } else if (mAction.equals(NetworkTeacherService.ACTION_NEW_SESSION)
                        && !isFragmentVisible && mListener != null){
                    isFragmentVisible = true;
                    sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
                    mListener.updateSessionCode(appStateParcel, sessionParcel);
                    showSessionCodeDialog();
                    //updateSessionWithCode();
                } else if (mAction.equals(NetworkTeacherService.ACTION_UPDATE_SESSION)){
                    if (sessionParcel.getQuestionType() != SessionParcel.Q_EVAL) {
                        retrieveChartData();
                    }
                }
            } else if (status == TeacherLessonController.CONNECTION){
                mListener.dialogLostConnection();
            }

        }
    }

    /*
    Etiquetas para el eje horizontal
     */
    private ArrayList<String> preparePercentageValues(AnswerList al){
        int value = 0;
        ArrayList<String> label = new ArrayList<>();
        value = (int) (100f*al.getAnsTrue()/(1f*al.getInLesson()));
        label.add(String.format("%d%%",value));
        value = (int) (100f*al.getAnsFalse()/(1f*al.getInLesson()));
        label.add(String.format("%d%%",value));
        value = (int) (100f*al.getAnsNA()/(1f*al.getInLesson()));
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
        entries.add(new BarEntry(answerList.getAnsTrue(),0));
        entries.add(new BarEntry(answerList.getAnsFalse(),1));
        entries.add(new BarEntry(answerList.getAnsNA(),2));
        entries.add(new BarEntry(answerList.getAnsNN(),3));

        /*
        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(answerList.getAnsTrue(),0, getString(R.string.symbol_v)));
        entries.add(new BarEntry(answerList.getAnsFalse(),1, getString(R.string.symbol_f)));
        entries.add(new BarEntry(answerList.getAnsNA(),2, getString(R.string.text_alternative_dont_know)));
        entries.add(new BarEntry(answerList.getAnsNN(),3, getString(R.string.text_alternative_no_answer)));
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
        xVals.add(getString(R.string.symbol_v));
        xVals.add(getString(R.string.symbol_f));
        xVals.add(getString(R.string.text_alternative_dont_know));
        xVals.add(getString(R.string.text_alternative_no_answer));
        BarData d = new BarData(xVals, xAltVals, sets);

        //BarData d = new BarData(ds);
        d.setValueFormatter(new MyFormat());

        mChart.setData(d);
        mChart.invalidate();

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
        progressBar.setVisibility(View.INVISIBLE);
        unbinder.unbind();
    }

    private void showSessionCodeDialog(){
        progressBar.setVisibility(View.INVISIBLE);
        DialogFragment newFragment = new SessionCodeSupportDialog();
        Bundle bd = new Bundle();
        bd.putString("CODE", sessionParcel.getAccesscode());
        newFragment.setArguments(bd);
        newFragment.setTargetFragment(ResultTFStateOneFragment.this,1234);
        newFragment.show(getFragmentManager(), null);
    }

    @Override
    public void onFireAcceptEventTwoButtonListener() {
        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_DISABLE_SESSION);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);
    }

    @Override
    public void onFireCancelEventTwoButtonListener() {

    }

    @Override
    public void onFireOkEventSessionCodeListener() {
        Log.d("profeplus.qtype", String.valueOf(sessionParcel.getQuestionType()));
        //startNewSessionWithCode();
        progressBar.setVisibility(View.VISIBLE);
        updateSessionWithCode();
        //retrieveChartData();
    }

    public void startNewSessionWithCode(){
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
        }
    }

    public void updateSessionWithCode(){
        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_UPDATE_SESSION);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);
    }

    public void retrieveChartData(){
        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_GET_DATA);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);
    }


}

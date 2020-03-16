package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
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
Este fragmento muestra los resultados parciales del paso 2 para banco de preguntas
 y cinco alternativas.
Se muestra las opciones
TERMINAR PASO 2
ACTUALIZAR RESULTADOS
 */
public class ResultStateThreeFragment extends Fragment {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_finish_two) Button btnFinishTwo;
    @BindView(R.id.btn_refresh_result) Button btnRefresh;
    @BindView(R.id.txt_run_descrption) TextView txtRunDescription;
    @BindView(R.id.legend_survey) TextView txtLegendSurvey;

    private Unbinder unbinder;

    private SessionParcel sessionParcel;
    private AppStateParcel appStateParcel;
    FinishTwoReceiver finishTwoReceiver;
    CompareDataReceiver compareDataReceiver;
    View layout;

    BarChart mChart;
    public final static int blue  = 0xFF74B47C;
    public final static int red  = 0xFFDAA520;
    public static final int[] BLUE_COLORS = {blue,blue,blue,blue,blue,blue};
    public static final int[] RED_COLORS = {red,red,red,red,red,red};

    AnswerList oldList;
    AnswerList newList;


    private ITeacherJobListener mListener;

    public ResultStateThreeFragment() { }


    public static ResultStateThreeFragment newInstance(SessionParcel sp, AppStateParcel asp, AnswerList al) {
        ResultStateThreeFragment fragment = new ResultStateThreeFragment();
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
        View v = inflater.inflate(R.layout.fragment_result_state_three, container, false);
        unbinder = ButterKnife.bind(this, v);

        layout = inflater.inflate(R.layout.code_toast_layout,
                (ViewGroup) v.findViewById(R.id.lin_code_toast));

        showNotification(getString(R.string.text_remember_student_update));

        txtLegendSurvey.setVisibility(View.GONE);
        //txtResults.setTypeface(LearnApp.appFont);
        btnFinishTwo.setTypeface(LearnApp.appFont);
        txtRunDescription.setTypeface(LearnApp.appFont);

        String labelBtn = String.format("%s \n %s %s",getString(R.string.text_terminate),
                getString(R.string.text_step),sessionParcel.getRun());
        btnFinishTwo.setText(labelBtn);


        setCaption(0);

        mChart = (BarChart) v.findViewById(com.equipu.profeplus.R.id.chart_results_task);

        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_COMPARE_DATA);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);

        // Botón para actualizar resultados del paso 2
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
                    intent.putExtra(NetworkTeacherService.SERVICE,
                            NetworkTeacherService.SERVICE_COMPARE_DATA);
                    intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                    intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
                    getActivity().startService(intent);
                } else {
                    mListener.dialogLostConnection();
                }
            }
        });

        // Botón para finalizar el paso 2
        btnFinishTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
                    intent.putExtra(NetworkTeacherService.SERVICE,
                            NetworkTeacherService.SERVICE_DISABLE_SESSION);
                    intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                    intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
                    getActivity().startService(intent);
                } else {
                    mListener.dialogLostConnection();
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
                mListener.finishLesson(sessionParcel);
            }
        });

        return v;
    }


    public void showNotification(String mMessage){
        TextView toastNot = (TextView) layout.findViewById(R.id.toast_answer_msg);
        toastNot.setText(mMessage);
        Toast mNot = new Toast(getContext());
        mNot.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        mNot.setDuration(Toast.LENGTH_LONG);
        mNot.setView(layout);
        mNot.show();
    }

    /*
    Colocar detalles del paso 2
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
    Mostrar los resultados finales del paso 2
     */
    public class FinishTwoReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS,0);
            if (status == TeacherLessonController.DONE){
                if (mListener != null){
                    mListener.startResultStateFour(sessionParcel, oldList, newList);
                }
            }
        }
    }


    /*
    Acquirir resultados del paso 2 y mostrar gráfico
     */
    public class CompareDataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS,0);
            if (status == TeacherLessonController.DONE){
                oldList = intent.getParcelableExtra(NetworkTeacherService.OLD_LIST);
                newList = intent.getParcelableExtra(NetworkTeacherService.NEW_LIST);
                setCaption(newList.getInLesson());
                comparativeChart(newList, oldList);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_DISABLE_SESSION);
        finishTwoReceiver = new FinishTwoReceiver();
        lbm.registerReceiver(finishTwoReceiver, filter);
        IntentFilter filter1 = new IntentFilter(NetworkTeacherService.ACTION_COMPARE_DATA);
        compareDataReceiver = new CompareDataReceiver();
        lbm.registerReceiver(compareDataReceiver, filter1);

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(finishTwoReceiver);
        lbm.unregisterReceiver(compareDataReceiver);
    }


    /*
    Etiquetas del eje horizontal
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

    protected void comparativeChart(AnswerList answerList, AnswerList ianswerList) {

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        BarDataSet ds, ds1;
        XAxis xAxis = mChart.getXAxis();
        YAxis leftAxis = mChart.getAxisLeft();

        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
        entries.add(new BarEntry(answerList.getAnsA(),0));
        entries.add(new BarEntry(answerList.getAnsB(),1));
        entries.add(new BarEntry(answerList.getAnsC(),2));
        entries.add(new BarEntry(answerList.getAnsD(),3));
        entries.add(new BarEntry(answerList.getAnsE(),4));
        entries.add(new BarEntry(answerList.getAnsNN(),5));
        entries2.add(new BarEntry(ianswerList.getAnsA(),0));
        entries2.add(new BarEntry(ianswerList.getAnsB(),1));
        entries2.add(new BarEntry(ianswerList.getAnsC(),2));
        entries2.add(new BarEntry(ianswerList.getAnsD(),3));
        entries2.add(new BarEntry(ianswerList.getAnsE(),4));
        entries2.add(new BarEntry(ianswerList.getAnsNN(),5));
        /* Chart library version > 3.0.0
        List<BarEntry> entries = new ArrayList<BarEntry>();
        List<BarEntry> entries2 = new ArrayList<BarEntry>();
        entries.add(new BarEntry(answerList.getAnsA(),0, "A"));
        entries.add(new BarEntry(answerList.getAnsB(),1, "B"));
        entries.add(new BarEntry(answerList.getAnsC(),2, "C"));
        entries.add(new BarEntry(answerList.getAnsD(),3, "D"));
        entries.add(new BarEntry(answerList.getAnsE(),4, "E"));
        entries.add(new BarEntry(answerList.getAnsNN(),5, "S.R."));
        entries2.add(new BarEntry(ianswerList.getAnsA(),0, "A"));
        entries2.add(new BarEntry(ianswerList.getAnsB(),1, "B"));
        entries2.add(new BarEntry(ianswerList.getAnsC(),2, "C"));
        entries2.add(new BarEntry(ianswerList.getAnsD(),3, "D"));
        entries2.add(new BarEntry(ianswerList.getAnsE(),4, "E"));
        entries2.add(new BarEntry(ianswerList.getAnsNN(),5, "S.R."));
        */
        ds = new BarDataSet(entries, getString(R.string.text_step_two));
        ds.setColors(RED_COLORS);
        ds1 = new BarDataSet(entries2, getString(R.string.text_step_one));
        ds1.setColors(BLUE_COLORS);
        ds.setValueFormatter(new MyFormat());
        ds1.setValueFormatter(new MyFormat());

        if (metrics.densityDpi < DisplayMetrics.DENSITY_HIGH) {
            xAxis.setTextSize(10f);
            ds.setValueTextSize(12f);
            ds1.setValueTextSize(12f);
            leftAxis.setTextSize(14f);
        } else {
            xAxis.setTextSize(12f);
            ds.setValueTextSize(12f);
            ds1.setValueTextSize(12f);
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
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(1);

        // Add if library version > 3.0.0
        // xAxis.setCenterAxisLabels(true);


        float maxY = 0f;
        if (answerList.getInLesson()==0){
            if (ianswerList.getInLesson() == 0){
                maxY = 10;
            } else {
                maxY = ianswerList.getInLesson();
            }
        } else if (answerList.getInLesson() < ianswerList.getInLesson()){
            maxY = ianswerList.getInLesson();
        } else {
            maxY = answerList.getInLesson();
        }

        if (maxY < 6) {
            leftAxis.setLabelCount(answerList.getInLesson()+1, true);
        }
        if (maxY == 6) {
            leftAxis.setLabelCount(4, true);
        }
        if (maxY > 6) {
            if (maxY > 2*(maxY/2)){
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
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        int[] lColors = new int[]{Color.rgb(0, 0, 255),Color.rgb(255, 0, 0)};
        String[] lLabels = new String[]{getString(R.string.text_step_one),getString(R.string.text_step_two)};
        l.setCustom(lColors, lLabels);
        l.setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);
        l.setEnabled(false);



        sets.add(ds1);
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
        mChart.setData(d);
        /*
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        BarData data = new BarData(ds, ds1);
        data.setBarWidth(barWidth); // set the width of each bar
        mChart.setData(data);
        mChart.groupBars(answerList.getAnsA(), groupSpace, barSpace);
        */
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

    /*
    public class MyFormat2 implements AxisValueFormatter {

        private DecimalFormat mFormat;

        public MyFormat2() {
            mFormat = new DecimalFormat("#");
        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value);
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }
    }
    */



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

}

package com.equipu.profeplus.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.equipu.profeplus.R;
import com.equipu.profeplus.models.AnswerList;
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

public class MockActivity extends AppCompatActivity {


    AnswerList newList, answerList;
    BarChart mChart;
    public final static int blue  = 0xFF74B47C;
    public final static int red  = 0xFFDAA520;
    public static final int[] BLUE_COLORS = {blue,blue,blue,blue,blue,blue};
    public static final int[] RED_COLORS = {red,red,red,red,red,red};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_state_three);

        mChart = (BarChart) findViewById(com.equipu.profeplus.R.id.chart_results_task);
        answerList = new AnswerList();
        answerList.setAnsA(5);
        answerList.setAnsB(10);
        answerList.setAnsC(20);
        answerList.setAnsD(5);
        answerList.setAnsE(5);
        answerList.setAnsNN(5);
        answerList.setInLesson(50);
        comparativeChart(answerList,answerList);
    }

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


    private void generateChart(AnswerList answerList) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

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

    protected void comparativeChart(AnswerList answerList, AnswerList ianswerList) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

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


}

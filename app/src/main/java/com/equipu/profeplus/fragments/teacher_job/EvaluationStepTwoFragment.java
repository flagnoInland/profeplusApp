package com.equipu.profeplus.fragments.teacher_job;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.dialogs.AppTimeDialog;
import com.equipu.profeplus.dialogs.BirthDateDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/*
Prototipo para crear evaluación paso 2.
No usado
 */
public class EvaluationStepTwoFragment extends Fragment {

    public final static int blue  = 0xFF74B47C;
    public final static int red  = 0xFFDAA520;
    public static final int[] BLUE_COLORS = {blue,blue,blue,blue,blue,blue};
    public static final int[] RED_COLORS = {red,red,red,red,red,red};
    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.txt_evaluation_start_now) TextView txtTitle1;
    @BindView(R.id.spn_start_now) Spinner spnStart;
    @BindView(R.id.layout_start_yes) LinearLayout layoutYes;
    @BindView(R.id.edt_duration) EditText edtDuration;
    @BindView(R.id.txt_duration_evaluation) TextView txtDuration;
    @BindView(R.id.layout_start_no) LinearLayout layoutNo;
    @BindView(R.id.edt_date) TextView edtDate;
    @BindView(R.id.txt_type_date) TextView txtDate;
    @BindView(R.id.edt_start) TextView edtStart;
    @BindView(R.id.txt_type_start_time) TextView txtStart;
    @BindView(R.id.txt_type_end_time) TextView txtEnd;
    @BindView(R.id.edt_end) TextView edtEnd;
    @BindView(R.id.txt_alert_type_question) TextView txtTitle2;
    @BindView(R.id.btn_next) Button btnNext;
    AppStateParcel appStateParcel;
    EvaluationParcel evaluationParcel;
    SessionParcel sessionParcel;
    private Unbinder unbinder;
    private EvaluationStepTwoListener mListener;

    public EvaluationStepTwoFragment() {
        // Required empty public constructor
    }


    public static EvaluationStepTwoFragment newInstance(AppStateParcel ap, SessionParcel sp,
                                                        EvaluationParcel ep) {
        EvaluationStepTwoFragment fragment = new EvaluationStepTwoFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
        args.putParcelable(LearnApp.PCL_EVALUATION, ep);
        args.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
            evaluationParcel = getArguments().getParcelable(LearnApp.PCL_EVALUATION);
            sessionParcel = getArguments().getParcelable(LearnApp.PCL_SESSION_PARCEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_evaluation_step_two, container, false);
        unbinder = ButterKnife.bind(this, v);

        appStateParcel.setPager(2);

        txtTitle1.setTypeface(LearnApp.appFont);
        txtTitle2.setTypeface(LearnApp.appFont);
        txtDuration.setTypeface(LearnApp.appFont);
        edtDate.setTypeface(LearnApp.appFont);
        txtDate.setTypeface(LearnApp.appFont);
        edtDuration.setTypeface(LearnApp.appFont);
        edtEnd.setTypeface(LearnApp.appFont);
        txtEnd.setTypeface(LearnApp.appFont);
        edtStart.setTypeface(LearnApp.appFont);
        txtStart.setTypeface(LearnApp.appFont);

        layoutNo.setVisibility(View.GONE);
        layoutYes.setVisibility(View.VISIBLE);


        Log.d("profeplus.dur", String.valueOf(evaluationParcel.getDuration()));
        if ( evaluationParcel.getDuration() != 0){
            edtDuration.setText(String.format("%d",evaluationParcel.getDuration()));
        } else {
            edtDuration.setText("");
        }

        if ( !evaluationParcel.getDate().equals("")){
            edtDate.setText(String.format("%s",evaluationParcel.getDate()));
        }

        if ( !evaluationParcel.getStartTime().equals("")){
            edtStart.setText(String.format("%s",evaluationParcel.getStartTime()));
        }

        if ( !evaluationParcel.getEndTime().equals("")){
            edtEnd.setText(String.format("%s",evaluationParcel.getEndTime()));
        }

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = BirthDateDialog.newInstance(new EvaluationStepTwoHandler());
                Bundle b = new Bundle();
                b.putInt("EDIT", 2);
                b.putString("MDATE","");
                newFragment.setArguments(b);
                newFragment.show(getChildFragmentManager(), "birthdate");
            }
        });

        edtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = AppTimeDialog.newIntance(new EvaluationStepTwoHandler());
                Bundle b = new Bundle();
                b.putInt("START", 1);
                newFragment.setArguments(b);
                newFragment.show(getChildFragmentManager(), "time");
            }
        });

        edtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = AppTimeDialog.newIntance(new EvaluationStepTwoHandler());
                Bundle b = new Bundle();
                b.putInt("START", 0);
                newFragment.setArguments(b);
                newFragment.show(getChildFragmentManager(), "time");
            }
        });

        ArrayAdapter adp1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_yes_no,R.layout.app_spinner_item);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStart.setAdapter(adp1);

        spnStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    layoutYes.setVisibility(View.VISIBLE);
                    layoutNo.setVisibility(View.GONE);
                    appStateParcel.setSaveEvaluation(0);
                    evaluationParcel.setStatusEval(EvaluationParcel.ACTIVE);
                    evaluationParcel.setStatusLesson(EvaluationParcel.ACTIVE);
                }
                if (position == 1) {
                    layoutYes.setVisibility(View.GONE);
                    layoutNo.setVisibility(View.VISIBLE);
                    appStateParcel.setSaveEvaluation(1);
                    evaluationParcel.setStatusEval(EvaluationParcel.ACTIVE);
                    evaluationParcel.setStatusLesson(EvaluationParcel.INACTIVE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                layoutNo.setVisibility(View.GONE);
                layoutYes.setVisibility(View.VISIBLE);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnStart.getSelectedItemPosition()==0 && validateFields()){
                    evaluationParcel.setDuration(
                            Integer.parseInt(edtDuration.getText().toString()));
                    mListener.startEvaluationStepThree(appStateParcel,
                            sessionParcel, evaluationParcel);
                } else if (spnStart.getSelectedItemPosition()==1 && validateFields()){
                    edtDuration.getText().toString().equals("0");
                    evaluationParcel.setDate(edtDate.getText().toString());
                    evaluationParcel.setStartTime(edtStart.getText().toString());
                    evaluationParcel.setEndTime(edtEnd.getText().toString());
                    mListener.startEvaluationStepThree(appStateParcel,
                            sessionParcel, evaluationParcel);
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
                mListener.finishEvaluation(appStateParcel, sessionParcel);
            }
        });



        return v;
    }



    public boolean validateFields(){
        if ( spnStart.getSelectedItemPosition()==0 ){
            if (!edtDuration.getText().toString().equals("")){
                return true;
            }
        } else if (spnStart.getSelectedItemPosition()== 1){
            if (!edtDate.getText().toString().equals("")
                    && !edtEnd.getText().toString().equals("")
                    && !edtStart.getText().toString().equals("") ){
                return true;
            }
        }
        DialogFragment newFragment = new NotificationOneButtonSupportDialog();
        Bundle b = new Bundle();
        b.putString("TITLE", getResources().getString(R.string.text_alerta));
        b.putString("MESSAGE", getResources().getString(R.string.msg_fill_all_fields));
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), "failure");
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EvaluationStepTwoListener) {
            mListener = (EvaluationStepTwoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EvaluationStepTwoListener");
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


    public interface EvaluationStepTwoListener {
        void startEvaluationStepThree(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep);
        void finishEvaluation(AppStateParcel ap, SessionParcel sp);
    }


    public class EvaluationStepTwoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LearnApp.MSG_BIRTH_DIALOG) {
                Log.d("profeplus.Msg", String.valueOf(msg.what));
                String birth = msg.getData().getString("BIRTH");
                edtDate.setText(birth);
            }
            if (msg.what == LearnApp.MSG_TIME_DIALOG) {
                Log.d("profeplus.Msg", String.valueOf(msg.what));
                String time = msg.getData().getString("TIME");
                int start = msg.getData().getInt("START");
                if (start == 1) {
                    edtStart.setText(time);
                } else {
                    edtEnd.setText(time);
                }
            }
        }
    }

}

package com.equipu.profeplus.fragments.teacher_job;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Prototipo para crear evaluación paso 1.
No usado
 */
public class EvaluationStepOneFragment extends Fragment {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.txt_type_next_information) TextView txtInformation;
    @BindView(R.id.txt_number_questions) TextView txtNumber;
    @BindView(R.id.txt_overall_score) TextView txtOverall;
    @BindView(R.id.edt_number_questions) EditText edtNumber;
    @BindView(R.id.edt_overall_score) EditText edtOverall;
    @BindView(R.id.txt_choose_question_weight) TextView txtWeight;
    @BindView(R.id.btn_equal_weight) Button btnEqual;
    @BindView(R.id.btn_different_weight) Button btnDifferent;

    private Unbinder unbinder;
    AppStateParcel appStateParcel;
    EvaluationParcel evaluationParcel;
    SessionParcel sessionParcel;

    private EvaluationStepOneListener mListener;


    public EvaluationStepOneFragment() {
        // Required empty public constructor
    }


    public static EvaluationStepOneFragment newInstance(AppStateParcel ap, EvaluationParcel ep,
                                                        SessionParcel sp) {
        EvaluationStepOneFragment fragment = new EvaluationStepOneFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
        args.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
        args.putParcelable(LearnApp.PCL_EVALUATION, ep);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
            sessionParcel = getArguments().getParcelable(LearnApp.PCL_SESSION_PARCEL);
            evaluationParcel = getArguments().getParcelable(LearnApp.PCL_EVALUATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_evaluation_step_one, container, false);
        unbinder = ButterKnife.bind(this, v);

        evaluationParcel.setTeacherId(Integer.parseInt(appStateParcel.getUserId()));
        appStateParcel.setPager(1);

        txtInformation.setTypeface(LearnApp.appFont1);
        edtNumber.setTypeface(LearnApp.appFont);
        txtNumber.setTypeface(LearnApp.appFont);
        edtOverall.setTypeface(LearnApp.appFont);
        txtOverall.setTypeface(LearnApp.appFont);
        txtWeight.setTypeface(LearnApp.appFont);
        btnEqual.setTypeface(LearnApp.appFont);
        btnDifferent.setTypeface(LearnApp.appFont);



        txtNumber.setText(getString(R.string.text_quantity_questions));
        txtOverall.setText(getString(R.string.text_overall_score_evaluation));

        if (evaluationParcel.getNumberQuestion() != 0){
            edtNumber.setText(String.format("%d", evaluationParcel.getNumberQuestion()));
            txtNumber.setText("");
        }

        if (evaluationParcel.getOverall() != 0){
            edtOverall.setText(String.format(new Locale("en_US"),
                    "%.1f", evaluationParcel.getOverall()));
            txtOverall.setText("");
        }

        edtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtNumber.setText("");
                if (editable.length() == 0){
                    txtNumber.setText(getString(R.string.text_quantity_questions));
                }
            }
        });

        edtOverall.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtOverall.setText("");
                if (editable.length() == 0){
                    txtOverall.setText(getString(R.string.text_overall_score_evaluation));
                }
            }
        });


        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields() && checkWeights()) {
                    prepareEvalParcel();
                    if (appStateParcel.getEditEvaluation()==0) {
                        calculateWeights();
                    }
                    mListener.startEvaluationStepTwo(appStateParcel,
                            sessionParcel, evaluationParcel);
                } else {
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", getResources().getString(R.string.text_alerta));
                    b.putString("MESSAGE", getResources().getString(R.string.msg_score_by_question));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), "failure");
                }
            }
        });

        btnDifferent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    prepareEvalParcel();
                    mListener.startEvaluationStepTwo(appStateParcel,
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
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    public boolean validateFields(){
        if (edtOverall.getText().toString().equals("")
                || edtNumber.getText().toString().equals("")) {
            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
            Bundle b = new Bundle();
            b.putString("TITLE", "");
            b.putString("MESSAGE", getResources().getString(R.string.msg_fill_all_fields));
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), "failure");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkWeights(){
        double q1 = Double.parseDouble(edtOverall.getText().toString());
        int q2 = Integer.parseInt(edtNumber.getText().toString());
        int res = (int) (q1 % q2);
        double qte = q1 / q2;
        int res1 = (int) (qte*10 % 5);
        if (res1 == 0){
            return true;
        }
        return false;
    }


    public void calculateWeights(){
        double overall = evaluationParcel.getOverall();
        int number = evaluationParcel.getNumberQuestion();
        double wgt = overall/number;
        double[] wgts = new double[number];
        wgts[number-1]=overall-(number-1)*wgt;
        wgt = (wgt == 0) ? 1 : wgt;
        Arrays.fill(wgts,wgt);
        evaluationParcel.setAnswerWeights(wgts);
    }

    public void prepareEvalParcel(){
        evaluationParcel.setOverall(Double.parseDouble(edtOverall.getText().toString()));
        int num =  Integer.parseInt(edtNumber.getText().toString());
        evaluationParcel.setNumberQuestion(num);
        //evaluationParcel.setPivotQuestion(num);
        //evaluationParcel.setPivotQuestion(0);
        if (appStateParcel.getEditEvaluation() == 1){
            int[] keys = evaluationParcel.getAnswerKeys();
            double[] weights = evaluationParcel.getAnswerWeights();
            evaluationParcel.setAnswerKeys(newAnswerArray(keys, num));
            evaluationParcel.setAnswerWeights(newAnswerDouble(weights, num));
        } else {
            evaluationParcel.setAnswerKeys(new int[num]);
            evaluationParcel.setAnswerWeights(new double[num]);
        }
    }

    public int[] newAnswerArray(int[] answers, int num){
        int[] ans = new int[num];
        if (num < answers.length){
            for (int i=0; i<num; i++){
                ans[i] = answers[i];
            }
        } else {
            for (int i = 0; i < answers.length; i++) {
                ans[i] = answers[i];
            }
        }
        return ans;
    }

    public double[] newAnswerDouble(double[] answers, int num){
        double[] ans = new double[num];
        if (num < answers.length){
            for (int i=0; i<num; i++){
                ans[i] = answers[i];
            }
        } else {
            for (int i = 0; i < answers.length; i++) {
                ans[i] = answers[i];
            }
        }
        return ans;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EvaluationStepOneListener) {
            mListener = (EvaluationStepOneListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EvaluationStepOneListener");
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

    public interface EvaluationStepOneListener {
        void startEvaluationStepTwo(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep);
    }
}

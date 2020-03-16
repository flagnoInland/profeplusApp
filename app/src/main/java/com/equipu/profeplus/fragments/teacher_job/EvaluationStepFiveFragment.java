package com.equipu.profeplus.fragments.teacher_job;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.TeacherJobActivity;
import com.equipu.profeplus.controllers.EvaluationController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import java.util.Arrays;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Prototipo para crear evaluación paso 5.
No usado
 */
public class EvaluationStepFiveFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.btn_finish) Button btnFinish;

    @BindView(R.id.txt_header_solutions) TextView txtHeader;
    @BindView(R.id.txt_question) TextView txtQuestion;
    @BindView(R.id.txt_answer) TextView txtAnswer;
    @BindView(R.id.txt_score) TextView txtScore;

    @BindView(R.id.txt_pos1) TextView txtPos1;
    @BindView(R.id.spn_pos1) Spinner spnPos1;
    @BindView(R.id.edt_pos1) EditText edtPos1;

    @BindView(R.id.txt_pos2) TextView txtPos2;
    @BindView(R.id.spn_pos2) Spinner spnPos2;
    @BindView(R.id.edt_pos2) EditText edtPos2;

    @BindView(R.id.txt_pos3) TextView txtPos3;
    @BindView(R.id.spn_pos3) Spinner spnPos3;
    @BindView(R.id.edt_pos3) EditText edtPos3;

    @BindView(R.id.txt_pos4) TextView txtPos4;
    @BindView(R.id.spn_pos4) Spinner spnPos4;
    @BindView(R.id.edt_pos4) EditText edtPos4;

    @BindView(R.id.txt_total) TextView txtTotal;
    @BindView(R.id.txt_total_number) TextView txtTotalNumber;

    int pivotQuestion;

    private Unbinder unbinder;

    AppStateParcel appStateParcel;
    EvaluationParcel evaluationParcel;
    SessionParcel sessionParcel;
    int[] keys;
    double[] weights;
    int qtn1, qtn2, qtn3, qtn4;
    int qnumber;

    private EvaluationStepFiveListener mListener;

    public EvaluationStepFiveFragment() {
        // Required empty public constructor
    }


    public static EvaluationStepFiveFragment newInstance(AppStateParcel ap, SessionParcel sp,
                                                         EvaluationParcel ep) {
        EvaluationStepFiveFragment fragment = new EvaluationStepFiveFragment();
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
        View  v = inflater.inflate(R.layout.fragment_evaluation_step_five, container, false);
        unbinder = ButterKnife.bind(this, v);

        appStateParcel.setPager(appStateParcel.getPager()+1);

        txtTotal.setTypeface(LearnApp.appFont);
        txtTotalNumber.setTypeface(LearnApp.appFont);
        txtAnswer.setTypeface(LearnApp.appFont);
        txtHeader.setTypeface(LearnApp.appFont);
        txtQuestion.setTypeface(LearnApp.appFont);
        txtScore.setTypeface(LearnApp.appFont);
        txtPos1.setTypeface(LearnApp.appFont);
        txtPos2.setTypeface(LearnApp.appFont);
        txtPos3.setTypeface(LearnApp.appFont);
        txtPos4.setTypeface(LearnApp.appFont);
        edtPos1.setTypeface(LearnApp.appFont);
        edtPos2.setTypeface(LearnApp.appFont);
        edtPos3.setTypeface(LearnApp.appFont);
        edtPos4.setTypeface(LearnApp.appFont);

        edtPos1.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtPos2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtPos3.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtPos4.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

        btnFinish.setVisibility(View.GONE);
        pivotQuestion = evaluationParcel.getPivotQuestion();

        keys = evaluationParcel.getAnswerKeys();
        weights = evaluationParcel.getAnswerWeights();
        String[] spnkeys = getResources().getStringArray(R.array.array_answer_keys);
        calculateScore();


        ArrayAdapter adapterKeys = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_answer_keys, R.layout.app_spinner_eval);
        adapterKeys.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPos1.setAdapter(adapterKeys);
        spnPos2.setAdapter(adapterKeys);
        spnPos3.setAdapter(adapterKeys);
        spnPos4.setAdapter(adapterKeys);

        qnumber = evaluationParcel.getNumberQuestion();
        qtn1= qnumber;
        qtn2= qnumber;
        qtn3= qnumber;
        qtn4= qnumber;


        if (pivotQuestion > qnumber-1){
            txtPos1.setVisibility(View.GONE);
            edtPos1.setVisibility(View.GONE);
            spnPos1.setVisibility(View.GONE);
        } else {
            qtn1 = pivotQuestion;
            ++pivotQuestion;
            txtPos1.setText(String.format("%d.",qtn1+1));
            spnPos1.setContentDescription(String.format("pos%d",qtn1+1));
            spnPos1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fillKeys();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            if (evaluationParcel.getAnswerWeights()[qtn1]!=0){
                edtPos1.setText(String.format(new Locale("en_US"),
                        "%.1f", evaluationParcel.getAnswerWeights()[qtn1]));
            }
            edtPos1.append("");
            if (evaluationParcel.getAnswerKeys()[qtn1]!= 0) {
                spnPos1.setSelection(evaluationParcel.getAnswerKeys()[qtn1]-1);
            }
            edtPos1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length()>0) {
                        fillKeys();
                        calculateScore();
                    }
                }
            });
        }

        if (pivotQuestion > qnumber-1){
            txtPos2.setVisibility(View.GONE);
            edtPos2.setVisibility(View.GONE);
            spnPos2.setVisibility(View.GONE);
        } else {
            qtn2 = pivotQuestion;
            ++pivotQuestion;
            txtPos2.setText(String.format("%d.",qtn2+1));
            spnPos2.setContentDescription(String.format("pos%d",qtn2+1));
            spnPos2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fillKeys();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            if (evaluationParcel.getAnswerWeights()[qtn2]!=0){
                edtPos2.setText(String.format(new Locale("en_US"),
                        "%.1f", evaluationParcel.getAnswerWeights()[qtn2]));
            }
            edtPos2.append("");
            if (evaluationParcel.getAnswerKeys()[qtn2]!= 0) {
                spnPos2.setSelection(evaluationParcel.getAnswerKeys()[qtn2]-1);
            }
            edtPos2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length()>0) {
                        fillKeys();
                        calculateScore();
                    }
                }
            });
        }

        if (pivotQuestion > qnumber-1){
            txtPos3.setVisibility(View.GONE);
            edtPos3.setVisibility(View.GONE);
            spnPos3.setVisibility(View.GONE);
        } else {
            qtn3 = pivotQuestion;
            ++pivotQuestion;
            txtPos3.setText(String.format("%d.",qtn3+1));
            spnPos3.setContentDescription(String.format("pos%d",qtn3+1));
            spnPos3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fillKeys();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            if (evaluationParcel.getAnswerWeights()[qtn3]!=0){
                edtPos3.setText(String.format(new Locale("en_US"),
                        "%.1f", evaluationParcel.getAnswerWeights()[qtn3]));
            }
            edtPos3.append("");
            if (evaluationParcel.getAnswerKeys()[qtn3]!= 0) {
                spnPos3.setSelection(evaluationParcel.getAnswerKeys()[qtn3]-1);
            }
            edtPos3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length()>0) {
                        fillKeys();
                        calculateScore();
                    }
                }
            });
        }

        if (pivotQuestion > qnumber-1){
            txtPos4.setVisibility(View.GONE);
            edtPos4.setVisibility(View.GONE);
            spnPos4.setVisibility(View.GONE);
        } else {
            qtn4 = pivotQuestion;
            ++pivotQuestion;
            txtPos4.setText(String.format("%d.",qtn4+1));
            spnPos4.setContentDescription(String.format("pos%d",qtn4+1));
            spnPos4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fillKeys();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            if (evaluationParcel.getAnswerWeights()[qtn4]!=0){
                edtPos4.setText(String.format(new Locale("en_US"),
                        "%.1f", evaluationParcel.getAnswerWeights()[qtn4]));
            }
            edtPos4.setText(String.format(new Locale("en_US"),
                    "%.1f", evaluationParcel.getAnswerWeights()[qtn4]));
            edtPos4.append("");
            if (evaluationParcel.getAnswerKeys()[qtn4]!= 0) {
                spnPos4.setSelection(evaluationParcel.getAnswerKeys()[qtn4]-1);
            }
            edtPos4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length()>0) {
                        fillKeys();
                        calculateScore();
                    }
                }
            });
        }

        Log.d("profeplus.piv", String.valueOf(pivotQuestion));
        if (pivotQuestion > evaluationParcel.getNumberQuestion()-1){
            btnFinish.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
        } else {
            btnFinish.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setContentDescription(String.format("esp5f%d",qtn4+2));
        }


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFinish.setEnabled(false);
                fillKeys();
                if (calculateScore()) {
                    saveEvaluation();
                } else {
                    DialogFragment newFragment =
                            new ConfirmTwoButtonsSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", "");
                    b.putString("MESSAGE", getResources().getString(R.string.text_sum_not_match));
                    newFragment.setArguments(b);
                    newFragment.setTargetFragment(EvaluationStepFiveFragment.this,1234);
                    newFragment.show(getFragmentManager(), "failure");
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluationParcel.setPivotQuestion(pivotQuestion);
                keys[qtn1] = spnPos1.getSelectedItemPosition()+1;
                keys[qtn2] = spnPos2.getSelectedItemPosition()+1;
                keys[qtn3] = spnPos3.getSelectedItemPosition()+1;
                keys[qtn4] = spnPos4.getSelectedItemPosition()+1;
                weights[qtn1] = Double.parseDouble(edtPos1.getText().toString());
                weights[qtn2] = Double.parseDouble(edtPos2.getText().toString());
                weights[qtn3] = Double.parseDouble(edtPos3.getText().toString());
                weights[qtn4] = Double.parseDouble(edtPos4.getText().toString());
                evaluationParcel.setAnswerWeights(weights);
                evaluationParcel.setAnswerKeys(keys);
                TeacherJobActivity.evaluationParcel = evaluationParcel;
                mListener.startEvaluationStepFive(appStateParcel, sessionParcel, evaluationParcel);
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

    private int setPivot(){
        int val = 0;
        return val;
    }


    public void fillKeys(){
        if ( qtn1<qnumber ){
            keys[qtn1] = spnPos1.getSelectedItemPosition()+1;
            if (edtPos1.getText().toString().equals("")){
                weights[qtn1] = 0;
            } else {
                weights[qtn1] = Double.parseDouble(edtPos1.getText().toString());
            }
        }
        if (qtn2<qnumber){
            keys[qtn2] = spnPos2.getSelectedItemPosition()+1;
            if (edtPos2.getText().toString().equals("")){
                weights[qtn2] = 0;
            } else {
                weights[qtn2] = Double.parseDouble(edtPos2.getText().toString());
            }
            Log.d("profeplus.val2", String.format("item:%d %f",qtn2,weights[qtn2]));
        }

        if (qtn3<qnumber){
            keys[qtn3] = spnPos3.getSelectedItemPosition()+1;
            if (edtPos3.getText().toString().equals("")){
                weights[qtn3] = 0;
            } else {
                weights[qtn3] = Double.parseDouble(edtPos3.getText().toString());
            }
            Log.d("profeplus.val2", String.format("item:%d %f",qtn3,weights[qtn2]));
        }

        if (qtn4<qnumber){
            keys[qtn4] = spnPos4.getSelectedItemPosition()+1;
            if (edtPos4.getText().toString().equals("")){
                weights[qtn4] = 0;
            } else {
                weights[qtn4] = Double.parseDouble(edtPos4.getText().toString());
            }
            Log.d("profeplus.val2", String.format("item:%d %f",qtn4,weights[qtn2]));
        }

        Log.d("profeplus.weights", Arrays.toString(weights));
        Log.d("profeplus.weights", Arrays.toString(evaluationParcel.getAnswerWeights()));
        evaluationParcel.setAnswerWeights(weights);
        evaluationParcel.setAnswerKeys(keys);
        TeacherJobActivity.evaluationParcel = evaluationParcel;
    }

    public void saveEvaluation(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            if (appStateParcel.getEditEvaluation() == 1) {
                EditEvalTask editEvalTask = new EditEvalTask();
                editEvalTask.execute();
            } else {
                NewEvalTask newEvalTask = new NewEvalTask();
                newEvalTask.execute();
            }
        } else {
            mListener.dialogLostConnection();
        }

    }


    public boolean calculateScore(){
        double newScore = 0;
        boolean isOk = false;
        for (int i=0; i<evaluationParcel.getNumberQuestion(); i++) {
            newScore = newScore + weights[i];
        }
        txtTotalNumber.setText(String.format(new Locale("en_US"),"%.1f",newScore));
        if (newScore == evaluationParcel.getOverall()) {
            isOk = true;
        } else {
            isOk = false;
        }
        return isOk;
    }

    public class EditEvalTask extends AsyncTask<Void,Void,Void> {

        EvaluationController evaluationController;

        @Override
        protected Void doInBackground(Void... p) {
            evaluationController = new EvaluationController();
            evaluationController.updateEvaluation(appStateParcel, evaluationParcel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                sessionParcel.setEvaluationId(evaluationController.getEvaluationId());
                appStateParcel.setEvaluations(evaluationController.getEvaluationCount());
                sessionParcel.setCourseName(evaluationParcel.getCourseName());
                sessionParcel.setSpeciality(evaluationParcel.getSpeciality());
                sessionParcel.setInstitution(evaluationParcel.getInstitution());
                sessionParcel.setInactive(appStateParcel.getSaveEvaluation());
                mListener.startEditEvaluation(appStateParcel, sessionParcel);
            } else {
                btnFinish.setEnabled(true);
            }
        }
    }


    public class NewEvalTask extends AsyncTask<Void,Void,Void> {

        EvaluationController evaluationController;

        @Override
        protected Void doInBackground(Void... p) {
            evaluationController = new EvaluationController();
            evaluationController.createNewEvaluation(appStateParcel, evaluationParcel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (evaluationController.getStatus() == EvaluationController.DONE) {
                sessionParcel.setEvaluationId(evaluationController.getEvaluationId());
                appStateParcel.setEvaluations(evaluationController.getEvaluationCount());
                sessionParcel.setCourseName(evaluationParcel.getCourseName());
                sessionParcel.setSpeciality(evaluationParcel.getSpeciality());
                sessionParcel.setInstitution(evaluationParcel.getInstitution());
                sessionParcel.setInactive(appStateParcel.getSaveEvaluation());
                mListener.startEvaluation(appStateParcel, sessionParcel, evaluationParcel);
            } else {
                btnFinish.setEnabled(true);
            }
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EvaluationStepFiveListener) {
            mListener = (EvaluationStepFiveListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EvaluationStepFiveListener");
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

    public interface EvaluationStepFiveListener {
        void startEvaluationStepFive(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep);
        void startEvaluation(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep);
        void startEditEvaluation(AppStateParcel ap, SessionParcel ep);
        void finishEvaluation(AppStateParcel ap, SessionParcel sp);
        void dialogLostConnection();
    }

    @Override
    public void onFireCancelEventTwoButtonListener() {
        btnFinish.setEnabled(true);
    }

    @Override
    public void onFireAcceptEventTwoButtonListener() {
        evaluationParcel.setOverall(Double.parseDouble(txtTotalNumber.getText().toString()));
        saveEvaluation();
    }

}

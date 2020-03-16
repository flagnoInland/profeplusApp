package com.equipu.profeplus.fragments.teacher_job;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Prototipo para crear evaluación paso 4.
No usado
 */
public class EvaluationStepFourFragment extends Fragment {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.txt_optional) TextView txtTitle;
    @BindView(R.id.txt_materials) TextView txtMaterial;
    @BindView(R.id.txt_observation) TextView txtObservation;
    @BindView(R.id.edt_observation) EditText edtObservation;
    @BindView(R.id.chck_calculator) CheckBox chkCalculator;
    @BindView(R.id.chck_notes) CheckBox chkNotes;
    @BindView(R.id.chck_books) CheckBox chkBooks;
    @BindView(R.id.chck_computer) CheckBox chkComputer;

    private Unbinder unbinder;

    AppStateParcel appStateParcel;
    EvaluationParcel evaluationParcel;
    SessionParcel sessionParcel;
    int[] materials;

    private EvaluationStepFourListener mListener;

    public EvaluationStepFourFragment() {
        // Required empty public constructor
    }


    public static EvaluationStepFourFragment newInstance(AppStateParcel ap, SessionParcel sp,
                                                         EvaluationParcel ep) {
        EvaluationStepFourFragment fragment = new EvaluationStepFourFragment();
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
        View v = inflater.inflate(R.layout.fragment_evaluation_step_four, container, false);
        unbinder = ButterKnife.bind(this, v);

        appStateParcel.setPager(4);


        txtMaterial.setTypeface(LearnApp.appFont);
        txtTitle.setTypeface(LearnApp.appFont);
        txtObservation.setTypeface(LearnApp.appFont);
        edtObservation.setTypeface(LearnApp.appFont);
        txtObservation.setText(getString(R.string.text_additional_indications));
        chkComputer.setTypeface(LearnApp.appFont);
        chkBooks.setTypeface(LearnApp.appFont);
        chkNotes.setTypeface(LearnApp.appFont);
        chkCalculator.setTypeface(LearnApp.appFont);

        edtObservation.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|
                InputType.TYPE_TEXT_VARIATION_NORMAL);

        edtObservation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                txtObservation.setText("");
                if (s.length() == 0){
                    txtObservation.setText(getString(R.string.text_additional_indications));
                }
            }
        });

        materials = evaluationParcel.getMaterials();

        chkCalculator.setChecked(materials[0] == 1);
        chkNotes.setChecked(materials[1] == 1);
        chkBooks.setChecked(materials[2] == 1);
        chkComputer.setChecked(materials[3] == 1);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMaterial();
                evaluationParcel.setMaterials(materials);
                evaluationParcel.setPivotQuestion(0);
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


    public void getMaterial(){
        if (chkCalculator.isChecked()){
            materials[0] = 1;
        } else {
            materials[0] = 0;
        }

        if (chkNotes.isChecked()){
            materials[1] = 1;
        } else {
            materials[1] = 0;
        }

        if (chkBooks.isChecked()){
            materials[2] = 1;
        } else {
            materials[2] = 0;
        }

        if (chkComputer.isChecked()){
            materials[3] = 1;
        } else {
            materials[3] = 0;
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EvaluationStepFourListener) {
            mListener = (EvaluationStepFourListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EvaluationStepFourListener");
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

    public interface EvaluationStepFourListener {
        void startEvaluationStepFive(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep);
        void finishEvaluation(AppStateParcel ap, SessionParcel sp);
    }
}

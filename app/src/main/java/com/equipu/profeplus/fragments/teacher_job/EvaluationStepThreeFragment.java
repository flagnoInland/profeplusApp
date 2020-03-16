package com.equipu.profeplus.fragments.teacher_job;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Prototipo para crear evaluación paso 3.
No usado
 */
public class EvaluationStepThreeFragment extends Fragment {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.txt_header_evaluation_info) TextView txtTitle;
    @BindView(R.id.edt_course_name) EditText edtCourse;
    @BindView(R.id.txt_course_name) TextView txtCourse;
    @BindView(R.id.edt_speciality) EditText edtSpeciality;
    @BindView(R.id.txt_speciality) TextView txtSpeciality;
    @BindView(R.id.edt_institution) EditText edtInstitution;
    @BindView(R.id.txt_institution) TextView txtInstitution;
    @BindView(R.id.edt_header) EditText edtHeader;
    @BindView(R.id.txt_header) TextView txtHeader;

    private Unbinder unbinder;

    AppStateParcel appStateParcel;
    EvaluationParcel evaluationParcel;
    SessionParcel sessionParcel;

    private EvaluationStepThreeListener mListener;

    public EvaluationStepThreeFragment() {
        // Required empty public constructor
    }


    public static EvaluationStepThreeFragment newInstance(AppStateParcel ap, SessionParcel sp,
                                                          EvaluationParcel ep) {
        EvaluationStepThreeFragment fragment = new EvaluationStepThreeFragment();
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
        View v =  inflater.inflate(R.layout.fragment_evaluation_step_three, container, false);
        unbinder = ButterKnife.bind(this, v);

        appStateParcel.setPager(3);

        txtTitle.setTypeface(LearnApp.appFont);
        txtCourse.setTypeface(LearnApp.appFont);
        txtSpeciality.setTypeface(LearnApp.appFont);
        txtInstitution.setTypeface(LearnApp.appFont);
        txtHeader.setTypeface(LearnApp.appFont);

        txtCourse.setText(getString(R.string.text_name_course));
        txtSpeciality.setText(getString(R.string.text_name_speciality));
        txtInstitution.setText(getString(R.string.text_name_institution));
        txtHeader.setText(getString(R.string.text_type_observations));

        edtCourse.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS|
                InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtSpeciality.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS|
                InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtInstitution.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS|
                InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtHeader.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS|
                InputType.TYPE_TEXT_VARIATION_NORMAL);

        if ( !evaluationParcel.getCourseName().equals("")){
            edtCourse.setText(String.format("%s",evaluationParcel.getCourseName()));
            txtCourse.setText("");
        }

        if ( !evaluationParcel.getSpeciality().equals("")){
            edtSpeciality.setText(String.format("%s",evaluationParcel.getSpeciality()));
            txtSpeciality.setText("");
        }

        if ( !evaluationParcel.getInstitution().equals("")){
            edtInstitution.setText(String.format("%s",evaluationParcel.getInstitution()));
            txtInstitution.setText("");
        }

        if ( !evaluationParcel.getHeader().equals("")){
            edtHeader.setText(String.format("%s",evaluationParcel.getHeader()));
            txtHeader.setText("");
        }

        edtCourse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                txtCourse.setText("");
                if (editable.length() == 0){
                    txtCourse.setText(getString(R.string.text_name_course));
                }
            }
        });

        edtSpeciality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                txtSpeciality.setText("");
                if(editable.length()==0){
                    txtSpeciality.setText(getString(R.string.text_name_speciality));
                }
            }
        });

        edtInstitution.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                txtInstitution.setText("");
                if(editable.length()==0){
                    txtInstitution.setText(getString(R.string.text_name_institution));
                }
            }
        });

        edtHeader.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                txtHeader.setText("");
                if(editable.length()==0){
                    txtHeader.setText(getString(R.string.text_type_observations));
                }
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()){
                    evaluationParcel.setCourseName(edtCourse.getText().toString());
                    evaluationParcel.setSpeciality(edtSpeciality.getText().toString());
                    evaluationParcel.setInstitution(edtInstitution.getText().toString());
                    evaluationParcel.setHeader(edtHeader.getText().toString());
                    mListener.startEvaluationStepFour(appStateParcel, sessionParcel, evaluationParcel);
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
        if (!edtCourse.getText().toString().equals("")){
            return true;
        } else {
            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
            Bundle b = new Bundle();
            b.putString("TITLE", getResources().getString(R.string.text_alerta));
            b.putString("MESSAGE", getResources().getString(R.string.msg_type_least_course_name));
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), "failure");
            return false;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EvaluationStepThreeListener) {
            mListener = (EvaluationStepThreeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EvaluationStepThreeListener");
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

    public interface EvaluationStepThreeListener {
        void startEvaluationStepFour(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep);
        void finishEvaluation(AppStateParcel ap, SessionParcel sp);
    }
}

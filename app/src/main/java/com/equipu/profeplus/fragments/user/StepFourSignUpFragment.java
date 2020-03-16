package com.equipu.profeplus.fragments.user;

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
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStartListener;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento inicializa la vista para el paso número cuatro del registro de usuario
Esta vista solo se muestra a estudiantes de educación superior
Los siguiente campos pueden ser llenados
INSTITUCIÓN
CARRERA
CÓDIGO DE ESTUDIANTE
 */
public class StepFourSignUpFragment extends Fragment {

    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.edt_institution) EditText edtInstitution;
    @BindView(R.id.txt_institution) TextView txtInstitution;
    @BindView(R.id.edt_speciality) EditText edtSpeciality;
    @BindView(R.id.txt_speciality) TextView txtSpeciality;
    @BindView(R.id.edt_student_id) EditText edtStudentId;
    @BindView(R.id.txt_student_id) TextView txtStudentId;
    @BindView(R.id.btn_support_team) Button btnSupport;
    @BindView(R.id.edt_concurso_group) EditText edtGroup;
    @BindView(R.id.txt_concurso_group) TextView txtGroup;
    private Unbinder unbinder;



    private IStartListener mListener;

    private UserParcel userParcel;
    private AppStateParcel appStateParcel;


    public StepFourSignUpFragment() {
        // Required empty public constructor
    }

    public static StepFourSignUpFragment newInstance(AppStateParcel appStateParcel, UserParcel userParcel) {
        StepFourSignUpFragment fragment = new StepFourSignUpFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_USER_PARCEL,userParcel);
        args.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userParcel = getArguments().getParcelable(LearnApp.PCL_USER_PARCEL);
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_four_sign_up, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtStudentId.setHint(getString(R.string.text_what_student_id));
        txtSpeciality.setHint(getString(R.string.text_what_do_you_study));
        txtInstitution.setHint(getString(R.string.text_where_do_you_study));
        txtGroup.setHint(getString(R.string.text_group_name));

        edtInstitution.setTypeface(LearnApp.appFont);
        edtInstitution.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtSpeciality.setTypeface(LearnApp.appFont);
        edtSpeciality.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtStudentId.setTypeface(LearnApp.appFont);
        edtStudentId.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);


        edtGroup.setTypeface(LearnApp.appFont);
        edtGroup.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtInstitution.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtInstitution.setHint("");
                if (editable.length() == 0){
                    txtInstitution.setHint(getString(R.string.text_where_do_you_study));
                }
            }
        });

        edtSpeciality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtSpeciality.setHint("");
                if (editable.length() == 0){
                    txtSpeciality.setHint(getString(R.string.text_what_do_you_study));
                }
            }
        });

        edtStudentId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtStudentId.setHint("");
                if (editable.length() == 0){
                    txtStudentId.setHint(getString(R.string.text_what_student_id));
                }
            }
        });

        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFields();
                if (checkFields()) {
                    if (mListener != null) {
                        mListener.goToConcurso(userParcel);
                    }
                } else {
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", getResources().getString(R.string.text_alerta));
                    b.putString("MESSAGE", getResources().getString(R.string.msg_fill_all_fields));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), "failure");
                }
            }
        });

        edtGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtGroup.setHint("");
                if (editable.length() == 0){
                    txtGroup.setHint(getString(R.string.text_where_do_you_study));
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (checkFields()) {
                    if (mListener != null) {
                        mListener.registerStudent(userParcel);
                    }
                } else {
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", getResources().getString(R.string.text_alerta));
                    b.putString("MESSAGE", getResources().getString(R.string.msg_fill_all_fields));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), "failure");
                }
            }
        });


        return v;
    }

    public void getFields(){
        userParcel.setStudentId(edtStudentId.getText().toString());
        userParcel.setSpeciality(edtSpeciality.getText().toString());
        userParcel.setInstitution(edtInstitution.getText().toString());
        //userParcel.setConcursoGroup(edtGroup.getText().toString());
        //userParcel.setConcursoInstitute("");

    }

    public boolean checkFields() {
        boolean value = true;
        if (userParcel.getStudentId().equals("")){
            value = value & false;
        }
        if (userParcel.getSpeciality().equals("")){
            value = value & false;
        }
        if (userParcel.getInstitution().equals("")){
            value = value & false;
        }
        return value;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IStartListener) {
            mListener = (IStartListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IStartListener");
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

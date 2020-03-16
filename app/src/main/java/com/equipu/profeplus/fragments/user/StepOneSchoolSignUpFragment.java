package com.equipu.profeplus.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
Este fragmento inicializa la vista del paso 1 para registar un estudiante de educación básica
Los campo mostrados son
NOMBRE
APELLIDOS
GÉNERO
CONTRASEÑA
 */
public class StepOneSchoolSignUpFragment extends Fragment {

    private IStartListener mListener;
    private UserParcel userParcel;
    private AppStateParcel appStateParcel;

    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.sign_up_pass) EditText edtPass;
    @BindView(R.id.sign_up_pass_again) EditText edtPassAgain;
    @BindView(R.id.sign_up_name) EditText edtName;
    @BindView(R.id.sign_up_surname) EditText edtSurname;
    @BindView(R.id.gender_spinner) Spinner spnGender;
    private Unbinder unbinder;


    public StepOneSchoolSignUpFragment() {
        // Required empty public constructor
    }


    public static StepOneSchoolSignUpFragment newInstance(UserParcel userParcel, AppStateParcel appStateParcel) {
        StepOneSchoolSignUpFragment fragment = new StepOneSchoolSignUpFragment();
        Bundle args = new Bundle();
        args.putParcelable("UserParcel",userParcel);
        args.putParcelable("AppStateParcel", appStateParcel);
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
        View v = inflater.inflate(R.layout.fragment_step_one_school_sign_up, container, false);
        unbinder = ButterKnife.bind(this, v);

        TextView txtName = (TextView) v.findViewById(R.id.text_name);
        TextView txtSurname = (TextView) v.findViewById(R.id.text_surname);
        TextView txtGender = (TextView) v.findViewById(R.id.text_gender);
        TextView txtPass = (TextView) v.findViewById(R.id.text_pass);
        TextView txtPassAgain = (TextView) v.findViewById(R.id.text_pass_again);
        txtPass.setTypeface(LearnApp.appFont);
        txtPassAgain.setTypeface(LearnApp.appFont);
        txtName.setTypeface(LearnApp.appFont);
        txtSurname.setTypeface(LearnApp.appFont);
        txtGender.setTypeface(LearnApp.appFont);

        edtName.setTypeface(LearnApp.appFont);
        edtName.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtSurname.setTypeface(LearnApp.appFont);
        edtSurname.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array,R.layout.app_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(arrayAdapter);

        edtPass.setTypeface(LearnApp.appFont);
        edtPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);

        edtPassAgain.setTypeface(LearnApp.appFont);
        edtPassAgain.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtPassAgain.setText("");

        btnNext.setTypeface(LearnApp.appFont);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (checkFields()) {
                    if (mListener != null) {
                        if (edtPassAgain.getText().toString().trim().equals(edtPass.getText().toString
                                ())) {
                            mListener.goToStepTwoSchoolSignUp(userParcel);
                        } else {
                            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                            Bundle b = new Bundle();
                            b.putString("TITLE", getResources().getString(R.string.text_confirm));
                            b.putString("MESSAGE", getResources().getString(R.string.msg_passwords_are_different));
                            newFragment.setArguments(b);
                            newFragment.show(getFragmentManager(), "failure");
                        }
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
        userParcel.setFirstame(edtName.getText().toString());
        userParcel.setLastname(edtSurname.getText().toString());
        userParcel.setGender(spnGender.getSelectedItem().toString());
        userParcel.setPassword(edtPass.getText().toString().trim());
    }

    public boolean checkFields() {
        boolean value = true;
        if (userParcel.getFirstame().equals("")){
            value = value & false;
        }
        if (userParcel.getLastname().equals("")){
            value = value & false;
        }
        if (userParcel.getGender().equals("")){
            value = value & false;
        }
        if (userParcel.getPassword().equals("")){
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

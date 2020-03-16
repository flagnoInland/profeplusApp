package com.equipu.profeplus.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
Este fragmento inicializa la vista del paso 2 del registro de estudiantes de educacón superior y
profesores.
Los campos mostrados son:
DNI : deshabilitado en esta versión
CONTRASEÑA
 */
public class StepTwoSignUpFragment extends Fragment {

    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.sign_up_dni) EditText edtDni;
    @BindView(R.id.sign_up_pass) EditText edtPass;
    @BindView(R.id.sign_up_pass_again) EditText edtPassAgain;
    private Unbinder unbinder;

    private IStartListener mListener;
    private UserParcel userParcel;
    private AppStateParcel appStateParcel;

    public StepTwoSignUpFragment() {}


    public static StepTwoSignUpFragment newInstance(UserParcel userParcel, AppStateParcel appStateParcel) {
        StepTwoSignUpFragment fragment = new StepTwoSignUpFragment();
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
            userParcel = getArguments().getParcelable("UserParcel");
            appStateParcel = getArguments().getParcelable("AppStateParcel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_two_sign_up, container, false);
        unbinder = ButterKnife.bind(this, v);

        TextView txtDni = (TextView) v.findViewById(R.id.text_dni);
        TextView txtPass = (TextView) v.findViewById(R.id.text_pass);
        TextView txtPassAgain = (TextView) v.findViewById(R.id.text_pass_again);
        txtDni.setTypeface(LearnApp.appFont);
        txtPass.setTypeface(LearnApp.appFont);
        txtPassAgain.setTypeface(LearnApp.appFont);


        txtDni.setVisibility(View.GONE);
        edtDni.setVisibility(View.GONE);



        edtDni.setTypeface(LearnApp.appFont);
        edtDni.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);

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
                            mListener.goToStepThreeSignUp(userParcel);
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
        //userParcel.setDni(edtDni.getText().toString());
        userParcel.setDni("0");
        userParcel.setPassword(edtPass.getText().toString().trim());
    }

    public boolean checkFields() {
        boolean value = true;
        if (userParcel.getPassword().equals("")){
            value = value & false;
        }
        if (userParcel.getDni().equals("")){
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

package com.equipu.profeplus.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
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
import com.equipu.profeplus.dialogs.BirthDateDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento inicializa la vista del paso 1 del registro de estudiantes de educacón superior y
profesores.
Los campos mostrados son:
NOMBRE
APELLIDOS
FECHA DE NACIMIENTO
GÉNERO
 */
public class StepOneSignUpFragment extends Fragment {

    private IStartListener mListener;
    private UserParcel userParcel;
    private AppStateParcel appStateParcel;

    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.sign_up_date) TextView edtBirth;
    @BindView(R.id.sign_up_name) EditText edtName;
    @BindView(R.id.sign_up_surname) EditText edtSurname;
    @BindView(R.id.gender_spinner) Spinner spnGender;
    private Unbinder unbinder;

    public StepOneSignUpFragment() {}


    public static StepOneSignUpFragment newInstance(UserParcel userParcel, AppStateParcel appStateParcel) {
        StepOneSignUpFragment fragment = new StepOneSignUpFragment();
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
        View v = inflater.inflate(R.layout.fragment_step_one_sign_up, container, false);
        unbinder = ButterKnife.bind(this, v);

        TextView txtName = (TextView) v.findViewById(R.id.text_name);
        TextView txtSurname = (TextView) v.findViewById(R.id.text_surname);
        TextView txtGender = (TextView) v.findViewById(R.id.text_gender);
        TextView txtBirth = (TextView) v.findViewById(R.id.text_birth);
        txtName.setTypeface(LearnApp.appFont);
        txtSurname.setTypeface(LearnApp.appFont);
        txtGender.setTypeface(LearnApp.appFont);
        txtBirth.setTypeface(LearnApp.appFont);



        edtName.setTypeface(LearnApp.appFont);
        edtName.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtSurname.setTypeface(LearnApp.appFont);
        edtSurname.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtBirth.setTypeface(LearnApp.appFont);
        edtBirth.setText("25-12-2012");

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array,R.layout.app_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(arrayAdapter);

        if(appStateParcel.getEditUser()==1){
            edtName.setText(userParcel.getFirstame());
            edtSurname.setText(userParcel.getLastname());
            edtBirth.setText(userParcel.getBirth());
            String[] gender = getResources().getStringArray(R.array.gender_array);
            for (int i = 0; i < gender.length; i++) {
                if (gender[i].equals(userParcel.getGender())) {
                    spnGender.setSelection(i);
                }
            }
        }


        edtBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = BirthDateDialog.newInstance(new StepOneSignUpHandler());
                Bundle b = new Bundle();
                b.putInt("EDIT", 1);
                b.putString("MDATE",edtBirth.getText().toString());
                newFragment.setArguments(b);
                newFragment.show(getChildFragmentManager(), "birthdate");
            }
        });

        btnNext = (Button) v.findViewById(R.id.btn_next);
        btnNext.setTypeface(LearnApp.appFont);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (checkFields()){
                    if (mListener != null) {
                        mListener.goToStepTwoSignUp(userParcel);
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
        userParcel.setBirth(formatMyDateServer(edtBirth.getText().toString()));
    }

    private String formatMyDateServer(String date){
        int year = Integer.parseInt(date.substring(6));
        int month = Integer.parseInt(date.substring(3, 5)) - 1;
        int day = Integer.parseInt(date.substring(0,2));
        String m = String.format("%02d", month + 1);
        String d = String.format("%02d", day);
        String value = String.valueOf(year) + '-' + m + '-' + d;
        return value;
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
        if (userParcel.getBirth().equals("")){
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

    public class StepOneSignUpHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LearnApp.MSG_BIRTH_DIALOG) {
                Log.d("profeplus.Msg", String.valueOf(msg.what));
                String birth = msg.getData().getString("BIRTH");
                edtBirth.setText(birth);
            }
            if (msg.what == 871){
                boolean value = msg.getData().getBoolean("CHOICE");
                if (value){
                    //Intent home = new Intent(getContext(), LoginActivity.class);
                    //home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //startActivity(home);
                }
            }
        }
    }




}

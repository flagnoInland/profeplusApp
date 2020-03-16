package com.equipu.profeplus.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStartListener;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.dialogs.TermsDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento inicializa la vista del paso 2 para registar un estudiante de educación básica
Los campo mostrados son
EMAIL
ESCUELA
CIUDAD
PAIS
TERMINOS
 */
public class StepTwoSchoolSignUpFragment extends Fragment {

    @BindView(R.id.btn_next)  Button btnNext;
    @BindView(R.id.sign_up_mail)  EditText edtMail;
    @BindView(R.id.sign_up_school)  EditText edtSchool;
    @BindView(R.id.sign_up_city)   AutoCompleteTextView edtCity;
    @BindView(R.id.sign_up_nationality) Spinner spnCountry;
    @BindView(R.id.chk_terms)  CheckBox chkTerms;
    @BindView(R.id.lnk_terms)  TextView lnkTerms;
    @BindView(R.id.edt_concurso_group) EditText edtGroup;
    @BindView(R.id.txt_concurso_group) TextView txtGroup;
    private Unbinder unbinder;

    private UserParcel userParcel;
    private AppStateParcel appStateParcel;

    private IStartListener mListener;

    public StepTwoSchoolSignUpFragment() {
        // Required empty public constructor
    }


    public static StepTwoSchoolSignUpFragment newInstance(UserParcel up, AppStateParcel ap) {
        StepTwoSchoolSignUpFragment fragment = new StepTwoSchoolSignUpFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_USER_PARCEL,up);
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
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
        View v = inflater.inflate(R.layout.fragment_step_two_school_sign_up, container, false);
        unbinder = ButterKnife.bind(this, v);

        TextView txtMail = (TextView) v.findViewById(R.id.text_mail);
        TextView txtSchool = (TextView) v.findViewById(R.id.text_school);
        TextView txtCountry = (TextView) v.findViewById(R.id.text_country);
        TextView txtCity = (TextView) v.findViewById(R.id.text_city);
        txtMail.setTypeface(LearnApp.appFont);
        txtSchool.setTypeface(LearnApp.appFont);
        txtCountry.setTypeface(LearnApp.appFont);
        txtCity.setTypeface(LearnApp.appFont);

        txtGroup.setTypeface(LearnApp.appFont);
        edtGroup.setTypeface(LearnApp.appFont);

        edtSchool.setTypeface(LearnApp.appFont);
        edtSchool.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);

        edtMail.setTypeface(LearnApp.appFont);
        edtMail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        edtCity.setTypeface(LearnApp.appFont);
        edtCity.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);

        ArrayAdapter<CharSequence> adapterCities = ArrayAdapter.createFromResource(getActivity(),
                R.array.cities, android.R.layout.simple_dropdown_item_1line);
        edtCity.setAdapter(adapterCities);

        edtCity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(edtCity.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    edtCity.clearFocus();
                }
                return false;
            }
        });


        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.countries_array,R.layout.app_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(arrayAdapter);
        spnCountry.setSelection(185);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (checkFields()) {
                    if (mListener != null) {
                        if (isValidEmail(userParcel.getMail())) {
                            if (chkTerms.isChecked()) {
                                mListener.startSchoolUserMode(userParcel);
                            } else {
                                DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                                Bundle b = new Bundle();
                                b.putString("TITLE", getResources().getString(R.string.text_confirm));
                                b.putString("MESSAGE", getResources().getString(R.string.msg_we_cannot_register));
                                newFragment.setArguments(b);
                                newFragment.show(getFragmentManager(), "failure");
                            }
                        } else {
                            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                            Bundle b = new Bundle();
                            b.putString("TITLE", getResources().getString(R.string.text_confirm));
                            b.putString("MESSAGE", getResources().getString(R.string.msg_email_is_invalid));
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


        lnkTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment termsDialog = new TermsDialog();
                termsDialog.show(getFragmentManager(), "dialog");
            }
        });



        return v;
    }


    public void getFields(){
        userParcel.setConcursoGroup(edtGroup.getText().toString());
        userParcel.setConcursoInstitute("");
        userParcel.setInstitution(edtSchool.getText().toString());
        String cleanMail = edtMail.getText().toString();
        cleanMail = cleanMail.replaceAll(" ","");
        cleanMail = cleanMail.trim();
        userParcel.setMail(cleanMail);
        userParcel.setCountry(spnCountry.getSelectedItem().toString());
        userParcel.setCity(edtCity.getText().toString());
    }

    public boolean checkFields() {
        boolean value = true;
        if (userParcel.getInstitution().equals("")){
            value = value & false;
        }
        if (userParcel.getMail().equals("")){
            value = value & false;
        }
        if (userParcel.getCountry().equals("")){
            value = value & false;
        }
        if (userParcel.getCity().equals("")){
            value = value & false;
        }
        return value;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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

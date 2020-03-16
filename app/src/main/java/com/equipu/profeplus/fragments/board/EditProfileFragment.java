package com.equipu.profeplus.fragments.board;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkBoardService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IUserBoardListener;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.dialogs.BirthDateDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento se usa para presentar los datos de usuario
y permitir su actualización
 */
public class EditProfileFragment extends Fragment {

    @BindView(R.id.sign_up_surname) EditText edtSurname;
    @BindView(R.id.sign_up_name) EditText edtName;
    @BindView(R.id.sign_up_date) TextView edtBirth;
    @BindView(R.id.sign_up_dni) EditText edtDni;
    @BindView(R.id.sign_up_nationality) Spinner spnCountry;
    @BindView(R.id.gender_spinner) Spinner spnGender;
    @BindView(R.id.sign_up_phone) EditText edtPhone;
    @BindView(R.id.sign_up_city) AutoCompleteTextView edtCity;
    @BindView(R.id.edt_institution) EditText edtInstitution;
    @BindView(R.id.edt_speciality) EditText edtSpeciality;
    @BindView(R.id.edt_student_id) EditText edtStudentId;

    @BindView(R.id.progress_bar) LinearLayout progressBar;
    @BindView(R.id.text_name) TextView txtName;
    @BindView(R.id.text_surname) TextView txtSurname;
    @BindView(R.id.text_gender) TextView txtGender;
    @BindView(R.id.text_birth) TextView txtBirth;
    @BindView(R.id.text_dni) TextView txtDni;
    @BindView(R.id.text_phone) TextView txtPhone;
    @BindView(R.id.text_country) TextView txtCountry;
    @BindView(R.id.text_city) TextView txtCity;
    @BindView(R.id.txt_where) TextView txtWhere;
    @BindView(R.id.txt_what) TextView txtWhat;
    @BindView(R.id.txt_which) TextView txtWhich;
    @BindView(R.id.version) TextView txtVersion;

    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.btn_next) Button btnNext;

    @BindView(R.id.btn_support_team) Button btnSupport;
    @BindView(R.id.edt_concurso_group) EditText edtSupportGroup;
    @BindView(R.id.txt_concurso_group) TextView txtSupportGroup;
    @BindView(R.id.edt_concurso_institute) EditText edtSupportInstitute;
    @BindView(R.id.txt_concurso_institute) TextView txtSupportInstitute;

    private Unbinder unbinder;


    AppStateParcel appStateParcel;
    UserParcel userParcel;
    EditUserReceiver editUserReceiver;

    private IUserBoardListener mListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    public static EditProfileFragment newInstance(AppStateParcel ap) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, v);

        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (getArguments() != null){
            userParcel = getArguments().getParcelable(LearnApp.PCL_USER_PARCEL);
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
        }

        txtDni.setTypeface(LearnApp.appFont);
        txtPhone.setTypeface(LearnApp.appFont);
        txtCountry.setTypeface(LearnApp.appFont);
        txtCity.setTypeface(LearnApp.appFont);
        txtName.setTypeface(LearnApp.appFont);
        txtSurname.setTypeface(LearnApp.appFont);
        txtGender.setTypeface(LearnApp.appFont);
        txtBirth.setTypeface(LearnApp.appFont);
        txtWhat.setTypeface(LearnApp.appFont);
        txtWhere.setTypeface(LearnApp.appFont);
        txtWhich.setTypeface(LearnApp.appFont);

        txtVersion.setText(LearnApp.getAppVersion());

        // Obtener institución
        edtInstitution.setTypeface(LearnApp.appFont);
        edtInstitution.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtInstitution.setText(userParcel.getInstitution());

        // Obtener especialidad
        edtSpeciality.setTypeface(LearnApp.appFont);
        edtSpeciality.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtSpeciality.setText(userParcel.getSpeciality());

        // Obtener codigo universitario
        edtStudentId.setTypeface(LearnApp.appFont);
        edtStudentId.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtStudentId.setText(userParcel.getStudentId());

        // Para profesores ocultar los campos anteriores
        if (appStateParcel.getUserType() == AppStateParcel.TEACHER){
            txtWhere.setVisibility(View.GONE);
            txtWhat.setVisibility(View.GONE);
            txtWhich.setVisibility(View.GONE);
            edtInstitution.setVisibility(View.GONE);
            edtSpeciality.setVisibility(View.GONE);
            edtStudentId.setVisibility(View.GONE);
        }


        // Obtener nombre
        edtName.setTypeface(LearnApp.appFont);
        edtName.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtName.setText(userParcel.getFirstame());

        // Obtener apellidos
        edtSurname.setTypeface(LearnApp.appFont);
        edtSurname.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
        edtSurname.setText(userParcel.getLastname());

        // Obtener fecha de nacimiento
        edtBirth.setTypeface(LearnApp.appFont);
        edtBirth.setText(formatMyDate(userParcel.getBirth()));

        edtBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = BirthDateDialog.newInstance(new EditProfileHandler());
                Bundle b = new Bundle();
                b.putInt("EDIT", appStateParcel.getEditUser());
                b.putString("MDATE",edtBirth.getText().toString());
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), "birthdate");
            }
        });


        // Obtener genero
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array,R.layout.app_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(arrayAdapter);
        String[] genders = getResources().getStringArray(R.array.gender_array);
        for (int i=0; i<genders.length; i++){
            if (userParcel.getGender().equals(genders[i])){
                spnGender.setSelection(i);
            }
        }

        // Obtener ocumento de identidad
        edtDni.setTypeface(LearnApp.appFont);
        edtDni.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
        edtDni.setText(userParcel.getDni());

        // Obtener telefono
        edtPhone.setTypeface(LearnApp.appFont);
        edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
        edtPhone.setText(userParcel.getPhone());

        // Obtener ciudad
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
        edtCity.setText(userParcel.getCity());


        // Obtener pais
        ArrayAdapter adp = ArrayAdapter.createFromResource(getActivity(),R.array.countries_array,
                R.layout.app_spinner_item);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCountry.setAdapter(adp);
        spnCountry.setSelection(185);


        // Concurso
        LinearLayout laySupport = (LinearLayout) v.findViewById(R.id.layout_support);
        laySupport.setVisibility(View.GONE);
        txtSupportGroup.setTypeface(LearnApp.appFont);
        txtSupportInstitute.setTypeface(LearnApp.appFont);
        txtSupportGroup.setVisibility(View.GONE);
        txtSupportInstitute.setVisibility(View.GONE);
        edtSupportGroup.setTypeface(LearnApp.appFont);
        edtSupportInstitute.setTypeface(LearnApp.appFont);
        edtSupportGroup.setVisibility(View.GONE);
        edtSupportInstitute.setVisibility(View.GONE);
        btnSupport.setVisibility(View.GONE);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToLogin(appStateParcel);
            }
        });

        btnNext.setTypeface(LearnApp.appFont);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getActivity(), NetworkBoardService.class);
                    intent.putExtra(NetworkBoardService.SERVICE,
                            NetworkBoardService.SERVICE_EDIT_PROFILE);
                    intent.putExtra(LearnApp.PCL_APP_STATE,appStateParcel);
                    intent.putExtra(LearnApp.PCL_USER_PARCEL, userParcel);
                    getActivity().startService(intent);
                } else {
                    mListener.dialogLostConnection();
                }

            }
        });

        return v;
    }

    private String formatMyDate(String date){
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        int day  = Integer.parseInt(date.substring(8));
        String m = String.format("%02d", month + 1);
        String d = String.format("%02d", day);
        String value = d + '-' + m + '-' + String.valueOf(year);
        return value;
    }

    public void getFields(){
        userParcel.setFirstame(edtName.getText().toString());
        userParcel.setLastname(edtSurname.getText().toString());
        userParcel.setGender(spnGender.getSelectedItem().toString());
        userParcel.setBirth(formatMyDateServer(edtBirth.getText().toString()));
        userParcel.setDni(edtDni.getText().toString());
        userParcel.setPhone(edtPhone.getText().toString());
        userParcel.setCountry(spnCountry.getSelectedItem().toString());
        userParcel.setCity(edtCity.getText().toString());
        userParcel.setInstitution(edtInstitution.getText().toString());
        userParcel.setSpeciality(edtSpeciality.getText().toString());
        userParcel.setStudentId(edtStudentId.getText().toString());
        userParcel.setConcursoGroup(edtSupportGroup.getText().toString());
        userParcel.setConcursoInstitute("");
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

    public class EditUserReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.INVISIBLE);
            int status = intent.getIntExtra(NetworkBoardService.STATUS,0);
            if(status == UserController.DONE) {
                mListener.reloadUserBoard(appStateParcel);
                /*
                if (appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE) {
                    mListener.editProfileMode(appStateParcel);
                } else {
                    mListener.reloadUserBoard(appStateParcel);
                }
                */
            } else if (status == UserController.CONNECTION){
                mListener.dialogLostConnection();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkBoardService.ACTION_EDIT_PROFILE);
        editUserReceiver = new EditUserReceiver();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(editUserReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.unregisterReceiver(editUserReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IUserBoardListener) {
            mListener = (IUserBoardListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IUserBoardListener");
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

    public class EditProfileHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LearnApp.MSG_BIRTH_DIALOG) {
                Log.d("profeplus.Msg", String.valueOf(msg.what));
                String birth = msg.getData().getString("BIRTH");
                edtBirth.setText(birth);
            }
        }
    }

}

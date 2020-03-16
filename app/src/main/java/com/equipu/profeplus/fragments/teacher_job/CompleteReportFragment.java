package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.ConfirmOneButtonSupportDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento permite agregar detalles adicionales al reporte como
CURSO
ESPECIALIDAD
INSTITUTO
OBSERVACIONES
Estos datos son agregados al reporte que será enviado al final del día
 */
public class CompleteReportFragment extends Fragment
        implements ConfirmOneButtonSupportDialog.OnFireEventOneButtonDialogListener {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_report) Button btnReport;
    @BindView(R.id.txt_complete_report_title) TextView txtTitle;
    @BindView(R.id.edt_course_name) EditText edtCourse;
    @BindView(R.id.txt_course_name) TextView txtCourse;
    @BindView(R.id.edt_speciality) EditText edtSpeciality;
    @BindView(R.id.txt_speciality) TextView txtSpeciality;
    @BindView(R.id.edt_institution) EditText edtInstitution;
    @BindView(R.id.txt_institution) TextView txtInstitution;
    @BindView(R.id.edt_observations) EditText edtObservation;
    @BindView(R.id.txt_observations) TextView txtObservation;

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private Unbinder unbinder;

    SessionParcel sessionParcel;
    AppStateParcel appStateParcel;
    UpdateCompleteSessionReceiver updateSessionReceiver;

    private ITeacherJobListener mListener;

    public CompleteReportFragment() {
        // Required empty public constructor
    }


    public static CompleteReportFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        CompleteReportFragment fragment = new CompleteReportFragment();
        Bundle args = new Bundle();
        args.putParcelable("SessionParcel", sp);
        args.putParcelable("AppStateParcel", asp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sessionParcel = getArguments().getParcelable("SessionParcel");
            appStateParcel = getArguments().getParcelable("AppStateParcel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_complete_report, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtTitle.setTypeface(LearnApp.appFont);

        txtCourse.setHint(getString(R.string.text_name_course));
        txtSpeciality.setHint(getString(R.string.text_name_speciality));
        txtInstitution.setHint(getString(R.string.text_name_institution));
        txtObservation.setHint(getString(R.string.text_type_observations));

        // Curso para la pregunta
        edtCourse.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|
                InputType.TYPE_TEXT_VARIATION_NORMAL);

        // Especialidad para la pregunta
        edtSpeciality.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS|
                InputType.TYPE_TEXT_VARIATION_NORMAL);

        // Institución para la pregunta
        edtInstitution.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS|
                InputType.TYPE_TEXT_VARIATION_NORMAL);

        // Observación para la pregunta
        edtObservation.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|
                InputType.TYPE_TEXT_VARIATION_NORMAL);

        if (!sessionParcel.getCourseName().equals("")){
            edtCourse.setText(sessionParcel.getCourseName());
            txtCourse.setHint("");
        }

        if (!sessionParcel.getInstitution().equals("")){
            edtInstitution.setText(sessionParcel.getInstitution());
            txtInstitution.setHint("");
        }

        if (!sessionParcel.getSpeciality().equals("")){
            edtSpeciality.setText(sessionParcel.getSpeciality());
            txtSpeciality.setHint("");
        }

        if (!sessionParcel.getComment().equals("")){
            edtObservation.setText(sessionParcel.getComment());
            txtObservation.setHint("");
        }

        edtCourse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtCourse.setHint("");
                if (editable.length() == 0){
                    txtCourse.setHint(getString(R.string.text_name_course));
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
                    txtSpeciality.setHint(getString(R.string.text_name_speciality));
                }
            }
        });

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
                    txtInstitution.setHint(getString(R.string.text_name_institution));
                }
            }
        });

        edtObservation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtObservation.setHint("");
                if (editable.length()==0){
                    txtObservation.setHint(getString(R.string.text_type_observations));
                }
            }
        });

        // Iniciar servicio para actualizar lección con los detalles en la base de datos
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (checkFields()){
                    ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()
                            && networkInfo.isAvailable()) {
                        progressBar.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
                        intent.putExtra(NetworkTeacherService.SERVICE,
                                NetworkTeacherService.SERVICE_UPDATE_FULL_REPORT);
                        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
                        getActivity().startService(intent);
                    } else {
                        mListener.dialogLostConnection();
                    }
                } else {
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", getResources().getString(R.string.text_alerta));
                    b.putString("MESSAGE", getResources().getString(R.string.text_fill_course_name));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), "failure");
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

    public void getFields(){
        sessionParcel.setCourseName(edtCourse.getText().toString());
        sessionParcel.setComment(edtObservation.getText().toString());
        sessionParcel.setSpeciality(edtSpeciality.getText().toString());
        sessionParcel.setInstitution(edtInstitution.getText().toString());

    }

    public boolean checkFields() {
        boolean value = true;
        if (sessionParcel.getCourseName().equals("")){
            value = value & false;
        }
        return value;
    }

    /*
    Receiver para capturar el estado del reporte enviado por mail
     */
    public class UpdateCompleteSessionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.INVISIBLE);
            int status = intent.getIntExtra(NetworkTeacherService.STATUS, 0);
            if (status== TeacherLessonController.DONE) {
                mListener.makeReport(appStateParcel, sessionParcel);
                DialogFragment newFragment = new ConfirmOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", "");
                b.putString("MESSAGE", getResources().getString(R.string.msg_will_be_sent_message));
                newFragment.setArguments(b);
                newFragment.setTargetFragment(CompleteReportFragment.this, 1234);
                newFragment.show(getFragmentManager(), "success");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_UPDATE_FULL_REPORT);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        updateSessionReceiver = new UpdateCompleteSessionReceiver();
        lbm.registerReceiver(updateSessionReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(updateSessionReceiver);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ITeacherJobListener) {
            mListener = (ITeacherJobListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ITeacherJobListener");
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

    @Override
    public void onFireAcceptEventOneButtonListener() {
        if (mListener!= null) {
            mListener.finishReport(sessionParcel);
        }
    }

    @Override
    public void onFireCancelEventOneButtonListener() {
        if (mListener!= null) {
            mListener.finishReport(sessionParcel);
        }
    }



}

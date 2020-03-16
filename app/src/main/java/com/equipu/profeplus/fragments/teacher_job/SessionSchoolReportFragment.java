package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.ConfirmOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento permite agregar detalles adicionales al reporte como
NIVEL DE EDUCACION
GRADO
SECCIÓN
INSTITUCIÓN
Estos datos son agregados al reporte que será enviado al final del día
 */
public class SessionSchoolReportFragment extends Fragment
        implements ConfirmOneButtonSupportDialog.OnFireEventOneButtonDialogListener {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.txt_write_data) TextView txtWriteData;
    @BindView(R.id.txt_level) TextView txtLevel;
    @BindView(R.id.txt_grade) TextView txtGrade;
    @BindView(R.id.txt_classroom) TextView txtClassroom;
    @BindView(R.id.txt_institution) TextView txtInstitution;
    @BindView(R.id.btn_report) Button btnReport;
    @BindView(R.id.edt_institution) EditText edtInstitution;
    @BindView(R.id.edt_classroom) EditText edtClassroom;
    @BindView(R.id.spn_level) Spinner spnLevel;
    @BindView(R.id.spn_grade) Spinner spnGrade;

    private Unbinder unbinder;

    SessionParcel sessionParcel;
    AppStateParcel appStateParcel;
    UpdateSchoolSessionReceiver updateSessionReceiver;

    private ProgressBar mProgress;


    private ITeacherJobListener mListener;

    public SessionSchoolReportFragment() {}


    public static SessionSchoolReportFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        SessionSchoolReportFragment fragment = new SessionSchoolReportFragment();
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
        View v = inflater.inflate(R.layout.fragment_session_school_report, container, false);
        unbinder = ButterKnife.bind(this, v);

        mProgress = (ProgressBar) v.findViewById(R.id.progress_bar);

        txtWriteData.setTypeface(LearnApp.appFont);
        txtLevel.setTypeface(LearnApp.appFont);
        txtGrade.setTypeface(LearnApp.appFont);
        txtClassroom.setTypeface(LearnApp.appFont);
        txtInstitution.setTypeface(LearnApp.appFont);

        btnReport.setTypeface(LearnApp.appFont);
        edtClassroom.setTypeface(LearnApp.appFont);
        edtInstitution.setTypeface(LearnApp.appFont);


        // Opción primaria secundari
        ArrayAdapter adp1 = ArrayAdapter.createFromResource(getActivity(),R.array.level_array,
                R.layout.app_spinner_item);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLevel.setAdapter(adp1);

        String[] levels = getResources().getStringArray(R.array.level_array);
        for (int i=0; i<levels.length; i++){
            if (sessionParcel.getLevel().equals(levels[i])){
                spnLevel.setSelection(i);
            }
        }

        // Grado para la lección
        ArrayAdapter adp2 = ArrayAdapter.createFromResource(getActivity(),R.array.grade_array,
                R.layout.app_spinner_item);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGrade.setAdapter(adp2);

        String[] grades = getResources().getStringArray(R.array.grade_array);
        for (int i=0; i<grades.length; i++){
            if (sessionParcel.getGrade().equals(grades[i])){
                spnGrade.setSelection(i);
            }
        }


        // Botón para enviar reporte
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionParcel.setInstitution(edtInstitution.getText().toString());
                sessionParcel.setClassroom(edtClassroom.getText().toString());
                if (spnGrade.getSelectedItemPosition()==0){
                    sessionParcel.setGrade("");
                } else{
                    sessionParcel.setGrade(spnGrade.getSelectedItem().toString());
                }
                if (spnLevel.getSelectedItemPosition()==0){
                    sessionParcel.setLevel("");
                } else {
                    sessionParcel.setLevel(spnLevel.getSelectedItem().toString());
                }
                mProgress.setVisibility(View.VISIBLE);
                mProgress.setProgress(0);
                Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
                intent.putExtra(NetworkTeacherService.SERVICE,
                        NetworkTeacherService.SERVICE_UPDATE_FULL_REPORT);
                intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
                getActivity().startService(intent);

            }
        });

        return v;

    }

    /*
    REceiver para capturar el estado del actualización de la lección con los detalles
     */
    public class UpdateSchoolSessionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mProgress.setVisibility(View.INVISIBLE);
            int status = intent.getIntExtra(NetworkTeacherService.STATUS, 0);
            if (status== TeacherLessonController.DONE) {
                mListener.scheduleMail(appStateParcel);
                DialogFragment newFragment = new ConfirmOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", "");
                b.putString("MESSAGE", getResources().getString(R.string.msg_will_be_sent_message));
                newFragment.setArguments(b);
                newFragment.setTargetFragment(SessionSchoolReportFragment.this, 1234);
                newFragment.show(getFragmentManager(), "success");
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_UPDATE_FULL_REPORT);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        updateSessionReceiver = new UpdateSchoolSessionReceiver();
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
        mListener.finishReport(sessionParcel);
    }

    @Override
    public void onFireCancelEventOneButtonListener() {
        mListener.finishReport(sessionParcel);
    }


}

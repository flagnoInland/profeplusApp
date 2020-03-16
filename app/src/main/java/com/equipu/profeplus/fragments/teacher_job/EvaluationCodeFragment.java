package com.equipu.profeplus.fragments.teacher_job;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.controllers.EvaluationController;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Prototipo para mostar el código de la evaluación
 */
public class EvaluationCodeFragment extends Fragment {

    @BindView(R.id.txt_header_evaluation) TextView txtHeader;
    @BindView(R.id.edt_rand_code_session) TextView txtRandom;
    @BindView(R.id.txt_connected) TextView txtHeader2;
    @BindView(R.id.edt_connected) TextView txtConnected;
    @BindView(R.id.btn_accept) Button btnAccept;
    @BindView(R.id.btn_edit) Button btnEdit;
    @BindView(R.id.img_back) ImageView btnback;
    @BindView(R.id.img_exit) ImageView btnexit;
    @BindView(R.id.btn_refresh_result) Button btnRefresh;
    private Unbinder unbinder;

    SessionParcel sessionParcel;
    AppStateParcel appStateParcel;
    EvaluationParcel evaluationParcel;

    private EvaluationCodeListener mListener;

    public EvaluationCodeFragment() {
        // Required empty public constructor
    }


    public static EvaluationCodeFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        EvaluationCodeFragment fragment = new EvaluationCodeFragment();
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
        View v = inflater.inflate(R.layout.fragment_evaluation_code, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtHeader.setTypeface(LearnApp.appFont);
        txtHeader2.setTypeface(LearnApp.appFont);
        txtRandom.setTypeface(LearnApp.appFont);
        txtConnected.setTypeface(LearnApp.appFont);

        String subtitle = "";
        String spe = sessionParcel.getSpeciality();
        String crs = sessionParcel.getCourseName();
        String ins = sessionParcel.getInstitution();

        if (!crs.equals("")&&!spe.equals("")&&!ins.equals("")){
            subtitle = String.format("%s\n%s\n%s",crs,ins,spe);
        } else if (crs.equals("")&&!spe.equals("")&&!ins.equals("")){
            subtitle = String.format("%s\n%s",ins,spe);
        } else if (!crs.equals("")&&spe.equals("")&&!ins.equals("")) {
            subtitle = String.format("%s\n%s", crs, ins);
        } else if (!crs.equals("")&&!spe.equals("")&&ins.equals("")) {
            subtitle = String.format("%s\n%s", crs, spe);
        }  else if (!crs.equals("")&&spe.equals("")&&ins.equals("")) {
            subtitle = crs;
        } else if (crs.equals("")&&!spe.equals("")&&ins.equals("")) {
            subtitle = spe;
        } else if (crs.equals("")&&spe.equals("")&&!ins.equals("")) {
            subtitle = ins;
        }

        txtHeader.setText(String.format("%s\n%s\n%s",
                getString(R.string.msg_evaluation_at_moment),
                sessionParcel.getEvalName(),
                subtitle));

        if (sessionParcel.getInactive() == 0){
            txtHeader.setText(String.format("%s\n%s\n%s",
                    getString(R.string.msg_evaluation_at_moment),
                    sessionParcel.getEvalName(),
                    subtitle));
            txtConnected.setVisibility(View.VISIBLE);
            txtHeader2.setVisibility(View.VISIBLE);
        } else {
            txtHeader.setText(String.format("%s\n%s",
                    sessionParcel.getEvalName(),
                    subtitle));
            txtConnected.setVisibility(View.GONE);
            txtHeader2.setVisibility(View.GONE);
        }

        txtRandom.setText(sessionParcel.getAccesscode());


        if (sessionParcel.getInactive() == 0){
            btnEdit.setVisibility(View.GONE);
            btnRefresh.setEnabled(true);
            btnRefresh.setVisibility(View.VISIBLE);
        } else {
            btnEdit.setVisibility(View.VISIBLE);
            btnRefresh.setEnabled(false);
            btnRefresh.setVisibility(View.GONE);
        }


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    EditTask editTask = new EditTask();
                    editTask.execute();
                } else {
                    mListener.dialogLostConnection();
                }

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    ConnectedTask connectedTask = new ConnectedTask();
                    connectedTask.execute();
                } else {
                    mListener.dialogLostConnection();
                }

            }
        });


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    public class ConnectedTask extends AsyncTask<Void,Void,Void> {

        EvaluationController evaluationController;

        @Override
        protected Void doInBackground(Void... p) {
            evaluationController = new EvaluationController();
            evaluationController.getConnected(appStateParcel, sessionParcel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                txtConnected.setText(evaluationController.getStudents());
            }
        }
    }

    public class EditTask extends AsyncTask<Void,Void,Void> {

        EvaluationController evaluationController;

        @Override
        protected Void doInBackground(Void... p) {
            evaluationController = new EvaluationController();
            evaluationController.evaluationComplete(appStateParcel, sessionParcel.getEvaluationId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                evaluationParcel = evaluationController.getEvaluationParcel();
                appStateParcel.setEditEvaluation(1);
                mListener.startEvaluationStepOne(appStateParcel, evaluationParcel, sessionParcel);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EvaluationCodeListener) {
            mListener = (EvaluationCodeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EvaluationCodeListener");
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


    public interface EvaluationCodeListener {
        void startEvaluationStepOne(AppStateParcel ap, EvaluationParcel ep, SessionParcel sp);
        void dialogLostConnection();
    }
}

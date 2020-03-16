package com.equipu.profeplus.fragments.teacher_job;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.TeacherJobActivity;
import com.equipu.profeplus.controllers.EvaluationController;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.Evaluation;
import com.equipu.profeplus.models.EvaluationList;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Prototipo para mostar evaluaciones activas
 */
public class ActiveEvaluationFragment extends Fragment {

    @BindView(R.id.txt_header_active_evaluation) TextView txtHeader;
    @BindView(R.id.txt_header_pending_evaluation) TextView txtHeader2;
    @BindView(R.id.lay_eval_now) LinearLayout layEvalNow;
    @BindView(R.id.lay_evaluations) LinearLayout layEvaluations;
    @BindView(R.id.btn_create_evaluation) Button btnCreate;

    @BindView(R.id.img_back) ImageView btnback;
    @BindView(R.id.img_exit) ImageView btnexit;
    private Unbinder unbinder;

    ImageView[] imgEvals;
    TextView[] txtEvals;
    EvaluationList evaluationList;
    EvaluationList evaluationActiveList;
    EvaluationParcel evaluationParcel;

    SessionParcel sessionParcel;
    AppStateParcel appStateParcel;

    private ActiveEvaluationListener mListener;

    public ActiveEvaluationFragment() {
        // Required empty public constructor
    }



    public static ActiveEvaluationFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        ActiveEvaluationFragment fragment = new ActiveEvaluationFragment();
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
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_active_evaluation, container, false);
        unbinder = ButterKnife.bind(this, v);

        evaluationParcel = new EvaluationParcel();

        txtHeader.setTypeface(LearnApp.appFont2);
        txtHeader2.setTypeface(LearnApp.appFont2);

        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            EvalsTask evalsTask = new EvalsTask();
            evalsTask.execute();

            ActiveEvalsTask activeEvalsTask = new ActiveEvalsTask();
            activeEvalsTask.execute();

        } else {
            onResume();
            //mListener.dialogLostConnection();
        }


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appStateParcel.setEditEvaluation(0);
                sessionParcel.setAccesscode("0000");
                mListener.startEvaluationStepOne(appStateParcel, evaluationParcel, sessionParcel);
            }
        });


        return v;

    }


    private void updateEvalList(final Evaluation evaluation){
        View evalView = LayoutInflater.from(getActivity())
                .inflate(R.layout.template_evaluation_view, layEvaluations, false);

        TextView txtName = (TextView) evalView.findViewById(R.id.txt_evaluation);
        txtName.setText(evaluation.getCourse());
        ImageView imgDelete = (ImageView) evalView.findViewById(R.id.btn_delete_evaluation);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    DeactivateEvalTask deactivateEvalTask = new DeactivateEvalTask();
                    deactivateEvalTask.execute(evaluation.getId());
                } else {
                    onResume();
                    //mListener.dialogLostConnection();
                }

            }
        });


        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionParcel.setEvalName(evaluation.getName());
                sessionParcel.setEvaluationId(evaluation.getId());
                sessionParcel.setSessionId(evaluation.getLesson());
                sessionParcel.setAccesscode(evaluation.getCode());
                sessionParcel.setCourseName(evaluation.getCourse());
                sessionParcel.setInstitution(evaluation.getInstitution());
                sessionParcel.setSpeciality(evaluation.getSpeciality());
                sessionParcel.setInactive(1);
                mListener.startSavedEvaluation(appStateParcel,sessionParcel);
            }
        });


        layEvaluations.addView(evalView);
    }

    private void updateActiveEvalList(final Evaluation evaluation){
        View evalView = LayoutInflater.from(getActivity())
                .inflate(R.layout.template_active_evaluation_view, layEvaluations, false);

        TextView txtName = (TextView) evalView.findViewById(R.id.txt_evaluation);
        txtName.setText(evaluation.getCourse());
        TextView imgDelete = (TextView) evalView.findViewById(R.id.btn_delete_evaluation);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    DeactivateLessonEvalTask deactivateLessonEvalTask = new DeactivateLessonEvalTask();
                    deactivateLessonEvalTask.execute(evaluation.getId(),
                            evaluation.getLesson());
                } else {
                    onResume();
                    //mListener.dialogLostConnection();
                }
            }
        });

        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionParcel.setEvalName(evaluation.getName());
                sessionParcel.setEvaluationId(evaluation.getId());
                sessionParcel.setSessionId(evaluation.getLesson());
                sessionParcel.setAccesscode(evaluation.getCode());
                sessionParcel.setCourseName(evaluation.getCourse());
                sessionParcel.setInstitution(evaluation.getInstitution());
                sessionParcel.setSpeciality(evaluation.getSpeciality());
                sessionParcel.setInactive(0);
                mListener.startSavedEvaluation(appStateParcel,sessionParcel);
            }
        });

        layEvalNow.addView(evalView);

    }

    public class EvalsTask extends AsyncTask<Void,Void,Void> {

        TeacherLessonController teacherLessonController;
        EvaluationController evaluationController;

        @Override
        protected Void doInBackground(Void... p) {
            evaluationController = new EvaluationController();
            evaluationController.getTeachersEvaluations(appStateParcel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                evaluationList = evaluationController.getEvaluationList();
                for (int i = 0; i<evaluationList.getEvaluations().length; i++){
                    updateEvalList(evaluationList.getEvaluations()[i]);
                }
            }
        }
    }

    public class ActiveEvalsTask extends AsyncTask<Void,Void,Void> {

        TeacherLessonController teacherLessonController;
        EvaluationController evaluationController;

        @Override
        protected Void doInBackground(Void... p) {
            evaluationController = new EvaluationController();
            evaluationController.getTeachersActiveEvaluations(appStateParcel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                evaluationActiveList = evaluationController.getEvaluationList();
                for (int i = 0; i<evaluationActiveList.getEvaluations().length; i++){
                    updateActiveEvalList(evaluationActiveList.getEvaluations()[i]);
                }
            }
        }
    }

    public class DeactivateEvalTask extends AsyncTask<Integer,Void,Void> {

        EvaluationController evaluationController;

        @Override
        protected Void doInBackground(Integer... p) {
            int evalId = p[0];
            evaluationController = new EvaluationController();
            evaluationController.deactivateEvaluation(appStateParcel, evalId );
            if (evaluationController.getStatus()==EvaluationController.DONE){
                appStateParcel = evaluationController.getAppStateParcel();
                TeacherJobActivity.appStateParcel = appStateParcel;
                evaluationController.getTeachersEvaluations(appStateParcel);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                layEvaluations.removeAllViewsInLayout();
                layEvaluations.invalidate();
                evaluationList = evaluationController.getEvaluationList();
                for (int i = 0; i<evaluationList.getEvaluations().length; i++){
                    updateEvalList(evaluationList.getEvaluations()[i]);
                }
            }
        }
    }

    public class DeactivateLessonEvalTask extends AsyncTask<Integer,Void,Void> {

        EvaluationController evaluationController;
        TeacherLessonController teacherLessonController;

        @Override
        protected Void doInBackground(Integer... p) {
            int evalId = p[0];
            int lessonId = p[1];
            evaluationController = new EvaluationController();
            evaluationController.deactivateLessonEvaluation(appStateParcel, evalId, lessonId);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                appStateParcel = evaluationController.getAppStateParcel();
                TeacherJobActivity.appStateParcel = appStateParcel;
                Log.d("profeplus.eval", String.valueOf(appStateParcel.getEvaluations()));
                evaluationController.getTeachersActiveEvaluations(appStateParcel);
                teacherLessonController = new TeacherLessonController();
                teacherLessonController.mailEval(appStateParcel.getUserId(), evalId);
                //mListener.mailEvaluation(appStateParcel,evalId);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                layEvalNow.removeAllViewsInLayout();
                layEvalNow.invalidate();
                evaluationActiveList = evaluationController.getEvaluationList();
                Log.d("profeplus.evals", String.valueOf(evaluationActiveList.getEvaluations().length));
                for (int i = 0; i<evaluationActiveList.getEvaluations().length; i++){
                    updateActiveEvalList(evaluationActiveList.getEvaluations()[i]);
                }
            }
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActiveEvaluationListener) {
            mListener = (ActiveEvaluationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ActiveEvaluationListener");
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


    public interface ActiveEvaluationListener {
        void startEvaluationStepOne(AppStateParcel ap, EvaluationParcel ep, SessionParcel sp);
        void startSavedEvaluation( AppStateParcel ap, SessionParcel sp);
        void mailEvaluation(AppStateParcel ap, int evalId);
    }
}

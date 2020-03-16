package com.equipu.profeplus.fragments.student_job;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.StudentJobActivity;
import com.equipu.profeplus.controllers.StudentLessonController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.TaskParcel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Prototipo para marcar pregruntas en evaluaciones
 */
public class StudentEvaluationFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener{

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_finish) Button btnFinish;

    @BindView(R.id.txt_header) TextView txtHeader;
    @BindView(R.id.txt_title) TextView txtTitle;
    @BindView(R.id.txt_observation) TextView txtObservation;
    @BindView(R.id.txt_question) TextView txtQuestion;
    @BindView(R.id.txt_answer) TextView txtAnswer;
    @BindView(R.id.txt_score) TextView txtScore;

    @BindView(R.id.question_group) LinearLayout layQuestions;


    private Unbinder unbinder;
    AppStateParcel appStateParcel;
    TaskParcel taskParcel;
    EvaluationParcel evaluationParcel;
    int[] keys;
    Spinner[] spnAnswers;
    int qtn1, qtn2, qtn3, qtn4;


    private StudentEvaluationListener mListener;

    public StudentEvaluationFragment() {
        // Required empty public constructor
    }


    public static StudentEvaluationFragment newInstance(AppStateParcel ap,
                                                        EvaluationParcel ep) {
        StudentEvaluationFragment fragment = new StudentEvaluationFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
        args.putParcelable(LearnApp.PCL_EVALUATION, ep);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
            evaluationParcel = getArguments().getParcelable(LearnApp.PCL_EVALUATION);
        }
    }

    private void startScheduleTask(){
        taskParcel = StudentJobActivity.taskParcel;
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                prepareTaskParcel();
                FinishTask finishTask = new FinishTask();
                finishTask.execute();
            }
        };
        timer.schedule(timerTask, taskParcel.getLeftTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_evaluation, container, false);
        unbinder = ButterKnife.bind(this, v);

        startScheduleTask();

        taskParcel = StudentJobActivity.taskParcel;

        keys = taskParcel.getEvalAnswers();

        txtTitle.setTypeface(LearnApp.appFont);
        txtObservation.setTypeface(LearnApp.appFont);
        txtAnswer.setTypeface(LearnApp.appFont);
        txtHeader.setTypeface(LearnApp.appFont);
        txtQuestion.setTypeface(LearnApp.appFont);
        txtScore.setTypeface(LearnApp.appFont);
        txtObservation.setVisibility(View.GONE);

        String speciality = evaluationParcel.getSpeciality();
        String course = evaluationParcel.getCourseName();
        String institution = evaluationParcel.getInstitution();
        String title = evaluationParcel.getHeader();
        String observation = evaluationParcel.getObservation();
        int[] mats = evaluationParcel.getMaterials();
        ArrayList<String> materials = new ArrayList();

        if (mats[0] == 1){
            materials.add(getString(R.string.text_calculator));
        }

        if (mats[1] == 1){
            materials.add(getString(R.string.text_notes));
        }

        if (mats[2] == 1){
            materials.add(getString(R.string.text_books));
        }

        if (mats[3] == 1){
            materials.add(getString(R.string.text_computer));
        }

        boolean hasInst = !institution.equals("");
        boolean hasCourse = !course.equals("");
        boolean hasSpec = !speciality.equals("");

        String msgHeader = getString(R.string.msg_evaluation_at_moment);
        if (hasInst && hasCourse && hasSpec) {
            txtHeader.setText(String.format("%s\n%s\n%s", institution, speciality, course));
        } else if (!hasInst && !hasCourse&& !hasSpec) {
            txtHeader.setText(msgHeader);
        } else if ( hasInst && !hasCourse && !hasSpec) {
            txtHeader.setText(String.format("%s\n%s",institution,msgHeader));
        } else if ( !hasInst && !hasCourse && hasSpec) {
            txtHeader.setText(String.format("%s\n%s",speciality,msgHeader));
        }  else if ( !hasInst && hasCourse && !hasSpec) {
            txtHeader.setText(course);
        } else if ( hasInst && hasCourse && !hasSpec) {
            txtHeader.setText(String.format("%s\n%s",institution,course));
        } else if ( !hasInst && hasCourse && hasSpec) {
            txtHeader.setText(String.format("%s\n%s", speciality, course));
        } else if (hasInst && !hasCourse && hasSpec) {
            txtHeader.setText(String.format("%s\n%s\n%s", institution, speciality, msgHeader));
        }

        if ( ! title.equals("")){
            txtTitle.setText(String.format("%s",title));
        } else {
            txtTitle.setVisibility(View.GONE);
        }

        if ( ! observation.equals("")){
            txtObservation.setText(String.format("%s %s\n%s\n%s",
                    getString(R.string.text_indications), title,
                    getString(R.string.text_materials_to_use),
                    materials.toString().replaceAll("\\[|\\]","")));
        } else {
            txtObservation.setVisibility(View.GONE);
        }

        loadExam();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareTaskParcel();
                DialogFragment newFragment = new ConfirmTwoButtonsSupportDialog();
                Bundle bd = new Bundle();
                bd.putString("TITLE", getString(R.string.text_confirm));
                bd.putString("MESSAGE", getString(R.string.text_want_finish));
                newFragment.setArguments(bd);
                newFragment.setTargetFragment(StudentEvaluationFragment.this,1234);
                newFragment.show(getFragmentManager(), "answer");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    private void loadExam(){

        spnAnswers = new Spinner[evaluationParcel.getNumberQuestion()];
        String[] spnkeys = getResources().getStringArray(R.array.array_answer_keys_student);

        ArrayAdapter<CharSequence> adapterKeys = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_answer_keys_student, R.layout.app_spinner_eval);
        adapterKeys.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        for (int i=0; i<evaluationParcel.getNumberQuestion(); i++){
            View evalView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.student_box_eval_question, layQuestions, false);

            TextView txtNum = (TextView) evalView.findViewById(R.id.txt_pos1);
            txtNum.setText(String.format("%d.",i+1));

            TextView edtPos1 = (TextView) evalView.findViewById(R.id.edt_pos1);
            edtPos1.setText(String.format("%.1f",evaluationParcel.getAnswerWeights()[qtn3]));

            spnAnswers[i] = (Spinner) evalView.findViewById(R.id.spn_pos1);
            spnAnswers[i].setAdapter(adapterKeys);
            final int aAnswer = taskParcel.getEvalAnswers()[i];
            spnAnswers[i].setSelection(aAnswer);
            spnAnswers[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(aAnswer != i){
                        prepareTaskParcel();
                        startSubmit();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {  }
            });

            layQuestions.addView(evalView);
        }
    }

    public class SubmitTask extends AsyncTask<Void,Void,Void>{

        StudentLessonController studentLessonController;

        @Override
        protected Void doInBackground(Void... params) {
            studentLessonController = new StudentLessonController();
            studentLessonController.submitSolution(taskParcel,appStateParcel);
            return null;
        }

    }

    public class FinishTask extends AsyncTask<Void,Void,Void>{

        StudentLessonController studentLessonController;

        @Override
        protected Void doInBackground(Void... params) {
            studentLessonController = new StudentLessonController();
            studentLessonController.finishSolution(taskParcel,appStateParcel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (studentLessonController.getStatus() == StudentLessonController.DONE){
                mListener.backToBoard(appStateParcel);
            }
        }
    }

    private void prepareTaskParcel(){
        int num = evaluationParcel.getNumberQuestion();
        for (int i=0; i<num; i++){
            keys[i] = spnAnswers[i].getSelectedItemPosition();
        }
        taskParcel.setEvalAnswers(keys);
        StudentJobActivity.taskParcel = taskParcel;
    }

    private void startSubmit(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            SubmitTask submitTask = new SubmitTask();
            submitTask.execute();
        } else {
            mListener.dialogLostConnection();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StudentEvaluationListener) {
            mListener = (StudentEvaluationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StudentEvaluationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface StudentEvaluationListener {
        void backToBoard(AppStateParcel ap);
        void dialogLostConnection();
    }

    @Override
    public void onFireCancelEventTwoButtonListener() {

    }

    @Override
    public void onFireAcceptEventTwoButtonListener() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            FinishTask finishTask = new FinishTask();
            finishTask.execute();
        } else {
            mListener.dialogLostConnection();
        }
    }
}

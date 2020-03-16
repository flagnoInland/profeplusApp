package com.equipu.profeplus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.controllers.EvaluationController;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.fragments.student_job.AnswerCircleFragment;
import com.equipu.profeplus.fragments.student_job.AnswerSquareFragment;
import com.equipu.profeplus.fragments.student_job.AnswerStepTwoFragment;
import com.equipu.profeplus.fragments.student_job.AnswerTrueFragment;
import com.equipu.profeplus.fragments.student_job.GuestLoginFragment;
import com.equipu.profeplus.fragments.student_job.StudentEvaluationFragment;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.TaskParcel;
import com.nullwire.trace.ExceptionHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

/*
Esta actividad es el punto de entrada para las tareas
RESPONDER PREGUNTA DE CINCO ALTERNATIVA
RESPONDER PREGUNTA DE UN BANCO
RESPONDER PREGUNTA DE OPINION
RESPONDER EVALUACIÓN
COMPARTIR CON ESTUDIANTE
 */
public class StudentJobActivity extends AppCompatActivity implements
        IStudentJobListener, StudentEvaluationFragment.StudentEvaluationListener {

    TaskParcel taskGuestParcel;
    public static TaskParcel taskParcel;
    AppStateParcel appStateParcel, appStateGuestParcel;
    EvaluationParcel evaluationParcel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ExceptionHandler.register(this,LearnApp.traceURL);
        //View decorView = getWindow().getDecorView();
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        LearnApp.initImageLoader(getApplicationContext());
        setContentView(R.layout.activity_student_job);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        taskParcel = getIntent().getParcelableExtra("TaskParcel");
        appStateParcel = getIntent().getParcelableExtra("AppStateParcel");


        // Finalizar esta actividad en cuatro horas.

        appStateParcel.setGuestMode(AppStateParcel.INACTIVE);
        taskParcel.setStatus(TaskParcel.ANS_NEW);
        taskGuestParcel = new TaskParcel();
        appStateGuestParcel = new AppStateParcel();

        // Si no es una evaluación comenzar normalmente
        if (taskParcel.getEvalId() == 0) {
            startAnswerStepOne(taskParcel, appStateParcel, taskGuestParcel, appStateGuestParcel);
        } else {
            prepareFinalTime();
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()
                    && networkInfo.isAvailable()) {
                EvalTask evalTask = new EvalTask();
                evalTask.execute();
            } else {
                dialogLostConnection();
            }

        }


    }

    @Nullable
    private void prepareFinalTime() {
        Calendar now = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(taskParcel.getUpdated());
            end.setTime(date);
            Log.d("profeplus.dur", String.valueOf(taskParcel.getDuration()));
            Log.d("profeplus.end", end.toString());
            end.add(Calendar.MINUTE,taskParcel.getDuration());
            Log.d("profeplus.end", end.toString());
            Log.d("profeplus.now", now.toString());
            long diff = end.getTimeInMillis() - now.getTimeInMillis();
            taskParcel.setLeftTime(diff);
            Log.d("profeplus.time", String.valueOf(taskParcel.getLeftTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /*
    Prototipo para iniciar una evaluación
     */
    public void startStudentEvaluation(AppStateParcel ap, EvaluationParcel ep){
        this.appStateParcel = ap;
        this.evaluationParcel = ep;
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_EVALUATION, ep);
        FragmentManager fm = getSupportFragmentManager();
        StudentEvaluationFragment fragment = new StudentEvaluationFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("StudentEvaluation")
                .commit();
    }

    /*
    Mostrar pantalla para cambiar el usuario
     */
    public void loginGuest(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                           AppStateParcel agp){
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TASK_PARCEL, tp);
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_TASK_GUEST_PARCEL, tgp);
        bundle.putParcelable(LearnApp.PCL_APP_STATE_GUEST, agp);
        GuestLoginFragment fragment = new GuestLoginFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("LoginGuest")
                .commit();
    }

    /*
    Mostrar pantalla para el paso 1
     */
    public void startAnswerStepOne(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                                   AppStateParcel agp){
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount()-1; ++i) {
            fm.popBackStack();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TASK_PARCEL, tp);
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_TASK_GUEST_PARCEL, tgp);
        bundle.putParcelable(LearnApp.PCL_APP_STATE_GUEST, agp);
        if (taskParcel.getQuestionType() == TaskParcel.Q_SURVEY){
            AnswerSquareFragment fragment = new AnswerSquareFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("AnswerSquare")
                    .commit();
        } else if (taskParcel.getQuestionType() == TaskParcel.Q_TRUE) {
            AnswerTrueFragment fragment = new AnswerTrueFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("AnswerTrue")
                    .commit();
        } else {
            AnswerCircleFragment fragment = new AnswerCircleFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("AnswerCircle")
                    .commit();

        }
    }


    /*
    Mostrar pantalla para el paso 2
     */
    public void startAnswerStepTwo(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                                   AppStateParcel agp){

        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TASK_PARCEL, tp);
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_TASK_GUEST_PARCEL, tgp);
        bundle.putParcelable(LearnApp.PCL_APP_STATE_GUEST, agp);
        if (taskParcel.getQuestionType() == TaskParcel.Q_SURVEY){
            AnswerSquareFragment fragment = new AnswerSquareFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("AnswerSquare2")
                    .commit();
        } else if (taskParcel.getQuestionType() == TaskParcel.Q_TRUE) {
            AnswerTrueFragment fragment = new AnswerTrueFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("AnswerTrue2")
                    .commit();
        } else {
            AnswerCircleFragment fragment = new AnswerCircleFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("AnswerCircle2")
                    .commit();

        }
    }

    /*
    Mostrar indicaciones antes de iniciar el paso 2
     */
    public void startQuestContent(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                                  AppStateParcel agp){
        updateTaskParcel(ap, agp, tp, tgp);
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TASK_PARCEL, tp);
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_TASK_GUEST_PARCEL, tgp);
        bundle.putParcelable(LearnApp.PCL_APP_STATE_GUEST, agp);
        AnswerStepTwoFragment fragment = new AnswerStepTwoFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("QuestContent")
                .commit();
    }

    /*
    Prototipo para finalizar evaluación
     */
    public class EvalTask extends AsyncTask<Void,Void,Void> {

        EvaluationController evaluationController;

        @Override
        protected Void doInBackground(Void... p) {
            evaluationController = new EvaluationController();
            evaluationController.evaluationComplete(appStateParcel, taskParcel.getEvalId());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (evaluationController.getStatus()==EvaluationController.DONE){
                evaluationParcel = evaluationController.getEvaluationParcel();
                evaluationParcel.setPivotQuestion(evaluationParcel.getNumberQuestion());
                taskParcel.setPivot(evaluationParcel.getNumberQuestion());
                startStudentEvaluation(appStateParcel, evaluationParcel);
            }
        }
    }

    /*
    Actualizar datos de la sesión de trabajo
     */
    @Override
    public void updateTaskParcel(AppStateParcel ap, AppStateParcel agp, TaskParcel tp,
                                 TaskParcel tgp) {
        this.taskParcel = tp;
        this.taskGuestParcel = tgp;
        this.appStateParcel = ap;
        this.appStateGuestParcel = agp;
    }

    /*
    Iniciar el paso 2
     */
    @Override
    public void goToStepTwo(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                            AppStateParcel agp) {
        updateTaskParcel(ap, agp, tp, tgp);
        startAnswerStepTwo(tp, ap, tgp, agp);
    }



    @Override
    public void backToBoard(AppStateParcel ap) {
        Intent options = new Intent(getApplicationContext(), UserBoardActivity.class);
        options.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        options.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        startActivity(options);
        finish();
    }

    @Override
    public void onBackPressed() {
        int transactionNumber = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("profeplus.tran",String.valueOf(transactionNumber));
        if (transactionNumber == 1) {
            backToBoard(appStateGuestParcel);
        } else {
            super.onBackPressed();
        }
    }


    public void dialogLostConnection(){
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment newFragment = new NotificationOneButtonSupportDialog();
        Bundle b = new Bundle();
        b.putString("TITLE", getString(R.string.text_problem));
        b.putString("MESSAGE", getString(R.string.msg_network_fails));
        newFragment.setArguments(b);
        newFragment.show(fm, null);
    }



}

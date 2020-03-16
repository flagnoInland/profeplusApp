package com.equipu.profeplus.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.equipu.profeplus.FinishEval;
import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.SendMail;
import com.equipu.profeplus.controllers.TaskTableAdapter;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.fragments.MaintenanceFragment;
import com.equipu.profeplus.fragments.TutorialFragment;
import com.equipu.profeplus.fragments.teacher_job.ActiveEvaluationFragment;
import com.equipu.profeplus.fragments.teacher_job.ChooseReportFragment;
import com.equipu.profeplus.fragments.teacher_job.CompleteReportFragment;
import com.equipu.profeplus.fragments.teacher_job.EvaluationCodeFragment;
import com.equipu.profeplus.fragments.teacher_job.EvaluationStepFiveFragment;
import com.equipu.profeplus.fragments.teacher_job.EvaluationStepFourFragment;
import com.equipu.profeplus.fragments.teacher_job.EvaluationStepOneFragment;
import com.equipu.profeplus.fragments.teacher_job.EvaluationStepThreeFragment;
import com.equipu.profeplus.fragments.teacher_job.EvaluationStepTwoFragment;
import com.equipu.profeplus.fragments.teacher_job.FreeOpinionFragment;
import com.equipu.profeplus.fragments.teacher_job.FreeQuestionFragment;
import com.equipu.profeplus.fragments.teacher_job.ResultStateFourFragment;
import com.equipu.profeplus.fragments.teacher_job.ResultStateOneFragment;
import com.equipu.profeplus.fragments.teacher_job.ResultStateThreeFragment;
import com.equipu.profeplus.fragments.teacher_job.ResultStateTwoFragment;
import com.equipu.profeplus.fragments.teacher_job.ResultTFStateFourFragment;
import com.equipu.profeplus.fragments.teacher_job.ResultTFStateOneFragment;
import com.equipu.profeplus.fragments.teacher_job.ResultTFStateThreeFragment;
import com.equipu.profeplus.fragments.teacher_job.ResultTFStateTwoFragment;
import com.equipu.profeplus.fragments.teacher_job.SessionCodeFragment;
import com.equipu.profeplus.fragments.teacher_job.SessionExerciseFragment;
import com.equipu.profeplus.fragments.teacher_job.SessionSchoolReportFragment;
import com.equipu.profeplus.models.AnswerList;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.equipu.profeplus.models.Tutorial;
import com.nullwire.trace.ExceptionHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
Esta actividad es un punto de entrada para las tareas:
ESCOGER PREGUNTA
ESCOGER PREGUNTA DE OPINIÓN
MOSTRAR CLAVE DE PREGUNTA
COMPLETAR DATOS DE PREGUNTA DE UN BANCO
VER RESULTADOS
VER RESULTADOS PARA PREGUNTA DE VERDADERO Y FALSO
ENVIAR INFORME SENCILLO
ENVIAR INFORME COMPLETO EDUCACIÓN SUPERIOR
ENVIAR INFORME COMPLET EDUCACIÓN BÁSICA
VER TUTORIALES
 */
public class TeacherJobActivity extends AppCompatActivity implements
        ITutorialListener,ITeacherJobListener,
        EvaluationStepOneFragment.EvaluationStepOneListener,
        EvaluationStepTwoFragment.EvaluationStepTwoListener,
        EvaluationStepThreeFragment.EvaluationStepThreeListener,
        EvaluationStepFourFragment.EvaluationStepFourListener,
        EvaluationStepFiveFragment.EvaluationStepFiveListener,
        ActiveEvaluationFragment.ActiveEvaluationListener,
        EvaluationCodeFragment.EvaluationCodeListener {


    private SessionParcel sessionParcel;
    public static AppStateParcel appStateParcel;
    public static EvaluationParcel evaluationParcel;
    FinishSessionReceiver finishSessionReceiver;
    Tutorial tutorial;
    int evaluationPager;

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
        setContentView(R.layout.activity_teacher_job);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sessionParcel = getIntent().getParcelableExtra("SessionParcel");
        appStateParcel = getIntent().getParcelableExtra("AppStateParcel");
        appStateParcel.setChangeExercise(0);

        sessionParcel.setRun(1);

        FragmentManager fm = getSupportFragmentManager();

        // Si no es una evaluación elegir una pregunta
        if (sessionParcel.getQuestionType() != SessionParcel.Q_EVAL) {
            FreeQuestionFragment fragment = new FreeQuestionFragment();
            fragment.setArguments(makeSB(sessionParcel,appStateParcel));
            fm.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack("FreeQuestion")
                    .commit();
        } else {
            ActiveEvaluationFragment fragment = new ActiveEvaluationFragment();
            fragment.setArguments(makeSB(sessionParcel,appStateParcel));
            fm.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack("ActiveEvaluation")
                    .commit();
        }

    }

    public static final int TASK_FINISH_SESSION = 1;
    public static final int TASK_FINISH_EXERCISE = 2;
    public static final int TASK_USER_BOARD = 3;


    /*
    Prototipo para rastrear lecciones sin finalizar
     */
    public class UnfinishedLessonTask extends AsyncTask<String,Void,Void>{

        TeacherLessonController teacherLessonController;

        @Override
        protected Void doInBackground(String... params) {
            teacherLessonController = new TeacherLessonController();
            TaskTableAdapter.TaskTableDBHelper helper =
                    new TaskTableAdapter.TaskTableDBHelper(getApplicationContext());
            SQLiteDatabase db = null;
            db = helper.getWritableDatabase();
            int[] lessons = helper.getUnfinishedTasks(db);
            for (int i=0; i<lessons.length; i++){
                String token = params[0];
                teacherLessonController.disableSession(lessons[i],token);
                if (teacherLessonController.getStatus()==TeacherLessonController.DONE){
                    helper.changeStatus(db,lessons[i]);
                }
            }
            helper.close();
            db.close();
            return null;
        }
    }

    /*
    Capturar estado de la lección ha deshabilitar/finalizar
     */
    public class FinishSessionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS,0);
            int task = intent.getIntExtra(NetworkTeacherService.TASK,0);
            if (status == TeacherLessonController.DONE &&
                    task == TASK_FINISH_SESSION){
                Log.d("profeplus.eval", String.valueOf(appStateParcel.getEvaluations()));
                Intent options = new Intent(getApplicationContext(), UserBoardActivity.class);
                options.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                options.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                startActivity(options);
                finish();
            } else if (status == TeacherLessonController.DONE &&
                    task == TASK_FINISH_EXERCISE){
                onBackPressed();
            }
        }
    }

    /*
    Registrar acción para finalizar lecciones
     */
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_DISABLE_SESSION);
        finishSessionReceiver = new FinishSessionReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(TeacherJobActivity.this);
        lbm.registerReceiver(finishSessionReceiver, filter);
    }

    /*
    Remover receiver para terminar lecciones
     */
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(TeacherJobActivity.this);
        lbm.unregisterReceiver(finishSessionReceiver);
    }

    /*
    Iniciar servicio para deshabilitar/finalizar lecciones
     */
    public void startFinishSession(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            Intent intent = new Intent(TeacherJobActivity.this, NetworkTeacherService.class);
            intent.putExtra(NetworkTeacherService.SERVICE,
                    NetworkTeacherService.SERVICE_DISABLE_SESSION);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
            intent.putExtra(NetworkTeacherService.TASK, TASK_FINISH_SESSION);
            startService(intent);
        } else {
            dialogLostConnection();
            /*
            TaskTableAdapter.TaskTableDBHelper helper =
                    new TaskTableAdapter.TaskTableDBHelper(getApplicationContext());
            SQLiteDatabase db = null;
            db = helper.getWritableDatabase();
            helper.addTask(db,appStateParcel.getUserId(),sessionParcel.getSessionId(),"",1);
            helper.close();
            db.close();
            Log.d("profeplus.eval", String.valueOf(appStateParcel.getEvaluations()));
            */
            Intent options = new Intent(getApplicationContext(), UserBoardActivity.class);
            options.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            options.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            startActivity(options);
            finish();
        }
    }

    /*
    Iniciar servicio para almacenar lecciones
     */
    public void startUserBoard(){
        Intent intent = new Intent(TeacherJobActivity.this, NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_DISABLE_SESSION);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        intent.putExtra(NetworkTeacherService.TASK, TASK_USER_BOARD);
        startService(intent);
    }

    /*
   Iniciar servicio para deshabilitar/finalizar lecciones
    */
    public void startFinishExercise(){
        Intent intent = new Intent(TeacherJobActivity.this, NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_DISABLE_SESSION);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        intent.putExtra(NetworkTeacherService.TASK, TASK_FINISH_EXERCISE);
        startService(intent);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int transactionNumber = fm.getBackStackEntryCount();
        String fname = fm.getBackStackEntryAt(transactionNumber-1).getName();
        Log.d("profeplus.tran",String.valueOf(transactionNumber));
        Log.d("profeplus.fname",fname);
        if (transactionNumber == 1) {
            if ( fname.equals("FreeQuestion") ) {
                backToUserBoard(appStateParcel);
            } else if ( fname.equals("ActiveEvaluation") ) {
                backToUserBoard(appStateParcel);
            } else {
                startFinishSession();
            }
        }  else {
            switch (fname){
                case "SessionCode":
                    sessionParcel.setAccesscode("0000");
                    startUserBoard();
                    fm.popBackStack();
                    fname = fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName();
                    if (fname.equals("ChooseOpinion")){
                        FreeOpinionFragment.newInstance(sessionParcel,appStateParcel);
                    } else  if (fname.equals("FreeQuestion")){
                        // Apparently there is no need to instantiate a fragment
                    }
                    break;
                case "Manual":
                    if (tutorial != null){
                        tutorial.previousPage();
                    }
                    fm.popBackStack();
                    break;
                case "ChooseReport":
                    fm.popBackStack();
                    break;
                case "SchoolReport":
                    fm.popBackStack();
                    break;
                case "CompleteReport":
                    fm.popBackStack();
                    break;
                case "Evaluation5":
                    evaluationParcel.setPivotQuestion(evaluationParcel.getPivotQuestion() - 4);
                    fm.popBackStack();
                    evaluationPager--;
                    Log.d("profeplus.pag", String.valueOf(evaluationParcel.getPivotQuestion()));
                    if (evaluationParcel.getPivotQuestion() >= 0){
                        fm.popBackStack();
                        startEvaluationStepFive(appStateParcel, sessionParcel, evaluationParcel);
                    }
                    break;
                default:
                    super.onBackPressed();
            }
        }

    }

    /*
    Actualizar clave para lección
     */
    @Override
    public void updateSessionCode(AppStateParcel ap, SessionParcel sp) {
        this.appStateParcel = ap;
        this.sessionParcel = sp;
    }

    /*
    Mostrar pantalla para eligir opinión
     */
    @Override
    public void chooseOpinionType(SessionParcel sp) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        FreeOpinionFragment fragment = new FreeOpinionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("ChooseOpinion")
                .commit();
    }

    /*
    Mostrar pantalla con la clave
     */
    @Override
    public void setCodeSession(SessionParcel sp) {
        this.sessionParcel = sp;
        /*
        FragmentManager fm = getSupportFragmentManager();
        SessionCodeFragment fragment = new SessionCodeFragment();
        fragment.setArguments(makeSB(sessionParcel,appStateParcel));
        fm.beginTransaction().add(R.id.fragment_container, fragment)
                .addToBackStack("SessionCode")
                .commit();
                */
        showResults(sp);
    }

    /*
    Mostrar resultados del paso 1 en actividad
     */
    @Override
    public void showResults(SessionParcel sp) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        int pile = fm.getBackStackEntryCount();
        for (int i =0; i<pile; i++){
            fm.popBackStack();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        if (sessionParcel.getQuestionType() == SessionParcel.Q_TRUE){
            ResultTFStateOneFragment fragment = new ResultTFStateOneFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("Result1")
                    .commit();
        } else {
            ResultStateOneFragment fragment = new ResultStateOneFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("Result1")
                    .commit();
        }
    }

    /*
    Mostrar resultados al finalizar el primer paso
     */
    @Override
    public void startResultStateTwo(SessionParcel sp, AnswerList answerList) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        ResultStateTwoFragment fragment = new ResultStateTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        bundle.putParcelable("AnswerList", answerList);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Result2")
                .commit();
    }

    /*
    Mostrar resultados del paso 2 en actividad
     */
    @Override
    public void startResultStateThree(SessionParcel sp) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        ResultStateThreeFragment fragment = new ResultStateThreeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Result3")
                .commit();
    }

    /*
     Mostrar resultados del paso 2 en actividad para verdadero-falso
      */
    @Override
    public void startResultTFStateThree(SessionParcel sp) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        ResultTFStateThreeFragment fragment = new ResultTFStateThreeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Result3")
                .commit();
    }

    /*
   Mostrar resultados al finalizar paso 1 para verdadero-falso
    */
    @Override
    public void startResultTFStateTwo(SessionParcel sp, AnswerList answerList) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        ResultTFStateTwoFragment fragment = new ResultTFStateTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        bundle.putParcelable("AnswerList", answerList);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Result2")
                .commit();
    }

    /*
    Mostrar pantalla para elegir reporte simple o completo
     */
    @Override
    public void setReportData(SessionParcel sp) {
        this.sessionParcel = sp;
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        FragmentManager fm = getSupportFragmentManager();
        ChooseReportFragment fragment = new ChooseReportFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("ChooseReport")
                .commit();
    }

    /*
    Mostrar resultados al finalizar paso 2
     */
    @Override
    public void startResultStateFour(SessionParcel sp, AnswerList ol, AnswerList nl) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        ResultStateFourFragment fragment = new ResultStateFourFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        bundle.putParcelable("OldAnswerList", ol);
        bundle.putParcelable("AnswerList", nl);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Result4")
                .commit();
    }

    /*
    Mostrar pantalla para preguntas de un banco
     */
    @Override
    public void setSubjectCourse(SessionParcel sp) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        SessionExerciseFragment fragment = new SessionExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("Subject")
                .commit();
    }

    /*
    Mostrar resultados al finalizar paso 2 para verdadero-falso
     */
    @Override
    public void startResultTFStateFour(SessionParcel sp, AnswerList ol, AnswerList nl) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        ResultTFStateFourFragment fragment = new ResultTFStateFourFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SessionParcel", sessionParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        bundle.putParcelable("OldAnswerList", ol);
        bundle.putParcelable("AnswerList", nl);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Result4")
                .commit();
    }

    /*
    Manual del profesor
     */
    @Override
    public void readTeacherManual() {
        String[] content = getResources().getStringArray(R.array.content_protocol);
        String[] titles = getResources().getStringArray(R.array.title_protocol);
        TypedArray images = getResources().obtainTypedArray(R.array.images_protocol);
        tutorial = new Tutorial(content, titles, images);
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Manual")
                .commit();
    }

    /*
    Finalizar pregunta
     */
    @Override
    public void finishLesson(SessionParcel sp) {
        this.sessionParcel = sp;
        startFinishSession();
    }

    /*
    Finalizar pregunta de banco
     */
    @Override
    public void finishExercise(SessionParcel sp) {
        this.sessionParcel = sp;
        startFinishExercise();
    }

    /*
    Mostar pantalla para reporte completo.
    Modo escolar difiere del modo superior
     */
    @Override
    public void makeCompleteReport(SessionParcel sp, AppStateParcel ap) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        if (appStateParcel.getAppMode()== AppStateParcel.SCHOOL_MODE) {
            SessionSchoolReportFragment fragment = new SessionSchoolReportFragment();
            fragment.setArguments(makeSB(sp,ap));
            fm.beginTransaction().add(R.id.fragment_container,fragment)
                    .addToBackStack("SchoolReport")
                    .commit();
        } else {
            CompleteReportFragment fragment = new CompleteReportFragment();
            fragment.setArguments(makeSB(sp,ap));
            fm.beginTransaction().add(R.id.fragment_container,fragment)
                    .addToBackStack("CompleteReport")
                    .commit();
        }
    }

    /*
    Los siguiente métodos son prototipos para un eventual funiconalidad
    de evaluaciones en el app
     */
    @Override
    public void startEvaluationStepOne(AppStateParcel ap, EvaluationParcel ep, SessionParcel sp) {
        this.evaluationParcel = ep;
        this.appStateParcel = ap;
        this.sessionParcel = sp;
        evaluationPager = 1;
        // TODO: ACTIVATE EVALUATION
        boolean ACTIVATE_EVAL = false;
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        if (ACTIVATE_EVAL) {
            bundle.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
            bundle.putParcelable(LearnApp.PCL_EVALUATION, ep);
            EvaluationStepOneFragment fragment = new EvaluationStepOneFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragment_container, fragment)
                    .addToBackStack("Evaluation1")
                    .commit();
        } else {
            MaintenanceFragment fragment = new MaintenanceFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragment_container, fragment)
                    .addToBackStack("Maintenance")
                    .commit();
        }
    }

    @Override
    public void startEvaluationStepFive(AppStateParcel ap, SessionParcel sp,EvaluationParcel ep) {
        this.appStateParcel = ap;
        this.evaluationParcel = ep;
        this.sessionParcel = sp;
        ++evaluationPager;
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_EVALUATION, ep);
        bundle.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
        FragmentManager fm = getSupportFragmentManager();
        EvaluationStepFiveFragment fragment = new EvaluationStepFiveFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("Evaluation5")
                .commit();
    }

    @Override
    public void startEvaluationStepTwo(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep) {
        this.appStateParcel = ap;
        this.evaluationParcel = ep;
        this.sessionParcel = sp;
        evaluationPager = 2;
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_EVALUATION, ep);
        bundle.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
        FragmentManager fm = getSupportFragmentManager();
        EvaluationStepTwoFragment fragment = new EvaluationStepTwoFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("Evaluation2")
                .commit();
    }

    @Override
    public void startEvaluationStepFour(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep) {
        this.appStateParcel = ap;
        this.evaluationParcel = ep;
        this.sessionParcel = sp;
        evaluationPager = 4;
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_EVALUATION, ep);
        bundle.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
        FragmentManager fm = getSupportFragmentManager();
        EvaluationStepFourFragment fragment = new EvaluationStepFourFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("Evaluation4")
                .commit();
    }

    @Override
    public void startEvaluationStepThree(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep) {
        this.appStateParcel = ap;
        this.evaluationParcel = ep;
        this.sessionParcel = sp;
        evaluationPager = 3;
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_EVALUATION, ep);
        bundle.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
        EvaluationStepThreeFragment fragment = new EvaluationStepThreeFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("Evaluation3")
                .commit();
    }

    /*
    Leer siguiente página del manual
     */
    @Override
    public void nextPage(Tutorial tutorial) {
        tutorial.nextPage();
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment).
                addToBackStack("Manual").
                commit();
    }

    /*
    Salir del manual
     */
    @Override
    public void endRead(Tutorial tutorial) {
        FragmentManager fm = getSupportFragmentManager();
        int num = tutorial.getPageNumber()+1;
        for (int i=0; i<num; i++){
            fm.popBackStack();
        }
    }


    /*
    Prototipo para funcionalidad evaluación
     */
    @Override
    public void startEvaluation(AppStateParcel ap, SessionParcel sp, EvaluationParcel ep) {
        this.sessionParcel = sp;
        this.appStateParcel = ap;
        int userID = Integer.parseInt(ap.getUserId());
        int lessonID = sp.getSessionId();
        scheduleEndEvaluation(userID, lessonID, ep);
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount()-1; ++i) {
            fm.popBackStack();
        }
        SessionCodeFragment fragment = new SessionCodeFragment();
        fragment.setArguments(makeSB(sp, ap));
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("SessionCode")
                .commit();
    }


    /*
    Prototipo para cronometrar la evaluacion
     */
    public void scheduleEndEvaluation(int uid, int lid, EvaluationParcel ep) {
        Intent intent = new Intent(getApplicationContext(), FinishEval.class);
        intent.putExtra("USER_ID",uid);
        intent.putExtra("LESSON_ID", lid);
        intent.putExtra("EVAL_ID", ep.getEvaluationId());
        PendingIntent finishEval = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        Calendar calendar = getEndDateTime(ep);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                finishEval);
    }


    /*
    Obtener hora de finalización para la funcionalaidad prototipo
     */
    public Calendar getEndDateTime(EvaluationParcel ep){
        String myDate = String.format("%s %s:00",ep.getDate(),ep.getEndTime());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(myDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    /*
    Almacena evaluación para la funcionalidad prototipo
     */
    @Override
    public void startSavedEvaluation(AppStateParcel ap, SessionParcel sp) {
        this.sessionParcel = sp;
        FragmentManager fm = getSupportFragmentManager();
        EvaluationCodeFragment fragment = new EvaluationCodeFragment();
        fragment.setArguments(makeSB(sp, ap));
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("EvaluationCode")
                .commit();
    }


    /*
    Terminar evaluación
     */
    @Override
    public void finishEvaluation(AppStateParcel ap, SessionParcel sp) {
        FragmentManager fm = getSupportFragmentManager();
        for (int i =0; i<evaluationPager; i++){
            fm.popBackStack();
        }
        FreeQuestionFragment fragment = new FreeQuestionFragment();
        fragment.setArguments(makeSB(sessionParcel,appStateParcel));
        fm.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack("FreeQuestion")
                .commit();
    }

    @Override
    public void backToFreeQuestion() {
        sessionParcel.setAccesscode("0000");
        FragmentManager fm = getSupportFragmentManager();
        for (int i =0; i < fm.getBackStackEntryCount(); i++){
            fm.popBackStack();
        }
        FreeQuestionFragment fragment = new FreeQuestionFragment();
        fragment.setArguments(makeSB(sessionParcel,appStateParcel));
        fm.beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack("FreeQuestion")
                .commit();
    }

    @Override
    public void finishReport(SessionParcel sessionParcel) {
        this.sessionParcel = sessionParcel;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        fm.popBackStack();
    }

    @Override
    public void backToUserBoardFromEval(AppStateParcel ap) {
        this.appStateParcel = ap;
        appStateParcel.setEvaluations(1);
        Intent board = new Intent(getApplicationContext(), UserBoardActivity.class);
        //board.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        board.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        finish();
        startActivity(board);
    }

    @Override
    public void backToUserBoard(AppStateParcel ap) {
        this.appStateParcel = ap;
        Intent board = new Intent(getApplicationContext(), UserBoardActivity.class);
        //board.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        board.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        finish();
        startActivity(board);
    }

    /*
    Prototipo para editar detalles de la evaluación
     */
    @Override
    public void startEditEvaluation(AppStateParcel ap, SessionParcel sp) {
        this.appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i<fm.getBackStackEntryCount(); i++){
            fm.popBackStack();
        }
        SessionCodeFragment fragment = new SessionCodeFragment();
        fragment.setArguments(makeSB(sp,ap));
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("SessionCode")
                .commit();
    }

    private Bundle makeSB(SessionParcel sp , AppStateParcel ap){
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, ap);
        bundle.putParcelable(LearnApp.PCL_SESSION_PARCEL, sp);
        return bundle;
    }

    @Override
    public void makeReport(AppStateParcel ap, SessionParcel sp) {
        Intent intent = new Intent(getApplicationContext(), SendMail.class);
        Log.d("profeplus.id",ap.getUserId());
        intent.putExtra("USER_ID",ap.getUserId());
        intent.putExtra("LESSON_ID",String.valueOf(sp.getSessionId()));
        startService(intent);
    }

    /*
        Enviar reporte de preguntas al final del día
         */
    @Override
    public void scheduleMail(AppStateParcel ap) {
        Intent intent = new Intent(getApplicationContext(), SendMail.class);
        Log.d("profeplus.id",ap.getUserId());
        intent.putExtra("USER_ID",ap.getUserId());
        PendingIntent sendEmail = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,58);
        calendar.set(Calendar.SECOND,0);
        long diffTime = calendar.getTimeInMillis() - now.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                sendEmail);
    }

    /*
    Prototipo par enviar evaluaciones por mail
     */
    @Override
    public void mailEvaluation(AppStateParcel ap, int evalId) {
        Intent intent = new Intent(getApplicationContext(), SendMail.class);
        Log.d("profeplus.id",ap.getUserId());
        intent.putExtra("USER_ID",ap.getUserId());
        intent.putExtra("EVAL_ID",evalId);
        PendingIntent sendEmail = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,58);
        calendar.set(Calendar.SECOND,0);
        long diffTime = calendar.getTimeInMillis() - now.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                sendEmail);
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

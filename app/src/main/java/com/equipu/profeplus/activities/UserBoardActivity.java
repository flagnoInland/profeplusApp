package com.equipu.profeplus.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkBoardService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.fragments.TutorialFragment;
import com.equipu.profeplus.fragments.board.EditProfileFragment;
import com.equipu.profeplus.fragments.board.InfoFragment;
import com.equipu.profeplus.fragments.board.MotivationFragment;
import com.equipu.profeplus.fragments.board.ProducersFragment;
import com.equipu.profeplus.fragments.board.ProfileModeFragment;
import com.equipu.profeplus.fragments.board.SchoolStudentBoardFragment;
import com.equipu.profeplus.fragments.board.ShareFragment;
import com.equipu.profeplus.fragments.board.StudentBoardFragment;
import com.equipu.profeplus.fragments.board.TeacherBoardFragment;
import com.equipu.profeplus.fragments.board.TeacherTutorialsFragment;
import com.equipu.profeplus.fragments.board.WhyUseFragment;
import com.equipu.profeplus.fragments.user.AppModeFragment;
import com.equipu.profeplus.fragments.user.GeneralUserModeFragment;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.equipu.profeplus.models.TaskParcel;
import com.equipu.profeplus.models.Tutorial;
import com.equipu.profeplus.models.UserParcel;
import com.nullwire.trace.ExceptionHandler;

/*
Esta actividad es el punto de entrada para
VER PANTALLA INICIAL DEL PROFESOR
VER PANTALLA INICIAL DEL ESTUDIANTE
VER PANTALLA INICIAL DEL ESCOLAR
TUTORIALES
INFORMACION DEL APP
ACTUALIZAR DATOS DE USAURIO
CAMBIAR MODO EDUCACIÓN SUPERIOR O BÁSICO
CAMBIAR ROL ESTUDIANTE O PROFESOR
 */
public class UserBoardActivity extends AppCompatActivity
        implements IChangeUserListener, IUserBoardListener, ITutorialListener {

    AppStateParcel appStateParcel;
    Tutorial tutorial;
    ModeUserReceiver modeUserReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        ExceptionHandler.register(this,LearnApp.traceURL);
        //View decorView = getWindow().getDecorView();
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);


        // Marcar la sesión como abierta
        setContentView(R.layout.activity_user_board);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SharedPreferences settings = getSharedPreferences("userSession", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("ON_BOARD", true);
        editor.commit();

        appStateParcel = getIntent().getParcelableExtra(LearnApp.PCL_APP_STATE);
        //updateUserMode();
        loadBoard();

    }


    /*
    Cargar el panel del profesor o del estudiante.
    El panel del estudiante escolar difiere del universitario.
     */
    public void loadBoard(){
        if (appStateParcel.getUserType() == AppStateParcel.STUDENT
                && appStateParcel.getAppMode() == AppStateParcel.SCHOOL_MODE) {
            goToSchoolStudentBoard(appStateParcel);
        } else if (appStateParcel.getUserType() == AppStateParcel.STUDENT
                && appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE){
            goToStudentBoard(appStateParcel);
        } else if (appStateParcel.getUserType() == AppStateParcel.TEACHER){
            appStateParcel.setEditEvaluation(0);
            goToTeacherBoard(appStateParcel);
        }
    }


    @Override
    public void onBackPressed() {
        int transactionNumber = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("profeplus.tran",String.valueOf(transactionNumber));
        if (transactionNumber == 1) {
            finish();
            //goToLogin(appStateParcel);
        } else {
            if (tutorial != null){
                tutorial.previousPage();
            }
            super.onBackPressed();
        }
    }


    /*
    Inicia el panel del estudiante en modo básico
     */
    public void goToSchoolStudentBoard(AppStateParcel appStateParcel){
        FragmentManager fm = getSupportFragmentManager();
        SchoolStudentBoardFragment fragment = new SchoolStudentBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Inicia el panel estudiante en modo superior
     */
    public void goToStudentBoard(AppStateParcel appStateParcel){
        FragmentManager fm = getSupportFragmentManager();
        StudentBoardFragment fragment = new StudentBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }


    /*
    Inicia el panel del profesor
     */
    public void goToTeacherBoard(AppStateParcel appStateParcel){
        FragmentManager fm = getSupportFragmentManager();
        TeacherBoardFragment fragment = new TeacherBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Inicia las actividades o preguntas disponibles al profesor
     */
    @Override
    public void startTeacherSession(SessionParcel sessionParcel) {
        Intent mIntent = new Intent(this, TeacherJobActivity.class);
        mIntent.putExtra("SessionParcel",sessionParcel);
        mIntent.putExtra("AppStateParcel", appStateParcel);
        startActivity(mIntent);
    }

    /*
    Inicia las actividades para marcar respuestas del estudiantre
     */
    @Override
    public void startStudentSession(TaskParcel taskParcel) {
        Intent studentAnswer = new Intent(getApplicationContext(), StudentJobActivity.class);
        studentAnswer.putExtra("TaskParcel",taskParcel);
        studentAnswer.putExtra("AppStateParcel", appStateParcel);
        startActivity(studentAnswer);
    }

    /*
    Actualizar el perfil del usuario
     */
    @Override
    public void updateProfile(AppStateParcel ap, UserParcel up) {
        appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        bundle.putParcelable(LearnApp.PCL_USER_PARCEL, up);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }


    /*
    Muestra los manuales
     */
    @Override
    public void goToTutorial(AppStateParcel appStateParcel) {
        FragmentManager fm = getSupportFragmentManager();
        TeacherTutorialsFragment fragment = new TeacherTutorialsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Manual de estudiante
     */
    @Override
    public void readStudentManual() {
        String[] content = getResources().getStringArray(R.array.manual_student_content);
        String[] titles = getResources().getStringArray(R.array.manual_student_titles);
        TypedArray images;
        if (appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE){
            images = getResources().obtainTypedArray(R.array.images_manual_uni);
        } else {
            images = getResources().obtainTypedArray(R.array.images_manual_school);
        }
        tutorial = new Tutorial(content, titles, images);
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Protocolo para el profesor
     */
    @Override
    public void readProtocol() {
        String[] content = getResources().getStringArray(R.array.content_protocol_main);
        String[] titles = getResources().getStringArray(R.array.title_protocol_main);
        TypedArray images = getResources().obtainTypedArray(R.array.images_empty);
        tutorial = new Tutorial(content, titles, images);
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
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
                .addToBackStack(null)
                .commit();
    }

    /*
    Manual para banco de preguntas
     */
    @Override
    public void readAskQuestion() {
        String[] content = getResources().getStringArray(R.array.content_databank);
        String[] titles = getResources().getStringArray(R.array.title_databank);
        TypedArray images = getResources().obtainTypedArray(R.array.images_empty);
        tutorial = new Tutorial(content, titles, images);
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Muestra una nueva página del manual
     */
    @Override
    public void nextPage(Tutorial tutorial) {
        tutorial.nextPage();
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
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
    Regresar al inicio de sesión
     */
    @Override
    public void goToLogin(AppStateParcel ap) {
        appStateParcel = ap;
        /*
        SharedPreferences settings = getSharedPreferences("userSession", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("ON_BOARD", false);
        editor.putString("PASSWORD", "");
        */
        Intent home = new Intent(getApplicationContext(), StartActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        home.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        startActivity(home);
        finish();
    }

    /*
    Cambiar modo del usuario
     */
    @Override
    public void setUserMode(AppStateParcel ap) {
        appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        AppModeFragment fragment = new AppModeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Formulario para editar perfil
     */
    @Override
    public void editProfileMode(AppStateParcel ap) {
        appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        ProfileModeFragment fragment = new ProfileModeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Recargar los paneles del usuario
     */
    @Override
    public void reloadUserBoard(AppStateParcel ap) {
        appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        if (appStateParcel.getUserType() == AppStateParcel.STUDENT
                && appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE) {
            goToStudentBoard(appStateParcel);
        } else if (appStateParcel.getUserType() == AppStateParcel.STUDENT
                && appStateParcel.getAppMode() == AppStateParcel.SCHOOL_MODE) {
            goToSchoolStudentBoard(appStateParcel);
        } else {
            goToTeacherBoard(appStateParcel);
        }
    }

    /*
    Ver modos disponibles al usuario
     */
    @Override
    public void startUserMode(AppStateParcel ap) {
        this.appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        GeneralUserModeFragment fragment = new GeneralUserModeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void startHome(AppStateParcel appStateParcel) {
        // Do nothing
    }

    @Override
    public void newSignUp(AppStateParcel appStateParcel) {
        // Do nothing
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

    /*
    Mostrar información sobre becas
     */
    @Override
    public void goToBecas() {
        FragmentManager fm = getSupportFragmentManager();
        //BecasFragment fragment = new BecasFragment();
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Mostra información sobre equipu
     */
    @Override
    public void readEmprenderInfo() {
        String[] content = getResources().getStringArray(R.array.emprender);
        String[] titles = getResources().getStringArray(R.array.title_emprender);
        TypedArray images = getResources().obtainTypedArray(R.array.images_empty);
        tutorial = new Tutorial(content, titles, images);
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Info sobre el comite
     */
    @Override
    public void readComiteInfo() {
        String[] content = getResources().getStringArray(R.array.comite);
        String[] titles = getResources().getStringArray(R.array.title_comite);
        TypedArray images = getResources().obtainTypedArray(R.array.images_empty);
        tutorial = new Tutorial(content, titles, images);
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Info sobre los creadores
     */
    @Override
    public void readCreatorInfo() {
        String[] content = getResources().getStringArray(R.array.motiv);
        String[] titles = getResources().getStringArray(R.array.title_motiv);
        TypedArray images = getResources().obtainTypedArray(R.array.images_empty);
        tutorial = new Tutorial(content, titles, images);
        FragmentManager fm = getSupportFragmentManager();
        TutorialFragment fragment = new TutorialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Agrdecimientos
     */
    @Override
    public void readThankInfo() {

    }

    /*
    Objetivos del proyecto
     */
    @Override
    public void readMotivInfo() {
        FragmentManager fm = getSupportFragmentManager();
        MotivationFragment fragment = new MotivationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
    Porque usar el app
     */
    @Override
    public void readWhyInfo() {
        FragmentManager fm = getSupportFragmentManager();
        //BecasFragment fragment = new BecasFragment();
        WhyUseFragment fragment = new WhyUseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void readProducersInfo() {
        FragmentManager fm = getSupportFragmentManager();
        //BecasFragment fragment = new BecasFragment();
        ProducersFragment fragment = new ProducersFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
        Promover el app
         */
    @Override
    public void goToShare() {
        FragmentManager fm = getSupportFragmentManager();
        ShareFragment fragment = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

    /*
        Iniciar servicio para cambiar modo de usuario
         */
    public void updateUserMode(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            Intent intent = new Intent(UserBoardActivity.this, NetworkBoardService.class);
            intent.putExtra(NetworkBoardService.SERVICE, NetworkBoardService.SERVICE_CHANGE_MODE);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            startService(intent);
        } else {
            dialogLostConnection();
        }
    }


    /*
    Capturar estado del cambio de usuario
     */
    public class ModeUserReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkBoardService.STATUS,0);
            if (status == UserController.CONNECTION) {
                dialogLostConnection();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkBoardService.ACTION_CHANGE_MODE);
        modeUserReceiver = new ModeUserReceiver();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(UserBoardActivity.this);
        localBroadcastManager.registerReceiver(modeUserReceiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(UserBoardActivity.this);
        localBroadcastManager.unregisterReceiver(modeUserReceiver);
    }


}

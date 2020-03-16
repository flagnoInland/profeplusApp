package com.equipu.profeplus.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkStartService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.dialogs.ConfirmOneButtonSupportDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.fragments.user.AppModeFragment;
import com.equipu.profeplus.fragments.user.ConcursoFragment;
import com.equipu.profeplus.fragments.user.GeneralUserModeFragment;
import com.equipu.profeplus.fragments.user.HomeFragment;
import com.equipu.profeplus.fragments.user.LoginFragment;
import com.equipu.profeplus.fragments.user.RecoverFragment;
import com.equipu.profeplus.fragments.user.StepFourSignUpFragment;
import com.equipu.profeplus.fragments.user.StepOneSchoolSignUpFragment;
import com.equipu.profeplus.fragments.user.StepOneSignUpFragment;
import com.equipu.profeplus.fragments.user.StepThreeSignUpFragment;
import com.equipu.profeplus.fragments.user.StepTwoSchoolSignUpFragment;
import com.equipu.profeplus.fragments.user.StepTwoSignUpFragment;
import com.equipu.profeplus.fragments.user.UniUserModeFragment;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;
import com.nullwire.trace.ExceptionHandler;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;

/*
Esta actividad es el punto de entrada para diferentes tareas
REGISTRAR PROFESOR : 3 PASOS
REGISTRAR ESTUDIANTE : 4 PASOS
REGISTRAR ESCOLAR : 2 PASOS
INICIAR SESIÓN
RECUPERAR CONTRASEÑA
ELEGIR MODO : EDUCACIÓN BASICA, EDUCACIÓN SUPERIOR
ELEGIR ROL : ESTUDIANTE, PROFESOR
 */
public class StartActivity extends AppCompatActivity implements IStartListener,
        IChangeUserListener, ConfirmOneButtonSupportDialog.OnFireEventOneButtonDialogListener {

    AppStateParcel appStateParcel;
    UserParcel userParcel;
    LoginReceiver loginReceiver;
    NewUserReceiver newUserReceiver;

    @BindView(R.id.progress_bar)
    LinearLayout progressBar;


    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        //outState.putParcelable(LearnApp.PCL_USER_PARCEL, userParcel);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //appStateParcel = savedInstanceState.getParcelable(LearnApp.PCL_APP_STATE);
        //userParcel = savedInstanceState.getParcelable(LearnApp.PCL_USER_PARCEL);
    }
    */


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
        setContentView(R.layout.activity_start);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TextView txtVersion = findViewById(R.id.version);
        txtVersion.setText(LearnApp.getAppVersion());

        // Recuperar datos del usuario
        SharedPreferences settings = getSharedPreferences("userSession", 0);
        String username = settings.getString("USERNAME", "");
        String password = settings.getString("PASSWORD", "");
        boolean userMe = !username.equals("")&&!password.equals("");
        boolean onBoard = settings.getBoolean("ON_BOARD",false);
        Log.d("profeplus.frag", String.valueOf(userMe));

        userParcel = new UserParcel();
        Set<String> names = settings.getStringSet("EMAILS", null);

        appStateParcel = getIntent().getParcelableExtra(LearnApp.PCL_APP_STATE);

        if (appStateParcel != null ){
            appStateParcel.setNewInstall(0);
            if (appStateParcel.getUserId().equals("0")){
                appStateParcel.setNewInstall(1);
                startAppMode(appStateParcel);//Para nuevas instalaciones
            } else if ( !appStateParcel.getUserId().equals("0") &&
                    appStateParcel.getAppMode() == AppStateParcel.SCHOOL_MODE &&
                    appStateParcel.getUserType() == AppStateParcel.STUDENT) {
                //startLogin(appStateParcel);
                startHome(appStateParcel);//Para usuarios que cerraron sesión
            } else if ( !appStateParcel.getUserId().equals("0") &&
                    appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE &&
                    appStateParcel.getUserType() == AppStateParcel.TEACHER ) {
                //startLogin(appStateParcel);
                startHome(appStateParcel);//Para usuarios que cerraron sesión
            } else if ( !appStateParcel.getUserId().equals("0") &&
                    appStateParcel.getAppMode() == AppStateParcel.SCHOOL_MODE &&
                    appStateParcel.getUserType() == AppStateParcel.TEACHER ) {
                //startLogin(appStateParcel);
                startHome(appStateParcel);//Para usuarios que cerraron sesión
            } else if ( !appStateParcel.getUserId().equals("0") &&
                    appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE &&
                    appStateParcel.getUserType() == AppStateParcel.STUDENT ){
                //startLogin(appStateParcel);
                startHome(appStateParcel);//Para usuarios que cerraron sesión
            }
        } else if (appStateParcel == null && names != null && !userMe){
            appStateParcel = new AppStateParcel();
            appStateParcel.setNewInstall(0);
            //startLogin(appStateParcel);
            startHome(appStateParcel);
        } else if (appStateParcel == null && names != null
                && userMe){
            Log.d("profeplus.tsct", names.toString());
            Log.d("profeplus.tsct", password);
            userParcel.setMail(username);
            userParcel.setPassword(password);
            appStateParcel = new AppStateParcel();
            appStateParcel.setNewInstall(0);
            startLoginToUserBoard(appStateParcel);//Para usuarios que no cerraron sesión
        } else {
            appStateParcel = new AppStateParcel();
            appStateParcel.setNewInstall(1);
            startAppMode(appStateParcel);// Para otros casos
        }


    }

    @Override
    public void onBackPressed() {
        String fname = "";
        FragmentManager fm = getSupportFragmentManager();
        int transactionNumber = fm.getBackStackEntryCount();
        if (transactionNumber > 0) {
            fname = fm.getBackStackEntryAt(transactionNumber - 1).getName();
        }
        Log.d("profeplus.tsct", String.valueOf(transactionNumber));
        if (transactionNumber == 1) {//Remover contraseña cuando cierra el app
            if (fname.equals("LoginUser")||fname.equals("Home")){
                SharedPreferences settings = getSharedPreferences("userSession", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("ON_BOARD", false);
                editor.putString("PASSWORD", "");
                editor.commit();
            }
            Log.d("profeplus.home","bye");
            finish();
        } else {
            super.onBackPressed();
        }
    }

    /*
    Paso 2 del registro escolar
     */
    @Override
    public void goToStepTwoSchoolSignUp(UserParcel up) {
        this.userParcel = up;
        FragmentManager fm = getSupportFragmentManager();
        StepTwoSchoolSignUpFragment fragment = new StepTwoSchoolSignUpFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("UserParcel", userParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("StepTwoSchoolSignUp")
                .commit();
    }

    /*
    Cambiar a modo escolar
     */
    @Override
    public void startSchoolUserMode(UserParcel up) {
        this.userParcel = up;
        userParcel.setTeacher(appStateParcel.getUserType());
        userParcel.setAppMode(appStateParcel.getAppMode());
        startNewUserTask();
    }

    /*
    Mostrar inicio de sesión
     */
    public void startLoginToUserBoard(AppStateParcel ap){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null){
            Log.d("profeplus.net",networkInfo.toString());
        }
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            Intent intent = new Intent(StartActivity.this, NetworkStartService.class);
            intent.putExtra(NetworkStartService.SERVICE, NetworkStartService.SERVICE_LOGIN);
            intent.putExtra(LearnApp.PCL_USER_PARCEL, userParcel);
            startService(intent);
        } else {
            startLogin(appStateParcel);
            dialogLostConnection();
        }

    }

    /*
    Iniciar el panel profesor o el panel estudiante despues de iniciar sesión.
    Si falla mostrar la pantalla de inicio de sesión.
     */
    public class LoginReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int  status = intent.getIntExtra(NetworkStartService.STATUS,0);
            if (status == UserController.DONE ){
                appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
                appStateParcel.setStartTime(System.currentTimeMillis());
                if (appStateParcel.getUserType() == AppStateParcel.STUDENT) {
                    goToStudentBoard(appStateParcel);
                } else {
                    goToTeacherBoard(appStateParcel);
                }
            } else if (status == UserController.CONNECTION){
                startLogin(appStateParcel);
                dialogLostConnection();
            } else {
                startLogin(appStateParcel);
            }
        }
    }

    /*
    Remover receiver para inicio de sesión y creación de usuario
    cuando la actividad hiberna.
     */
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(StartActivity.this);
        localBroadcastManager.unregisterReceiver(loginReceiver);
        localBroadcastManager.unregisterReceiver(newUserReceiver);


    }

    /*
    Iniciar receiver para inicio de seión y creación de usuario cuando
    la actividad esta en primer plano.
    Filtrar las acciones en los servicios para estos receivers.
     */
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkStartService.ACTION_LOGIN);
        IntentFilter filter1 = new IntentFilter(NetworkStartService.ACTION_NEW_USER);
        loginReceiver = new LoginReceiver();
        newUserReceiver = new NewUserReceiver();
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(StartActivity.this);
        localBroadcastManager.registerReceiver(loginReceiver, filter);
        localBroadcastManager.registerReceiver(newUserReceiver, filter1);
    }


    /*
    Fragmento que muestra los modos disponibles a los usuarios.
    Se aceptan dos modos: superior y básica.
     */
    public void startAppMode(AppStateParcel ap){
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        AppModeFragment fragment = new AppModeFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container, fragment)
                .addToBackStack("AppMode")
                .commit();
    }

    /*
    Fragmento que muestra los roles disponibles a los usuarios.
    Se aceptan dos roles: profesor y estudiante
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
                .addToBackStack("GeneralUserMode")
                .commitAllowingStateLoss();
    }

    /*
    Fragmento que permite iniciar sesión, registrase o cambiar contraseña
     */
    @Override
    public void startHome(AppStateParcel ap) {
        this.appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Home")
                .commitAllowingStateLoss();
    }

    /*
    Fragmento que muestra el inicio de sesión
     */
    @Override
    public void startLogin(AppStateParcel ap) {
        this.appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Login")
                .commit();
    }


    @Override
    public void startSignUp(AppStateParcel ap) {
        if (ap.getNewInstall() == 0){
            startAppMode(ap);
        } else {
            newSignUp(ap);
        }
    }

    /*
    Fragmento que muestra la pantalla de registro del primer paso.
    El modo básico difiere del modo superior.
     */
    @Override
    public void newSignUp(AppStateParcel ap) {
        this.appStateParcel = ap;
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_USER_PARCEL, userParcel);
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        FragmentManager fm = getSupportFragmentManager();
        if (ap.getAppMode()== AppStateParcel.SCHOOL_MODE &&
                ap.getUserType() == AppStateParcel.STUDENT){
            StepOneSchoolSignUpFragment fragment = new StepOneSchoolSignUpFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("StepOneSchoolSignUp")
                    .commit();
        } else {
            StepOneSignUpFragment fragment = new StepOneSignUpFragment();
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fragment_container,fragment)
                    .addToBackStack("StepOneSignUp")
                    .commit();
        }
    }

    /*
        Inicia la pantalla para cambiar contraseña
         */
    @Override
    public void startRecover(AppStateParcel ap) {
        this.appStateParcel = ap;
        FragmentManager fm = getSupportFragmentManager();
        RecoverFragment fragment = new RecoverFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container,fragment)
                .addToBackStack("Recover")
                .commit();
    }


    /*
    Paso 2 del registro en modo superior
     */
    @Override
    public void goToStepTwoSignUp(UserParcel up) {
        this.userParcel = up;
        FragmentManager fm = getSupportFragmentManager();
        StepTwoSignUpFragment fragment = new StepTwoSignUpFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("UserParcel", userParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("StepTwoSignUp")
                .commit();
    }

    /*
    Paso 3 del registro en modo superior
     */
    @Override
    public void goToStepThreeSignUp(UserParcel up) {
        this.userParcel = up;
        FragmentManager fm = getSupportFragmentManager();
        StepThreeSignUpFragment fragment = new StepThreeSignUpFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("UserParcel", userParcel);
        bundle.putParcelable("AppStateParcel", appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("StepThreeSignUp")
                .commit();
    }

    /*
    Cambio a modo profesor
     */
    @Override
    public void startTeacherSession(UserParcel up) {
        this.userParcel = up;
        userParcel.setTeacher(appStateParcel.getUserType());
        userParcel.setAppMode(appStateParcel.getAppMode());
        startNewUserTask();
    }

    /*
    Cuando el modo es superior volver a elegir el rol de profesor o estudiante.
    El rol de profesor termina el registro.
    El rol de estudiante muestra un paso adicional
     */
    @Override
    public void startUniUserMode(UserParcel up) {
        this.userParcel = up;
        FragmentManager fm = getSupportFragmentManager();
        UniUserModeFragment fragment = new UniUserModeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        bundle.putParcelable(LearnApp.PCL_USER_PARCEL, userParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("UniUserMode")
                .commit();
    }

    /*
    Paso 4 del registro en modo superior.
    Sólo visible para estudiantes.
     */
    @Override
    public void goToStepFourSignUp(UserParcel up) {
        this.userParcel = up;
        FragmentManager fm = getSupportFragmentManager();
        StepFourSignUpFragment fragment = new StepFourSignUpFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        bundle.putParcelable(LearnApp.PCL_USER_PARCEL, userParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("StepFourSignUp")
                .commit();
    }


    @Override
    public void goToConcurso(UserParcel up) {
        this.userParcel = up;
        FragmentManager fm = getSupportFragmentManager();
        ConcursoFragment fragment = new ConcursoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        bundle.putParcelable(LearnApp.PCL_USER_PARCEL, userParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack("Concurso")
                .commit();
    }

    /*
        Registra usuario como profesor
         */
    @Override
    public void registerUniTeacher(UserParcel up) {
        this.userParcel = up;
        startNewUserTask();
    }

    /*
    Registra usuario como estudiante
     */
    @Override
    public void registerStudent(UserParcel up) {
        this.userParcel = up;
        startNewUserTask();
    }

    @Override
    public void backToHome(UserParcel userParcel) {

    }

    /*
    Muestra el panel del estudiante
     */
    @Override
    public void goToStudentBoard(AppStateParcel appStateParcel) {
        finish();
        Intent studentSession = new Intent(getApplicationContext(), UserBoardActivity.class);
        studentSession.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP/*|Intent.FLAG_ACTIVITY_NO_HISTORY*/);
        studentSession.putExtra("AppStateParcel", appStateParcel);
        Log.d("profeplus.typ", String.valueOf(appStateParcel.getUserType()));
        startActivity(studentSession);

    }

    /*
    Muestra el panel del profesor
     */
    @Override
    public void goToTeacherBoard(AppStateParcel appStateParcel) {
        finish();
        Intent teacherSession = new Intent(getApplicationContext(), UserBoardActivity.class);
        teacherSession.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP/*|Intent.FLAG_ACTIVITY_NO_HISTORY*/);
        teacherSession.putExtra("AppStateParcel", appStateParcel);
        Log.d("profeplus.typ", String.valueOf(appStateParcel.getUserType()));
        startActivity(teacherSession);
    }


    /*
    Servicio para registrar un nuevo usuario
     */
    public void startNewUserTask(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(StartActivity.this, NetworkStartService.class);
            intent.putExtra(NetworkStartService.SERVICE, NetworkStartService.SERVICE_NEW_USER);
            intent.putExtra(LearnApp.PCL_USER_PARCEL, userParcel);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            startService(intent);
        } else {
            dialogLostConnection();
        }
    }

    /*
    Receiver para capturar el estado del registro.
    En caso de falla muestra un mensaje.
     */
    public class NewUserReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            SharedPreferences settings = getSharedPreferences("userSession", 0);
            Set<String> empty = new HashSet<String>();
            Set<String> names = settings.getStringSet("EMAILS", empty);
            SharedPreferences.Editor editor = settings.edit();
            int  status = intent.getIntExtra(NetworkStartService.STATUS,0);
            if (status == UserController.DONE) {
                names.add(userParcel.getMail().trim());
                editor.putString("USERNAME", userParcel.getMail().trim());
                editor.putStringSet("EMAILS", names);
                editor.commit();
                appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
                Log.d("profeplus.reg",appStateParcel.getUserId());
                if (appStateParcel.getUserType() == AppStateParcel.TEACHER) {
                    FragmentManager fm = getSupportFragmentManager();
                    DialogFragment newFragment = new ConfirmOneButtonSupportDialog();
                    Bundle bd = new Bundle();
                    bd.putString("TITLE", getString(R.string.text_alerta));
                    bd.putString("MESSAGE", getString(R.string.msg_know_real_time_students_learning_process));
                    newFragment.setArguments(bd);
                    newFragment.show(fm, "success");
                } else {
                    startUserBoard();
                }
            } else {
                FragmentManager fm = getSupportFragmentManager();
                DialogFragment fragment = new NotificationOneButtonSupportDialog();;
                Bundle b = new Bundle();
                b.putString("TITLE", getString(R.string.text_error));
                b.putString("MESSAGE", getString(R.string.msg_mail_already_used));
                fragment.setArguments(b);
                fragment.show(fm,"failure");
            }
        }
    }

    /*
    Inicia el panel usuario
     */
    public void startUserBoard(){
        Intent userSession = new Intent(getApplicationContext(), UserBoardActivity.class);
        userSession.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP/*|Intent.FLAG_ACTIVITY_NO_HISTORY*/);
        userSession.putExtra("AppStateParcel", appStateParcel);
        startActivity(userSession);
        finish();
    }

    @Override
    public void reloadUserBoard(AppStateParcel appStateParcel) {
        // Do nothing
    }


    @Override
    public void onFireAcceptEventOneButtonListener() {
        startUserBoard();
    }

    @Override
    public void onFireCancelEventOneButtonListener() {
        startUserBoard();
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

    @Override
    public void onRecoverSuccess(AppStateParcel ap) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        /*
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
                */
    }



}

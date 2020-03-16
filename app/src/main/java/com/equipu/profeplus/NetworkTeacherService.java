package com.equipu.profeplus;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.equipu.profeplus.utils.NetworkFailureException;

/**
 * Created by Herbert Caller on 24/08/2016.
 * Este servicio se encarga de procesar tareas del profesor
 * SERVICE_DISABLE_SESSION : finaliza una pregunta
 * SERVICE_NEW_SESSION : crea una nueva pregunta
 * SERVICE_UPDATE_SESSION : agrega datos auna pregunta
 * SERVICE_GET_DATA : obtiene datos de una pregunta
 * SERVICE_COMPARE_DATA : obtiene resultados del paso 1 y paso 2
 * SERVICE_NORMAL_LESSON : crea una pregunta de 5 alternativas
 * SERVICE_SURVEY_LESSON : crea una pregunta de opinión
 * SERVICE_BANK_LESSON : crea una pregunta con tema
 * SERVICE_UPDATE_FULL_REPORT : agrega datos para el reporte
 * SERVICE_UPDATE_SIMPLE_REPORT : agrega solución a pregunta para el reporte
 */
public class NetworkTeacherService extends IntentService {

    public static final String SERVICE = "Service";
    public static final String STATUS = "Status";
    public static final String TASK = "TeacherTask";
    public static final String NEW_LIST = "NewList";
    public static final String OLD_LIST = "OldList";
    public static final int SERVICE_DISABLE_SESSION = 1;
    public static final String ACTION_DISABLE_SESSION = "BroadcastDisableSession";
    public static final int SERVICE_NEW_SESSION = 2;
    public static final String ACTION_NEW_SESSION = "BroadcastNewSession";
    public static final int SERVICE_UPDATE_SESSION = 3;
    public static final String ACTION_UPDATE_SESSION = "BroadcastUpdateSession";
    public static final int SERVICE_GET_DATA = 4;
    public static final String ACTION_GET_DATA = "BroadcastGetData";
    public static final int SERVICE_COMPARE_DATA = 5;
    public static final String ACTION_COMPARE_DATA = "BroadcastCompareData";
    public static final int SERVICE_NORMAL_LESSON = 6;
    public static final String ACTION_NORMAL_LESSON = "BroadcastNormalLesson";
    public static final int SERVICE_SURVEY_LESSON = 7;
    public static final String ACTION_SURVEY_LESSON = "BroadcastSurveyLesson";
    public static final int SERVICE_BANK_LESSON = 8;
    public static final String ACTION_BANK_LESSON = "BroadcastBankLesson";
    public static final int SERVICE_UPDATE_FULL_REPORT = 9;
    public static final String ACTION_UPDATE_FULL_REPORT = "BroadcastUpdateFullReport";
    public static final int SERVICE_UPDATE_SIMPLE_REPORT = 10;
    public static final String ACTION_UPDATE_SIMPLE_REPORT = "BroadcastUpdateSimpleReport";

    LocalBroadcastManager localBroadcastManager;


    public NetworkTeacherService() {
        super("NetworkTeacherService");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int service = intent.getIntExtra(SERVICE,0);
        try {
            if (service == SERVICE_DISABLE_SESSION){
                disableSessionService(intent);
            } else if (service == SERVICE_NEW_SESSION){
                newSessionService(intent , ACTION_NEW_SESSION);
            } else if (service == SERVICE_BANK_LESSON){
                newSessionService(intent , ACTION_BANK_LESSON);
            } else if (service == SERVICE_SURVEY_LESSON){
                newSessionService(intent , ACTION_SURVEY_LESSON);
            } else if (service == SERVICE_NORMAL_LESSON){
                newSessionService(intent , ACTION_NORMAL_LESSON);
            } else if (service == SERVICE_UPDATE_SESSION){
                updateSessionService(intent, ACTION_UPDATE_SESSION);
            } else if (service == SERVICE_UPDATE_FULL_REPORT){
                updateSessionService(intent, ACTION_UPDATE_FULL_REPORT);
            } else if (service == SERVICE_UPDATE_SIMPLE_REPORT){
                updateSessionService(intent, ACTION_UPDATE_SIMPLE_REPORT);
            } else if (service == SERVICE_GET_DATA){
                getDataService(intent);
            } else if (service == SERVICE_COMPARE_DATA){
                compareDataService(intent);
            }
        } catch (NetworkFailureException e) {
            e.printStackTrace();
            Intent broadcast = new Intent();
            broadcast.putExtra(STATUS, TeacherLessonController.CONNECTION);
            localBroadcastManager.sendBroadcast(broadcast);
        }
    }

    private void compareDataService(Intent intent) throws NetworkFailureException{
        if (checkConnectivity()) {
            SessionParcel sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
            AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
            TeacherLessonController teacherLessonController = new TeacherLessonController();
            teacherLessonController.compareDataPoints(sessionParcel, appStateParcel.getToken());
            Intent broadcast = new Intent();
            broadcast.setAction(ACTION_COMPARE_DATA);
            if (teacherLessonController.getStatus() == TeacherLessonController.DONE) {
                broadcast.putExtra(STATUS, teacherLessonController.getStatus());
                broadcast.putExtra(NEW_LIST, teacherLessonController.getAnswerList());
                broadcast.putExtra(OLD_LIST, teacherLessonController.getPreviousAnswerList());
            } else {
                broadcast.putExtra(STATUS, teacherLessonController.getStatus());
            }
            localBroadcastManager.sendBroadcast(broadcast);
        } else {
            throw new NetworkFailureException();
        }
    }

    private void getDataService(Intent intent) throws NetworkFailureException {
        if (checkConnectivity()) {
            SessionParcel sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
            AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
            TeacherLessonController teacherLessonController = new TeacherLessonController();
            teacherLessonController.getDataPoints(sessionParcel, appStateParcel.getToken());
            Intent broadcast = new Intent();
            broadcast.setAction(ACTION_GET_DATA);
            if (teacherLessonController.getStatus() == TeacherLessonController.DONE) {
                broadcast.putExtra(STATUS, teacherLessonController.getStatus());
                broadcast.putExtra(NEW_LIST, teacherLessonController.getAnswerList());
            } else {
                broadcast.putExtra(STATUS, teacherLessonController.getStatus());
            }
            localBroadcastManager.sendBroadcast(broadcast);
        } else {
            throw new NetworkFailureException();
        }
    }

    private void newSessionService(Intent intent , String action) throws NetworkFailureException {
        if (checkConnectivity()) {
            SessionParcel sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
            AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
            TeacherLessonController teacherLessonController = new TeacherLessonController();
            teacherLessonController.createNewSession(sessionParcel, appStateParcel);
            Intent broadcast = new Intent();
            broadcast.setAction(action);
            if (teacherLessonController.getStatus() == TeacherLessonController.DONE) {
                broadcast.putExtra(STATUS, teacherLessonController.getStatus());
                broadcast.putExtra(LearnApp.PCL_SESSION_PARCEL,
                        teacherLessonController.getSessionParcel());
            } else {
                broadcast.putExtra(STATUS, teacherLessonController.getStatus());
            }
            localBroadcastManager.sendBroadcast(broadcast);
        } else {
            throw new NetworkFailureException();
        }
    }

    private void updateSessionService(Intent intent, String action) throws NetworkFailureException {
        if (checkConnectivity()) {
            SessionParcel sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
            AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
            TeacherLessonController teacherLessonController = new TeacherLessonController();
            teacherLessonController.updateSession(sessionParcel, appStateParcel.getToken());
            Intent broadcast = new Intent();
            broadcast.setAction(action);
            broadcast.putExtra(STATUS, teacherLessonController.getStatus());
            localBroadcastManager.sendBroadcast(broadcast);
        } else {
            throw new NetworkFailureException();
        }
    }

    private void disableSessionService(Intent intent) throws NetworkFailureException {
        if (checkConnectivity()) {
            SessionParcel sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
            AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
            int task = intent.getIntExtra(TASK,0);
            TeacherLessonController teacherLessonController = new TeacherLessonController();
            teacherLessonController.disableSession(sessionParcel.getSessionId(),
                    appStateParcel.getToken());
            Intent broadcast = new Intent();
            broadcast.setAction(ACTION_DISABLE_SESSION);
            broadcast.putExtra(STATUS, teacherLessonController.getStatus());
            broadcast.putExtra(TASK, task);
            localBroadcastManager.sendBroadcast(broadcast);
        } else {
            throw new NetworkFailureException();
        }
    }

    private boolean checkConnectivity(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            return true;
        }
        return false;
    }


}

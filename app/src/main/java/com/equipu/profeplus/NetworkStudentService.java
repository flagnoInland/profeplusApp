package com.equipu.profeplus;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.equipu.profeplus.controllers.StudentLessonController;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;
import com.equipu.profeplus.models.UserParcel;

/**
 * Created by Herbert Caller on 24/08/2016.
 * Este servicio se encarga de procesar tareas del estudiante
 * SERVICE_REGISTER_SESSION : acceso a preguntas
 * SERVICE_ANSWER_QUESTION : responder a preguntas
 * SERVICE_GUEST_LOGIN: cambiar de estudiante
 */
public class NetworkStudentService extends IntentService {

    public static final String SERVICE = "Service";
    public static final String STATUS = "Status";
    public static final String ANSWER = "Answer";
    public static final int SERVICE_REGISTER_SESSION = 1;
    public static final String ACTION_REGISTER_SESSION = "BroadcastRegisterSession2";
    public static final int SERVICE_ANSWER_QUESTION = 2;
    public static final String ACTION_ANSWER_QUESTION = "BroadcastAnswerQuestion";
    public static final int SERVICE_GUEST_LOGIN = 3;
    public static final String ACTION_GUEST_LOGIN = "BroadcastGuestLogin";
    public static final int SERVICE_GUEST_SCHOOL_LOGIN = 4;
    public static final String ACTION_GUEST_SCHOOL_LOGIN = "BroadcastGuestSchoolLogin";

    public NetworkStudentService() {
        super("NetworkStudentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int service = intent.getIntExtra(SERVICE,0);
        if (service == SERVICE_REGISTER_SESSION){
            registerSessionService(intent);
        }
        if (service == SERVICE_ANSWER_QUESTION){
            answerQuestionService(intent);
        }
        if (service == SERVICE_GUEST_LOGIN){
            guestLoginService(intent);
        }
        if (service == SERVICE_GUEST_SCHOOL_LOGIN){
            guestSchoolLoginService(intent);
        }
    }

    private void guestSchoolLoginService(Intent intent){
        UserParcel userParcel = intent.getParcelableExtra(LearnApp.PCL_USER_PARCEL);
        TaskParcel taskParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
        AppStateParcel appStateParcel0 = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
        UserController userController = new UserController();
        StudentLessonController studentLessonController = new StudentLessonController();
        userController.guestSchoolLogin(userParcel);
        if (userController.getStatus()==UserController.DONE){
            AppStateParcel appStateParcel = userController.getAppStateParcel();
            appStateParcel.setOwnerId(appStateParcel0.getOwnerId());
            studentLessonController.getLessonId(taskParcel, appStateParcel);
        }
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_GUEST_SCHOOL_LOGIN);
        if (studentLessonController.getStatus() == StudentLessonController.DONE) {
            broadcast.putExtra(STATUS, studentLessonController.getStatus());
            broadcast.putExtra(LearnApp.PCL_TASK_PARCEL, studentLessonController.getTaskParcel());
            broadcast.putExtra(LearnApp.PCL_APP_STATE, userController.getAppStateParcel());
        } else {
            broadcast.putExtra(STATUS, studentLessonController.getStatus());
        }
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }

    private void guestLoginService(Intent intent) {
        UserParcel userParcel = intent.getParcelableExtra(LearnApp.PCL_USER_PARCEL);
        TaskParcel taskParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
        AppStateParcel appStateParcel0 = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
        UserController userController = new UserController();
        StudentLessonController studentLessonController = new StudentLessonController();
        userController.loginUser(userParcel);
        if (userController.getStatus()==UserController.DONE){
            AppStateParcel appStateParcel = userController.getAppStateParcel();
            appStateParcel.setOwnerId(appStateParcel0.getOwnerId());
            studentLessonController.getLessonId(taskParcel, appStateParcel);
        }
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_GUEST_LOGIN);
        if (studentLessonController.getStatus() == StudentLessonController.DONE) {
            broadcast.putExtra(STATUS, studentLessonController.getStatus());
            broadcast.putExtra(LearnApp.PCL_TASK_PARCEL, studentLessonController.getTaskParcel());
            broadcast.putExtra(LearnApp.PCL_APP_STATE, userController.getAppStateParcel());
        } else {
            broadcast.putExtra(STATUS, studentLessonController.getStatus());
        }
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }


    private void answerQuestionService(Intent intent) {
        TaskParcel taskParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
        AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
        String answer = intent.getStringExtra(ANSWER);
        StudentLessonController studentLessonController = new StudentLessonController();
        studentLessonController.answerQuestion(taskParcel, appStateParcel, answer);
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_ANSWER_QUESTION);
        broadcast.putExtra(ANSWER, answer);
        broadcast.putExtra(STATUS, studentLessonController.getStatus());
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }


    private void registerSessionService(Intent intent) {
        TaskParcel taskParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
        AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
        StudentLessonController studentLessonController = new StudentLessonController();
        studentLessonController.getLessonId(taskParcel, appStateParcel);
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_REGISTER_SESSION);
        if (studentLessonController.getStatus() == StudentLessonController.DONE) {
            broadcast.putExtra(STATUS, studentLessonController.getStatus());
            broadcast.putExtra(LearnApp.PCL_TASK_PARCEL, studentLessonController.getTaskParcel());
        } else {
            broadcast.putExtra(STATUS, studentLessonController.getStatus());
        }
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }

}

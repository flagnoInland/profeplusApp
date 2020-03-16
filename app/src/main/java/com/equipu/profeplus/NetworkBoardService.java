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
 * Este servicio se encarga de procesar
 * SERVICE_CHANGE_MODE : cambio de modo,
 * SERVICE_EDIT_PROFILE : actualización del perfil,
 * SERVICE_GET_USER : obtener datos del usuario,
 * SERVICE_REGISTER_SESSION : acceso de estudiantes a preguntas.
 */
public class NetworkBoardService extends IntentService {

    public static final int SERVICE_CHANGE_MODE = 1;
    public static final String ACTION_CHANGE_MODE = "BroadcastChangeMode";
    public static final int SERVICE_EDIT_PROFILE = 2;
    public static final String ACTION_EDIT_PROFILE = "BroadcastEditProfile";
    public static final int SERVICE_GET_USER = 3;
    public static final String ACTION_GET_USER = "BroadcastGetUser";
    public static final int SERVICE_REGISTER_SESSION = 4;
    public static final String ACTION_REGISTER_SESSION = "BroadcastRegisterSession";
    public static final String SERVICE = "Service";
    public static final String STATUS = "Status";

    public NetworkBoardService() {
        super("NetworkBoardService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int service = intent.getIntExtra(SERVICE,0);
        if (service == SERVICE_CHANGE_MODE){
            changeModeService(intent);
        }
        if (service == SERVICE_EDIT_PROFILE){
            editProfileService(intent);
        }
        if (service == SERVICE_GET_USER){
            getUserService(intent);
        }
        if (service == SERVICE_REGISTER_SESSION){
            registerSessionService(intent);
        }
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

    private void getUserService(Intent intent) {
        AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
        UserController userController = new UserController();
        userController.GetUserData(appStateParcel.getToken());
        userController.GetUserData(appStateParcel.getUserId());
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_GET_USER);
        if (userController.getStatus() == UserController.DONE) {
            broadcast.putExtra(STATUS, userController.getStatus());
            broadcast.putExtra(LearnApp.PCL_USER_PARCEL, userController.getUserParcel());
        } else {
            broadcast.putExtra(STATUS, userController.getStatus());
        }
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }

    private void editProfileService(Intent intent) {
        UserParcel userParcel = intent.getParcelableExtra(LearnApp.PCL_USER_PARCEL);
        AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
        UserController userController = new UserController();
        userController.updaterUser(appStateParcel, userParcel);
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_EDIT_PROFILE);
        if (userController.getStatus() == UserController.DONE) {
            broadcast.putExtra(STATUS, userController.getStatus());
        } else {
            broadcast.putExtra(STATUS, userController.getStatus());
        }
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }


    private void changeModeService(Intent intent) {
        AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
        UserController userController = new UserController();
        userController.reloadProfile(appStateParcel);
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_CHANGE_MODE);
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }




}

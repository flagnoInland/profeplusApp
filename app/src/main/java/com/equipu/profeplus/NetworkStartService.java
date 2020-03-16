package com.equipu.profeplus;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

/**
 * Created by Herbert Caller on 23/08/2016.
 * Este servicio se encarga de procesar:
 * SERVICE_LOGIN : iniciar sesión
 * SERVICE_NEW_USER : crear un nuevo usuario
 * SERVICE_RECOVER_PASS : cambio de contraseña
 */
public class NetworkStartService extends IntentService {

    public static final int SERVICE_LOGIN = 1;
    public static final int SERVICE_NEW_USER = 2;
    public static final int SERVICE_RECOVER_PASS = 3;
    public static final String SERVICE = "Service";
    public static final String STATUS = "Status";
    public static final String ACTION_LOGIN = "BroadcastLogin";
    public static final String ACTION_NEW_USER = "BroadcastNewUser";
    public static final String ACTION_RECOVER_PASS = "BroadcastRecoverPass";

    public NetworkStartService() {
        super("NetworkStartService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int service = intent.getIntExtra(SERVICE,0);
        if (service == SERVICE_LOGIN){
            loginService(intent);
        }
        if (service == SERVICE_NEW_USER){
            newUserService(intent);
        }
        if (service == SERVICE_RECOVER_PASS){
            recoverPassService(intent);
        }

    }



    private void recoverPassService(Intent intent) {
        UserParcel userParcel = intent.getParcelableExtra(LearnApp.PCL_USER_PARCEL);
        UserController userController = new UserController();
        userController.changePassword(userParcel);
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_RECOVER_PASS);
        if (userController.getStatus() == UserController.DONE) {
            broadcast.putExtra(STATUS, userController.getStatus());
        } else {
            broadcast.putExtra(STATUS, userController.getStatus());
        }
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }


    private void newUserService(Intent intent){
        UserParcel userParcel = intent.getParcelableExtra(LearnApp.PCL_USER_PARCEL);
        AppStateParcel appStateParcel = intent.getParcelableExtra(LearnApp.PCL_APP_STATE);
        UserController userController = new UserController();
        userController.RegisterUser(userParcel, appStateParcel);
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_NEW_USER);
        if (userController.getStatus() == UserController.DONE) {
            broadcast.putExtra(STATUS, userController.getStatus());
            broadcast.putExtra(LearnApp.PCL_APP_STATE, userController.getAppStateParcel());
        } else {
            broadcast.putExtra(STATUS, userController.getStatus());
        }
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }

    private void loginService(Intent intent){
        UserParcel userParcel = intent.getParcelableExtra(LearnApp.PCL_USER_PARCEL);
        Log.d("profeplus.mail",userParcel.getMail());
        UserController userController = new UserController();
        userController.loginUser(userParcel);
        Intent broadcast = new Intent();
        broadcast.setAction(ACTION_LOGIN);
        if (userController.getStatus() == UserController.DONE) {
            broadcast.putExtra(STATUS, userController.getStatus());
            broadcast.putExtra(LearnApp.PCL_APP_STATE, userController.getAppStateParcel());
        } else {
            broadcast.putExtra(STATUS, userController.getStatus());
        }
        LocalBroadcastManager localBroadcastManager =
                LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcast);
    }



}

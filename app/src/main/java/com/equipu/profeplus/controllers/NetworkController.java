package com.equipu.profeplus.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Herbert Caller on 05/08/2016.
 * Este controlador verifica el estado de la red
 * Este servicio no esta disponible en esta versión
 */
public class NetworkController extends BroadcastReceiver {

    public static final int RED = 0;
    public static final int GREEN = 0;

    private int status;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifiManager =
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            boolean wifiEnabled = wifiManager.isWifiEnabled();
            WifiManager.WifiLock wifiLock =
                    wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "LockTag");
            wifiLock.setReferenceCounted(true);
            if (wifiEnabled && !wifiLock.isHeld()) {
                wifiLock.acquire();
            }
            /*
            Toast.makeText(context,
                    context.getString(R.string.msg_connection_success),
                    Toast.LENGTH_LONG).show();
                    */
        } else {
            /*
            wifiManager.setWifiEnabled(true);
            WifiManager.WifiLock wifiLock =
                    wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "LockTag");
            wifiLock.acquire();

            Toast.makeText(context,
                    context.getString(R.string.msg_connection_fails),
                    Toast.LENGTH_LONG).show();
                    */
        }
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

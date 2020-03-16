package com.equipu.profeplus;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StatsTaskService extends Service {

    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        appStateParcel = intent.getParcelableExtra("AppStateParcel");
        sessionParcel = intent.getParcelableExtra("SessionParcel");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        ExitTask mExit = new ExitTask();
        mExit.execute();
        stopSelf();
    }

    private void deleteSession() {
        final String url = String.format("%s/byesession/%d%s%s", LearnApp.baseURL, sessionParcel.getSessionId(), LearnApp.tokenURL, appStateParcel.getToken());
        Log.d("profeplus.url", url);
        URL thisURL = null;
        try {
            thisURL = new URL(url);
            HttpURLConnection thisConnection = null;
            try {
                thisConnection = (HttpURLConnection) thisURL.openConnection();
                thisConnection.setConnectTimeout(LearnApp.TIMEOUT);
                thisConnection.setReadTimeout(LearnApp.READTIMEOUT);
                thisConnection.setRequestProperty("User-Agent", LearnApp.mAgent);
                thisConnection.setRequestMethod("DELETE");
                if (thisConnection.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                    Log.d("profeplus.msg", String.valueOf(thisConnection.getResponseCode()));
                }
                thisConnection.disconnect();
                LearnApp.TASK_DONE = true;
            } catch (IOException e1) {
                LearnApp.TASK_DONE = false;
                Log.d("profeplus.msg", "Failed");
                e1.printStackTrace();
                return;
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }

    public class ExitTask extends AsyncTask<Void, Void, Void> {

        public ExitTask() {
        }

        protected Void doInBackground(Void... m) {
            if (appStateParcel.getExerciseDone()==0) {
                deleteSession();
            }
            return null;
        }

    }




}

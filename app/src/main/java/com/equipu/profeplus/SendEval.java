package com.equipu.profeplus;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.equipu.profeplus.controllers.TeacherLessonController;

/**
 * Created by Herbert Caller on 18/07/2016.
 * Prototipo del servico para enviar el resultados de los exámenes
 */
public class SendEval extends Service {

    TeacherLessonController teacherLessonController;
    String userID;
    int evalID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userID = intent.getStringExtra("USER_ID");
        evalID = intent.getIntExtra("EVAL_ID",0);
        Log.d("profeplus.id",userID);
        MailTask mailTask = new MailTask(userID,evalID);
        mailTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MailTask extends AsyncTask<Void,Void,Void> {

        String userID;
        int evalID;

        public MailTask(String user, int eval){
            this.userID = user;
            this.evalID = eval;
        }
        @Override
        protected Void doInBackground(Void... params) {
            teacherLessonController = new TeacherLessonController();
            teacherLessonController.mailEval(userID,evalID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stopSelf();
        }
    }

}

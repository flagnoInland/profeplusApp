package com.equipu.profeplus;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.equipu.profeplus.controllers.TeacherLessonController;

/**
 * Created by Herbert Caller on 15/07/2016.
 */
public class FinishEval extends Service {

    TeacherLessonController teacherLessonController;
    int userID;
    int lessonID;
    int evalID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userID = intent.getIntExtra("USER_ID",0);
        lessonID = intent.getIntExtra("LESSON_ID",0);
        evalID = intent.getIntExtra("EVAL_ID",0);
        Log.d("profeplus.id", String.valueOf(userID));
        MailTask mailTask = new MailTask();
        mailTask.execute(userID,lessonID,evalID);
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MailTask extends AsyncTask<Integer,Void,Void>{
        @Override
        protected Void doInBackground(Integer... params) {
            teacherLessonController = new TeacherLessonController();
            teacherLessonController.endingEval(params[0],params[1],params[2]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stopSelf();
        }
    }

}

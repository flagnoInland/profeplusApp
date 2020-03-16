package com.equipu.profeplus;

import android.app.IntentService;
import android.content.Intent;

import com.equipu.profeplus.controllers.TeacherLessonController;

/**
 * Created by Herbert Caller on 15/07/2016.
 * Este servicio llama al método para enviar emails
 * Este método se encuentra en el controlador TeacherLessonController
 * Este servicio está conectado con la actividad TeacherJobActivity
 */
public class SendMail extends IntentService {

    TeacherLessonController teacherLessonController;
    String userID;
    String lessonID;

    public SendMail() {
        super("SendMail");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        userID = intent.getStringExtra("USER_ID");
        lessonID = intent.getStringExtra("LESSON_ID");
        teacherLessonController = new TeacherLessonController();
        //teacherLessonController.sendEmail(userID);
        teacherLessonController.makeReport(userID, lessonID);
    }


}

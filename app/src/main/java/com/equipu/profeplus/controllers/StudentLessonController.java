package com.equipu.profeplus.controllers;

import android.util.Log;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.gson_models.Lesson;
import com.equipu.profeplus.gson_models.SchoolLesson;
import com.equipu.profeplus.models.AnswerList;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Herbert Caller on 7/1/2016.
 * Este controlador permite hacer peticiones por HTTP al servidor
 * para obtener datos de una respuesta en formato JSON
 */
public class StudentLessonController extends BaseController {


    AnswerList answerList;
    AnswerList previousAnswerList;
    TaskParcel taskParcel;

    public StudentLessonController() {
        //client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public AnswerList getAnswerList() {
        return answerList;
    }

    public void setAnswerList(AnswerList answerList) {
        this.answerList = answerList;
    }

    public AnswerList getPreviousAnswerList() {
        return previousAnswerList;
    }

    public void setPreviousAnswerList(AnswerList previousAnswerList) {
        this.previousAnswerList = previousAnswerList;
    }

    public TaskParcel getTaskParcel() {
        return taskParcel;
    }

    public void setTaskParcel(TaskParcel taskParcel) {
        this.taskParcel = taskParcel;
    }

    public void getSchoolLessonId(TaskParcel taskParcel, AppStateParcel appStateParcel) {
        setStatus(FAILED);
        final String url = String.format("%s/lesson/%s/%d/%d/%s%s%s",
                LearnApp.baseURL, taskParcel.getAccesscode(), taskParcel.getSession(),
                taskParcel.getCheck(), taskParcel.getLastAnswer(), LearnApp.tokenURL,
                appStateParcel.getToken());
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            setStatus(DONE);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            SchoolLesson schoolLesson = gson.fromJson(jsonString.toString(),SchoolLesson.class);
            taskParcel.setSession(schoolLesson.getId());
            taskParcel.setSubject(schoolLesson.getSubject());
            taskParcel.setExercise(schoolLesson.getExercise());
            taskParcel.setRun(schoolLesson.getRun());
            taskParcel.setSurvey(schoolLesson.getSurvey());
            taskParcel.setQuestionType(schoolLesson.getQuestion_type());
            taskParcel.setQuestionMode(schoolLesson.getQuestion_mode());
            taskParcel.setLastAnswer(schoolLesson.getLast_answer());
            taskParcel.setCheck(schoolLesson.getCheck());
            setTaskParcel(taskParcel);
        } catch (IOException e1) {
            setStatus(FAILED);
            e1.printStackTrace();
        }
    }

    public void getLessonId(TaskParcel taskParcel, AppStateParcel appStateParcel) {
        setStatus(FAILED);
        final String url = String.format("%s/student/%s/%s/lesson/%s/%s/%d%s%s",
                LearnApp.baseURL, appStateParcel.getUserId(), appStateParcel.getOwnerId(),
                taskParcel.getAccesscode(), taskParcel.getLastAnswer(), appStateParcel.getAppMode(),
                LearnApp.tokenURL, appStateParcel.getToken());
        Log.d("profeplus.url", url);
        do {
            try {
                request = new Request.Builder()
                        .url(url)
                        .addHeader("Keep-Alive","timeout=5, max=50")
                        .build();
                response = client.newCall(request).execute();
                inputStream = response.body().byteStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder jsonString = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    jsonString.append(line);
                }
                socketOn = true;
                Log.d("profeplus.code", String.valueOf(response.code()));
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    setStatus(DONE);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Lesson lesson = gson.fromJson(jsonString.toString(), Lesson.class);
                    taskParcel.setSession(lesson.getId());
                    taskParcel.setSubject(lesson.getSubject());
                    taskParcel.setExercise(lesson.getExercise());
                    taskParcel.setRun(lesson.getRun());
                    taskParcel.setSurvey(lesson.getSurvey());
                    taskParcel.setQuestionType(lesson.getQuestion_type());
                    taskParcel.setQuestionMode(lesson.getQuestion_mode());
                    taskParcel.setLastAnswer(lesson.getLast_answer());
                    taskParcel.setCheck(lesson.getCheck());
                    taskParcel.setEvalId(lesson.getEvaluation_id());
                    taskParcel.setDuration(lesson.getDuration());
                    taskParcel.setUpdated(lesson.getUpdated());
                    taskParcel.setEvalAnswers(turnStringIntoArray(lesson.getSolutions()));
                    taskParcel.setLeftTime(prepareFinalTime(lesson));
                    setTaskParcel(taskParcel);
                } else {
                    setStatus(FAILED);
                }
            } catch (SocketException e) {
                socketOn = false;
                e.printStackTrace();
            } catch (IOException e1) {
                setStatus(FAILED);
                e1.printStackTrace();
            }
        } while (!socketOn);
    }

    public void answerQuestion(TaskParcel taskParcel, AppStateParcel appStateParcel, String answer) {
        setStatus(FAILED);
        final String url = String.format("%s/user/%s/lesson/%d/answer/%s%s%s", LearnApp.baseURL,
                appStateParcel.getUserId(), taskParcel.getSession(), answer, LearnApp.tokenURL,
                appStateParcel.getToken());
        Log.d("profeplus.url", url);
        do {
            try {
                request = new Request.Builder()
                        .url(url)
                        .addHeader("Keep-Alive","timeout=5, max=50")
                        .build();
                response = client.newCall(request).execute();
                response.body().string();
                socketOn = true;
                Log.d("profeplus.answer", String.valueOf(response.code()));
                switch (response.code()) {
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        setStatus(NOT_FOUND);
                        break;
                    case HttpURLConnection.HTTP_CREATED:
                        setStatus(DONE);
                        break;
                    case HttpURLConnection.HTTP_OK:
                        setStatus(DONE);
                        break;
                    default:
                        Log.d("HeyResponse", response.message());
                        setStatus(FAILED);
                }
            } catch (SocketException e) {
                socketOn = false;
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } while (!socketOn);

    }

    public JSONObject getJSONsols(TaskParcel tp) {
        JSONObject mSession = new JSONObject();
        try {
            mSession.put("solutions",
                    Arrays.toString(tp.getEvalAnswers()).replaceAll("\\[|\\]",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mSession;
    }

    public void submitSolution(TaskParcel tp, AppStateParcel ap) {
        setStatus(FAILED);
        String url = String.format("%s/student/%s/lesson/%d/evaluation/%d/sols%s%s",
                LearnApp.baseURL, ap.getUserId(),tp.getSession(),tp.getEvalId(),
                LearnApp.tokenURL, ap.getToken());
        Log.d("profeplus.url", url);
        try {
            JSONObject mData = getJSONsols(tp);
            Log.d("profeplus.json", mData.toString());
            body = RequestBody.create(JSON, mData.toString());
            request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            response.body().string();
            Log.d("profeplus.sols",String.valueOf(response.code()));
            Log.d("profeplus.sols",mData.toString());
            switch (response.code()){
                case HttpURLConnection.HTTP_NOT_FOUND :
                    setStatus(NOT_FOUND);
                    break;
                case HttpURLConnection.HTTP_OK :
                    setStatus(DONE);
                    break;
                default:
                    Log.d("HeyResponse",response.message());
                    setStatus(FAILED);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void finishSolution(TaskParcel tp, AppStateParcel ap) {
        setStatus(FAILED);
        String url = String.format("%s/student/%s/lesson/%d/evaluation/%d/finish%s%s",
                        LearnApp.baseURL, ap.getUserId(),tp.getSession(),tp.getEvalId(),
                        LearnApp.tokenURL, ap.getToken());
        Log.d("profeplus.url", url);
        try {
            JSONObject mData = getJSONsols(tp);
            Log.d("profeplus.json", mData.toString());
            body = RequestBody.create(JSON, mData.toString());
            request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            response.body().string();
            Log.d("profeplus.sols",String.valueOf(response.code()));
            Log.d("profeplus.sols",mData.toString());
            switch (response.code()){
                case HttpURLConnection.HTTP_NOT_FOUND :
                    setStatus(NOT_FOUND);
                    break;
                case HttpURLConnection.HTTP_OK :
                    setStatus(DONE);
                    break;
                default:
                    Log.d("HeyResponse",response.message());
                    setStatus(FAILED);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private long prepareFinalTime(Lesson lesson) {
        long diff = -1;
        if (lesson.getQuestion_type() == TaskParcel.Q_EVAL) {
            Calendar now = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = sdf.parse(lesson.getUpdated());
                end.setTime(date);
                Log.d("profeplus.dur", String.valueOf(lesson.getDuration()));
                Log.d("profeplus.end", end.toString());
                end.add(Calendar.MINUTE, lesson.getDuration());
                Log.d("profeplus.end", end.toString());
                Log.d("profeplus.now", now.toString());
                diff = end.getTimeInMillis() - now.getTimeInMillis();
                Log.d("profeplus.time", String.valueOf(diff));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return diff;

    }

    public int[] turnStringIntoArray(String text){
        //text.replace("\\[","");
        //text.replace("\\]","");
        String items[] = text.split(",");
        int[] result = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            result[i] = Integer.parseInt(items[i].trim());
        }

        return result;
    }



}

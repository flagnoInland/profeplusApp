package com.equipu.profeplus.controllers;

import android.util.JsonReader;
import android.util.Log;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.gson_models.FullEval;
import com.equipu.profeplus.gson_models.NewEval;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.Evaluation;
import com.equipu.profeplus.models.EvaluationList;
import com.equipu.profeplus.models.EvaluationParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Arrays;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Herbert Caller on 7/7/2016.
 * Este controlador permite hacer peticiones por HTTP al servidor
 * para obtener datos de una evaluación en formato JSON
 */
public class EvaluationController extends BaseController{


    int evaluationId;
    int evaluationCount;
    int students;
    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;
    EvaluationList evaluationList;
    EvaluationParcel evaluationParcel;

    public EvaluationController(){
    }

    public AppStateParcel getAppStateParcel() {
        return appStateParcel;
    }

    public void setAppStateParcel(AppStateParcel appStateParcel) {
        this.appStateParcel = appStateParcel;
    }

    public int getEvaluationCount() {
        return evaluationCount;
    }

    public void setEvaluationCount(int evaluationCount) {
        this.evaluationCount = evaluationCount;
    }

    public EvaluationParcel getEvaluationParcel() {
        return evaluationParcel;
    }

    public void setEvaluationParcel(EvaluationParcel evaluationParcel) {
        this.evaluationParcel = evaluationParcel;
    }

    public SessionParcel getSessionParcel() {
        return sessionParcel;
    }

    public void setSessionParcel(SessionParcel sessionParcel) {
        this.sessionParcel = sessionParcel;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public EvaluationList getEvaluationList() {
        return evaluationList;
    }

    public void setEvaluationList(EvaluationList evaluationList) {
        this.evaluationList = evaluationList;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getStartDateTime(EvaluationParcel ep){
        return String.format("%s %s:00",ep.getDate(),ep.getStartTime());
    }

    public String getEndDateTime(EvaluationParcel ep){
        return String.format("%s %s:00",ep.getDate(),ep.getEndTime());
    }

    public JSONObject getJSONevaluation(EvaluationParcel ep) {
        JSONObject mSession = new JSONObject();
        try {
            mSession.put("user_id", ep.getTeacherId());
            mSession.put("number_question", ep.getNumberQuestion());
            mSession.put("overall_score", ep.getOverall());
            mSession.put("start_time", getStartDateTime(ep));
            mSession.put("end_time", getEndDateTime(ep));
            mSession.put("date", ep.getDate());
            mSession.put("duration", ep.getDuration());
            mSession.put("course_name", ep.getCourseName());
            mSession.put("speciality", ep.getSpeciality());
            mSession.put("institution", ep.getInstitution());
            mSession.put("exam_title", ep.getHeader());
            mSession.put("materials",
                    Arrays.toString(ep.getMaterials()).replaceAll("\\[|\\]",""));
            mSession.put("answer_keys",
                    Arrays.toString(ep.getAnswerKeys()).replaceAll("\\[|\\]",""));
            mSession.put("answer_weights",
                    Arrays.toString(ep.getAnswerWeights()).replaceAll("\\[|\\]",""));
            mSession.put("status", ep.getStatusEval());
            mSession.put("statusLesson", ep.getStatusLesson());
            mSession.put("lesson_id", ep.getLessonId());
            mSession.put("observations", ep.getObservation());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mSession;
    }

    public void createNewEvaluation(AppStateParcel ap, EvaluationParcel ep) {
        String route = String.format("/user/%s/evaluation/create", ap.getUserId());
        if (ap.getSaveEvaluation() == 1) {
            route = String.format("/user/%s/evaluation/save", ap.getUserId());
        }
        final String url = LearnApp.baseURL + route + LearnApp.tokenURL+ ap.getToken();
        setStatus(FAILED);
        Log.d("profeplus.url", url);
        try {
            JSONObject mData = getJSONevaluation(ep);
            Log.d("profeplus.json", mData.toString());
            body = RequestBody.create(JSON, mData.toString());
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            if (response.code() != HttpURLConnection.HTTP_CREATED) {
                Log.d("profeplus.response", response.message());
                Log.d("profeplus.response", mData.toString());
                setStatus(FAILED);
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder jsonString = new StringBuilder();
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    jsonString.append(line);
                }
                setStatus(DONE);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                NewEval newEval = gson.fromJson(jsonString.toString(),NewEval.class);
                setEvaluationId(newEval.getEvalId());
                setEvaluationCount(newEval.getEvaluations());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void getConnected(AppStateParcel ap, SessionParcel sp) {
        final String url = String.format("%s/evaluation/%d%/lesson/%d%s%s",
                LearnApp.baseURL, sp.getEvaluationId(), sp.getSessionId(), LearnApp.tokenURL,
                        ap.getToken());
        setStatus(FAILED);
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("connected")) {
                    setStudents(reader.nextInt());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            setStatus(DONE);
        } catch (IOException e1) {
            setStatus(FAILED);
            e1.printStackTrace();
        }
    }


    public void getTeachersEvaluations(AppStateParcel ap){
        setStatus(FAILED);
        final String url = String.format("%s/teacher/%s/evaluations%s%s",
                LearnApp.baseURL, ap.getUserId(), LearnApp.tokenURL, ap.getToken());
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jString = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                jString.append(line);
            }
            setStatus(DONE);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            EvaluationList evaluationList = new EvaluationList();
            // entries : id, name, course, speciality, institution
            Evaluation[] evaluations = gson.fromJson(jString.toString(),Evaluation[].class);
            evaluationList.setEvaluations(evaluations);
            setEvaluationList(evaluationList);
        } catch (IOException e1) {
            setStatus(FAILED);
            e1.printStackTrace();
        }
    }


    public void getTeachersActiveEvaluations(AppStateParcel ap){
        setStatus(FAILED);
        final String url = String.format("%s/teacher/%s/active-evaluations%s%s",
                LearnApp.baseURL, ap.getUserId(), LearnApp.tokenURL, ap.getToken());
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jString = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                jString.append(line);
            }
            setStatus(DONE);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            EvaluationList evaluationList = new EvaluationList();
            Evaluation[] evaluations = gson.fromJson(jString.toString(),Evaluation[].class);
            evaluationList.setEvaluations(evaluations);
            setEvaluationList(evaluationList);
        } catch (IOException e1) {
            setStatus(FAILED);
            e1.printStackTrace();
        }
    }


    public void deactivateLessonEvaluation(AppStateParcel ap, int evalId, int lessonId) {
        setStatus(FAILED);
        String url = LearnApp.baseURL +
                String.format("/teacher/%s/lesson/%d/evaluation/%d/finish",
                        ap.getUserId(), lessonId, evalId)
                + LearnApp.tokenURL + ap.getToken();
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("evals")) {
                    ap.setEvaluations(reader.nextInt());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            setAppStateParcel(ap);
            setStatus(DONE);
        } catch (IOException e1) {
            setStatus(FAILED);
            Log.d("profeplus.msg", "Failed");
            e1.printStackTrace();
        }
    }

    public void deactivateEvaluation(AppStateParcel ap, int evalId) {
        setStatus(FAILED);
        String url = LearnApp.baseURL +
                String.format("/teacher/%s/evaluation/%d/deactivate",ap.getUserId(),evalId)
                + LearnApp.tokenURL + ap.getToken();
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("evals")) {
                    ap.setEvaluations(reader.nextInt());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            setAppStateParcel(ap);
            setStatus(DONE);
        } catch (IOException e1) {
            setStatus(FAILED);
            Log.d("profeplus.msg", "Failed");
            e1.printStackTrace();
        }
    }


    public void evaluationComplete(AppStateParcel ap, int evalId){
        setStatus(FAILED);
        String url = LearnApp.baseURL +
                String.format("/teacher/%s/evaluation/%d/full-details",ap.getUserId(),evalId)
                + LearnApp.tokenURL + ap.getToken();
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jString = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                jString.append(line);
            }
            setStatus(DONE);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            FullEval fullEval = gson.fromJson(jString.toString(),FullEval.class);
            EvaluationParcel ep = new EvaluationParcel();
            ep.setEvaluationId(fullEval.getId());
            ep.setTeacherId(fullEval.getUser_id());
            ep.setNumberQuestion(fullEval.getNumber_question());
            ep.setOverall(fullEval.getOverall_score());
            ep.setDuration(fullEval.getDuration());
            ep.setStartTime(fullEval.getStart_time().substring(11,16));
            ep.setEndTime(fullEval.getEnd_time().substring(11,16));
            ep.setDate(fullEval.getDate());
            ep.setCourseName(fullEval.getCourse_name());
            ep.setSpeciality(fullEval.getSpeciality());
            ep.setInstitution(fullEval.getInstitution());
            ep.setHeader(fullEval.getExam_title());
            ep.setMaterials(turnStringIntoArray(fullEval.getMaterials()));
            ep.setAnswerKeys(turnStringIntoArray(fullEval.getAnswer_keys()));
            ep.setAnswerWeights(turnStringIntoDoubles(fullEval.getAnswer_weights()));
            ep.setObservation(fullEval.getObservations());
            setEvaluationParcel(ep);
        } catch (IOException e1) {
            setStatus(FAILED);
            e1.printStackTrace();
        }
    }


    public void updateEvaluation(AppStateParcel ap, EvaluationParcel ep) {
        setStatus(FAILED);
        String url = LearnApp.baseURL +
                String.format("/teacher/%s/evaluation/%d/update",ap.getUserId(),ep.getEvaluationId())
                + LearnApp.tokenURL + ap.getToken();
        Log.d("profeplus.url", url);
        try {
            JSONObject mData = getJSONevaluation(ep);
            Log.d("profeplus.json", mData.toString());
            body = RequestBody.create(JSON, mData.toString());
            request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            if (response.code() != HttpURLConnection.HTTP_OK) {
                Log.d("profeplus.response", response.message());
                Log.d("profeplus.response", mData.toString());
                setStatus(FAILED);
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder jsonString = new StringBuilder();
                String line = "";
                while ((line= bufferedReader.readLine())!= null) {
                    jsonString.append(line);
                }
                setStatus(DONE);
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                NewEval newEval = gson.fromJson(jsonString.toString(),NewEval.class);
                setEvaluationId(newEval.getEvalId());
                setEvaluationCount(newEval.getEvaluations());
            }
        } catch (IOException e1) {
            setStatus(FAILED);
            e1.printStackTrace();
        }
    }


    public double[] turnStringIntoDoubles(String text){
        text.replace("\\[","");
        text.replace("\\]","");
        String items[] = text.split(",");
        double[] result = new double[items.length];

        for (int i = 0; i < items.length; i++) {
            result[i] = Double.parseDouble(items[i].trim());
        }

        return result;
    }

    public int[] turnStringIntoArray(String text){
        text.replace("\\[","");
        text.replace("\\]","");
        String items[] = text.split(",");
        int[] result = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            result[i] = Integer.parseInt(items[i].trim());
        }

        return result;
    }


}

package com.equipu.profeplus.controllers;

import android.util.Log;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.gson_models.CompareData;
import com.equipu.profeplus.gson_models.DataPoint;
import com.equipu.profeplus.gson_models.NewSession;
import com.equipu.profeplus.models.AnswerList;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.util.Locale;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Herbert Caller on 7/4/2016.
 * Este controlador permite hacer peticiones por HTTP al servidor
 * para obtener datos de una pregunta en formato JSON
 */
public class TeacherLessonController extends BaseController {


    AppStateParcel appStateParcel;
    SessionParcel sessionParcel;
    AnswerList answerList;
    AnswerList previousAnswerList;


    public TeacherLessonController() {
        //client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public AppStateParcel getAppStateParcel() {
        return appStateParcel;
    }

    public void setAppStateParcel(AppStateParcel appStateParcel) {
        this.appStateParcel = appStateParcel;
    }

    public SessionParcel getSessionParcel() {
        return sessionParcel;
    }

    public void setSessionParcel(SessionParcel sessionParcel) {
        this.sessionParcel = sessionParcel;
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


    // Json objects

    public JSONObject getJSONsession(SessionParcel sessionParcel) {
        JSONObject mSession = new JSONObject();
        try {
            mSession.put("course_id", sessionParcel.getCourseId());
            mSession.put("evaluation_id", sessionParcel.getEvaluationId());
            mSession.put("accesscode", sessionParcel.getAccesscode());
            mSession.put("subject", sessionParcel.getSubject());
            mSession.put("exercise", sessionParcel.getExercise());
            mSession.put("app_mode", sessionParcel.getAppMode());
            mSession.put("ans1", 0);
            mSession.put("ans2", 0);
            mSession.put("ans3", 0);
            mSession.put("ans4", 0);
            mSession.put("ans5", 0);
            mSession.put("ans_yes", 0);
            mSession.put("ans_no", 0);
            mSession.put("ans_na", 0);
            mSession.put("ansnn", 0);
            mSession.put("run", sessionParcel.getRun());
            mSession.put("inlesson", 0);
            mSession.put("inactive", sessionParcel.getInactive());
            mSession.put("survey", sessionParcel.getSurvey());
            mSession.put("question_type", sessionParcel.getQuestionType());
            mSession.put("question_mode", sessionParcel.getQuestionMode());
            mSession.put("level", sessionParcel.getLevel());
            mSession.put("grade", sessionParcel.getGrade());
            mSession.put("observation", sessionParcel.getComment());
            mSession.put("classroom", sessionParcel.getClassroom());
            mSession.put("institution", sessionParcel.getInstitution());
            mSession.put("speciality", sessionParcel.getSpeciality());
            mSession.put("course_name", sessionParcel.getCourseName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mSession;
    }

    public JSONObject getJSONupdatesession(SessionParcel sessionParcel) {
        JSONObject mSession = new JSONObject();
        try {
            mSession.put("course_id", sessionParcel.getCourseId());
            mSession.put("evaluation_id", sessionParcel.getEvaluationId());
            mSession.put("accesscode", sessionParcel.getAccesscode());
            mSession.put("subject", sessionParcel.getSubject());
            mSession.put("exercise", sessionParcel.getExercise());
            mSession.put("run", sessionParcel.getRun());
            mSession.put("survey", sessionParcel.getSurvey());
            mSession.put("question_type", sessionParcel.getQuestionType());
            mSession.put("question_mode", sessionParcel.getQuestionMode());
            mSession.put("level", sessionParcel.getLevel());
            mSession.put("grade", sessionParcel.getGrade());
            mSession.put("observation", sessionParcel.getComment());
            mSession.put("classroom", sessionParcel.getClassroom());
            mSession.put("institution", sessionParcel.getInstitution());
            mSession.put("speciality", sessionParcel.getSpeciality());
            mSession.put("course_name", sessionParcel.getCourseName());
            mSession.put("answer_keys", sessionParcel.getAnswerKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mSession;
    }

    // Http requests

    public void createNewSession(SessionParcel sessionParcel, AppStateParcel appStateParcel) {
        String url = String.format("%s/lesson/fix-032017/create/%s%s%s",LearnApp.baseURL,
                appStateParcel.getUserId(),LearnApp.tokenURL,appStateParcel.getToken());
        setStatus(FAILED);
        Log.d("profeplus.url", url);
        do {
            try {
                JSONObject mData = getJSONsession(sessionParcel);
                Log.d("profeplus.json", mData.toString());
                body = RequestBody.create(JSON, mData.toString());
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Keep-Alive","timeout=5, max=50")
                        .build();
                response = client.newCall(request).execute();
                inputStream = response.body().byteStream();
                socketOn = true;
                if (response.code() != HttpURLConnection.HTTP_CREATED) {
                    Log.d("profeplus.response", String.valueOf(response.code()));
                    Log.d("profeplus.response", response.message());
                    Log.d("profeplus.response", mData.toString());
                    setStatus(FAILED);
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder jsonString = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        jsonString.append(line);
                    }
                    setStatus(DONE);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    NewSession newSession = gson.fromJson(jsonString.toString(), NewSession.class);
                    sessionParcel.setSessionId(newSession.getId());
                    sessionParcel.setAccesscode(newSession.getAccesscode());
                    setSessionParcel(sessionParcel);
                }
            } catch (SocketException e) {
                socketOn = false;
                e.printStackTrace();
            } catch (IOException e1) {
                setStatus(CONNECTION);
                e1.printStackTrace();
            }
        } while ( !socketOn );
    }


    public void updateSession(SessionParcel sessionParcel, String token) {
        String url = String.format("%s/lesson/update/%d%s%s", LearnApp.baseURL,
                sessionParcel.getSessionId(), LearnApp.tokenURL, token);
        Log.d("profeplus.url", url);
        setStatus(FAILED);
        do {
            try {
                JSONObject mData = getJSONupdatesession(sessionParcel);
                Log.d("profeplus.json", mData.toString());
                body = RequestBody.create(JSON, mData.toString());
                request = new Request.Builder()
                        .url(url)
                        .put(body)
                        .addHeader("Keep-Alive","timeout=5, max=50")
                        .build();
                response = client.newCall(request).execute();
                response.body().string();
                socketOn = true;
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Log.d("profeplus.response", response.message());
                    Log.d("profeplus.json", mData.toString());
                    setStatus(FAILED);
                } else {
                    setStatus(DONE);
                }
            } catch (SocketException e) {
                socketOn = false;
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } while (!socketOn);
    }

    public void disableSession(int lessonid, String token) {
        setStatus(FAILED);
        String url = String.format("%s/lesson/disable/%d%s%s", LearnApp.baseURL,
                lessonid, LearnApp.tokenURL, token);
        Log.d("profeplus.url", url);
        do {
            try {
                request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("Keep-Alive","timeout=5, max=50")
                        .build();
                response = client.newCall(request).execute();
                response.body().string();
                socketOn = true;
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Log.d("profeplus.response", response.message());
                    setStatus(FAILED);
                } else {
                    setStatus(DONE);
                }
            } catch (SocketException e) {
                socketOn = false;
                e.printStackTrace();
            } catch (IOException e1) {
                Log.d("profeplus.msg", "Failed");
                e1.printStackTrace();
            }
        } while ( !socketOn);
    }


    public void disableCode(SessionParcel sessionParcel, String token) {
        setStatus(FAILED);
        String url = String.format("%s/code/disable/%s%s%s", LearnApp.baseURL,
                sessionParcel.getAccesscode(), LearnApp.tokenURL, token);
        Log.d("profeplus.url", url);
        do {
            try {
                request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("Keep-Alive","timeout=5, max=50")
                        .build();
                response = client.newCall(request).execute();
                response.body().string();
                socketOn = true;
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Log.d("profeplus.response", response.message());
                    setStatus(FAILED);
                } else {
                    setStatus(DONE);
                }
            } catch (SocketException e) {
                socketOn = false;
                e.printStackTrace();
            } catch (IOException e1) {
                Log.d("profeplus.msg", "Failed");
                e1.printStackTrace();
            }
        } while ( !socketOn);
    }

    public void getDataPoints(SessionParcel sessionParcel, String token) {
        setStatus(FAILED);
        AnswerList answerList = new AnswerList();
        final String url = String.format("%s/lesson/result/%d/%d%s%s",LearnApp.baseURL,
                sessionParcel.getSessionId(), sessionParcel.getRun(), LearnApp.tokenURL, token);
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
            DataPoint dataPoint = gson.fromJson(jsonString.toString(), DataPoint.class);
            answerList.setAnsA(dataPoint.getAns1());
            answerList.setAnsB(dataPoint.getAns2());
            answerList.setAnsC(dataPoint.getAns3());
            answerList.setAnsD(dataPoint.getAns4());
            answerList.setAnsE(dataPoint.getAns5());
            answerList.setAnsNN(dataPoint.getAnsnn());
            answerList.setInLesson(dataPoint.getInlesson());
            answerList.setAnsTrue(dataPoint.getAns_yes());
            answerList.setAnsFalse(dataPoint.getAns_no());
            answerList.setAnsNA(dataPoint.getAns_na());
            setAnswerList(answerList);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JsonSyntaxException e2) {
            e2.printStackTrace();
        }
    }


    public void compareDataPoints(SessionParcel sessionParcel, String token) {
        setStatus(FAILED);
        AnswerList answerList = new AnswerList();
        AnswerList ianswerList = new AnswerList();
        final String url = String.format("%s/lesson/compare/%d/%d%s%s",LearnApp.baseURL,
                sessionParcel.getSessionId(), sessionParcel.getPreviousId(), LearnApp.tokenURL, token);
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
            CompareData compareData = gson.fromJson(jsonString.toString(),CompareData.class);
            answerList.setAnsA(compareData.getAns1());
            answerList.setAnsB(compareData.getAns2());
            answerList.setAnsC(compareData.getAns3());
            answerList.setAnsD(compareData.getAns4());
            answerList.setAnsE(compareData.getAns5());
            answerList.setAnsNN(compareData.getAnsnn());
            answerList.setInLesson(compareData.getInlesson());
            answerList.setAnsTrue(compareData.getAns_yes());
            answerList.setAnsFalse(compareData.getAns_no());
            answerList.setAnsNA(compareData.getAns_na());
            ianswerList.setAnsA(compareData.getAns1i());
            ianswerList.setAnsB(compareData.getAns2i());
            ianswerList.setAnsC(compareData.getAns3i());
            ianswerList.setAnsD(compareData.getAns4i());
            ianswerList.setAnsE(compareData.getAns5i());
            ianswerList.setAnsNN(compareData.getAnsnni());
            ianswerList.setInLesson(compareData.getInlessoni());
            ianswerList.setAnsTrue(compareData.getAns_yesi());
            ianswerList.setAnsFalse(compareData.getAns_noi());
            ianswerList.setAnsNA(compareData.getAns_nai());
            setAnswerList(answerList);
            setPreviousAnswerList(ianswerList);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void makeReport(String userId, String lessonId){
        Log.d("profeplus.Local", Locale.getDefault().getLanguage());
        setStatus(FAILED);
        final String url = String.format("%s/makereport/%s/lesson/%s",
                LearnApp.baseURL, userId, lessonId);
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            response.body().string();
            if (response.code() != HttpURLConnection.HTTP_OK) {
                Log.d("profeplus.msg", response.message());
                setStatus(FAILED);
            } else {
                setStatus(DONE);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void sendEmail(String userId){
        Log.d("profeplus.Local", Locale.getDefault().getLanguage());
        setStatus(FAILED);
        final String url = String.format("%s/sendemail/%s", LearnApp.baseURL, userId);
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            response.body().string();
            if (response.code() != HttpURLConnection.HTTP_OK) {
                Log.d("profeplus.msg", response.message());
                setStatus(FAILED);
            } else {
                setStatus(DONE);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void mailEval(String userId, int evalId){
        Log.d("profeplus.Local", Locale.getDefault().getLanguage());
        setStatus(FAILED);
        String urlSession = String.format("/sendemail/%s/eval/%d",userId,evalId);
        final String url = LearnApp.baseURL + urlSession;
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
                if (response.code() != HttpURLConnection.HTTP_OK) {
                    Log.d("profeplus.msg", response.message());
                    setStatus(FAILED);
                } else {
                    setStatus(DONE);
                }
            } catch (SocketException e) {
                socketOn = false;
                e.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } while (!socketOn);
    }


    public void endingEval(int userId, int evalId, int lessonId){
        Log.d("profeplus.Local", Locale.getDefault().getLanguage());
        setStatus(FAILED);
        String url = String.format("%/teacher/%d/lesson/%d/eval/%d/autofinish",
                LearnApp.baseURL, userId, lessonId, evalId);
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            response.body().string();
            if (response.code() != HttpURLConnection.HTTP_OK) {
                Log.d("profeplus.msg", response.message());
                setStatus(FAILED);
            } else {
                setStatus(DONE);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}

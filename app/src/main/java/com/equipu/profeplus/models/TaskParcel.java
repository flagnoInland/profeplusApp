package com.equipu.profeplus.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by equipu on 16/03/2016.
 * Esta clase contiene los atributos de una respuesta dada por el estudiante en un paquete.
 * Contiene datos como la pregunta que responde, la alterntiva escogida,
 * detalles de la pregunta.
 */
public class TaskParcel implements Parcelable {

    public static final int S_NONE = 1;
    public static final int S_SATISFACTION = 2;
    public static final int S_AGREEMENT = 3;
    public static final int S_QUALIFICATION = 4;
    public static final int S_SPEAKER = 5;

    public static final int Q_NORMAL = 1;
    public static final int Q_FREE = 2;
    public static final int Q_TRUE = 3;
    public static final int Q_SURVEY = 4;
    public static final int Q_EVAL = 5;


    public static final int M_INDIVIDUAL = 1;
    public static final int M_PEER = 2;

    public static final int ANS_NEW = 0;
    public static final int ANS_PASS1 = 1;
    public static final int ANS_PASS2 = 2;

    int session;
    int subject;
    int exercise;
    int run;
    int check;
    int questionType;
    int questionMode;
    int survey;
    String user;
    String accesscode;
    String lastAnswer;
    String cookie;
    int status;
    int pivot;
    int evalId;
    int[] evalAnswers;
    String updated;
    int duration;
    long leftTime;


    public TaskParcel(){
        this.session = 0;
        this.subject = 0;
        this.exercise = 0;
        this.run = 1;
        this.check = 0;
        this.questionType = Q_NORMAL;
        this.questionMode = M_INDIVIDUAL;
        this.survey = S_NONE;
        this.user = "";
        this.accesscode = "0";
        this.lastAnswer = "Z";
        this.cookie = "";
        this.status = 0;
        this.pivot = 0;
        this.evalId = 0;
        this.evalAnswers = new int[]{0};
        this.updated = "";
        this.duration = 30;
        this.leftTime = 30;
    }


    protected TaskParcel(Parcel in) {
        session = in.readInt();
        subject = in.readInt();
        exercise = in.readInt();
        run = in.readInt();
        check = in.readInt();
        questionType = in.readInt();
        questionMode = in.readInt();
        survey = in.readInt();
        user = in.readString();
        accesscode = in.readString();
        lastAnswer = in.readString();
        cookie = in.readString();
        status = in.readInt();
        pivot = in.readInt();
        evalId = in.readInt();
        evalAnswers = in.createIntArray();
        updated = in.readString();
        duration = in.readInt();
        leftTime = in.readLong();
    }

    public static final Creator<TaskParcel> CREATOR = new Creator<TaskParcel>() {
        @Override
        public TaskParcel createFromParcel(Parcel in) {
            return new TaskParcel(in);
        }

        @Override
        public TaskParcel[] newArray(int size) {
            return new TaskParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(session);
        dest.writeInt(subject);
        dest.writeInt(exercise);
        dest.writeInt(run);
        dest.writeInt(check);
        dest.writeInt(questionType);
        dest.writeInt(questionMode);
        dest.writeInt(survey);
        dest.writeString(user);
        dest.writeString(accesscode);
        dest.writeString(lastAnswer);
        dest.writeString(cookie);
        dest.writeInt(status);
        dest.writeInt(pivot);
        dest.writeInt(evalId);
        dest.writeIntArray(evalAnswers);
        dest.writeString(updated);
        dest.writeInt(duration);
        dest.writeLong(leftTime);
    }

    public int getPivot() {
        return pivot;
    }

    public void setPivot(int pivot) {
        this.pivot = pivot;
    }

    public int getEvalId() {
        return evalId;
    }

    public void setEvalId(int evalId) {
        this.evalId = evalId;
    }

    public int[] getEvalAnswers() {
        return evalAnswers;
    }

    public void setEvalAnswers(int[] evalAnswers) {
        this.evalAnswers = evalAnswers;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getExercise() {
        return exercise;
    }

    public void setExercise(int exercise) {
        this.exercise = exercise;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public int getQuestionMode() {
        return questionMode;
    }

    public void setQuestionMode(int questionMode) {
        this.questionMode = questionMode;
    }

    public int getSurvey() {
        return survey;
    }

    public void setSurvey(int survey) {
        this.survey = survey;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAccesscode() {
        return accesscode;
    }

    public void setAccesscode(String accesscode) {
        this.accesscode = accesscode;
    }

    public String getLastAnswer() {
        return lastAnswer;
    }

    public void setLastAnswer(String lastAnswer) {
        this.lastAnswer = lastAnswer;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(long leftTime) {
        this.leftTime = leftTime;
    }
}

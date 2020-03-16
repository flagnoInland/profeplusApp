package com.equipu.profeplus.gson_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Herbert Caller on 30/07/2016.
 */
public class SchoolLesson {
    @SerializedName("id")
    int id;
    @SerializedName("subject")
    int subject;
    @SerializedName("exercise")
    int exercise;
    @SerializedName("run")
    int run;
    @SerializedName("survey")
    int survey;
    @SerializedName("question_type")
    int question_type;
    @SerializedName("question_mode")
    int question_mode;
    @SerializedName("last_answer")
    String last_answer;
    @SerializedName("check")
    int check;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSurvey() {
        return survey;
    }

    public void setSurvey(int survey) {
        this.survey = survey;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    public int getQuestion_mode() {
        return question_mode;
    }

    public void setQuestion_mode(int question_mode) {
        this.question_mode = question_mode;
    }

    public String getLast_answer() {
        return last_answer;
    }

    public void setLast_answer(String last_answer) {
        this.last_answer = last_answer;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}

package com.equipu.profeplus.gson_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Herbert Caller on 30/07/2016.
 * Esta clase sirve para convertir los datos JSON en objetos con detalles de la lección
 */
public class Lesson{
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
    @SerializedName("evaluation_id")
    int evaluation_id;
    @SerializedName("duration")
    int duration;
    @SerializedName("updated")
    String updated;
    @SerializedName("solutions")
    String solutions;


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

    public int getEvaluation_id() {
        return evaluation_id;
    }

    public void setEvaluation_id(int evaluation_id) {
        this.evaluation_id = evaluation_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getSolutions() {
        return solutions;
    }

    public void setSolutions(String solutions) {
        this.solutions = solutions;
    }
}

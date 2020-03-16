package com.equipu.profeplus.gson_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Herbert Caller on 30/07/2016.
 */
public class FullEval{
    @SerializedName("id")
    int id;
    @SerializedName("user_id")
    int user_id;
    @SerializedName("number_question")
    int number_question;
    @SerializedName("overall_score")
    double overall_score;
    @SerializedName("duration")
    int duration;
    @SerializedName("start_time")
    String start_time;
    @SerializedName("end_time")
    String end_time;
    @SerializedName("date")
    String date;
    @SerializedName("course_name")
    String course_name;
    @SerializedName("speciality")
    String speciality;
    @SerializedName("institution")
    String institution;
    @SerializedName("exam_title")
    String exam_title;
    @SerializedName("materials")
    String materials;
    @SerializedName("answer_keys")
    String answer_keys;
    @SerializedName("answer_weights")
    String answer_weights;
    @SerializedName("observations")
    String observations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getNumber_question() {
        return number_question;
    }

    public void setNumber_question(int number_question) {
        this.number_question = number_question;
    }

    public double getOverall_score() {
        return overall_score;
    }

    public void setOverall_score(float overall_score) {
        this.overall_score = overall_score;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getExam_title() {
        return exam_title;
    }

    public void setExam_title(String exam_title) {
        this.exam_title = exam_title;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getAnswer_keys() {
        return answer_keys;
    }

    public void setAnswer_keys(String answer_keys) {
        this.answer_keys = answer_keys;
    }

    public String getAnswer_weights() {
        return answer_weights;
    }

    public void setAnswer_weights(String answer_weights) {
        this.answer_weights = answer_weights;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}

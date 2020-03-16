package com.equipu.profeplus.gson_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Herbert Caller on 30/07/2016.
 * Esta clase sirve para convertir datos JSON con datos de inicio de sesión en un objeto que
 * será usado por AppStateParcel
 */
public class Login{
    @SerializedName("token")
    String token;
    @SerializedName("user_id")
    int user_id;
    @SerializedName("appmode")
    int appmode;
    @SerializedName("teacher")
    int teacher;
    @SerializedName("country")
    String country;
    @SerializedName("name")
    String name;
    @SerializedName("document_id")
    String document_id;
    @SerializedName("evaluations")
    int evaluations;
    @SerializedName("gender")
    String gender;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAppmode() {
        return appmode;
    }

    public void setAppmode(int appmode) {
        this.appmode = appmode;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public int getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(int evaluations) {
        this.evaluations = evaluations;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

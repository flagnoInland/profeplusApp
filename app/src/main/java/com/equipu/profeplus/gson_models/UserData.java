package com.equipu.profeplus.gson_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Herbert Caller on 30/07/2016.
 * Esta clase sirve para convertir datos JSON de un usuario en un objeto que
 * será usado por UserParcel.
 */
public class UserData{
    @SerializedName("id")
    int id;
    @SerializedName("last_name")
    String last_name;
    @SerializedName("first_name")
    String first_name;
    @SerializedName("email")
    String email;
    @SerializedName("nationid")
    int nationid;
    @SerializedName("birthdate")
    String birthdate;
    @SerializedName("country")
    String country;
    @SerializedName("city")
    String city;
    @SerializedName("phone")
    String phone;
    @SerializedName("appmode")
    int appmode;
    @SerializedName("gender")
    String gender;
    @SerializedName("teacher")
    int teacher;
    @SerializedName("studentid")
    String studentid;
    @SerializedName("speciality")
    String speciality;
    @SerializedName("institution")
    String institution;
    @SerializedName("concurso")
    int concurso;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNationid() {
        return nationid;
    }

    public void setNationid(int nationid) {
        this.nationid = nationid;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAppmode() {
        return appmode;
    }

    public void setAppmode(int appmode) {
        this.appmode = appmode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
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

    public int getConcurso() {
        return concurso;
    }

    public void setConcurso(int concurso) {
        this.concurso = concurso;
    }
}

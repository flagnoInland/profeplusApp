package com.equipu.profeplus.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Herbert Caller on 5/27/2016.
 * Esta clase es usada para recoger todos los datos de un usuario en un paquete
 */
public class UserParcel implements Parcelable {

    public static final int STUDENT = 0;
    public static final int TEACHER = 1;

    public static final int NORMAL_MODE = 1;
    public static final int SCHOOL_MODE = 2;


    int userId;
    String firstame;
    String lastname;
    String dni;
    String birth;
    String country;
    String city;
    String phone;
    String gender;
    int teacher;
    String studentId;
    String password;
    String password2;
    String mail;
    int appMode;
    String institution;
    String speciality;
    int concurso;
    String concursoGroup;
    String concursoInstitute;


    public UserParcel(){
        this.userId = 0;
        this.firstame = "";
        this.lastname = "";
        this.dni = "";
        this.birth = "";
        this.country = "Peru";
        this.city = "Lima";
        this.phone = "";
        this.gender = "";
        this.teacher = STUDENT;
        this.studentId = "";
        this.password = "";
        this.password2 = "";
        this.mail = "";
        this.appMode = NORMAL_MODE;
        this.institution = "";
        this.speciality = "";
        this.concurso = 0;
        this.concursoGroup = "";
        this.concursoInstitute = "";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstame() {
        return firstame;
    }

    public void setFirstame(String firstame) {
        this.firstame = firstame;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getAppMode() {
        return appMode;
    }

    public void setAppMode(int appMode) {
        this.appMode = appMode;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public int getConcurso() {
        return concurso;
    }

    public void setConcurso(int concurso) {
        this.concurso = concurso;
    }

    public String getConcursoGroup() {
        return concursoGroup;
    }

    public void setConcursoGroup(String concursoGroup) {
        this.concursoGroup = concursoGroup;
    }

    public String getConcursoInstitute() {
        return concursoInstitute;
    }

    public void setConcursoInstitute(String concursoInstitute) {
        this.concursoInstitute = concursoInstitute;
    }

    public static final Creator<UserParcel> CREATOR = new Creator<UserParcel>() {
        @Override
        public UserParcel createFromParcel(Parcel in) {
            return new UserParcel(in);
        }

        @Override
        public UserParcel[] newArray(int size) {
            return new UserParcel[size];
        }
    };


    protected UserParcel(Parcel in) {
        userId = in.readInt();
        firstame = in.readString();
        lastname = in.readString();
        dni = in.readString();
        birth = in.readString();
        country = in.readString();
        city = in.readString();
        phone = in.readString();
        gender = in.readString();
        teacher = in.readInt();
        studentId = in.readString();
        password = in.readString();
        password2 = in.readString();
        mail = in.readString();
        appMode = in.readInt();
        institution = in.readString();
        speciality = in.readString();
        concurso = in.readInt();
        concursoGroup = in.readString();
        concursoInstitute = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(firstame);
        dest.writeString(lastname);
        dest.writeString(dni);
        dest.writeString(birth);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(phone);
        dest.writeString(gender);
        dest.writeInt(teacher);
        dest.writeString(studentId);
        dest.writeString(password);
        dest.writeString(password2);
        dest.writeString(mail);
        dest.writeInt(appMode);
        dest.writeString(institution);
        dest.writeString(speciality);
        dest.writeInt(concurso);
        dest.writeString(concursoGroup);
        dest.writeString(concursoInstitute);
    }
}

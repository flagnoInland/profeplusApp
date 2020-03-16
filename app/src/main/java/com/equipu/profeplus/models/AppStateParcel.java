package com.equipu.profeplus.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Herbert Caller on 17/05/2016.
 * Esta clase recolecta datos que son usados por la aplicación para procesar
 * las tareas de los usuarios en un paquete
 * Algunas variables no son usadas preo se prevee su uso para nuevas funcionalidades.
 * NORMAL_MODE : modo usado por profesores y estudiante de educación superior
 * SCHOOL_MODE : modo usado por proefsores y estudiantes de educación básica
 */
public class AppStateParcel implements Parcelable {

    public static final int STUDENT = 0;
    public static final int TEACHER = 1;

    public static final int NORMAL_MODE = 1;
    public static final int SCHOOL_MODE = 2;

    public static final int ACTIVE = 0;
    public static final int INACTIVE = 1;

    String userId;
    String sessionId;
    String username;
    int exerciseDone;
    int lessonDone;
    String token;
    int editUser;
    int userType;
    int continueLesson;
    int editCourse;
    String courseId;
    int sessionMode;
    int appMode;
    int activityNumber;
    int changeExercise;
    String country;
    int guestMode;
    String documentId;
    int pager;
    int evaluations;
    int editEvaluation;
    int saveEvaluation;
    String gender;
    String sessionCode;
    long startTime;
    int keepCode;
    String ownerId;
    int newInstall;

    public AppStateParcel(){
        userId = "0";
        sessionId = "0";
        username = "0";
        editUser = 0;
        userType = STUDENT;
        editCourse = 0;
        token = "0";
        appMode = NORMAL_MODE;
        guestMode = 0;
        editEvaluation = 0;
        saveEvaluation = 0;
        gender = "";
        sessionCode = "0000";
        startTime = 0;
        keepCode = 0;
        ownerId = "0";
        newInstall = 0;
    }

    public int getSaveEvaluation() {
        return saveEvaluation;
    }

    public void setSaveEvaluation(int saveEvaluation) {
        this.saveEvaluation = saveEvaluation;
    }

    public int getEditEvaluation() {
        return editEvaluation;
    }

    public void setEditEvaluation(int editEvaluation) {
        this.editEvaluation = editEvaluation;
    }

    public int getContinueLesson() {
        return continueLesson;
    }

    public void setContinueLesson(int continueLesson) {
        this.continueLesson = continueLesson;
    }

    public int getEditUser() {
        return editUser;
    }

    public void setEditUser(int editUser) {
        this.editUser = editUser;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExerciseDone() {
        return exerciseDone;
    }

    public void setExerciseDone(int exerciseDone) {
        this.exerciseDone = exerciseDone;
    }

    public int getLessonDone() {
        return lessonDone;
    }

    public void setLessonDone(int lessonDone) {
        this.lessonDone = lessonDone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getEditCourse() {
        return editCourse;
    }

    public void setEditCourse(int editCourse) {
        this.editCourse = editCourse;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getSessionMode() {
        return sessionMode;
    }

    public void setSessionMode(int sessionMode) {
        this.sessionMode = sessionMode;
    }

    public int getAppMode() {
        return appMode;
    }

    public void setAppMode(int appMode) {
        this.appMode = appMode;
    }

    public int getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
    }

    public int getChangeExercise() {
        return changeExercise;
    }

    public void setChangeExercise(int changeExercise) {
        this.changeExercise = changeExercise;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getGuestMode() {
        return guestMode;
    }

    public void setGuestMode(int guestMode) {
        this.guestMode = guestMode;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getPager() {
        return pager;
    }

    public void setPager(int pager) {
        this.pager = pager;
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

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getKeepCode() {
        return keepCode;
    }

    public void setKeepCode(int keepCode) {
        this.keepCode = keepCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getNewInstall() {
        return newInstall;
    }

    public void setNewInstall(int newInstall) {
        this.newInstall = newInstall;
    }

    protected AppStateParcel(Parcel in) {
        userId = in.readString();
        sessionId = in.readString();
        username = in.readString();
        exerciseDone = in.readInt();
        lessonDone = in.readInt();
        token = in.readString();
        editUser = in.readInt();
        userType = in.readInt();
        continueLesson = in.readInt();
        editCourse = in.readInt();
        courseId = in.readString();
        sessionMode = in.readInt();
        appMode = in.readInt();
        activityNumber = in.readInt();
        changeExercise = in.readInt();
        country = in.readString();
        guestMode = in.readInt();
        documentId = in.readString();
        pager = in.readInt();
        evaluations = in.readInt();
        editEvaluation = in.readInt();
        saveEvaluation = in.readInt();
        gender = in.readString();
        sessionCode = in.readString();
        startTime = in.readLong();
        keepCode = in.readInt();
        ownerId = in.readString();
        newInstall = in.readInt();
    }


    public static final Creator<AppStateParcel> CREATOR = new Creator<AppStateParcel>() {
        @Override
        public AppStateParcel createFromParcel(Parcel in) {
            return new AppStateParcel(in);
        }

        @Override
        public AppStateParcel[] newArray(int size) {
            return new AppStateParcel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(sessionId);
        dest.writeString(username);
        dest.writeInt(exerciseDone);
        dest.writeInt(lessonDone);
        dest.writeString(token);
        dest.writeInt(editUser);
        dest.writeInt(userType);
        dest.writeInt(continueLesson);
        dest.writeInt(editCourse);
        dest.writeString(courseId);
        dest.writeInt(sessionMode);
        dest.writeInt(appMode);
        dest.writeInt(activityNumber);
        dest.writeInt(changeExercise);
        dest.writeString(country);
        dest.writeInt(guestMode);
        dest.writeString(documentId);
        dest.writeInt(pager);
        dest.writeInt(evaluations);
        dest.writeInt(editEvaluation);
        dest.writeInt(saveEvaluation);
        dest.writeString(gender);
        dest.writeString(sessionCode);
        dest.writeLong(startTime);
        dest.writeInt(keepCode);
        dest.writeString(ownerId);
        dest.writeInt(newInstall);
    }


}

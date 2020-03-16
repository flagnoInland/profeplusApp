package com.equipu.profeplus.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Herbert Caller on 7/6/2016.
 */
public class EvaluationParcel implements Parcelable {

    public static final int ACTIVE = 0;
    public static final int INACTIVE = 1;

    int evaluationId;
    int teacherId;
    int numberQuestion;
    double overall;
    String startTime;
    String endTime;
    String date;
    String courseName;
    String speciality;
    String institution;
    String header;
    int[] materials;
    int[] answerKeys;
    double[] answerWeights;
    int duration;
    String observation;
    int pivotQuestion;
    int statusEval;
    int statusLesson;
    int lessonId;


    public EvaluationParcel(){
        evaluationId = 0;
        teacherId = 0;
        numberQuestion = 0;
        overall = 0.0;
        startTime = "";
        endTime = "";
        date = "";
        courseName = "";
        speciality = "";
        institution = "";
        header = "";
        materials = new int[]{0,0,0,0};
        answerKeys = new int[]{0};
        answerWeights = new double[]{0.0};
        duration = 0;
        observation = "";
        pivotQuestion = 0;
        statusEval = 0;
        statusLesson = 0;
        lessonId = 0;
    }

    protected EvaluationParcel(Parcel in) {
        evaluationId = in.readInt();
        teacherId = in.readInt();
        numberQuestion = in.readInt();
        overall = in.readDouble();
        startTime = in.readString();
        endTime = in.readString();
        date = in.readString();
        courseName = in.readString();
        speciality = in.readString();
        institution = in.readString();
        header = in.readString();
        materials = in.createIntArray();
        answerKeys = in.createIntArray();
        answerWeights = in.createDoubleArray();
        duration = in.readInt();
        observation = in.readString();
        pivotQuestion = in.readInt();
        statusEval = in.readInt();
        statusLesson = in.readInt();
        lessonId = in.readInt();
    }

    public static final Creator<EvaluationParcel> CREATOR = new Creator<EvaluationParcel>() {
        @Override
        public EvaluationParcel createFromParcel(Parcel in) {
            return new EvaluationParcel(in);
        }

        @Override
        public EvaluationParcel[] newArray(int size) {
            return new EvaluationParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(evaluationId);
        dest.writeInt(teacherId);
        dest.writeInt(numberQuestion);
        dest.writeDouble(overall);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(date);
        dest.writeString(courseName);
        dest.writeString(speciality);
        dest.writeString(institution);
        dest.writeString(header);
        dest.writeIntArray(materials);
        dest.writeIntArray(answerKeys);
        dest.writeDoubleArray(answerWeights);
        dest.writeInt(duration);
        dest.writeString(observation);
        dest.writeInt(pivotQuestion);
        dest.writeInt(statusEval);
        dest.writeInt(statusLesson);
        dest.writeInt(lessonId);
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int numberQuestion) {
        this.numberQuestion = numberQuestion;
    }

    public double getOverall() {
        return overall;
    }

    public void setOverall(double overall) {
        this.overall = overall;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int[] getMaterials() {
        return materials;
    }

    public void setMaterials(int[] materials) {
        this.materials = materials;
    }

    public int[] getAnswerKeys() {
        return answerKeys;
    }

    public void setAnswerKeys(int[] answerKeys) {
        this.answerKeys = answerKeys;
    }

    public double[] getAnswerWeights() {
        return answerWeights;
    }

    public void setAnswerWeights(double[] answerWeights) {
        this.answerWeights = answerWeights;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getPivotQuestion() {
        return pivotQuestion;
    }

    public void setPivotQuestion(int pivotQuestion) {
        this.pivotQuestion = pivotQuestion;
    }

    public int getStatusEval() {
        return statusEval;
    }

    public void setStatusEval(int statusEval) {
        this.statusEval = statusEval;
    }

    public int getStatusLesson() {
        return statusLesson;
    }

    public void setStatusLesson(int statusLesson) {
        this.statusLesson = statusLesson;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }


}

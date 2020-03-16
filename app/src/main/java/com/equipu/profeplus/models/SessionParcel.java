package com.equipu.profeplus.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Herbert Caller on 30/04/2016.
 * Esta clase contiene los atributos de una pregunta creada por el profesor en un paquete
 */
public class SessionParcel implements Parcelable {

    public static final int NORMAL_MODE = 1;
    public static final int SCHOOL_MODE = 2;

    public static final int S_NONE = 1;
    public static final int S_SATISFACTION = 2;
    public static final int S_AGREEMENT = 3;
    public static final int S_QUALIFICATION = 4;
    public static final int S_SPEAKER = 5;

    public static final int Q_NORMAL = 1;
    public static final int Q_BANK = 2;
    public static final int Q_TRUE = 3;
    public static final int Q_SURVEY = 4;
    public static final int Q_EVAL = 5;

    public static final int M_INDIVIDUAL = 1;
    public static final int M_PEER = 2;

    public static final int R_SIMPLE = 1;
    public static final int R_COMPLETE = 2;

    int sessionId;
    int courseId;
    String courseName;
    String courseCode;
    String courseTime;
    String accesscode;
    String exercise;
    String subject;
    int run;
    int questionType;
    int questionMode;
    int survey;
    int newSession;
    String level;
    String grade;
    String classroom;
    String comment;
    int previousId;
    String answerKey;
    String institution;
    String speciality;
    int report;
    int evaluationId;
    int inactive;
    String evalName;
    int appMode;


    public SessionParcel(){
        this.sessionId = 0;
        this.courseId = -1;
        this.courseName = "";
        this.courseCode = "";
        this.courseTime = "";
        this.accesscode = "0000";
        this.exercise = "0";
        this.subject = "0";
        this.run = 1;
        this.questionType = 1;
        this.questionMode = 1;
        this.survey = 1;
        this.newSession = 0;
        this.level = "";
        this.grade = "";
        this.classroom = "";
        this.comment = "";
        this.previousId = 0;
        this.answerKey = "";
        this.institution = "";
        this.speciality = "";
        this.report = 1;
        this.evaluationId = 0;
        this.inactive = 0;
        this.evalName = "";
        this.appMode = NORMAL_MODE;
    }


    protected SessionParcel(Parcel in) {
        sessionId = in.readInt();
        courseId = in.readInt();
        courseName = in.readString();
        courseCode = in.readString();
        courseTime = in.readString();
        accesscode = in.readString();
        exercise = in.readString();
        subject = in.readString();
        run = in.readInt();
        questionType = in.readInt();
        questionMode = in.readInt();
        survey = in.readInt();
        newSession = in.readInt();
        level = in.readString();
        grade = in.readString();
        classroom = in.readString();
        comment = in.readString();
        previousId = in.readInt();
        answerKey = in.readString();
        institution = in.readString();
        speciality = in.readString();
        report = in.readInt();
        evaluationId = in.readInt();
        inactive = in.readInt();
        evalName = in.readString();
        appMode = in.readInt();
    }

    public static final Creator<SessionParcel> CREATOR = new Creator<SessionParcel>() {
        @Override
        public SessionParcel createFromParcel(Parcel in) {
            return new SessionParcel(in);
        }

        @Override
        public SessionParcel[] newArray(int size) {
            return new SessionParcel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sessionId);
        dest.writeInt(courseId);
        dest.writeString(courseName);
        dest.writeString(courseCode);
        dest.writeString(courseTime);
        dest.writeString(accesscode);
        dest.writeString(exercise);
        dest.writeString(subject);
        dest.writeInt(run);
        dest.writeInt(questionType);
        dest.writeInt(questionMode);
        dest.writeInt(survey);
        dest.writeInt(newSession);
        dest.writeString(level);
        dest.writeString(grade);
        dest.writeString(classroom);
        dest.writeString(comment);
        dest.writeInt(previousId);
        dest.writeString(answerKey);
        dest.writeString(institution);
        dest.writeString(speciality);
        dest.writeInt(report);
        dest.writeInt(evaluationId);
        dest.writeInt(inactive);
        dest.writeString(evalName);
        dest.writeInt(appMode);
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public String getAccesscode() {
        return accesscode;
    }

    public void setAccesscode(String accesscode) {
        this.accesscode = accesscode;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
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

    public int getNewSession() {
        return newSession;
    }

    public void setNewSession(int newSession) {
        this.newSession = newSession;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPreviousId() {
        return previousId;
    }

    public void setPreviousId(int previousId) {
        this.previousId = previousId;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
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

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getInactive() {
        return inactive;
    }

    public void setInactive(int inactive) {
        this.inactive = inactive;
    }

    public String getEvalName() {
        return evalName;
    }

    public void setEvalName(String evalName) {
        this.evalName = evalName;
    }

    public int getAppMode() {
        return appMode;
    }

    public void setAppMode(int appMode) {
        this.appMode = appMode;
    }
}

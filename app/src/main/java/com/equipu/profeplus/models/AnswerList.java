package com.equipu.profeplus.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Herbert Caller on 6/2/2016.
 * Esta clase funciona como un paquete de los resultados para cada alternativa.
 * Los siguientes parámetros deben ser explicados
 * ansNA : corresponde a estudiantes que marcan "NO SE"
 * ansNN : corresponde a estudiante que no responden la pregunta
 * inLesson : corresponde a los estudiante que accedieron a la pregunta
 */
public class AnswerList implements Parcelable{

    int ansA;
    int ansB;
    int ansC;
    int ansD;
    int ansE;
    int ansTrue;
    int ansFalse;
    int ansNA;
    int ansNN;
    int inLesson;

    public AnswerList(){
        this.ansA = 0;
        this.ansB = 0;
        this.ansC = 0;
        this.ansD = 0;
        this.ansE = 0;
        this.ansTrue = 0;
        this.ansFalse = 0;
        this.ansNA = 0;
        this.ansNN = 0;
        this.inLesson = 0;
    }

    protected AnswerList(Parcel in) {
        ansA = in.readInt();
        ansB = in.readInt();
        ansC = in.readInt();
        ansD = in.readInt();
        ansE = in.readInt();
        ansTrue = in.readInt();
        ansFalse = in.readInt();
        ansNA = in.readInt();
        ansNN = in.readInt();
        inLesson = in.readInt();
    }

    public static final Creator<AnswerList> CREATOR = new Creator<AnswerList>() {
        @Override
        public AnswerList createFromParcel(Parcel in) {
            return new AnswerList(in);
        }

        @Override
        public AnswerList[] newArray(int size) {
            return new AnswerList[size];
        }
    };

    public int getAnsA() {
        return ansA;
    }

    public void setAnsA(int ansA) {
        this.ansA = ansA;
    }

    public int getAnsB() {
        return ansB;
    }

    public void setAnsB(int ansB) {
        this.ansB = ansB;
    }

    public int getAnsC() {
        return ansC;
    }

    public void setAnsC(int ansC) {
        this.ansC = ansC;
    }

    public int getAnsD() {
        return ansD;
    }

    public void setAnsD(int ansD) {
        this.ansD = ansD;
    }

    public int getAnsE() {
        return ansE;
    }

    public void setAnsE(int ansE) {
        this.ansE = ansE;
    }

    public int getAnsTrue() {
        return ansTrue;
    }

    public void setAnsTrue(int ansTrue) {
        this.ansTrue = ansTrue;
    }

    public int getAnsFalse() {
        return ansFalse;
    }

    public void setAnsFalse(int ansFalse) {
        this.ansFalse = ansFalse;
    }

    public int getAnsNA() {
        return ansNA;
    }

    public void setAnsNA(int ansNA) {
        this.ansNA = ansNA;
    }

    public int getAnsNN() {
        return ansNN;
    }

    public void setAnsNN(int ansNN) {
        this.ansNN = ansNN;
    }

    public int getInLesson() {
        return inLesson;
    }

    public void setInLesson(int inLesson) {
        this.inLesson = inLesson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ansA);
        dest.writeInt(ansB);
        dest.writeInt(ansC);
        dest.writeInt(ansD);
        dest.writeInt(ansE);
        dest.writeInt(ansTrue);
        dest.writeInt(ansFalse);
        dest.writeInt(ansNA);
        dest.writeInt(ansNN);
        dest.writeInt(inLesson);
    }
}

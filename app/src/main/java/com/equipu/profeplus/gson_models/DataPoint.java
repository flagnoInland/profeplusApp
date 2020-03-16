package com.equipu.profeplus.gson_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Herbert Caller on 30/07/2016.
 * Esta clase convierte los datos JSON en resultados del paso 1
 */
public class DataPoint{
    @SerializedName("ans1")
    int ans1;
    @SerializedName("ans2")
    int ans2;
    @SerializedName("ans3")
    int ans3;
    @SerializedName("ans4")
    int ans4;
    @SerializedName("ans5")
    int ans5;
    @SerializedName("ansnn")
    int ansnn;
    @SerializedName("inlesson")
    int inlesson;
    @SerializedName("ans_yes")
    int ans_yes;
    @SerializedName("ans_no")
    int ans_no;
    @SerializedName("ans_na")
    int ans_na;

    public int getAns1() {
        return ans1;
    }

    public void setAns1(int ans1) {
        this.ans1 = ans1;
    }

    public int getAns2() {
        return ans2;
    }

    public void setAns2(int ans2) {
        this.ans2 = ans2;
    }

    public int getAns3() {
        return ans3;
    }

    public void setAns3(int ans3) {
        this.ans3 = ans3;
    }

    public int getAns4() {
        return ans4;
    }

    public void setAns4(int ans4) {
        this.ans4 = ans4;
    }

    public int getAns5() {
        return ans5;
    }

    public void setAns5(int ans5) {
        this.ans5 = ans5;
    }

    public int getAnsnn() {
        return ansnn;
    }

    public void setAnsnn(int ansnn) {
        this.ansnn = ansnn;
    }

    public int getInlesson() {
        return inlesson;
    }

    public void setInlesson(int inlesson) {
        this.inlesson = inlesson;
    }

    public int getAns_yes() {
        return ans_yes;
    }

    public void setAns_yes(int ans_yes) {
        this.ans_yes = ans_yes;
    }

    public int getAns_no() {
        return ans_no;
    }

    public void setAns_no(int ans_no) {
        this.ans_no = ans_no;
    }

    public int getAns_na() {
        return ans_na;
    }

    public void setAns_na(int ans_na) {
        this.ans_na = ans_na;
    }
}

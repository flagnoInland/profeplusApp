package com.equipu.profeplus.gson_models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Herbert Caller on 30/07/2016.
 * Este clase sirve para convertir datos JSON de una pregunta en un objeto que será usado
 * por TaskParcel.
 */
public class NewSession{
    @SerializedName("id")
    int id;
    @SerializedName("run")
    int run;
    @SerializedName("accesscode")
    String accesscode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public String getAccesscode() {
        return accesscode;
    }

    public void setAccesscode(String accesscode) {
        this.accesscode = accesscode;
    }
}
package com.equipu.profeplus.utils;

/**
 * Created by herbert.caller on 3/27/2018.
 */

public class NetworkFailureException extends Exception {

    public NetworkFailureException() {
        //"Sorry! You need Mobile Data or WiFi Connection."
        super("Sorry! You need Mobile Data or WiFi Connection.");
    }
}

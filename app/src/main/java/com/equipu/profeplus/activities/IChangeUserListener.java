package com.equipu.profeplus.activities;

import com.equipu.profeplus.models.AppStateParcel;

/**
 * Created by Herbert Caller on 24/09/2016.
 */

public interface IChangeUserListener {

    void startUserMode(AppStateParcel appStateParcel);

    void startHome(AppStateParcel appStateParcel);

    void reloadUserBoard(AppStateParcel appStateParcel);

    void newSignUp(AppStateParcel appStateParcel);

}

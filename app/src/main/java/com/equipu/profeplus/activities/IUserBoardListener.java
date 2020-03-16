package com.equipu.profeplus.activities;

import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;
import com.equipu.profeplus.models.TaskParcel;
import com.equipu.profeplus.models.UserParcel;

/**
 * Created by Herbert Caller on 24/09/2016.
 */

public interface IUserBoardListener extends ILearnAppListener{

    void goToLogin(AppStateParcel appStateParcel);
    void editProfileMode(AppStateParcel appStateParcel);
    void reloadUserBoard(AppStateParcel appStateParcel);

    void readComiteInfo();
    void readCreatorInfo();
    void readThankInfo();
    void readMotivInfo();
    void readWhyInfo();
    void readEmprenderInfo();
    void readProducersInfo();

    void setUserMode(AppStateParcel ap);
    void startStudentSession(TaskParcel taskParcel);
    void readStudentManual();
    void updateUserMode();

    void goToBecas();
    void updateProfile(AppStateParcel appStateParcel, UserParcel userParcel);

    void startTeacherSession(SessionParcel sessionParcel);
    void goToTutorial(AppStateParcel appStateParcel);

    void readProtocol();
    void readTeacherManual();
    void readAskQuestion();

    void goToShare();

}

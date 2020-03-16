package com.equipu.profeplus.activities;

import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

/**
 * Created by Herbert Caller on 24/09/2016.
 */

public interface IStartListener extends ILearnAppListener{

    void startLogin(AppStateParcel appStateParcel);

    void startSignUp(AppStateParcel appStateParcel);

    void goToStudentBoard(AppStateParcel appStateParcel);

    void goToTeacherBoard(AppStateParcel appStateParcel);

    void startRecover(AppStateParcel appStateParcel);

    void backToHome(UserParcel userParcel);

    void onRecoverSuccess(AppStateParcel ap);

    void registerStudent(UserParcel userParcel);

    void goToStepTwoSchoolSignUp(UserParcel userParcel);

    void goToStepTwoSignUp(UserParcel userParcel);

    void startTeacherSession(UserParcel userParcel);

    void startUniUserMode(UserParcel userParcel);

    void startSchoolUserMode(UserParcel userParcel);

    void goToStepThreeSignUp(UserParcel userParcel);

    void goToStepFourSignUp(UserParcel userParcel);

    void registerUniTeacher(UserParcel userParcel);

    void goToConcurso(UserParcel userParcel);

}

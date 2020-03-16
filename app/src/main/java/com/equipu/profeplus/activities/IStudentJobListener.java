package com.equipu.profeplus.activities;

import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;

/**
 * Created by Herbert Caller on 24/09/2016.
 */

public interface IStudentJobListener  extends ILearnAppListener {

    void updateTaskParcel(AppStateParcel ap, AppStateParcel agp, TaskParcel tp, TaskParcel tgp);

    void loginGuest(TaskParcel tp, AppStateParcel ap, TaskParcel tgp, AppStateParcel agp);

    void startAnswerStepOne(TaskParcel tp, AppStateParcel ap, TaskParcel tgp, AppStateParcel agp);

    void startAnswerStepTwo(TaskParcel tp, AppStateParcel ap, TaskParcel tgp, AppStateParcel agp);

    void startQuestContent(TaskParcel tp, AppStateParcel ap, TaskParcel tgp, AppStateParcel agp);

    void backToBoard(AppStateParcel ap);

    void goToStepTwo(TaskParcel tp, AppStateParcel ap, TaskParcel tgp, AppStateParcel agp);

}

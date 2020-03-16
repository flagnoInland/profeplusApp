package com.equipu.profeplus.activities;

import com.equipu.profeplus.models.AnswerList;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;

/**
 * Created by Herbert Caller on 24/09/2016.
 */

public interface ITeacherJobListener extends ILearnAppListener{

    void makeCompleteReport(SessionParcel sp, AppStateParcel ap);
    void scheduleMail(AppStateParcel ap);
    void finishReport(SessionParcel sessionParcel);
    void makeReport(AppStateParcel ap, SessionParcel sessionParcel);

    void showResults(SessionParcel sessionParcel);
    void setCodeSession(SessionParcel sessionParcel);
    void updateSessionCode(AppStateParcel ap, SessionParcel sp);

    void chooseOpinionType(SessionParcel sessionParcel);
    void setSubjectCourse(SessionParcel sessionParcel);

    void setReportData(SessionParcel sessionParcel);
    void finishLesson(SessionParcel sessionParcel);

    void startResultStateTwo(SessionParcel sessionParcel, AnswerList answerList);
    void startResultStateFour(SessionParcel sessionParcel, AnswerList answerList,
                              AnswerList answerList1);

    void startResultStateThree(SessionParcel sessionParcel);
    void startResultTFStateTwo(SessionParcel sessionParcel, AnswerList answerList);
    void startResultTFStateFour(SessionParcel sessionParcel, AnswerList answerList,
                                AnswerList answerList1);
    void startResultTFStateThree(SessionParcel sessionParcel);

    void readTeacherManual();
    void backToUserBoard(AppStateParcel ap);
    void backToUserBoardFromEval(AppStateParcel ap);
    void backToFreeQuestion();

    void finishExercise(SessionParcel sessionParcel);

}

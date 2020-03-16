package com.equipu.profeplus.fragments.student_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkStudentService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStudentJobListener;
import com.equipu.profeplus.controllers.StudentLessonController;
import com.equipu.profeplus.dialogs.AppAnswerDialog;
import com.equipu.profeplus.dialogs.ConfirmOneButtonSupportDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento fue creado para responde a las preguntas de opinión
Este fragmento muestra las opciones de la pregunta con botones rectangulares
 */
public class AnswerSquareFragment extends Fragment
        implements AppAnswerDialog.OnFireEventAnswerDialogListener,
        ConfirmOneButtonSupportDialog.OnFireEventOneButtonDialogListener {

    //@BindView(R.id.img_fondo) ImageView bckg;
    @BindView(R.id.header_tmpl) ViewStub vHeader;
    @BindView(R.id.txt_run_view) TextView runNumber;
    @BindView(R.id.btn_quest) Button btnQuest;
    @BindView(R.id.btn_share) Button btnShare;
    @BindView(R.id.btn_share_disabled) Button btnDisabledShare;
    @BindView(R.id.btn_guest_session) Button btnGuest;
    ImageView btnback;
    ImageView btnRefresh;
    ImageView btnexit;

    @BindView(R.id.chkb_a) Button btnA;
    @BindView(R.id.chkb_b) Button btnB;
    @BindView(R.id.chkb_c) Button btnC;
    @BindView(R.id.chkb_d) Button btnD;
    @BindView(R.id.chkb_e) Button btnE;
    @BindView(R.id.txt_roulette_action) TextView btnChange;
    private Unbinder unbinder;

    private static int attempts = 1;

    TaskParcel taskParcel, taskGuestParcel;
    AppStateParcel appStateParcel, appStateGuestParcel;
    RegisterSessionReceiver registerSessionReceiver;
    MakeAnswerReceiver makeAnswerReceiver;

    View layout;
    TextView toastNot;


    private IStudentJobListener mListener;

    public AnswerSquareFragment() {}


    public static AnswerSquareFragment newInstance(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                                                   AppStateParcel agp) {
        AnswerSquareFragment fragment = new AnswerSquareFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_TASK_PARCEL, tp);
        args.putParcelable(LearnApp.PCL_TASK_GUEST_PARCEL, tgp);
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
        args.putParcelable(LearnApp.PCL_APP_STATE_GUEST,agp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskParcel = getArguments().getParcelable(LearnApp.PCL_TASK_PARCEL);
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
            taskGuestParcel = getArguments().getParcelable(LearnApp.PCL_TASK_GUEST_PARCEL);
            appStateGuestParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE_GUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_square_answer, container, false);
        unbinder = ButterKnife.bind(this, v);

        appStateParcel.setOwnerId(appStateParcel.getUserId());
        appStateGuestParcel.setOwnerId(appStateParcel.getUserId());

        // Cargar la vista para el paso 1
        if (taskParcel.getRun() == 1) {
            vHeader.setLayoutResource(R.layout.header_template5);
            View inflated = vHeader.inflate();
            btnRefresh = (ImageView) inflated.findViewById(R.id.btn_refresh_session);
            btnback = (ImageView) inflated.findViewById(R.id.img_back);
            btnexit = (ImageView) inflated.findViewById(R.id.img_exit);
        }

        // Cargar la vista para el paso 1
        if (taskParcel.getRun() == 2){
            vHeader.setLayoutResource(R.layout.header_template4);
            View inflated = vHeader.inflate();
            btnback = (ImageView) inflated.findViewById(R.id.img_back);
            btnexit = (ImageView) inflated.findViewById(R.id.img_exit);
        }

        // Ocultar botones segun el paso y el tipo de estudiante
        runNumber.setVisibility(View.GONE);
        btnChange.setVisibility(View.GONE);

        if (taskParcel.getRun()== 1 &&
                appStateParcel.getGuestMode() == AppStateParcel.INACTIVE &&
                appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE ){
            btnQuest.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
            //btnDisabledShare.setVisibility(View.VISIBLE);
            btnDisabledShare.setVisibility(View.GONE);
            btnGuest.setVisibility(View.GONE);
        } else if (taskParcel.getRun()== 2 &&
                appStateParcel.getGuestMode() == AppStateParcel.INACTIVE &&
                appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE ){
            btnQuest.setVisibility(View.VISIBLE);
            //btnDisabledShare.setVisibility(View.VISIBLE);
            btnDisabledShare.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
            btnGuest.setVisibility(View.GONE);
        } else if (taskParcel.getRun()== 1 &&
                appStateParcel.getGuestMode() == AppStateParcel.ACTIVE &&
                appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE ) {
            btnQuest.setVisibility(View.GONE);
            btnDisabledShare.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
            btnGuest.setVisibility(View.VISIBLE);
        } else if (taskParcel.getRun()== 2 &&
                appStateParcel.getGuestMode() == AppStateParcel.ACTIVE &&
                appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE ) {
            btnQuest.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.GONE);
            btnDisabledShare.setVisibility(View.GONE);
            btnGuest.setVisibility(View.VISIBLE);
        } else if (appStateParcel.getAppMode() == AppStateParcel.SCHOOL_MODE){
            btnShare.setVisibility(View.GONE);
            btnGuest.setVisibility(View.GONE);
            btnQuest.setVisibility(View.GONE);
            btnDisabledShare.setVisibility(View.GONE);
        }


        btnA.setTypeface(LearnApp.appFont);
        btnB.setTypeface(LearnApp.appFont);
        btnC.setTypeface(LearnApp.appFont);
        btnD.setTypeface(LearnApp.appFont);
        btnE.setTypeface(LearnApp.appFont);
        btnChange.setTypeface(LearnApp.appFont);


        String[] alternatives = new String[]{"A","B","C","D","E"};

        // Mostrar alterntivas segín el tipo de pregunta de opinión
        if (taskParcel.getSurvey() == TaskParcel.S_QUALIFICATION){
            alternatives = getResources().getStringArray(R.array.question_opinion3_array);
        } else if (taskParcel.getSurvey() == TaskParcel.S_SATISFACTION){
            alternatives = getResources().getStringArray(R.array.question_opinion1_array);
        } else if (taskParcel.getSurvey() == TaskParcel.S_SPEAKER){
            alternatives = getResources().getStringArray(R.array.question_speaker_array);
        }

        btnA.setText(alternatives[0]);
        btnB.setText(alternatives[1]);
        btnC.setText(alternatives[2]);
        btnD.setText(alternatives[3]);
        btnE.setText(alternatives[4]);


        btnChange.setEnabled(false);
        btnChange.setText(getString(R.string.msg_tap_correct_answer));

        btnA.setOnClickListener(new AnswerClickListener("A"));
        btnB.setOnClickListener(new AnswerClickListener("B"));
        btnC.setOnClickListener(new AnswerClickListener("C"));
        btnD.setOnClickListener(new AnswerClickListener("D"));
        btnE.setOnClickListener(new AnswerClickListener("E"));

        btnChange.setText(getString(R.string.msg_tap_correct_answer));
        if (appStateParcel.getGuestMode()== AppStateParcel.ACTIVE){
            btnChange.setText(getString(R.string.text_important_only_one_chance));
        }

        btnShare.setVisibility(View.GONE);

        if ( taskParcel.getCheck()== 1 &&
                appStateParcel.getGuestMode()== AppStateParcel.INACTIVE ){
            onAnswerSuccess();
        } else if ( taskGuestParcel.getCheck() == 1 &&
                appStateParcel.getGuestMode() == AppStateParcel.ACTIVE ){
            onAnswerSuccess();
        }


        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appStateParcel.getAppMode() == AppStateParcel.SCHOOL_MODE) {
                        //RegisterSchoolSessionTask registerSessionTask = new
                        // RegisterSchoolSessionTask();
                        startRegistration();
                    } else {
                        startRegistration();
                    }

                }
            });
        }

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null) {
                    mListener.backToBoard(appStateParcel);
                }
            }
        });

        // Botón para visualizar las indicaciones del paso 2
        btnQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                Bundle bd = new Bundle();
                bd.putString("MESSAGE", getString(R.string.text_answer_step_two_student_simple));
                newFragment.setArguments(bd);
                newFragment.show(getFragmentManager(), null);
            }
        });

        // Botón para compartir con otro usuario
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null) {
                    mListener.loginGuest(taskParcel, appStateParcel, taskGuestParcel, appStateGuestParcel);
                }
            }
        });

        // Botón para retornar al propietario
        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOwnerLogged();
            }
        });

        // Botón de compartir bloqueado
        btnDisabledShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNot("",getString(R.string.text_first_choose_answer));
            }
        });


        layout = inflater.inflate(R.layout.answer_toast_layout, (ViewGroup) v.findViewById(R.id.lin_lay_toast));
        toastNot = (TextView) layout.findViewById(R.id.toast_answer_msg);

        return v;

    }


    /*
    Iniciar el servicio para permitir al estudiante responder la pregunta
     */
    public void startRegistration(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            Intent intent = new Intent(getActivity(), NetworkStudentService.class);
            intent.putExtra(NetworkStudentService.SERVICE,
                    NetworkStudentService.SERVICE_REGISTER_SESSION);
            if (appStateParcel.getGuestMode()==AppStateParcel.ACTIVE){
                intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskGuestParcel);
                intent.putExtra(LearnApp.PCL_APP_STATE, appStateGuestParcel);
            } else {
                intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskParcel);
                intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            }
            getActivity().startService(intent);
        } else {
            if (mListener!=null) {
                mListener.dialogLostConnection();
            }
        }
    }

    /*
    Receiver para capturar el estado del permiso al estudiante para acceder a la pregunta
     */
    public class RegisterSessionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int userRun, userCheck;
            int status = intent.getIntExtra(NetworkStudentService.STATUS, 0);
            if (appStateParcel.getGuestMode() == AppStateParcel.ACTIVE &&
                    status == StudentLessonController.DONE){
                taskGuestParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
                userCheck = taskGuestParcel.getCheck();
                userRun = taskGuestParcel.getRun();
                if (userCheck == 0 && userRun == 2) {
                    taskGuestParcel.setStatus(TaskParcel.ANS_NEW);
                    if (mListener != null) {
                        mListener.startQuestContent(taskParcel, appStateParcel, taskGuestParcel,
                                appStateGuestParcel);
                    }
                }
            } else if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE &&
                    status == StudentLessonController.DONE) {
                taskParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
                userCheck = taskParcel.getCheck();
                userRun = taskParcel.getRun();
                if (userCheck == 0 && userRun == 2){
                    taskParcel.setStatus(TaskParcel.ANS_NEW);
                    if (mListener!=null) {
                        mListener.startQuestContent(taskParcel, appStateParcel, taskGuestParcel,
                                appStateGuestParcel);
                    }
                }
            }
        }
    }

    /*
   Mostar detalles del paso
    */
    private void setLabelRun(){
        String mRunNumber = "";
        if (taskParcel.getRun() == 1) {
            //bckg.setBackgroundResource(R.drawable.fondopregunta1);
            mRunNumber = String.format("%s %d : %s",
                    getString(R.string.text_step),
                    taskParcel.getRun(),
                    getString(R.string.text_individual_answer));
        } else {
            //bckg.setBackgroundResource(R.drawable.fondopregunta2);
            if (taskParcel.getQuestionMode() == TaskParcel.M_PEER) {
                mRunNumber = String.format("%s %d : %s",
                        getString(R.string.text_step),
                        taskParcel.getRun(),
                        getString(R.string.text_peer_instruction));
            } else {
                mRunNumber = String.format("%s %d : %s",
                        getString(R.string.text_step),
                        taskParcel.getRun(),
                        getString(R.string.text_team_work));
            }
        }
        runNumber.setText(mRunNumber);
    }

    /*
    Mandar respuesta con click del usuario
     */
    private class AnswerClickListener implements View.OnClickListener {

        String answer;

        public AnswerClickListener(String answer){
            this.answer = answer;
        }
        @Override
        public void onClick(View v) {
            disableCheck();
            v.setSelected(true);
            newAnswer(answer);
        }
    }

    // Bloquear botones
    private void disableCheck(){
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnD.setEnabled(false);
        btnC.setEnabled(false);
        btnE.setEnabled(false);
        btnA.setSelected(true);
        btnB.setSelected(true);
        btnC.setSelected(true);
        btnD.setSelected(true);
        btnE.setSelected(true);
    }

    // Activar botones
    private void enableCheck(){
        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnD.setEnabled(true);
        btnC.setEnabled(true);
        btnE.setEnabled(true);
        btnA.setSelected(false);
        btnB.setSelected(false);
        btnC.setSelected(false);
        btnD.setSelected(false);
        btnE.setSelected(false);
    }

    /*
    Inicia servicio par enviar repuesta del estudiante
     */
    public void newAnswer(String answer){
        if ( attempts > 1 ){
            Intent intent = new Intent(getActivity(), NetworkStudentService.class);
            intent.putExtra(NetworkStudentService.SERVICE,
                    NetworkStudentService.SERVICE_ANSWER_QUESTION);
            intent.putExtra(NetworkStudentService.ANSWER, answer);
            if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
                intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskParcel);
                intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            } else {
                intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskGuestParcel);
                intent.putExtra(LearnApp.PCL_APP_STATE, appStateGuestParcel);
            }
            getActivity().startService(intent);
        } else {
            ++attempts;
            dialogAnswer(answer);
        }
    }

    /*
    Mostrar diálogo para confirmar respuesta
     */
    public void dialogAnswer(String answer){
        DialogFragment newFragment = new AppAnswerDialog();
        Bundle bd = new Bundle();
        if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
            bd.putParcelable("TaskParcel", taskParcel);
            bd.putParcelable("AppStateParcel", appStateParcel);
        } else {
            bd.putParcelable("TaskParcel", taskGuestParcel);
            bd.putParcelable("AppStateParcel", appStateGuestParcel);
        }
        bd.putString("ANSWER", answer);
        newFragment.setArguments(bd);
        newFragment.setTargetFragment(AnswerSquareFragment.this,1234);
        newFragment.show(getFragmentManager(), "answer");
    }

    public void sendNot(String title, String message){
        DialogFragment newFragment = new NotificationOneButtonSupportDialog();
        Bundle b = new Bundle();
        b.putString("TITLE", title);
        b.putString("MESSAGE", message);
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), null);
    }

    public void showNotification(String mMessage){
        toastNot = (TextView) layout.findViewById(R.id.toast_answer_msg);
        toastNot.setText(mMessage);
        Toast mNot = new Toast(getContext());
        mNot.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        mNot.setDuration(Toast.LENGTH_SHORT);
        mNot.setView(layout);
        mNot.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IStudentJobListener) {
            mListener = (IStudentJobListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IStudentJobListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*
    Receiver para capturar si la respuesta fue recibida.
    Si se logra actualizar paquete de parámetros
     */
    public class MakeAnswerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkStudentService.STATUS,0);
            String answer = intent.getStringExtra(NetworkStudentService.ANSWER);
            if (status == StudentLessonController.DONE){
                if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
                    taskParcel.setLastAnswer(answer);
                    taskParcel.setCheck(1);
                } else {
                    taskGuestParcel.setLastAnswer(answer);
                    taskGuestParcel.setCheck(1);
                }
                if (mListener != null) {
                    mListener.updateTaskParcel(appStateParcel, appStateGuestParcel,
                            taskParcel,taskGuestParcel);
                }
            }
            handleMakeAnswer(status);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkStudentService.ACTION_ANSWER_QUESTION);
        IntentFilter filter1 = new IntentFilter(NetworkStudentService.ACTION_REGISTER_SESSION);
        makeAnswerReceiver = new MakeAnswerReceiver();
        registerSessionReceiver = new RegisterSessionReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(makeAnswerReceiver, filter);
        lbm.registerReceiver(registerSessionReceiver, filter1);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(registerSessionReceiver);
        lbm.unregisterReceiver(makeAnswerReceiver);
    }

    /*
   Mostrar mensaje luego de confirmar el envío de respuesta o cancelarlo
    */

    @Override
    public void onFireAcceptEventAnswerListener(String answer) {
        Intent intent = new Intent(getActivity(), NetworkStudentService.class);
        intent.putExtra(NetworkStudentService.SERVICE,
                NetworkStudentService.SERVICE_ANSWER_QUESTION);
        intent.putExtra(NetworkStudentService.ANSWER, answer);
        if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
            intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskParcel);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        } else {
            intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskGuestParcel);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateGuestParcel);
        }
        getActivity().startService(intent);
    }

    @Override
    public void onFireCancelEventAnswerListener() {
        handleMakeAnswer(StudentLessonController.CANCEL);
    }


    @Override
    public void onFireAcceptEventOneButtonListener() {
        if (mListener!=null) {
            mListener.backToBoard(appStateParcel);
        }
    }

    @Override
    public void onFireCancelEventOneButtonListener() {
        if (mListener!=null) {
            mListener.backToBoard(appStateParcel);
        }
    }

    /*
    Cambiar paquetes de parámetres luego de retornar al propietario
     */
    public void onOwnerLogged(){
        appStateParcel.setGuestMode(AppStateParcel.INACTIVE);
        if (appStateParcel.getUserType() == AppStateParcel.STUDENT) {
            if (taskParcel.getRun() == 1){
                if (mListener!=null) {
                    mListener.startAnswerStepOne(taskParcel, appStateParcel, taskGuestParcel,
                            appStateGuestParcel);
                }
            } else {
                if (mListener!=null) {
                    mListener.startAnswerStepTwo(taskParcel, appStateParcel, taskGuestParcel,
                            appStateGuestParcel);
                }
            }
        }
        startRegistration();
    }


    /*
   Mensaje mostrado si la respuesta fue considerada o no.
    */
    public void handleMakeAnswer(int response){
        String mMessage = "";
        Log.d("HeyResponse",String.valueOf(response));
        switch (response) {
            case StudentLessonController.DONE:
                onAnswerSuccess();
                DialogFragment newFragment = new ConfirmOneButtonSupportDialog();
                Bundle bd = new Bundle();
                bd.putString("TITLE", "");
                bd.putString("MESSAGE", getString(R.string.text_send_answer));
                newFragment.setArguments(bd);
                newFragment.setTargetFragment(AnswerSquareFragment.this, 1234);
                newFragment.show(getFragmentManager(), null);
                break;
            case StudentLessonController.NOT_FOUND:
                enableCheck();
                sendNot("",getString(R.string.text_answer_not_used));
                break;
            case StudentLessonController.CANCEL:
                enableCheck();
                sendNot(getString(R.string.text_important),getString(R.string.text_change_one_chance));
                break;
            default:
                enableCheck();
                break;
        }
    }

    /*
    Actualizar paquete de parámetros después de responder la pregunta
     */
    public void onAnswerSuccess(){
        btnShare.setEnabled(true);
        btnShare.setVisibility(View.VISIBLE);
        btnDisabledShare.setVisibility(View.GONE);
        disableCheck();
        taskParcel.setStatus(taskParcel.getStatus()+1);
        if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
            if (taskParcel.getRun() == 1) {
                btnChange.setText(getString(R.string.text_wait_teacher));
            }
        }
        if (appStateParcel.getGuestMode() == AppStateParcel.ACTIVE){
            //btnChange.setText(String.format(getString(R.string.text_thank_owner),
              //      appStateParcel.getUsername()));
        }
    }


}

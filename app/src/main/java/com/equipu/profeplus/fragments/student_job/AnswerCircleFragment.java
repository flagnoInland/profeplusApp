package com.equipu.profeplus.fragments.student_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import com.equipu.profeplus.components.FiveZonesWheel;
import com.equipu.profeplus.controllers.StudentLessonController;
import com.equipu.profeplus.dialogs.AppAnswerDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento fue creado para responder preguntas de cinco alternativas
y preguntas de un banco para el paso 1 y el paso 2.
Este fragmento muestra un rueda con las cinco alternativas y las opciones
CAMBIAR USUARIO
RETORNAR USUARIO
PREGUNTAS RETO
 */
public class AnswerCircleFragment extends Fragment
        implements FiveZonesWheel.ClickOnLetterWheelListener, AppAnswerDialog.OnFireEventAnswerDialogListener {

    @BindView(R.id.header_tmpl) ViewStub vHeader;
    @BindView(R.id.img_fondo) ImageView bckg;
    @BindView(R.id.txt_run_view) TextView runNumber;
    @BindView(R.id.btn_quest) Button btnQuest;
    @BindView(R.id.btn_share) Button btnShare;
    @BindView(R.id.btn_share_disabled) Button btnDisabledShare;
    @BindView(R.id.btn_guest_session) Button btnGuest;
    ImageView btnback;
    ImageView btnRefresh;
    ImageView btnexit;

    @BindView(R.id.wheel) FiveZonesWheel mWheel;

    @BindView(R.id.txt_roulette_action) TextView btnChange;

    private Unbinder unbinder;
    private static int attempts = 1;

    TaskParcel taskParcel, taskGuestParcel, currentTask;
    AppStateParcel appStateParcel, appStateGuestParcel;

    TextView toastNot;

    View layout;

    private IStudentJobListener mListener;
    RegisterSessionReceiver registerSessionReceiver;
    MakeAnswerReceiver makeAnswerReceiver;

    public AnswerCircleFragment() { }


    public static AnswerCircleFragment newInstance(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                                                   AppStateParcel agp) {
        AnswerCircleFragment fragment = new AnswerCircleFragment();
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
        View v = inflater.inflate(R.layout.school_circle_answer, container, false);
        unbinder = ButterKnife.bind(this, v);

        appStateParcel.setOwnerId(appStateParcel.getUserId());
        appStateGuestParcel.setOwnerId(appStateParcel.getUserId());
        currentTask = new TaskParcel();


        // Fijar el paquete de parametros según el usuario
        if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE){
            currentTask = taskParcel;
        } else {
            currentTask = taskGuestParcel;
        }


        // Fijar el nùmero de inentos para responder a cero
        if (currentTask.getCheck()== 0){
            attempts = 1;
        }

        // Cargar la vista para el paso 1
        if (currentTask.getRun() == 1) {
            vHeader.setLayoutResource(R.layout.header_template5);
            View inflated = vHeader.inflate();
            btnRefresh = (ImageView) inflated.findViewById(R.id.btn_refresh_session);
            btnback = (ImageView) inflated.findViewById(R.id.img_back);
            btnexit = (ImageView) inflated.findViewById(R.id.img_exit);
        }

        // Cargar la vista para el paso 2
        if (currentTask.getRun() == 2){
            vHeader.setLayoutResource(R.layout.header_template4);
            View inflated = vHeader.inflate();
            btnback = (ImageView) inflated.findViewById(R.id.img_back);
            btnexit = (ImageView) inflated.findViewById(R.id.img_exit);
        }

        // Ocultar botones segun el paso y el tipo de estudiante
        Log.d("profeplus.st0", String.valueOf(currentTask.getRun()));
        if (currentTask.getRun()== 1 &&
                appStateParcel.getGuestMode() == AppStateParcel.INACTIVE  ){
            btnQuest.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
            btnDisabledShare.setVisibility(View.VISIBLE);
            btnGuest.setVisibility(View.GONE);
        } else if (currentTask.getRun()== 2 &&
                appStateParcel.getGuestMode() == AppStateParcel.INACTIVE  ){
            btnQuest.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.GONE);
            btnDisabledShare.setVisibility(View.VISIBLE);
            btnGuest.setVisibility(View.GONE);
        } else if (currentTask.getRun()== 1 &&
                appStateParcel.getGuestMode() == AppStateParcel.ACTIVE  ) {
            btnQuest.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
            btnDisabledShare.setVisibility(View.GONE);
            //btnGuest.setVisibility(View.VISIBLE);
        } else if (currentTask.getRun()== 2 &&
                appStateParcel.getGuestMode() == AppStateParcel.ACTIVE ) {
            btnQuest.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.GONE);
            btnDisabledShare.setVisibility(View.GONE);
            //btnGuest.setVisibility(View.VISIBLE);
        }


        // Colocar detalles de la pregunta
        runNumber.setTypeface(LearnApp.appFont);
        setLabelRun();

        //mColor = getResources().obtainTypedArray(R.array.images_wheel_colour);
        //mGray = getResources().obtainTypedArray(R.array.images_wheel_grey);

        btnChange.setTypeface(LearnApp.appFont);

        final int[] idx = new int[]{0, 1, 2, 3, 4};
        final String[] letters2 = new String[]{"A", "B", "C", "D", "E"};

        // Mezclar opciones de respuesta
        Random rnd = new Random();
        for (int i = idx.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = idx[index];
            idx[index] = idx[i];
            idx[i] = a;
        }

        mWheel.shuffleLetters();
        mWheel.setClickOnLetterWheelListener(this);

        btnChange.setText(getString(R.string.msg_tap_correct_answer));
        if (appStateParcel.getGuestMode()== AppStateParcel.ACTIVE){
            btnChange.setText(getString(R.string.text_important_only_one_chance));
        }

        btnShare.setEnabled(false);
        btnShare.setVisibility(View.GONE);

        layout = inflater.inflate(R.layout.answer_toast_layout,
                (ViewGroup) v.findViewById(R.id.lin_lay_toast));

        if ( currentTask.getCheck()== 1  ){
            onAnswerSuccess();
            disableButtons();
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

        // Botón para cambiar usuario
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null) {
                    taskParcel = currentTask;
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

        return v;
    }

    // Iniciar el servicio para permitir al estudiante responder la pregunta
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
                taskGuestParcel = currentTask;
                intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskGuestParcel);
                intent.putExtra(LearnApp.PCL_APP_STATE, appStateGuestParcel);
            } else {
                taskParcel = currentTask;
                intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskParcel);
                intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            }
            Log.d("profeplus.st1", String.valueOf(currentTask.getRun()));
            getActivity().startService(intent);
        } else {
            if (mListener!=null) {
                mListener.dialogLostConnection();
            }
        }
    }

    // Receiver para capturar el estado del permiso al estudiante para acceder a la pregunta
    public class RegisterSessionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int userRun, userCheck;
            int status = intent.getIntExtra(NetworkStudentService.STATUS, 0);
            if (appStateParcel.getGuestMode() == AppStateParcel.ACTIVE &&
                    status == StudentLessonController.DONE){
                taskGuestParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
                Log.d("profeplus.st2", String.valueOf(taskGuestParcel.getRun()));
                userCheck = taskGuestParcel.getCheck();
                userRun = taskGuestParcel.getRun();
                if (userCheck == 0 && userRun == 2){
                    taskGuestParcel.setStatus(TaskParcel.ANS_NEW);
                    if (mListener!=null) {
                        mListener.startQuestContent(taskParcel, appStateParcel, taskGuestParcel,
                                appStateGuestParcel);
                    }
                } else if (userCheck == 1 && userRun == 2){
                    //getActivity().onBackPressed();
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
                } else if (userCheck == 1 && userRun == 2){
                    //getActivity().onBackPressed();
                }
            }
        }
    }

    // Mostar detalles del paso
    private void setLabelRun(){
        String mRunNumber = "";
        if (currentTask.getRun() == 1) {
            bckg.setBackgroundResource(R.drawable.fondopregunta1);
            mRunNumber = String.format("%s %d : %s",
                    getString(R.string.text_step),
                    currentTask.getRun(),
                    getString(R.string.text_individual_answer));
        } else {
            bckg.setBackgroundResource(R.drawable.fondopregunta2);
            if (currentTask.getQuestionMode() == TaskParcel.M_PEER) {
                mRunNumber = String.format("%s %d : %s",
                        getString(R.string.text_step),
                        currentTask.getRun(),
                        getString(R.string.text_peer_instruction));
            } else {
                mRunNumber = String.format("%s %d : %s",
                        getString(R.string.text_step),
                        currentTask.getRun(),
                        getString(R.string.text_team_work));
            }
        }
        runNumber.setText(mRunNumber);
    }

    // Mandar respuesta con click del usuario
    @Override
    public void onClickLetter(String answer) {
        newAnswer(answer);
    }


    // Bloquear botones
    private void disableButtons() {
        mWheel.setEnabled(false);
    }

    // Activar botones
    private void enableButtons() {
        mWheel.setEnabled(true);
    }

    // Colorear ruedar y activar botones
    private void restartWheel(){
        mWheel.rearrangeColors();
        mWheel.colorize();
        enableButtons();
    }

    // Inicia servicio par enviar repuesta del estudiante
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

    // Mostrar diálogo para confirmar respuesta
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
        newFragment.setTargetFragment(AnswerCircleFragment.this,1234);
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
                currentTask.setLastAnswer(answer);
                currentTask.setCheck(1);
                if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
                    taskParcel = currentTask;
                } else {
                    taskGuestParcel = currentTask;
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


    // Mostrar mensaje luego de confirmar el envío de respuesta o cancelarlo
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


    // Cambiar paquetes de parámetres luego de retornar al propietario
    public void onOwnerLogged(){
        appStateParcel.setGuestMode(AppStateParcel.INACTIVE);
        taskGuestParcel = currentTask;
        currentTask = taskParcel;
        if (appStateParcel.getUserType() == AppStateParcel.STUDENT) {
            if (currentTask.getRun() == 1){
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
        // Go to step 2
        startRegistration();
    }



    // Mensaje mostrado si la respuesta fue considerada o no.
    public void handleMakeAnswer(int response){
        switch (response) {
            case StudentLessonController.DONE:
                if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
                    btnShare.setEnabled(true);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onOwnerLogged();
                        }
                    }, 500);
                }
                onAnswerSuccess();
                disableButtons();
                showNotification(getString(R.string.text_send_answer));
                break;
            case StudentLessonController.NOT_FOUND:
                restartWheel();
                sendNot("",getString(R.string.text_answer_not_used));
                break;
            case StudentLessonController.CANCEL:
                restartWheel();
                sendNot(getString(R.string.text_important),getString(R.string.text_change_one_chance));
                break;
            default:
                //mImageAnswer.setImageDrawable(mColor.getDrawable(ansImagePos));
                mWheel.colorize();
                break;
        }
    }


    // Actualizar paquete de parámetros después de responder la pregunta
    public void onAnswerSuccess(){
        if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
            btnShare.setEnabled(true);
            btnShare.setVisibility(View.VISIBLE);
            btnDisabledShare.setVisibility(View.GONE);
        }
        mWheel.setGrayColors();
        currentTask.setStatus(currentTask.getStatus()+1);
        if (appStateParcel.getGuestMode() == AppStateParcel.INACTIVE) {
            taskParcel = currentTask;
            if (taskParcel.getRun() == 1) {
                btnChange.setText(getString(R.string.text_wait_teacher));
            }
        }
        if (appStateParcel.getGuestMode() == AppStateParcel.ACTIVE){
            taskGuestParcel = currentTask;
            //btnChange.setText(String.format(getString(R.string.text_thank_owner),
              //      appStateParcel.getUsername()));

        }
    }



}

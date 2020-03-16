package com.equipu.profeplus.fragments.board;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkBoardService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IUserBoardListener;
import com.equipu.profeplus.controllers.StudentLessonController;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;
import com.equipu.profeplus.models.UserParcel;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento se usa para presentar a los estudiantes de educación superior
un pantalla con las opciones:
ENTRAR A CLASE
CAMBIAR MODO
EDITAR PERFIL
INFORMACIÓN DEL APP
VER MANUAL DE USUARIO
 */
public class StudentBoardFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener {

    //private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;


    @BindView(R.id.start_task_btn) Button btnStudentTask;
    @BindView(R.id.start_eval_btn) Button btnEvalTask;
    @BindView(R.id.btn_read_manual) Button btnStudentManual;
    @BindView(R.id.welcome_code_msg) TextView welcome;
    @BindView(R.id.edtinputcode) EditText edtCodeTeacher;
    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.btn_edit_user) ImageView btnEditUser;
    @BindView(R.id.btn_becas) ImageView btnBecas;
    @BindView(R.id.btn_user_mode) ImageView btnUserMode;
    @BindView(R.id.btn_share) ImageView btnShare;
    @BindView(R.id.img_iphone) ImageView btnIPhone;
    @BindView(R.id.txt_welcome) TextView helloName;
    private Unbinder unbinder;


    TaskParcel taskParcel;
    AppStateParcel appStateParcel;
    UpdateUserReceiver updateUserReceiver;
    RegisterSessionReceiver registerSessionReceiver;

    private IUserBoardListener mListener;
    private ProgressBar mProgress;

    public StudentBoardFragment() {
        // Required empty public constructor
    }

    public static StudentBoardFragment newInstance(AppStateParcel ap, TaskParcel tp) {
        StudentBoardFragment fragment = new StudentBoardFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
        args.putParcelable(LearnApp.PCL_TASK_PARCEL, tp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LearnApp application = (LearnApp) getActivity().getApplication();
        //mTracker = application.getDefaultTracker();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
            taskParcel = getArguments().getParcelable(LearnApp.PCL_TASK_PARCEL);
        }


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_board, container, false);
        unbinder = ButterKnife.bind(this, v);

        mProgress = (ProgressBar) v.findViewById(R.id.progress_bar);

        mListener.updateUserMode();
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        taskParcel = new TaskParcel();

        welcome.setTypeface(LearnApp.appFont);
        btnStudentTask.setTypeface(LearnApp.appFont1);
        edtCodeTeacher.setTypeface(LearnApp.appFont);
        btnStudentManual.setTypeface(LearnApp.appFont1);
        helloName.setTypeface(LearnApp.appFont1);

        // Mostrar el nombre del usuario
        if (appStateParcel.getGender().equals("Female")
                || appStateParcel.getGender().equals("Femenino")) {
            helloName.setText(String.format(getString(R.string.msg_hello_username_f),
                    appStateParcel.getUsername()));
        } else {
            helloName.setText(String.format(getString(R.string.msg_hello_username),
                    appStateParcel.getUsername()));
        }

        // Obtener clave de acceso a la pregunta
        // Cargar pregunta despues de ingresar los 4 digitos
        // En caso de falla mostrar un mensaje
        edtCodeTeacher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                welcome.setVisibility(View.INVISIBLE);
                if (s.length() == 0){
                    welcome.setVisibility(View.VISIBLE);
                }
                if (s.length() == 4){
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtCodeTeacher.getWindowToken(), 0);
                    enterToClass();
                }
            }
        });

        // Botón para entrar en clase. Invisible
        btnStudentTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterToClass();
            }
        });

        // Botón para acceder a evaluaciones
        btnEvalTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterToExam();
            }
        });

        // Botón para leer los manuales
        btnStudentManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.readStudentManual();
            }
        });


        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    Intent intent = new Intent(getActivity(), NetworkBoardService.class);
                    intent.putExtra(NetworkBoardService.SERVICE, NetworkBoardService.SERVICE_GET_USER);
                    intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                    getActivity().startService(intent);
                } else {
                    mListener.dialogLostConnection();
                }
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                //mListener.goToLogin(appStateParcel);
            }
        });

        btnBecas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToBecas();
            }
        });

        btnUserMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setUserMode(appStateParcel);
            }
        });

        //Botón compartir
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToShare();
            }
        });

        btnIPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", getResources().getString(R.string.text_iphone_users));
                b.putString("MESSAGE", getResources().getString(R.string.msg_iphone_users_hint));
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), null);
            }
        });

        return v;
    }

    /*
   Iniciar evaluaciones en el navegador
    */
    public void enterToExam(){
        String mUrl = String.format( "%s/web/exam/student/%s/1",
                LearnApp.baseURL, appStateParcel.getUserId());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mUrl));
        startActivity(intent);
    }

    /*
   Método para cargar la pregunta con el código
    */
    public void enterToClass(){
        mProgress.setVisibility(View.VISIBLE);
        mProgress.setProgress(0);
        appStateParcel.setOwnerId(appStateParcel.getUserId());
        taskParcel.setAccesscode(edtCodeTeacher.getText().toString());
        if (taskParcel.getAccesscode().equals("")){
            DialogFragment newFragment = new NotificationOneButtonSupportDialog();
            Bundle b = new Bundle();
            b.putString("TITLE", getResources().getString(R.string.text_notification));
            b.putString("MESSAGE", getResources().getString(R.string.msg_you_didnt_write_code));
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), null);
        }else{
            taskParcel.setLastAnswer("Z");
            ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()
                    && networkInfo.isAvailable()) {
                Intent intent = new Intent(getActivity(), NetworkBoardService.class);
                intent.putExtra(NetworkBoardService.SERVICE,
                        NetworkBoardService.SERVICE_REGISTER_SESSION);
                intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                intent.putExtra(LearnApp.PCL_TASK_PARCEL, taskParcel);
                getActivity().startService(intent);
            } else {
                mListener.dialogLostConnection();
            }
        }
    }

    /*
    Receiver para capturar el estado de la pregunta segun el código.
    Si se adquiere la pregunta pasa a la pantalla de repuestas
    Si la pregunta no esta disponible mostar mensaje
     */
    public class RegisterSessionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkBoardService.STATUS,0);
            mProgress.setVisibility(View.INVISIBLE);
            if (status == StudentLessonController.DONE) {
                taskParcel = intent.getParcelableExtra(LearnApp.PCL_TASK_PARCEL);
                if (taskParcel.getQuestionType() == TaskParcel.Q_EVAL &&
                        taskParcel.getLeftTime() < 0){
                    edtCodeTeacher.setText("");
                    messageWrongCode();
                } else {
                    mListener.startStudentSession(taskParcel);
                }
            } else {
                edtCodeTeacher.setText("");
                messageWrongCode();
            }
        }
    }


    private void messageWrongCode(){
        DialogFragment newFragment = new NotificationOneButtonSupportDialog();
        Bundle b = new Bundle();
        b.putString("TITLE", getResources().getString(R.string.text_notification));
        b.putString("MESSAGE", getResources().getString(R.string.msg_wrong_code_session_over));
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), null);
    }


    public class UpdateUserReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkBoardService.STATUS,0);
            if (status == UserController.DONE) {
                UserParcel userParcel = intent.getParcelableExtra(LearnApp.PCL_USER_PARCEL);
                appStateParcel.setEditUser(1);
                mListener.updateProfile(appStateParcel, userParcel);
            } else if (status == UserController.CONNECTION){
                mListener.dialogLostConnection();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //mTracker.setScreenName("Panel::Estudiante");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // Registrar acción en firebase
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "20");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PANEL::Estudiante~Superior");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "PANEL::Estudiante~Superior");
        // Filtrar acción para acquisicion de pregunta
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        IntentFilter filter = new IntentFilter(NetworkBoardService.ACTION_GET_USER);
        IntentFilter filter1 = new IntentFilter(NetworkBoardService.ACTION_REGISTER_SESSION);
        updateUserReceiver = new UpdateUserReceiver();
        registerSessionReceiver = new RegisterSessionReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(updateUserReceiver, filter);
        lbm.registerReceiver(registerSessionReceiver, filter1);
    }


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(updateUserReceiver);
        lbm.unregisterReceiver(registerSessionReceiver);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IUserBoardListener) {
            mListener = (IUserBoardListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IUserBoardListener");
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


    @Override
    public void onFireCancelEventTwoButtonListener() {

    }

    @Override
    public void onFireAcceptEventTwoButtonListener() {
        mListener.goToLogin(appStateParcel);
    }

}

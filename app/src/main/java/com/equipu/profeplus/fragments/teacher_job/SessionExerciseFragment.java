package com.equipu.profeplus.fragments.teacher_job;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkTeacherService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITeacherJobListener;
import com.equipu.profeplus.controllers.TeacherLessonController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.SessionParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento permite fijar el número de tema y el número de ejercicio
para un pregunta que se encuentra en un banco.
 */
public class SessionExerciseFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener {

    @BindView(R.id.txt_session_code) TextView txtSessionCode;
    @BindView(R.id.txt_bank_allow) TextView txtBankAllow;
    @BindView(R.id.txt_topic_number) TextView txtTopicNumber;
    @BindView(R.id.txt_exercise_number) TextView txtExerciseNumber;
    @BindView(R.id.edt_topic_number) EditText edtSubject;
    @BindView(R.id.edt_exercise_number) EditText edtExercise;
    @BindView(R.id.btn_results_chart) Button btnResult;
    @BindView(R.id.img_back) ImageView btnback;
    @BindView(R.id.img_exit) ImageView btnexit;
    private Unbinder unbinder;

    SessionParcel sessionParcel;
    AppStateParcel appStateParcel;
    long now, elapsed;

    private ITeacherJobListener mListener;
    NewSessionReceiverE newSessionReceiver;

    public SessionExerciseFragment() { }


    public static SessionExerciseFragment newInstance(SessionParcel sp, AppStateParcel asp) {
        SessionExerciseFragment fragment = new SessionExerciseFragment();
        Bundle args = new Bundle();
        args.putParcelable("SessionParcel", sp);
        args.putParcelable("AppStateParcel", asp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sessionParcel = getArguments().getParcelable("SessionParcel");
            appStateParcel = getArguments().getParcelable("AppStateParcel");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_session_exercise, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtSessionCode.setTypeface(LearnApp.appFont);
        txtSessionCode.setText(String.format("%s : %s",getString(R.string.text_uni_code),sessionParcel.getAccesscode()));
        txtSessionCode.setVisibility(View.GONE);

        txtBankAllow.setTypeface(LearnApp.appFont);
        txtTopicNumber.setTypeface(LearnApp.appFont);
        txtExerciseNumber.setTypeface(LearnApp.appFont);
        edtSubject.setTypeface(LearnApp.appFont);
        edtExercise.setTypeface(LearnApp.appFont);
        btnResult.setTypeface(LearnApp.appFont);

        // Obtener el número de tema
        edtSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {  }

            @Override
            public void afterTextChanged(Editable s) {
                txtTopicNumber.setVisibility(View.INVISIBLE);
                if (s.length() == 0){
                    txtTopicNumber.setVisibility(View.VISIBLE);
                }
            }
        });

        // Obtener el número de ejercicio
        edtExercise.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                txtExerciseNumber.setVisibility(View.INVISIBLE);
                if (s.length() == 0){
                    txtExerciseNumber.setVisibility(View.VISIBLE);
                }
            }
        });

        edtSubject.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String mstring = edtSubject.getText().toString();
                    if (!mstring.equals("")) {
                        mstring = String.format("%02d", Integer.parseInt(mstring));
                        edtSubject.setText(mstring);
                    }
                }
                return false;
            }
        });

        edtExercise.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    String mstring = edtExercise.getText().toString();
                    if (!mstring.equals("")) {
                        mstring = String.format("%02d", Integer.parseInt(mstring));
                        edtExercise.setText(mstring);
                    }
                }
                return false;
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(btnResult.getWindowToken(), 0);
                    int exNum;
                    int sbNum;
                    if (edtSubject.getText().toString().equals("")){
                        exNum = 0;
                    } else {
                        exNum = Integer.parseInt(edtSubject.getText().toString());
                    }
                    if (edtExercise.getText().toString().equals("")){
                        sbNum = 0;
                    } else {
                        sbNum = Integer.parseInt(edtExercise.getText().toString());
                    }
                    if ( exNum != 0 && sbNum != 0 ) {
                        sessionParcel.setExercise(String.valueOf(exNum));
                        sessionParcel.setSubject(String.valueOf(sbNum));
                        //mListener.setCodeSession(sessionParcel);
                        checkSameSession();
                        /*
                        if (appStateParcel.getChangeExercise()== 1) {
                            NewSessionTask newSessionTask = new NewSessionTask();
                            newSessionTask.execute(new String[]{appStateParcel.getToken()});
                        } else {
                            UpdateSessionTask updateSessionTask = new UpdateSessionTask();
                            updateSessionTask.execute(new String[]{appStateParcel.getToken()});
                        }
                        */

                    } else {
                        DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                        Bundle b = new Bundle();
                        b.putString("TITLE", getString(R.string.text_alerta));
                        b.putString("MESSAGE", getString(R.string.msg_numbers_greater_zero));
                        newFragment.setArguments(b);
                        newFragment.show(getFragmentManager(), "failure");
                    }
                }
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mListener.finishExercise(sessionParcel);
                getActivity().onBackPressed();
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mListener.finishLesson(sessionParcel);
                mListener.backToFreeQuestion();

            }
        });

        now = System.currentTimeMillis();
        elapsed = (now - appStateParcel.getStartTime())/(60*1000);

        return v;

    }

    /*
    Iniciar servico para crear una leccion con un nuevo codigo o
    mostar diálogo de confirmación
     */
    public void checkSameSession(){
        boolean isZero = appStateParcel.getSessionCode().equals("0000");
        int keepCode = appStateParcel.getKeepCode();
        if (isZero) {
            appStateParcel.setKeepCode(1);
            mListener.setCodeSession(sessionParcel);
            /*
        } else if( elapsed > 30 && !isZero && keepCode == 1 ) {
            showDialogQuestion();
        } else if( elapsed > 30 && !isZero && keepCode == 2 ){
            sessionParcel.setInactive(0);
            startFragmentCoded();
        } else if( elapsed > 60 && !isZero && keepCode == 2 ) {
            showDialogQuestion();
        } else if( elapsed > 60 && !isZero && keepCode == 3 ){
            sessionParcel.setInactive(0);
            startFragmentCoded();
        } else if( elapsed > 90 && !isZero && keepCode == 3 ) {
            showDialogQuestion();
        } else if( elapsed > 90 && !isZero && keepCode == 4 ){
            sessionParcel.setInactive(0);
            startFragmentCoded();
            */
        } else  if (elapsed > 120 && !isZero){
            appStateParcel.setKeepCode(1);
            appStateParcel.setStartTime(System.currentTimeMillis());
            appStateParcel.setSessionCode("0000");
            mListener.setCodeSession(sessionParcel);
        } else {
            showDialogQuestion();
            //sessionParcel.setInactive(0);
            //startFragmentCoded();
        }
    }


    /*
    Dialógo para confirmar si se usa el mismo código
     */
    public void showDialogQuestion(){
        DialogFragment newFragment =
                new ConfirmTwoButtonsSupportDialog();
        Bundle bd = new Bundle();
        bd.putString("TITLE", "");
        bd.putString("MESSAGE", getString(R.string.text_is_same_lesson));
        newFragment.setArguments(bd);
        newFragment.setTargetFragment(SessionExerciseFragment.this,1234);
        newFragment.show(getFragmentManager(), "confirm");
    }

    @Override
    public void onFireCancelEventTwoButtonListener() {
        appStateParcel.setKeepCode(1);
        appStateParcel.setStartTime(System.currentTimeMillis());
        appStateParcel.setSessionCode("0000");
        sessionParcel.setAccesscode("0000");
        mListener.updateSessionCode(appStateParcel, sessionParcel);
        mListener.setCodeSession(sessionParcel);
    }

    // Pantalla a mostar si se mantiene el código o no
    @Override
    public void onFireAcceptEventTwoButtonListener() {
        /*
        if ( elapsed > 30 ){
            appStateParcel.setKeepCode(2);
        } else if ( elapsed > 60 ){
            appStateParcel.setKeepCode(3);
        } else if ( elapsed > 90 ){
            appStateParcel.setKeepCode(4);
        }
        */
        appStateParcel.setKeepCode(2);
        sessionParcel.setInactive(0);
        startFragmentCoded();
    }

    /*
    Iniciar servicio para crear una lección con el mismo código
     */
    public void startFragmentCoded(){
        sessionParcel.setAccesscode(appStateParcel.getSessionCode());
        Intent intent = new Intent(getActivity(), NetworkTeacherService.class);
        intent.putExtra(NetworkTeacherService.SERVICE,
                NetworkTeacherService.SERVICE_BANK_LESSON);
        intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
        intent.putExtra(LearnApp.PCL_SESSION_PARCEL, sessionParcel);
        getActivity().startService(intent);
    }


    /*
    Receiver para capturar el estado del a creación de una pregunta/lección
    con el mismo código
     */
    public class NewSessionReceiverE extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(NetworkTeacherService.STATUS, 0);
            if (status == TeacherLessonController.DONE) {
                sessionParcel = intent.getParcelableExtra(LearnApp.PCL_SESSION_PARCEL);
                appStateParcel.setSessionCode(sessionParcel.getAccesscode());
                mListener.updateSessionCode(appStateParcel, sessionParcel);
                mListener.showResults(sessionParcel);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkTeacherService.ACTION_BANK_LESSON);
        newSessionReceiver = new NewSessionReceiverE();;
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(newSessionReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(newSessionReceiver);
    }


    public class NewSessionTask extends AsyncTask<String,Void,Void> {

        TeacherLessonController teacherLessonController;

        @Override
        protected Void doInBackground(String... params) {
            String token = params[0];
            teacherLessonController = new TeacherLessonController();
            teacherLessonController.createNewSession(sessionParcel, appStateParcel);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (teacherLessonController.getStatus()== TeacherLessonController.DONE){
                SessionParcel sessionParcel2 = teacherLessonController.getSessionParcel();
                sessionParcel.setSessionId(sessionParcel2.getSessionId());
                mListener.showResults(sessionParcel);
            }
        }
    }


    public class UpdateSessionTask extends AsyncTask<String,Void,Void> {

        TeacherLessonController teacherLessonController;

        @Override
        protected Void doInBackground(String... params) {
            String token = params[0];
            teacherLessonController = new TeacherLessonController();
            teacherLessonController.updateSession(sessionParcel, token);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (teacherLessonController.getStatus()== TeacherLessonController.DONE){
                mListener.showResults(sessionParcel);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ITeacherJobListener) {
            mListener = (ITeacherJobListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ITeacherJobListener");
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

}

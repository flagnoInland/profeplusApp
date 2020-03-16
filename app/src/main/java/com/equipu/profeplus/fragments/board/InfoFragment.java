package com.equipu.profeplus.fragments.board;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkBoardService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IUserBoardListener;
import com.equipu.profeplus.controllers.UserController;
import com.equipu.profeplus.dialogs.ConfirmTwoButtonsSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento se usa para presentar los manuales disponibles a
los usuarios
 */
public class InfoFragment extends Fragment
        implements ConfirmTwoButtonsSupportDialog.OnFireEventTwoButtonDialogListener {


    @BindView(R.id.btn_motivation) Button btnMotiv;
    @BindView(R.id.btn_comite) Button btnComit;


    @BindView(R.id.btn_user_manual) Button btnUserManual;
    @BindView(R.id.btn_edit_profile) Button btnEditProfile;
    @BindView(R.id.btn_close_session) Button btnCloseSession;

    @BindView(R.id.progress_bar) ProgressBar progressBar;


    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;

    AppStateParcel appStateParcel;
    private Unbinder unbinder;
    private IUserBoardListener mListener;
    UpdateUserReceiver updateUserReceiver;

    public InfoFragment() {
        // Required empty public constructor
    }


    public static InfoFragment newInstance(AppStateParcel appStateParcel) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        unbinder = ButterKnife.bind(this, v);

        btnUserManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appStateParcel.getUserType() == AppStateParcel.STUDENT) {
                    mListener.readStudentManual();
                } else {
                    mListener.readTeacherManual();
                }

            }
        });

        // Info sobre el comite
        btnComit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mListener.readComiteInfo();
                mListener.readProducersInfo();

            }
        });

        // Info sobre los objetivos
        btnMotiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.readMotivInfo();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()
                        && networkInfo.isAvailable()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getActivity(), NetworkBoardService.class);
                    intent.putExtra(NetworkBoardService.SERVICE,
                            NetworkBoardService.SERVICE_GET_USER);
                    intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
                    getActivity().startService(intent);
                } else {
                    mListener.dialogLostConnection();
                }
            }
        });

        btnCloseSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ConfirmTwoButtonsSupportDialog();
                Bundle bd = new Bundle();
                bd.putString("TITLE", getString(R.string.text_confirm));
                bd.putString("MESSAGE", getString(R.string.text_want_logout));
                newFragment.setArguments(bd);
                newFragment.setTargetFragment(InfoFragment.this,1234);
                newFragment.show(getFragmentManager(), "answer");
            }
        });

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkBoardService.ACTION_GET_USER);
        updateUserReceiver = new UpdateUserReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(updateUserReceiver, filter);
    }


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(updateUserReceiver);
    }


    public class UpdateUserReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
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

package com.equipu.profeplus.fragments.board;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.NetworkBoardService;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IUserBoardListener;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Portotipo para mostrar una pantalla cambiar de modd par usuario de educación superior
 */
public class ProfileModeFragment extends Fragment {

    @BindView(R.id.btn_uni_student) ImageView uniStudent;
    @BindView(R.id.btn_uni_teacher) ImageView uniTeacher;
    @BindView(R.id.btn_school_teacher) ImageView schoolTeacher;
    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;
    private Unbinder unbinder;

    AppStateParcel appStateParcel;
    EditUserReceiver editUserReceiver;

    private IUserBoardListener mListener;

    public ProfileModeFragment() {
        // Required empty public constructor
    }


    public static ProfileModeFragment newInstance(AppStateParcel appStateParcel) {
        ProfileModeFragment fragment = new ProfileModeFragment();
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
        View v = inflater.inflate(R.layout.fragment_profile_mode, container, false);
        unbinder = ButterKnife.bind(this, v);

        uniStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appStateParcel.setAppMode(AppStateParcel.NORMAL_MODE);
                appStateParcel.setUserType(AppStateParcel.STUDENT);
                startEditUser();
            }
        });

        schoolTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appStateParcel.setAppMode(AppStateParcel.SCHOOL_MODE);
                appStateParcel.setUserType(AppStateParcel.TEACHER);
                startEditUser();
            }
        });

        uniTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appStateParcel.setAppMode(AppStateParcel.NORMAL_MODE);
                appStateParcel.setUserType(AppStateParcel.TEACHER);
                startEditUser();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToLogin(appStateParcel);
            }
        });

        return v;
    }

    public void startEditUser(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            Intent intent = new Intent(getActivity(), NetworkBoardService.class);
            intent.putExtra(NetworkBoardService.SERVICE, NetworkBoardService.SERVICE_CHANGE_MODE);
            intent.putExtra(LearnApp.PCL_APP_STATE, appStateParcel);
            getActivity().startService(intent);
        } else {
            mListener.dialogLostConnection();
        }
    }

    public class EditUserReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            mListener.reloadUserBoard(appStateParcel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NetworkBoardService.ACTION_CHANGE_MODE);
        editUserReceiver = new EditUserReceiver();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.registerReceiver(editUserReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(editUserReceiver);
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

}

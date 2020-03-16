package com.equipu.profeplus.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IChangeUserListener;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento muestra una vista con dos botones rectangulares para elegir el modo:
EDUCACION BASICA
EDUCACION SUPERIOR
Esta vista solo se mostrará la primera que se instala el app.
Esta vista también es invocada cuando se cambia de modo.
 */
public class AppModeFragment extends Fragment {

    @BindView(R.id.btn_uni_mode) Button uniMode;
    @BindView(R.id.btn_school_mode) Button schoolMode;
    private Unbinder unbinder;

    AppStateParcel appStateParcel;

    private IChangeUserListener mListener;

    public AppModeFragment() {}


    public static AppModeFragment newInstance(AppStateParcel appStateParcel) {
        AppModeFragment fragment = new AppModeFragment();
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
        View v = inflater.inflate(R.layout.fragment_app_mode, container, false);
        unbinder = ButterKnife.bind(this, v);

        // Botón para el modo educación básica
        schoolMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appStateParcel.setAppMode(AppStateParcel.SCHOOL_MODE);
                mListener.startUserMode(appStateParcel);
            }
        });


        // Botón para el modo educación superior
        uniMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appStateParcel.setAppMode(AppStateParcel.NORMAL_MODE);
                mListener.startUserMode(appStateParcel);
            }
        });

        return v;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IChangeUserListener) {
            mListener = (IChangeUserListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IChangeUserListener");
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

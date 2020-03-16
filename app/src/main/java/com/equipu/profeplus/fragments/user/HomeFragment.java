package com.equipu.profeplus.fragments.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStartListener;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento inicializa una vista con tres botones con las opciones
INICIAR SESION
REGISTRARSE
RECUPERAR
Esta vista será la pantalla principal del app
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    @BindView(R.id.btn_start_login) Button startLogin;
    @BindView(R.id.btn_sign_up) Button signUp;
    @BindView(R.id.btn_recover) Button recover;
    private Unbinder unbinder;

    AppStateParcel appStateParcel;

    private IStartListener mListener;

    public HomeFragment() {    }


    public static HomeFragment newInstance(AppStateParcel appStateParcel) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, v);

        // Botón para iniciar sesión
        startLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startLogin(appStateParcel);
            }
        });

        // Botón para registarr nuevo usuario
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startSignUp(appStateParcel);
            }
        });

        // Botón para cambiar contraseña
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startRecover(appStateParcel);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences("userSession", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("PASSWORD");
                editor.commit();
                getActivity().onBackPressed();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getActivity().getSharedPreferences("userSession", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("PASSWORD");
                editor.commit();
                getActivity().finish();
            }
        });



        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IStartListener) {
            mListener = (IStartListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IStartListener");
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

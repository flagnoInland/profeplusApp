package com.equipu.profeplus.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento muestra un mensaje de mantenimiento
 */
public class MaintenanceFragment extends Fragment {


    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;

    AppStateParcel appStateParcel;
    private Unbinder unbinder;

    public MaintenanceFragment() {
        // Required empty public constructor
    }


    public static MaintenanceFragment newInstance(AppStateParcel ap) {
        MaintenanceFragment fragment = new MaintenanceFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_APP_STATE, ap);
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
        View v = inflater.inflate(R.layout.fragment_maintenance, container, false);
        unbinder = ButterKnife.bind(this, v);

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


        return v;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

package com.equipu.profeplus.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IChangeUserListener;
import com.equipu.profeplus.activities.StartActivity;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento crea una vista con dos botones circulares para escoger
el rol del usuario:
ESTUDIANTE
PROFESOR
Las figura cambian de acuerdo con el modo escogido previamente.
 */
public class GeneralUserModeFragment extends Fragment {

    @BindView(R.id.btn_user_teacher) ImageView userTeacher;
    @BindView(R.id.btn_user_student) ImageView userStudent;
    private Unbinder unbinder;

    AppStateParcel appStateParcel;

    private IChangeUserListener mListener;

    public GeneralUserModeFragment() {}


    public static GeneralUserModeFragment newInstance(AppStateParcel appStateParcel) {
        GeneralUserModeFragment fragment = new GeneralUserModeFragment();
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
        View v = inflater.inflate(R.layout.fragment_general_user_mode, container, false);
        unbinder = ButterKnife.bind(this, v);

        final String myActivity = getActivity().getClass().getSimpleName();

        if (appStateParcel.getAppMode() == AppStateParcel.SCHOOL_MODE){
            userTeacher.setImageResource(R.drawable.ic_teacher2);
        }

        // Botón rol estudiante
        userStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appStateParcel.setUserType(AppStateParcel.STUDENT);
                if (myActivity.equals(StartActivity.class.getSimpleName())){
                    if (appStateParcel.getNewInstall() == 1){
                        mListener.startHome(appStateParcel);
                    } else {
                        mListener.newSignUp(appStateParcel);
                    }

                    /*
                    if (appStateParcel.getAppMode() == AppStateParcel.NORMAL_MODE) {
                        mListener.startHome(appStateParcel);
                    } else {
                        mListener.goToStudentBoard(appStateParcel);
                    }
                    */
                } else {
                    mListener.reloadUserBoard(appStateParcel);
                }

            }
        });

        // Botón rol profesor
        userTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appStateParcel.setUserType(AppStateParcel.TEACHER);
                if (myActivity.equals(StartActivity.class.getSimpleName())){
                    //mListener.startHome(appStateParcel);
                    if (appStateParcel.getNewInstall() == 1){
                        mListener.startHome(appStateParcel);
                    } else {
                        mListener.newSignUp(appStateParcel);
                    }
                } else {
                    mListener.reloadUserBoard(appStateParcel);
                }
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

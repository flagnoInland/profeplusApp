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
import com.equipu.profeplus.activities.IStartListener;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class UniUserModeFragment extends Fragment {

    @BindView(R.id.btn_user_teacher) ImageView userTeacher;
    @BindView(R.id.btn_user_student) ImageView userStudent;
    private Unbinder unbinder;

    private UserParcel userParcel;
    private AppStateParcel appStateParcel;

    private IStartListener mListener;

    public UniUserModeFragment() {
        // Required empty public constructor
    }


    public static UniUserModeFragment newInstance(AppStateParcel appStateParcel, UserParcel userParcel) {
        UniUserModeFragment fragment = new UniUserModeFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_USER_PARCEL,userParcel);
        args.putParcelable(LearnApp.PCL_APP_STATE, appStateParcel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
            userParcel = getArguments().getParcelable(LearnApp.PCL_USER_PARCEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_uni_user_mode, container, false);

        unbinder = ButterKnife.bind(this, v);

        // Inciar paso 4 para el estudiante
        userStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userParcel.setTeacher(0);
                appStateParcel.setUserType(AppStateParcel.STUDENT);
                mListener.goToStepFourSignUp(userParcel);
            }
        });

        // Registrar profesor en la base de datos
        userTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userParcel.setTeacher(1);
                appStateParcel.setUserType(AppStateParcel.TEACHER);
                mListener.registerUniTeacher(userParcel);
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

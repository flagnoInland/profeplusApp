package com.equipu.profeplus.fragments.student_job;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStudentJobListener;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.TaskParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento se usa para mostrar las tareas a realizar antes de comenzar el paso 2.
 */
public class AnswerStepTwoFragment extends Fragment {

    @BindView(R.id.txt_content) TextView content;
    @BindView(R.id.img_back) ImageView btnback;
    @BindView(R.id.img_exit) ImageView btnexit;

    @BindView(R.id.btn_accept) TextView btnAccept;
    private Unbinder unbinder;
    TaskParcel taskParcel, taskGuestParcel;
    AppStateParcel appStateParcel, appStateGuestParcel;

    private IStudentJobListener mListener;

    public AnswerStepTwoFragment() {
        // Required empty public constructor
    }


    public static AnswerStepTwoFragment newInstance(TaskParcel tp, AppStateParcel ap, TaskParcel tgp,
                                                    AppStateParcel agp) {
        AnswerStepTwoFragment fragment = new AnswerStepTwoFragment();
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
        View v = inflater.inflate(R.layout.fragment_answer_step_two, container, false);
        unbinder = ButterKnife.bind(this, v);

        content.setTypeface(LearnApp.appFont);

        // Botón para comenzar el paso 2
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null) {
                    mListener.goToStepTwo(taskParcel, appStateParcel,
                            taskGuestParcel,appStateGuestParcel);
                }
            }
        });

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

        return v;
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

}

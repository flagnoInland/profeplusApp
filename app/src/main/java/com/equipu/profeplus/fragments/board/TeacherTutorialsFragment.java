package com.equipu.profeplus.fragments.board;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IUserBoardListener;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragment sirve para presenta los tutoriales del profesor
PROTOCOLO
MANUAL DE USUARIO
BANCO DE PREGUNTAS
 */
public class TeacherTutorialsFragment extends Fragment {

    @BindView(R.id.btn_read_protocol) Button btnProtocol;
    @BindView(R.id.btn_read_manual) Button btnManual;
    @BindView(R.id.btn_read_question) Button btnQuestion;
    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;
    private Unbinder unbinder;

    AppStateParcel appStateParcel;
    private IUserBoardListener mListener;

    public TeacherTutorialsFragment() {
        // Required empty public constructor
    }


    public static TeacherTutorialsFragment newInstance(AppStateParcel appStateParcel) {
        TeacherTutorialsFragment fragment = new TeacherTutorialsFragment();
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
        View v = inflater.inflate(R.layout.fragment_teacher_tutorials, container, false);
        unbinder = ButterKnife.bind(this, v);

        // Botón para el protocolo
        btnProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.readProtocol();
            }
        });

        // Botón para leer el manual del profesor
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.readTeacherManual();
            }
        });

        // Botón para leer como usar un banco de preguntas
        btnQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.readAskQuestion();
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


        return v;
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

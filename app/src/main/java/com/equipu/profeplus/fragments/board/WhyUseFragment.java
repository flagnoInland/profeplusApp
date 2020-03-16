package com.equipu.profeplus.fragments.board;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento fue creado especialemente para mostar
los beneficios de uso del app
 */
public class WhyUseFragment extends Fragment {


    @BindView(R.id.txt_title) TextView txtTitle;
    @BindView(R.id.txt_content) TextView txtContent;

    @BindView(R.id.txt_benefits_society) TextView txtSociety;
    @BindView(R.id.txt_benefits_student) TextView txtStudent;
    @BindView(R.id.txt_benefits_teacher) TextView txtTeacher;
    @BindView(R.id.txt_benefits_society_content) TextView txtSocietyCt;
    @BindView(R.id.txt_benefits_student_content) TextView txtStudentCt;
    @BindView(R.id.txt_benefits_teacher_content) TextView txtTeacherCt;

    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.btn_next)
    Button btnNext;

    AppStateParcel appStateParcel;
    private Unbinder unbinder;

    public WhyUseFragment() {
        // Required empty public constructor
    }


    public static WhyUseFragment newInstance(AppStateParcel appStateParcel) {
        WhyUseFragment fragment = new WhyUseFragment();
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
        View v = inflater.inflate(R.layout.fragment_why_use, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtTitle.setTypeface(LearnApp.appFont2);
        txtContent.setTypeface(LearnApp.appFont);

        txtSocietyCt.setVisibility(View.GONE);
        txtStudentCt.setVisibility(View.GONE);
        txtTeacherCt.setVisibility(View.GONE);

        // Mostrar benificios al estudiante
        txtStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtStudentCt.getVisibility() == View.GONE){
                    txtStudentCt.setVisibility(View.VISIBLE);
                } else if (txtStudentCt.getVisibility() == View.VISIBLE) {
                    txtStudentCt.setVisibility(View.GONE);
                }
            }
        });

        // Mostrar benificios al profesor
        txtTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtTeacherCt.getVisibility() == View.GONE){
                    txtTeacherCt.setVisibility(View.VISIBLE);
                } else if (txtTeacherCt.getVisibility() == View.VISIBLE) {
                    txtTeacherCt.setVisibility(View.GONE);
                }
            }
        });

        // Mostrar beneficios a la sociedad
        txtSociety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtSocietyCt.getVisibility() == View.GONE){
                    txtSocietyCt.setVisibility(View.VISIBLE);
                } else if (txtSocietyCt.getVisibility() == View.VISIBLE){
                    txtSocietyCt.setVisibility(View.GONE);
                }
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

        btnNext.setTypeface(LearnApp.appFont);
        btnNext.setOnClickListener(new View.OnClickListener() {
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

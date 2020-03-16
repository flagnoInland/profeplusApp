package com.equipu.profeplus.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.ITutorialListener;
import com.equipu.profeplus.models.Tutorial;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
Este fragmento inicializa la visualización de tutoriales
 */
public class TutorialFragment extends Fragment {

    @BindView(R.id.txt_title) TextView txtTitle;
    @BindView(R.id.txt_content) TextView txtContent;
    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_page) ImageView imgPage;
    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.btn_next) Button btnNext;
    private Unbinder unbinder;

    Tutorial tutorial;
    ITutorialListener mListener;


    public static TutorialFragment newInstance(Tutorial tutorial) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putParcelable(LearnApp.PCL_TUTORIAL, tutorial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tutorial = getArguments().getParcelable(LearnApp.PCL_TUTORIAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tutorial, container, false);
        unbinder = ButterKnife.bind(this, v);


        txtTitle.setTypeface(LearnApp.appFont2);
        txtContent.setTypeface(LearnApp.appFont);
        txtContent.setGravity(Gravity.LEFT);

        txtTitle.setText(tutorial.getPageTitle());
        txtContent.setText(Html.fromHtml(tutorial.getPageContent()));
        // Abre el navegador cuando se pincha los links
        txtContent.setMovementMethod(LinkMovementMethod.getInstance());

        // Mostrar imagenes
        Log.d("profeplus.img",String.valueOf(tutorial.getPageImage()));
        if (tutorial.getPageImage() != 0){
            imgPage.setVisibility(View.VISIBLE);
            imgPage.setImageResource(tutorial.getPageImage());
        } else {
            imgPage.setVisibility(View.GONE);
        }


        btnNext.setTypeface(LearnApp.appFont);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tutorial.hasPage()) {
                    mListener.nextPage(tutorial);
                } else {
                    mListener.endRead(tutorial);
                }
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.endRead(tutorial);
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
        if (context instanceof ITutorialListener) {
            mListener = (ITutorialListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TutorialListener");
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

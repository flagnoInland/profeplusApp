package com.equipu.profeplus.fragments.board;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ProducersFragment extends Fragment {

    @BindView(R.id.txt_title) TextView txtTitle;

    @BindView(R.id.txt_creators_names) TextView txtCreators;
    @BindView(R.id.txt_thanks) TextView txtThanks;
    @BindView(R.id.txt_creators_names_content) TextView txtCreatorsCt;
    @BindView(R.id.txt_thanks_content) TextView txtThanksCt;
    @BindView(R.id.txt_content) TextView txtContent;

    @BindView(R.id.img_exit) ImageView btnExit;
    @BindView(R.id.img_back) ImageView btnBack;


    AppStateParcel appStateParcel;
    private Unbinder unbinder;

    public ProducersFragment() {
        // Required empty public constructor
    }


    public static ProducersFragment newInstance(AppStateParcel appStateParcel) {
        ProducersFragment fragment = new ProducersFragment();
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
        View v = inflater.inflate(R.layout.fragment_producers, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtTitle.setTypeface(LearnApp.appFont2);

        String[] oldcontent = getResources().getStringArray(R.array.comite);
        String[] titles = getResources().getStringArray(R.array.title_comite);
        txtTitle.setText(titles[0]);
        txtContent.setText(Html.fromHtml(oldcontent[0]));

        String[] content = getResources().getStringArray(R.array.creator);
        txtCreatorsCt.setText(Html.fromHtml(content[0]));
        content = getResources().getStringArray(R.array.thanks);
        txtThanksCt.setText(Html.fromHtml(content[0]));
        txtCreatorsCt.setVisibility(View.GONE);
        txtThanksCt.setVisibility(View.GONE);

        //Mostrar Creadores
        txtCreators.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtCreatorsCt.getVisibility() == View.GONE){
                    txtCreatorsCt.setVisibility(View.VISIBLE);
                } else if (txtCreatorsCt.getVisibility() == View.VISIBLE){
                    txtCreatorsCt.setVisibility(View.GONE);
                }
            }
        });

        //Mostrar Agradecimientos
        txtThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtThanksCt.getVisibility() == View.GONE){
                    txtThanksCt.setVisibility(View.VISIBLE);
                } else if (txtThanksCt.getVisibility() == View.VISIBLE){
                    txtThanksCt.setVisibility(View.GONE);
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


        return v;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

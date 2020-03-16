package com.equipu.profeplus.fragments.board;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/*
Este fragmento fue usado para presentar información sobre becas
y certificaciones. Esta deshabilitado en la presente versión.
 */
public class BecasFragment extends Fragment {

    @BindView(R.id.txt_title) TextView txtTitle;
    @BindView(R.id.txt_content) TextView txtFoot;
    @BindView(R.id.txt_foot) TextView txtContent;
    @BindView(R.id.img_back) ImageView btnBack;
    @BindView(R.id.img_exit) ImageView btnExit;

    private Unbinder unbinder;

    AppStateParcel appStateParcel;

    //private BecasListener mListener;

    public BecasFragment() {
        // Required empty public constructor
    }


    public static BecasFragment newInstance(AppStateParcel appStateParcel) {
        BecasFragment fragment = new BecasFragment();
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
        View v = inflater.inflate(R.layout.fragment_becas, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtTitle.setTypeface(LearnApp.appFont);
        txtContent.setTypeface(LearnApp.appFont);
        txtFoot.setTypeface(LearnApp.appFont);

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


    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BecasListener) {
            mListener = (BecasListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BecasListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}

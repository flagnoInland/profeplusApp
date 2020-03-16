package com.equipu.profeplus.fragments.board;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IUserBoardListener;
import com.equipu.profeplus.models.AppStateParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Herbert Caller on 04/08/2018.
 */
public class MotivationFragment extends Fragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;

    @BindView(R.id.txt_content)
    TextView txtContent;

    @BindView(R.id.btn_motivation)
    Button btnMotiv;

    @BindView(R.id.btn_why_use)
    Button btnWhy;

    @BindView(R.id.img_exit)
    ImageView btnExit;

    @BindView(R.id.img_back)
    ImageView btnBack;


    AppStateParcel appStateParcel;
    private Unbinder unbinder;
    private IUserBoardListener mListener;

    public MotivationFragment() {
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
        View v = inflater.inflate(R.layout.fragment_motivation, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtTitle.setTypeface(LearnApp.appFont2);


        String content = getResources().getString(R.string.text_whatmotivation_content);
        txtContent.setText(Html.fromHtml(content));

        btnWhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.readWhyInfo();
            }
        });

        btnMotiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.readCreatorInfo();
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

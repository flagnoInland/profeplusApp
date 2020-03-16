package com.equipu.profeplus.fragments.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IStartListener;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ConcursoFragment extends Fragment {

    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.edt_concurso_group) EditText edtGroup;
    @BindView(R.id.txt_concurso_group) TextView txtGroup;
    @BindView(R.id.edt_concurso_institute) EditText edtInstitute;
    @BindView(R.id.txt_concurso_institute) TextView txtInstitute;


    private Unbinder unbinder;

    private IStartListener mListener;

    private UserParcel userParcel;
    private AppStateParcel appStateParcel;

    public ConcursoFragment() {
        // Required empty public constructor
    }

    public static ConcursoFragment newInstance(AppStateParcel appStateParcel, UserParcel userParcel) {
        ConcursoFragment fragment = new ConcursoFragment();
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
            userParcel = getArguments().getParcelable(LearnApp.PCL_USER_PARCEL);
            appStateParcel = getArguments().getParcelable(LearnApp.PCL_APP_STATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_concurso, container, false);
        unbinder = ButterKnife.bind(this, v);

        txtGroup.setHint(getString(R.string.text_group_name));
        txtInstitute.setHint(getString(R.string.text_institute_for_group));

        edtGroup.setTypeface(LearnApp.appFont);
        edtGroup.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtInstitute.setTypeface(LearnApp.appFont);
        edtInstitute.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL
                |InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edtGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtGroup.setHint("");
                if (editable.length() == 0){
                    txtGroup.setHint(getString(R.string.text_where_do_you_study));
                }
            }
        });

        edtInstitute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtInstitute.setHint("");
                if (editable.length() == 0){
                    txtInstitute.setHint(getString(R.string.text_what_do_you_study));
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFields();
                if (checkFields()) {
                    if (mListener != null) {
                        mListener.registerStudent(userParcel);
                    }
                } else {
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", getResources().getString(R.string.text_alerta));
                    b.putString("MESSAGE", getResources().getString(R.string.msg_fill_all_fields));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), "failure");
                }
            }
        });

        return v;
    }

    public void getFields(){
        userParcel.setConcursoGroup(edtGroup.getText().toString());
        userParcel.setConcursoInstitute(edtInstitute.getText().toString());

    }

    public boolean checkFields() {
        boolean value = true;
        if (userParcel.getConcursoGroup().equals("")){
            value = value & false;
        }
        /*
        if (userParcel.getConcursoInstitute().equals("")){
            value = value & false;
        }
        */
        return value;
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

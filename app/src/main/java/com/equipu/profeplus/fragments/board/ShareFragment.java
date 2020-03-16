package com.equipu.profeplus.fragments.board;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.R;
import com.equipu.profeplus.activities.IUserBoardListener;
import com.equipu.profeplus.dialogs.NotificationOneButtonSupportDialog;
import com.equipu.profeplus.models.AppStateParcel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ShareFragment extends Fragment {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mHxg5unrjycEINav4vQ1HHXgI";
    private static final String TWITTER_SECRET = "rWhqiJrf3FYjfTg23lLE8n2WbSvhM52gqFnCEvHnAROh4tPmMf";

    @BindView(R.id.img_exit)
    ImageView btnExit;

    @BindView(R.id.img_back)
    ImageView btnBack;

    @BindView(R.id.btn_como_usar_profeplus)
    Button btnComoUsarProfeplus;

    @BindView(R.id.btn_testimonios)
    Button btnTestimonios;

    @BindView(R.id.btn_coloquios)
    Button btnColoquios;

    @BindView(R.id.btn_facebook)
    ImageView btnFacebook;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    AppStateParcel appStateParcel;
    private Unbinder unbinder;
    private IUserBoardListener mListener ;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Uri imageUri;
    Bitmap imageBitmap;

    public ShareFragment() {
        // Required empty public constructor
    }




    public static ShareFragment newInstance(AppStateParcel appStateParcel) {
        ShareFragment fragment = new ShareFragment();
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

        View v = inflater.inflate(R.layout.fragment_share, container, false);
        unbinder = ButterKnife.bind(this, v);


        imageUri = Uri.parse("android.resource://com.equipu.profeplus/" + R.raw.post_image_sharing);
        imageBitmap = getShareImage(imageUri);
        //imageUri = Uri.parse("http://www.e-quipu.pe/ProfePlus/assets/images/logo.png");
        setUpFacebook();
        //setUpTwitter();

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

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d("ImageUri",imageUri.toString());
                if (ShareDialog.canShow(SharePhotoContent.class)) {

                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(imageBitmap)
                            .setCaption("Usa Profeplus: Una nueva herramienta educativa.")
                            .setUserGenerated(false)
                            .build();

                    SharePhotoContent linkContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(ShareFragment.this, linkContent);
                }

            }
        });

        btnComoUsarProfeplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlWithBrowser("https://www.youtube.com/watch?v=bH1HRPM8OMw&t=1s");
            }
        });

        btnTestimonios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlWithBrowser("https://www.youtube.com/user/ProfePlus1");
            }
        });

        btnColoquios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlWithBrowser("https://educast.pucp.edu.pe/video/9570/coloquio_por_la_coinnovacion_y_el_trabajo_en_equipo_en_el_aula_de_clase");
            }
        });

        return v;
    }

    private void openUrlWithBrowser(String url){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private Bitmap getShareImage(Uri imageUri){
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            File path = Environment.getExternalStorageDirectory();
            File imageFile = new File(path,"post_image_sharing.png");
            FileOutputStream fos = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void setUpFacebook(){

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(ShareFragment.this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (result.getPostId() != null){
                    //Log.d("profeplus.facebook", result.getPostId());
                    DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                    Bundle b = new Bundle();
                    b.putString("TITLE", getResources().getString(R.string.text_alerta));
                    b.putString("MESSAGE", getResources().getString(R.string.text_thank_for_sharing));
                    newFragment.setArguments(b);
                    newFragment.show(getFragmentManager(), "failure");
                }
            }

            @Override
            public void onCancel() {
                DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", getResources().getString(R.string.text_alerta));
                b.putString("MESSAGE", getResources().getString(R.string.text_uncomplete_operation));
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), "failure");
            }

            @Override
            public void onError(FacebookException error) {
                DialogFragment newFragment = new NotificationOneButtonSupportDialog();
                Bundle b = new Bundle();
                b.putString("TITLE", getResources().getString(R.string.text_alerta));
                b.putString("MESSAGE", getResources().getString(R.string.text_install_facebook));
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), "failure");
            }
        });



    }

    private void setUpTwitter(){

        //TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //Fabric.with(getActivity(), new TwitterCore(authConfig), new TweetComposer());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("profeplus.share","show results");
        progressBar.setVisibility(View.INVISIBLE);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

package com.equipu.profeplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.equipu.profeplus.activities.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.version) TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        txtVersion.setText(LearnApp.getAppVersion());

        finish();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }


}

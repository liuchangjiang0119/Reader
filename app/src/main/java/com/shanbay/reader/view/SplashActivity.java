package com.shanbay.reader.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shanbay.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import immortalz.me.library.TransitionsHeleper;

public class SplashActivity extends Activity {


    private Handler mHandler;
    private Runnable runnable;

    @BindView(R.id.activity_splash)
    View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        mHandler = new Handler();
        final Intent intent = new Intent(this,MainActivity.class);
        runnable = new Runnable() {
            @Override
            public void run() {
                TransitionsHeleper.startActivity(SplashActivity.this,intent,mView);
                finish();
            }
        };


        mHandler.postDelayed(runnable,3000);



    }
}

package com.example.front_village.gogi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class ScreenTransformActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //상태바 & 타이틀바 없애기
        setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
        setContentView(R.layout.screen_transform);
        LottieAnimationView animationView = (LottieAnimationView)findViewById(R.id.screen_loading);
        animationView.setAnimation("particle.json");
        animationView.loop(true);
        animationView.playAnimation();
        handler.sendEmptyMessageDelayed(0,3000);

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(ScreenTransformActivity.this, MenuActivity.class);
            startActivity(intent);

            finish();
            overridePendingTransition(R.anim.loadfadein,R.anim.loadfadeout);
        }
    };

}

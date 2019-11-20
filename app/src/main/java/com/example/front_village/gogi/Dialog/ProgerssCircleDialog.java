package com.example.front_village.gogi.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;
import com.example.front_village.gogi.R;

public class ProgerssCircleDialog extends AlertDialog {
    Context context;
    public LottieAnimationView progressBar;

    public ProgerssCircleDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.progersscircle_dialog);

        progressBar = (LottieAnimationView) findViewById(R.id.progressbar);
        progressBar.loop(true);
        progressBar.playAnimation();

//        progressBar.setIndeterminate(true);
//        progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(66, 60, 179), PorterDuff.Mode.MULTIPLY);

    }

}

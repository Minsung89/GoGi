package com.example.front_village.gogi.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.front_village.gogi.R;


public class OkDialog extends Dialog {
    String title, message;


    TextView dialogTitle, dialogMessage;
    public Button dialogOkBtn;

    public OkDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.ok_dialog);

        dialogTitle = (TextView)findViewById(R.id.dialog_title);
        dialogMessage = (TextView)findViewById(R.id.dialog_message);
        dialogOkBtn = (Button)findViewById(R.id.dialog_ok);


        dialogTitle.setText(title);
        dialogMessage.setText(message);
        dialogOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setMessage(String message){
        this.message = message;
    }
}

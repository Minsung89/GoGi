package com.example.front_village.gogi.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.front_village.gogi.Adapter.OrderListAdapter;
import com.example.front_village.gogi.R;


public class CalculationDialog extends Dialog {
    Context context;
    String title, message;

    public RadioGroup radioGroup;
    ListView dialogList;
    TextView dialogTitle;
    public Button dialogOkBtn,dialogBackBtn;



    OrderListAdapter orderListAdapter;

    public CalculationDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.calculation_dialog);

        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        dialogTitle = (TextView)findViewById(R.id.dialog_title);
        dialogList = (ListView)findViewById(R.id.dialog_listview);
        dialogOkBtn = (Button)findViewById(R.id.dialog_ok);
        dialogBackBtn = (Button)findViewById(R.id.dialog_back);

        dialogTitle.setText(title);
        dialogList.setAdapter(orderListAdapter);
        dialogBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setMessage(OrderListAdapter orderListAdapter){
        this.orderListAdapter = orderListAdapter;
    }
}

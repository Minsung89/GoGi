package com.example.front_village.gogi.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.front_village.gogi.R;

import java.util.ArrayList;


public class UpdateDialog extends Dialog {
    Context context;
    String title, nowQuantity;
    public String update = "";

    ArrayList<String> quantity;
    Spinner updateQuantity;
    TextView dialogTitle;
    public Button dialogUpdateBtn,dialogDeleteBtn;


    public UpdateDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setContentView(R.layout.update_dialog);



        dialogTitle = (TextView)findViewById(R.id.dialog_u_title);
        updateQuantity = (Spinner) findViewById(R.id.update_quantity);
        dialogUpdateBtn = (Button)findViewById(R.id.dialog_update);
        dialogDeleteBtn = (Button)findViewById(R.id.dialog_u_delete);

        dialogTitle.setText(title);

        //스피너
        setSpinner();
        //스피너 이벤트
        updateQuantity();
    }

    @Override
    public void onBackPressed() {
        dismiss();
        super.onBackPressed();
    }

    //제목
    public void setTitle(String title){
        this.title = title;
    }



    //스피너 등록
    public void setSpinner() {
        quantity = new ArrayList<String>();
        for (int i = 1 ; i < 100 ; i++){
            quantity.add(String.valueOf(i));
        }
        ArrayAdapter adapter1 = new ArrayAdapter(context,R.layout.order_menu_dropdown_item_line,quantity);

        updateQuantity.setAdapter(adapter1);
        //현재 수량 설정
        for (int i = 0 ; i < quantity.size() ; i++){
            if(quantity.get(i).equals(nowQuantity)){
                updateQuantity.setSelection(i);
            }
        }
    }
    //현재 수량
    public void nowQuantity(String quantity){
        nowQuantity = quantity;
    }

    //수정 수량
    public void updateQuantity(){
        updateQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update = updateQuantity.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}

package com.example.front_village.gogi;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.front_village.gogi.Adapter.OrderListAdapter;
import com.example.front_village.gogi.DatabaseHelper.MenuHelper;
import com.example.front_village.gogi.DatabaseHelper.TemporaryHelper;

import java.util.ArrayList;


public class OrderManagementListActivity extends AppCompatActivity implements View.OnTouchListener{


    RelativeLayout one,second,three,four,five,six,seven,eight;
    TextView oneTv,secondTv,threeTv,fourTv,fiveTv,sixTv,sevenTv,eightTv;
    //내부 DB
    SQLiteDatabase db;
    //DB 이름
    String DATABASE_NAME = "GOGI";
    //DB Helper
    TemporaryHelper temporaryHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_management_list);


        ActionBar ab = getSupportActionBar() ;

        ab.setIcon(R.drawable.meat) ;
        ab.setDisplayUseLogoEnabled(true) ;
        ab.setDisplayShowHomeEnabled(true) ;


        //DB 접속
        connectDatabase();
        //Helper 사용
        temporaryHelper = new TemporaryHelper(this,DATABASE_NAME);



        one = (RelativeLayout) findViewById(R.id.one_table);
        second = (RelativeLayout) findViewById(R.id.second_table);
        three = (RelativeLayout) findViewById(R.id.three_table);
        four = (RelativeLayout) findViewById(R.id.four_table);
        five = (RelativeLayout) findViewById(R.id.five_table);
        six = (RelativeLayout) findViewById(R.id.six_table);
        seven = (RelativeLayout) findViewById(R.id.seven_table);
        eight = (RelativeLayout) findViewById(R.id.eight_table);

        oneTv = (TextView)findViewById(R.id.tb_tv_01);
        secondTv = (TextView)findViewById(R.id.tb_tv_02);
        threeTv = (TextView)findViewById(R.id.tb_tv_03);
        fourTv = (TextView)findViewById(R.id.tb_tv_04);
        fiveTv = (TextView)findViewById(R.id.tb_tv_05);
        sixTv = (TextView)findViewById(R.id.tb_tv_06);
        sevenTv = (TextView)findViewById(R.id.tb_tv_07);
        eightTv = (TextView)findViewById(R.id.tb_tv_08);

        one.setOnTouchListener(this);
        second.setOnTouchListener(this);
        three.setOnTouchListener(this);
        four.setOnTouchListener(this);
        five.setOnTouchListener(this);
        six.setOnTouchListener(this);
        seven.setOnTouchListener(this);
        eight.setOnTouchListener(this);


    }

    @Override
    protected void onStart() {
        oneTv.setText(totalPrice(1));
        secondTv.setText(totalPrice(2));
        threeTv.setText(totalPrice(3));
        fourTv.setText(totalPrice(4));
        fiveTv.setText(totalPrice(5));
        sixTv.setText(totalPrice(6));
        sevenTv.setText(totalPrice(7));
        eightTv.setText(totalPrice(8));
        super.onStart();
    }

    //총 가격 계산
    public String totalPrice(int tableNum){
        ArrayList<String> total =  temporaryHelper.getTotalPrice(tableNum);
        int totalPrice = 0;
        if(total != null){
            for (int i = 0 ; i < total.size() ; i++){
                totalPrice += Integer.valueOf(total.get(i));
            }
            return String.format("%,d",totalPrice)+"원";
        }
        else{
            return "빈 테이블";
        }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            int table = 0;
            switch (view.getId()){
                case R.id.one_table: table = 1;
                    break;
                case R.id.second_table: table = 2;
                    break;
                case R.id.three_table: table = 3;
                    break;
                case R.id.four_table: table = 4;
                    break;
                case R.id.five_table: table = 5;
                    break;
                case R.id.six_table: table = 6;
                    break;
                case R.id.seven_table: table = 7;
                    break;
                case R.id.eight_table: table = 8;
                    break;
            }
            Intent intent = new Intent(OrderManagementListActivity.this, OrderInsertActivity.class);
            intent.putExtra("table",table);
            startActivity(intent);
        }
        return true;
    }

    //데이터베이스 접속 또는 만들기
    private void connectDatabase(){
        try {
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Log.i("Database", "OK");
        }catch (Exception e){
            e.printStackTrace();
            Log.i("Database", "NO");
        }
    }
}

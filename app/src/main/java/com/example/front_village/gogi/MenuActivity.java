package com.example.front_village.gogi;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.front_village.gogi.DatabaseHelper.MenuHelper;
import com.example.front_village.gogi.DatabaseHelper.OrderInfoHelper;
import com.example.front_village.gogi.Dialog.OkDialog;
import com.example.front_village.gogi.Dialog.ProgerssCircleDialog;
import com.example.front_village.gogi.Entity.OrderInfo;
import com.example.front_village.gogi.Service.MenuService;
import com.example.front_village.gogi.Service.OrderService;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity implements Button.OnClickListener {

    //통신 URL
    String URL = "http://192.168.0.171:8080";
    //내부 DB
    SQLiteDatabase db;
    //DB 이름
    String DATABASE_NAME = "GOGI";
    //DB Helper
    OrderInfoHelper orderInfoHelper;
    MenuHelper menuHelper;

    ProgerssCircleDialog progerssCircleDialog;




    Button btnOrder, btnSales, btnPast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ActionBar ab = getSupportActionBar() ;

        ab.setIcon(R.drawable.meat) ;
        ab.setDisplayUseLogoEnabled(true) ;
        ab.setDisplayShowHomeEnabled(true) ;

        //DB 접속 또는 DB생성
        connectDatabase();
        //TABLE 만들기
        createTable();
        //Helper 사용
        menuHelper = new MenuHelper(this,DATABASE_NAME);
        orderInfoHelper = new OrderInfoHelper(this,DATABASE_NAME);
        //프로그레스바
        progerssCircleDialog = new ProgerssCircleDialog(MenuActivity.this);
        progerssCircleDialog.setCanceledOnTouchOutside(false);



        btnOrder = (Button)findViewById(R.id.btn_order);
        btnSales = (Button)findViewById(R.id.btn_sales);
        btnPast = (Button)findViewById(R.id.btn_past);
        btnOrder.setOnClickListener(this);
        btnPast.setOnClickListener(this);
        btnSales.setOnClickListener(this);
        menuHelper.menuInsert(null);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_order :
                Intent intent = new Intent(MenuActivity.this,OrderManagementListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sales :
                Intent intent1 = new Intent(MenuActivity.this,SalesManagementListActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_past :
                Intent intent2 = new Intent(MenuActivity.this,PastHistoryListActivity.class);
                startActivity(intent2);
                break;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.synchronization,menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.synchronization){
            menuHelper.menuInsert(null);
            progerssCircleDialog.show();
            progerssCircleDialog.progressBar.playAnimation();

            createTable();
            db.execSQL("DROP TABLE TEMPORARY_STORAGE;");
            db.execSQL("DROP TABLE MENU;");
            db.execSQL("DROP TABLE ORDER_INFO;");
            db.execSQL("DROP TABLE ORDER_DETAIL;");
            createTable();
            //DB에서 메뉴 가져오기
            data();
        }

        return super.onOptionsItemSelected(item);
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

    //테이블 만들기
    private void createTable(){
        //메뉴
        db.execSQL("CREATE TABLE IF NOT EXISTS MENU(MENU_NO BIGINT not null, MENU_NM varchar2(255) not null, MENU_PRICE varchar2(255));");
        //영수증의 정보
        db.execSQL("CREATE TABLE IF NOT EXISTS ORDER_INFO(ORDER_ID varchar2(255) not null, ORDER_TABLE_NO varchar2(255) not null, ORDER_DATE date not null, ORDER_RESULT varchar2(255), TOTAL_PRICE varchar2(255) not null, ORDER_TYPE varchar2(255) not null);");
        //영수증
        db.execSQL("CREATE TABLE IF NOT EXISTS ORDER_DETAIL(ORDER_ID varchar2(255) not null, MENU_NO BIGINT not null, ORDER_QUANTITY varchar2(255) not null);");
        //계산 하기 전까지 필요한 임시테이블
        db.execSQL("CREATE TABLE IF NOT EXISTS TEMPORARY_STORAGE(TABLE_NUM BIGINT not null, MENU_NM vaarchar2(255) not null, QUANTITY varchar2(255) not null, TOTAL_PRICE varchar2(255) not null);");

    }

    //persons DB값 가져오기
    public void data() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MenuService menuService = retrofit.create(MenuService.class);
        OrderService orderService = retrofit.create(OrderService.class);

//        final retrofit2.Call<List<com.example.front_village.gogi.Entity.Menu>> call = menuService.getMenu();        //동기
        final retrofit2.Call<List<OrderInfo>> call1 = orderService.getOrderInfo();        //동기
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<com.example.front_village.gogi.Entity.Menu> menuList;
                final List<OrderInfo> orderInfoList;
                try {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //값 받아오기
//                    menuList = call.execute().body();
                    orderInfoList = call1.execute().body();

//                    Log.i("menuList",menuList +"");
                    Log.i("orderInfoList",orderInfoList+"");



                    //SQLite에 저장
//                    menuHelper.menuInsert(menuList);
//                    menuHelper.menuInsert(null);

                    orderInfoHelper.orderInfoInsert(orderInfoList);


                    //프로그레스바 끝
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progerssCircleDialog.progressBar.pauseAnimation();
                            progerssCircleDialog.dismiss();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progerssCircleDialog.progressBar.pauseAnimation();
                            progerssCircleDialog.dismiss();
                            OkDialog dialog = new OkDialog(MenuActivity.this);
                            dialog.setTitle("연결");
                            dialog.setMessage("네트워크 상태를 확인해주세요");
                            dialog.show();
                        }
                    });
                }

            }

        });
        thread.start();

    }



}

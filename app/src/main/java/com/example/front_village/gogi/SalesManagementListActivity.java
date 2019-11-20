package com.example.front_village.gogi;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.front_village.gogi.Adapter.SalesListAdapter;
import com.example.front_village.gogi.DatabaseHelper.OrderInfoHelper;
import com.example.front_village.gogi.Entity.OrderInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SalesManagementListActivity extends AppCompatActivity {


    SalesListAdapter salesListAdapter;
    //내부 DB
    SQLiteDatabase db;
    //DB 이름
    String DATABASE_NAME = "GOGI";
    //DB Helper
    OrderInfoHelper orderInfoHelper;

    int tableNo;

    ListView salesList;
    TextView salesTotalPrice;
    Spinner tableNoSpinner;
    Button salesSearch,btnSaleBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_management_list);

        ActionBar ab = getSupportActionBar() ;

        ab.setIcon(R.drawable.meat) ;
        ab.setDisplayUseLogoEnabled(true) ;
        ab.setDisplayShowHomeEnabled(true) ;

        salesList = (ListView)findViewById(R.id.sales_list);
        salesTotalPrice = (TextView)findViewById(R.id.sales_total_price);
        tableNoSpinner = (Spinner)findViewById(R.id.sales_table_no);
        salesSearch = (Button)findViewById(R.id.sales_search);
        btnSaleBack = (Button)findViewById(R.id.btn_sales_back);


        orderInfoHelper = new OrderInfoHelper(this,DATABASE_NAME);


        //스피너 등록
        setSpinner();

        //하루 총 값
        totalData();

        //스피너 선택
        searchTableNo();

        //검색
        searchTableNoBtn();
        //뒤로가기
        backBtn();
    }

    //전체
    public void totalData(){

        //오늘 데이터들
        String today = todaysDate();
        ArrayList<OrderInfo> orderInfos = orderInfoHelper.getReceipt(today);
        datas(orderInfos);

    }

    //테이블 검색
    public void searchData(int tableNo){
        ArrayList<OrderInfo> orderInfos = orderInfoHelper.getTableReceipt(tableNo);
        datas(orderInfos);

    }

    //데이터 넣기
    public void datas(final ArrayList<OrderInfo> orderInfos){
        if(orderInfos != null) {
            salesListAdapter = new SalesListAdapter(orderInfos);
            salesList.setAdapter(salesListAdapter);
            salesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    orderInfos.get(position).getOrderId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            salesTotalPrice.setText(salesListAdapter.totalPrice());
        }
        else{
            salesList.setAdapter(null);
            salesTotalPrice.setText("0");
        }
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

    //스피너 등록
    public void setSpinner() {
        ArrayList<String> tableNo = new ArrayList<String>();
        for (int i = 1 ; i < 9 ; i++){
            tableNo.add(String.valueOf(i));
        }

        //테이블 번호
        tableNo.add(0,"선택");
        ArrayAdapter adapter = new ArrayAdapter(SalesManagementListActivity.this,R.layout.support_simple_spinner_dropdown_item,tableNo);
        tableNoSpinner.setAdapter(adapter);

    }

    //테이블 선택
    public void searchTableNo(){
        tableNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int positing, long l) {
                tableNo = positing;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //검색 버튼
    public void searchTableNoBtn(){
        salesSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TableNo",tableNo+"");
                if(tableNo == 0){
                    totalData();
                }
                else
                searchData(tableNo);
            }
        });
    }

    //오늘 날짜
    public String todaysDate(){
        Long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    //뒤로가기
    public void backBtn(){
        btnSaleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
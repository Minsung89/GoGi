package com.example.front_village.gogi;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.front_village.gogi.Adapter.OrderListAdapter;
import com.example.front_village.gogi.DatabaseHelper.MenuHelper;
import com.example.front_village.gogi.DatabaseHelper.OrderDetailHelper;
import com.example.front_village.gogi.DatabaseHelper.OrderInfoHelper;
import com.example.front_village.gogi.Dialog.DatePickDialog;
import com.example.front_village.gogi.Dialog.PastDetailDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PastHistoryListActivity extends AppCompatActivity implements View.OnClickListener{

    //통신 URL
    String URL = "http://192.168.0.171:8080";
    //내부 DB
    SQLiteDatabase db;
    //DB 이름
    String DATABASE_NAME = "GOGI";
    //DB Helper
    OrderInfoHelper orderInfoHelper;
    MenuHelper menuHelper;
    OrderDetailHelper orderDetailHelper;
    //위젯
    BarChart barChart;
    TextView pastDate, pastTotalCount;
    Button pastSearch, btnPastBack;
    FloatingActionButton fAB;
    DatePickDialog datePickDialog;
    PastDetailDialog pastDetailDialog;
    //선택날짜
    String weekDay;

    //날짜 메뉴별 총 수량
    ArrayList<String> menuTotalQuantity;

    //다이얼로그에 붙일 어댑터
    OrderListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_history_list);

        ActionBar ab = getSupportActionBar() ;

        ab.setIcon(R.drawable.meat) ;
        ab.setDisplayUseLogoEnabled(true) ;
        ab.setDisplayShowHomeEnabled(true) ;


        //위젯
        barChart = (BarChart)findViewById(R.id.chart);
        pastSearch = (Button)findViewById(R.id.past_search);
        btnPastBack = (Button)findViewById(R.id.btn_past_back);
        pastDate = (TextView)findViewById(R.id.past_date);
        pastTotalCount = (TextView)findViewById(R.id.past_totalCount);
        fAB = (FloatingActionButton)findViewById(R.id.past_fab);
        //다이얼로그
        datePickDialog = new DatePickDialog(this);

        //버튼 리스너 
        btnPastBack.setOnClickListener(this);
        pastDate.setOnClickListener(this);
        fAB.setOnClickListener(this);

        //Helper 사용
        menuHelper = new MenuHelper(this,DATABASE_NAME);
        orderInfoHelper = new OrderInfoHelper(this,DATABASE_NAME);
        orderDetailHelper = new OrderDetailHelper(this,DATABASE_NAME);
        //어댑터
        listAdapter = new OrderListAdapter();

        //날짜 메뉴별 총 수량
        menuTotalQuantity = new ArrayList<String>();

        //처음 화면 띄울 시 오늘날짜 차트 출력
        chart();


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.past_date :
                datePickDialog.show();
                datePickDialog.datePicker.init(datePickDialog.datePicker.getYear(), datePickDialog.datePicker.getMonth(), datePickDialog.datePicker.getDayOfMonth(),
                        new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Log.i("날짜",datePickDialog.datePicker.getFirstDayOfWeek()+"");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy / MM / dd  ( EEEE )");
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year,monthOfYear,dayOfMonth);
                                String searchDay = simpleDateFormat.format(calendar.getTime());
                                Toast.makeText(PastHistoryListActivity.this,searchDay,Toast.LENGTH_SHORT).show();
                                pastDate.setText(searchDay);
                                weekDay = dateFormat.format(calendar.getTime());
                                Log.i("weekDay",weekDay);
                                chart();
                                datePickDialog.dismiss();
                            }
                        });
                break;
            case R.id.past_fab :
                //다이얼로그로 띄울 리스트 값 넣기
                menuListPrice();
                pastDetailDialog = new PastDetailDialog(this);
                //값 붙이기
                pastDetailDialog.setTitle("자세히 보기");

                Log.i("가격들",listAdapter.price+"");
                pastDetailDialog.setMessage(listAdapter);
                pastDetailDialog.show();
                break;

            case R.id.btn_past_back :
                onBackPressed();
        }



    }

    public void anim(){

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

    //차트 설정
    public void chart(){
        //처음 들어왔을 때 오늘 날짜 검색

        if(null == weekDay){
            weekDay = todaysDate();
        }
        //수량 차트
        ArrayList<BarEntry> entries = quantityChart(weekDay);
        BarDataSet barDataSet = new BarDataSet(entries, "메뉴");

        barDataSet.setValueTextSize(12);
        barDataSet.setFormLineWidth(30);
        barDataSet.setColors(Color.rgb(125,193,225));
        barDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);
        //소수점 없애기
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return  new DecimalFormat("###,###,##0").format(value);
            }
        });

        //X축에 들어갈 이름들
        final ArrayList<String> text = menuHelper.getMenu();
        BarData data = new BarData(barDataSet);

        XAxis xAxis = barChart.getXAxis();
        //하단 글씨
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //하단 글씨 색깔
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(5.0f);
        xAxis.setGranularity(1); // 하단 글씨 출력 간격
        xAxis.setLabelCount(11);// 하단 막대 맥스값
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return  text.get((int)value-1);
            }
        });
        xAxis.setDrawGridLines(false);

        YAxis left = barChart.getAxisLeft();
        YAxis right = barChart.getAxisRight();
        left.setAxisMinimum(0);
        left.setDrawGridLines(false);


        barChart.setDescription(null);

        //그래프 크기
        data.setBarWidth(0.9f);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getXAxis().setGranularity(1);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
        String totalSales =  ""+orderInfoHelper.getTotalPrice(weekDay);
        pastTotalCount.setText(totalSales);

        //문자
        //날짜 weekDay
        //총 판매액 totalSales


//        sendSMS("minsung021@naver.com","["+weekDay+"]"+"\r\n"+"총 금액 : "+totalSales+"원 \r\n"+"메렁 또니얌");


    }


    //수량 차트
    public ArrayList<BarEntry> quantityChart(String day){


        // 1~11번 메뉴들의 수량 저장
        ArrayList<BarEntry> entries = new ArrayList<>();
        menuTotalQuantity.clear();
        for (int i = 1 ; i < 12 ; i++){
            //메뉴 검색해서 수량을 합친값들 저장
            int totalQuantity = orderDetailHelper.getMenuQuantity(day, i);
            entries.add(new BarEntry(i, totalQuantity));

            menuTotalQuantity.add(String.valueOf(totalQuantity));
        }
        return entries;
    }

    public void menuListPrice(){

        //메뉴 이름
        listAdapter.menu = menuHelper.getMenu();
        //메뉴 수량
        listAdapter.quantity = menuTotalQuantity;

        //가격 리셋 후 넣기
        listAdapter.price.clear();

        for(int i = 0 ; i < listAdapter.menu.size() ; i++){
            //메뉴 가격
            int price = Integer.valueOf(menuHelper.getMenuInfo(listAdapter.menu.get(i)));

            //수량
            int menuTotalPrice = Integer.valueOf(listAdapter.quantity.get(i));

            //메뉴 총합
            listAdapter.price.add(String.valueOf(price*menuTotalPrice));
        }

        listAdapter.notifyDataSetChanged();

    }


    //오늘 날짜
    public String todaysDate(){
        Long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    //문자
    private void sendSMS(String phoneNumber, String message)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},1);
            Toast.makeText(this,"권한을 허용하고 재전송해주세요",Toast.LENGTH_LONG).show();
        } else {
            SmsManager sms = SmsManager.getDefault();

            // 아래 구문으로 지정된 핸드폰으로 문자 메시지를 보낸다
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this,"전송을 완료하였습니다",Toast.LENGTH_LONG).show();
        }

    }
}
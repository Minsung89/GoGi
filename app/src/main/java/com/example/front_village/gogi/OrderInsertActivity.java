package com.example.front_village.gogi;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.front_village.gogi.Adapter.OrderListAdapter;
import com.example.front_village.gogi.DatabaseHelper.MenuHelper;
import com.example.front_village.gogi.DatabaseHelper.OrderDetailHelper;
import com.example.front_village.gogi.DatabaseHelper.OrderInfoHelper;
import com.example.front_village.gogi.DatabaseHelper.TemporaryHelper;
import com.example.front_village.gogi.Dialog.CalculationDialog;
import com.example.front_village.gogi.Dialog.UpdateDialog;
import com.example.front_village.gogi.Entity.OrderDetail;
import com.example.front_village.gogi.Entity.OrderInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OrderInsertActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    //내부 DB
    SQLiteDatabase db;
    //DB 이름
    String DATABASE_NAME = "GOGI";
    //DB Helper
    MenuHelper menuHelper;
    TemporaryHelper temporaryHelper;
    OrderInfoHelper orderInfoHelper;
    OrderDetailHelper orderDetailHelper;

    //Table 번호
    int tableNum;
    //ListView 어댑터
    OrderListAdapter orderListAdapter;


    ImageView orderTable;
    Spinner orderMenu, orderQuantity;
    Button orderMenuInsert, orderInsertOk, orderInsertCalculation;
    TextView totalPrice;
    ListView orderMenuList;

    //선택한 메뉴 && 수량
    String selectorderMenu, selectorderQuantity;
    //결제
    String payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_insert);
        ActionBar ab = getSupportActionBar() ;

        ab.setIcon(R.drawable.meat) ;
        ab.setDisplayUseLogoEnabled(true) ;
        ab.setDisplayShowHomeEnabled(true) ;
        Intent intent = getIntent();

        //테이블 번호
        tableNum = (int)intent.getExtras().get("table");

        //DB 접속
        connectDatabase();
        //Helper 사용
        menuHelper = new MenuHelper(this, DATABASE_NAME);
        temporaryHelper = new TemporaryHelper(this,DATABASE_NAME);
        orderInfoHelper = new OrderInfoHelper(this,DATABASE_NAME);
        orderDetailHelper = new OrderDetailHelper(this,DATABASE_NAME);
        //어댑터
        orderListAdapter = new OrderListAdapter();


        //위젯
        orderTable = (ImageView)findViewById(R.id.order_table);
        orderMenu = (Spinner)findViewById(R.id.order_menu);
        orderQuantity = (Spinner)findViewById(R.id.order_quantity);
        orderMenuInsert = (Button)findViewById(R.id.order_menu_insert);
        orderInsertOk = (Button) findViewById(R.id.order_insert_ok);
        orderInsertCalculation = (Button)findViewById(R.id.order_insert_calculation);
        totalPrice = (TextView) findViewById(R.id.total_price);
        orderMenuList = (ListView) findViewById(R.id.order_menu_list);



        //테이블 이미지
        tableImage();

        //메뉴리스트
        tableOrderMenu();

        //스피너 등록
        setSpinner();

        //리스너
        orderMenu.setOnItemSelectedListener(this);
        orderQuantity.setOnItemSelectedListener(this);

        orderMenuInsert.setOnClickListener(this);
        orderInsertOk.setOnClickListener(this);
        orderInsertCalculation.setOnClickListener(this);

    }

    //스피너 등록
    public void setSpinner() {

        ArrayList<String> spItem = menuHelper.getMenu();
        if(spItem == null){
            spItem = new ArrayList<String>();
        }
        ArrayList<String> quantity = new ArrayList<String>();
        for (int i = 1 ; i < 10 ; i++){
            quantity.add(String.valueOf(i));
        }

        //메뉴
        spItem.add(0,"선택");
        ArrayAdapter adapter = new ArrayAdapter(OrderInsertActivity.this,R.layout.order_menu_dropdown_item_line,spItem);
        orderMenu.setAdapter(adapter);

        //수량
        quantity.add(0,"수량");
        ArrayAdapter adapter1 = new ArrayAdapter(OrderInsertActivity.this,R.layout.order_menu_dropdown_item_line,quantity);
        orderQuantity.setAdapter(adapter1);

    }


    //메뉴 & 수량 선택
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int positing, long l) {
        switch (parent.getId()){
            case R.id.order_menu:
                if(positing > 0){selectorderMenu = orderMenu.getSelectedItem().toString();
                Log.i(selectorderMenu,selectorderMenu);
                }else selectorderMenu = null;
                break;
            case R.id.order_quantity:
                if(positing > 0){selectorderQuantity = orderQuantity.getSelectedItem().toString();
                    Log.i(selectorderQuantity,selectorderQuantity);}else selectorderMenu = null;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_menu_insert:
                //선택 또는 수량 메뉴를 값 없을 시 리턴
                if(null == selectorderMenu || null == selectorderQuantity){
                    return;
                }
                menuList();
                break;

            case R.id.order_insert_ok:
                onBackPressed();
                break;
            case R.id. order_insert_calculation:
                final CalculationDialog calculationDialog = new CalculationDialog(this);
                calculationDialog.setTitle("총 : "+orderListAdapter.totalPrice());
                calculationDialog.setMessage(orderListAdapter);
                calculationDialog.setCancelable(false);
                calculationDialog.show();

                calculationDialog.dialogOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //카드 || 현금
                        int id = calculationDialog.radioGroup.getCheckedRadioButtonId();
                        RadioButton rb = (RadioButton) calculationDialog.findViewById(id);
                        if(null == rb || null == rb.getText().toString()) {
                            Toast.makeText(OrderInsertActivity.this,"카드 또는 현금을 선택하세요",Toast.LENGTH_LONG).show();
                            return;

                        }
                        else{
                            payment = rb.getText().toString();
                        }

                        //영수증 번호 조회
                        String todaysDate = todaysDate();
                        String receiptSearch = todaysDate+"TB0"+tableNum;
                        int receiptNum = orderInfoHelper.orderReceiptSearch(receiptSearch);
                        String receip = null;
                        if(receiptNum < 10){
                            if(receiptNum == 0){
                                receip = receiptSearch + "01";
                            }
                            else {
                                receip = receiptSearch + "0" + (receiptNum + 1);
                            }
                        }
                        else{
                            receip = receiptSearch+receiptNum;
                        }
                        //영수증 저장
                        OrderInfo orderInfo = new OrderInfo();
                        orderInfo.setOrderId(receip);
                        orderInfo.setOrderTableNo(String.valueOf(tableNum));
                        orderInfo.setOrderDate(new Date());
                        Log.i("현재시간은?",new Date()+"");
                        orderInfo.setOrderResult("Y");
                        orderInfo.setTotalPrice(orderListAdapter.tPrice());
                        orderInfo.setOrderType(payment);
                        orderInfoHelper.calculationInsert(orderInfo);

                        for(int i = 0 ; i < orderListAdapter.menu.size() ; i++) {
                            OrderDetail orderDetail = new OrderDetail();
                            //메뉴 번호
                            orderDetail.setMenuNo(Integer.valueOf(menuHelper.getMenuNo(orderListAdapter.menu.get(i))));
                            //영수증 번호
                            orderDetail.setOrderId(receip);
                            //메뉴 총 수량
                            orderDetail.setOrderQuantity(orderListAdapter.quantity.get(i));
                            //자세한 영수증 등록
                            orderDetailHelper.Insert(orderDetail);
                        }

                        //뷰 삭제
                        orderListAdapter.clear();
                        //임시저장 삭제
                        temporaryHelper.deleteTable(tableNum);
                        onBackPressed();
                        calculationDialog.dismiss();

                        //SELECT DISTINCT A FROM tableName WHERE B > 80; 중복 제거 후
                        //ORDER_ID = 20181105TB0101   전체 갯수 구한 후 숫자 반환 +1 해주기
//                                   20181105TB0102
//                        ORDER_TABLE_NO = 1
//                        ORDER_DATA = "DATETIME DEFAULT (datetime('now','localtime'))"
                        //ORDER_RESULT = 'Y'
                        //TOTAL_PRICE = 43000
                        //ORDER_TYPE = "카드" || "현근"
//                        db.execSQL("CREATE TABLE IF NOT EXISTS ORDER_INFO(ORDER_ID varchar2(255) not null, ORDER_TABLE_NO BIGINT not null, ORDER_DATE date not null, ORDER_RESULT varchar2(255), TOTAL_PRICE varchar2(255) not null, ORDER_TYPE varchar2(255) not null);");







                    }
                });
                break;

        }
    }
    //리스트뷰
    public void menuList(){
        //메뉴에 대한 가격 조회
        String price = menuHelper.getMenuInfo(selectorderMenu);
        //메뉴 가격 * 수량 = 총가격
        int total = Integer.valueOf(price) * Integer.valueOf(selectorderQuantity);
        Log.i("total", "가격은"+total);

        ArrayList<String> tempray = temporaryHelper.menuSelect(tableNum,selectorderMenu);

        //임시 저장 판단
        if(null == tempray){
            //임시에 값이 없을 시 저장
            temporaryHelper.menuAdd(tableNum,selectorderMenu,selectorderQuantity,String.valueOf(total));
        }
        else {
            //임시에 값이 있을 시 업데이트
            //수량 더하기
            int quantityPlus = Integer.valueOf(selectorderQuantity) + Integer.valueOf(tempray.get(0));
            //가격 더하기
            int totalPlus = Integer.valueOf(total) + Integer.valueOf(tempray.get(1));
            temporaryHelper.menuUpdate(tableNum,selectorderMenu,String.valueOf(quantityPlus),String.valueOf(totalPlus));
        }



        //리스트에 메뉴 추가
        orderListAdapter.add(selectorderMenu,selectorderQuantity,String.valueOf(total));
        listPrint();
    }


    //임시저장 불러오기
    public void tableOrderMenu(){
        ArrayList<String> tableOrderMenu = temporaryHelper.tableMenuInfo(tableNum);
        if(tableOrderMenu != null) {
            for (int i = 0; i < tableOrderMenu.size(); i++) {
                String[] item = tableOrderMenu.get(i).split(",");
                orderListAdapter.add(item[0], item[1], item[2]);

            }
            listPrint();
        }

    }

    //출력
    public void listPrint(){
        if(orderMenuList.getHeaderViewsCount() == 0){
            View header = getLayoutInflater().inflate(R.layout.order_list_header, null, false);
            orderMenuList.addHeaderView(header);
        }
        orderMenuList.setAdapter(orderListAdapter);


        //수정
        orderMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i("orderListAdapter",orderListAdapter.getItem(position)+"");
                if(position == 0){
                    return;
                }

                final String menuName = orderListAdapter.menu.get(position-1);
                final String menuQuantity = orderListAdapter.quantity.get(position-1);
                final String menuPrice = orderListAdapter.price.get(position-1);
                final UpdateDialog updateDialog = new UpdateDialog(OrderInsertActivity.this);
                updateDialog.setTitle("수정(수량)");
                updateDialog.nowQuantity(menuQuantity);
                updateDialog.show();
                updateDialog.dialogUpdateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //하나의 수량 가격 구하기
                        String onePrice = orderListAdapter.menuPrice(menuQuantity,menuPrice);
                        //가격 저장
                        orderListAdapter.price.set(position-1,orderListAdapter.updateMenuPrice(updateDialog.update,onePrice));
                        orderListAdapter.quantity.set(position-1,updateDialog.update);
                        //합계
                        totalPrice.setText(orderListAdapter.totalPrice());
                        //수정된거 임시 저장
                        temporaryHelper.menuUpdate(tableNum,menuName,orderListAdapter.quantity.get(position-1),orderListAdapter.price.get(position-1));
                        updateDialog.dismiss();
                        orderListAdapter.notifyDataSetChanged();
                    }
                });
                updateDialog.dialogDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //어댑터에서 삭제
                        orderListAdapter.deleteMenu(position-1);
                        //임시 저장에 있는거 삭제
                        temporaryHelper.menuDelete(tableNum,menuName);
                        updateDialog.dismiss();
                        orderListAdapter.notifyDataSetChanged();
                    }
                });


            }
        });


        //합계
        totalPrice.setText(orderListAdapter.totalPrice());
    }

    public void tableImage(){
        switch (tableNum){
            case 1: orderTable.setImageResource(R.drawable.one);
                break;
            case 2: orderTable.setImageResource(R.drawable.two);
                break;
            case 3: orderTable.setImageResource(R.drawable.three);
                break;
            case 4: orderTable.setImageResource(R.drawable.four);
                break;
            case 5: orderTable.setImageResource(R.drawable.five);
                break;
            case 6: orderTable.setImageResource(R.drawable.six);
                break;
            case 7: orderTable.setImageResource(R.drawable.seven);
                break;
            case 8: orderTable.setImageResource(R.drawable.eight);
                break;
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


    //오늘 날짜
    public String todaysDate(){
        Long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

}
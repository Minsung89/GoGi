package com.example.front_village.gogi.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.front_village.gogi.Entity.OrderDetail;
import com.example.front_village.gogi.Entity.OrderInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class OrderDetailHelper extends SQLiteOpenHelper {
    String TABLE_NAME = "ORDER_DETAIL";

    public OrderDetailHelper(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    //db version upgrade
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void update(String item, int price) {

    }


//    db.execSQL("CREATE TABLE IF NOT EXISTS ORDER_DETAIL(ORDER_ID varchar2(255) not null, MENU_NO BIGINT not null, ORDER_QUANTITY varchar2(255) not null);");
    //계산 한 값 넣기
    public void Insert(OrderDetail orderDetail){
        SQLiteDatabase db = getReadableDatabase();
        //                        db.execSQL("CREATE TABLE IF NOT EXISTS ORDER_INFO(ORDER_ID varchar2(255) not null, ORDER_TABLE_NO BIGINT not null, ORDER_DATE date not null, ORDER_RESULT varchar2(255), TOTAL_PRICE varchar2(255) not null, ORDER_TYPE varchar2(255) not null);");

        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+orderDetail.getOrderId()+"',"+orderDetail.getMenuNo()+", '"+orderDetail.getOrderQuantity()+"');");
        Log.i("잘 들어갔우?",orderDetail.getOrderId()+"");
        db.close();
    }


    //영수증하고 메뉴 번호로 수량 검색
    public int getMenuQuantity(String day, int menu) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ORDER_QUANTITY  FROM " +TABLE_NAME+  " WHERE ORDER_ID LIKE '"+day+"%' AND MENU_NO = "+menu+";" , null);
        int recordCount = cursor.getCount();
        int totalQuantity = 0;
        if (recordCount < 1) {
            return totalQuantity;
        }
        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
            totalQuantity += Integer.valueOf(cursor.getString(0));
        }
        Log.i("토탈 수량은",totalQuantity+"");
        return totalQuantity;

    }

}

package com.example.front_village.gogi.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.front_village.gogi.Entity.OrderInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class OrderInfoHelper extends SQLiteOpenHelper {
    String TABLE_NAME = "ORDER_INFO";

    public OrderInfoHelper(Context context, String DATABASE_NAME) {
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

    public void orderInfoInsert(List<OrderInfo> orderInfoList){
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0 ; i < orderInfoList.size() ; i++){
                db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+orderInfoList.get(i).getOrderId()+"','"+orderInfoList.get(i).getOrderTableNo()+"', '"+orderInfoList.get(i).getOrderDate()+
                        "', '"+orderInfoList.get(i).getOrderResult()+"','"+orderInfoList.get(i).getTotalPrice()+"', '"+orderInfoList.get(i).getOrderType()+"');");
            }
            db.setTransactionSuccessful();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

//    서버에서 가져온 DB 등록
//    public void orderInsert(List<OrderInfo> personsList){
//        SQLiteDatabase db = getReadableDatabase();
//        try {
//            db.beginTransaction();
//            for(int i = 0 ; i < personsList.size() ; i++){
//                String personId = personsList.get(i).getPersonId();
//                String empNo = personsList.get(i).getEmpNo();
//                String empName = personsList.get(i).getEmpName();
//                String uTagId = personsList.get(i).getuTagId();
//                db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+personId+"','"+empNo+"', '"+empName+"','"+uTagId+"');");
//                Log.i("personsInsert","personId :"+personId+", empName : "+empName+ ", empNo : "+empNo+", uTagId : "+uTagId);
//            }
//            db.setTransactionSuccessful();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }finally {
//            db.endTransaction();
//            db.close();
//        }
//    }
//
    //영수증 번호를 순차적으로 넣기 위해 조회
    public int orderReceiptSearch(String today){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT ORDER_ID  FROM " +TABLE_NAME+ " WHERE ORDER_ID LIKE '"+today+"%'" , null);
        int recordCount = cursor.getCount();
        if (recordCount < 1) {
            return 0;
        }
        Log.i("값은 몇개?",recordCount+"");
        return recordCount;
    }

    //계산 한 값 넣기
    public void calculationInsert(OrderInfo orderInfo){
        SQLiteDatabase db = getReadableDatabase();
        //                        db.execSQL("CREATE TABLE IF NOT EXISTS ORDER_INFO(ORDER_ID varchar2(255) not null, ORDER_TABLE_NO BIGINT not null, ORDER_DATE date not null, ORDER_RESULT varchar2(255), TOTAL_PRICE varchar2(255) not null, ORDER_TYPE varchar2(255) not null);");

        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+orderInfo.getOrderId()+"','"+orderInfo.getOrderTableNo()+"', '"+orderInfo.getOrderDate()+"','"+orderInfo.getOrderResult()+"', '"+orderInfo.getTotalPrice()+"', '"+orderInfo.getOrderType()+"');");


        db.close();
    }

//    //전체 값 가져오기
    public ArrayList<OrderInfo> getReceipt(String today){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME+ " WHERE ORDER_ID LIKE '"+today+"%'"+" ORDER BY ORDER_TABLE_NO ASC", null);
        int recordCount = cursor.getCount();
        if (recordCount < 1) {
            return null;
        }

        ArrayList<OrderInfo> orderInfoArrayList = new ArrayList<OrderInfo>();
        for (int i = 0; i < recordCount; i++) {

            cursor.moveToNext();
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(cursor.getString(0));
            orderInfo.setOrderTableNo(cursor.getString(1));
            String dateS = cursor.getString(2);
            Log.i("시간은?",dateS+"");

            try {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                Date dt = format.parse(dateS);
                orderInfo.setOrderDate(dt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            orderInfo.setOrderResult(cursor.getString(3));
            orderInfo.setTotalPrice(cursor.getString(4));
            orderInfo.setOrderType(cursor.getString(5));
            orderInfoArrayList.add(orderInfo);
        }
        return orderInfoArrayList;

    }
    //전체 값 가져오기
    public ArrayList<OrderInfo> getTableReceipt(int tableNo){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME+ " WHERE ORDER_TABLE_NO = '"+tableNo+"'", null);
        int recordCount = cursor.getCount();
        if (recordCount < 1) {
            return null;
        }

        ArrayList<OrderInfo> orderInfoArrayList = new ArrayList<OrderInfo>();
        for (int i = 0; i < recordCount; i++) {

            cursor.moveToNext();
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(cursor.getString(0));
            orderInfo.setOrderTableNo(cursor.getString(1));
            String dateS = cursor.getString(2);
            Log.i("시간은?",dateS+"");
            try {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                Date dt = format.parse(dateS);
                orderInfo.setOrderDate(dt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            orderInfo.setOrderResult(cursor.getString(3));
            orderInfo.setTotalPrice(cursor.getString(4));
            orderInfo.setOrderType(cursor.getString(5));
            orderInfoArrayList.add(orderInfo);
        }
        return orderInfoArrayList;

    }
    public int getTotalPrice(String day) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT TOTAL_PRICE  FROM " +TABLE_NAME+  " WHERE ORDER_ID LIKE '"+day+"%'", null);
        int recordCount = cursor.getCount();
        int totalPrice = 0;
        if (recordCount < 1) {
            return totalPrice;
        }
        for (int i = 0; i < recordCount; i++) {
            cursor.moveToNext();
            totalPrice += Integer.valueOf(cursor.getString(0));
        }
        Log.i("토탈 가격",totalPrice+"");
        return totalPrice;

    }

//
//    //사원번호 조회
//    public ArrayList<Persons> getPersons(String search){
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME+ " WHERE EMP_NO = '"+search+"';", null);
//        int recordCount = cursor.getCount();
//        if (recordCount < 1) {
//            return null;
//        }
//
//        ArrayList<Persons> personsArrayList = new ArrayList<Persons>();
//        for (int i = 0; i < recordCount; i++) {
//            cursor.moveToNext();
//            Persons persons = new Persons();
//            persons.setPersonId(cursor.getString(0));
//            persons.setEmpNo(cursor.getString(1));
//            persons.setEmpName(cursor.getString(2));
//            persons.setuTagId(cursor.getString(3));
//            personsArrayList.add(persons);
//        }
//        return personsArrayList;
//
//    }
//
//    //계열사 & 회사 코드
//    public ArrayList<String> getCompanyCode(){
//        SQLiteDatabase db = getReadableDatabase();
//
//        Cursor cursor = db.rawQuery("SELECT DISTINCT PERSON_ID FROM "+TABLE_NAME+" ORDER BY PERSON_ID;",null);
//        int recordCount = cursor.getCount();
//        if(recordCount < 1 ){
//            return null;
//        }
//        ArrayList<String> companycode = new ArrayList<String>();
//        for (int i = 0 ; i < recordCount ; i++){
//            cursor.moveToNext();
//            companycode.add(cursor.getString(0));
//            Log.i("code",cursor.getString(0));
//        }
//        companycode.remove("");
//        Log.i("code","///////////////////////"+companycode);
//        return companycode;
//    }
//
//    public String getIssueCount(String companyCode, String empNo) {
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT substr(U_TAG_ID,length(U_TAG_ID) - 3,  length(U_TAG_ID)) FROM " +TABLE_NAME+ " WHERE PERSON_ID = '"+companyCode+"' AND EMP_NO = '" + empNo+"';", null);
//        int recordCount = cursor.getCount();
//        if (recordCount < 1) {
//            return null;
//        }
//        cursor.moveToNext();
//        Log.i("발급 횟수" , cursor.getString(0));
//        return  cursor.getString(0);
//    }
}

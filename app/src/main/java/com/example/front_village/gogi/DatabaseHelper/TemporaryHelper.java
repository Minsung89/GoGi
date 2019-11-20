package com.example.front_village.gogi.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class TemporaryHelper extends SQLiteOpenHelper {
    String TABLE_NAME = "TEMPORARY_STORAGE";

    public TemporaryHelper(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    //db version upgrade
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    //테이블에 대한 메뉴 검색
    public ArrayList<String> menuSelect(int tableNum, String menuNm){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT QUANTITY, TOTAL_PRICE FROM "+TABLE_NAME+" WHERE TABLE_NUM = "+tableNum+" AND MENU_NM = '"+menuNm+"';",null);

        int recordCount = cursor.getCount();
        if(recordCount < 1 ){
            return null;
        }
        ArrayList<String> searchMenu = new ArrayList<String>();
        cursor.moveToNext();
        searchMenu.add(cursor.getString(0));
        searchMenu.add(cursor.getString(1));
        return searchMenu;
    }


    //메뉴 등록
    public void menuAdd(int tableNum, String menuNm, String quantity, String totalPrice){
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+tableNum+"','"+menuNm+"', '"+quantity+"', '"+totalPrice+"');");
            db.setTransactionSuccessful();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    //메뉴 수정
    public void menuUpdate(int tableNum, String menuNm, String quantity, String totalPrice){
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("UPDATE " + TABLE_NAME + " SET QUANTITY = '"+quantity+"', TOTAL_PRICE = '"+totalPrice+"' WHERE TABLE_NUM = '"+tableNum+"' AND MENU_NM = '"+menuNm+"';");
            db.setTransactionSuccessful();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    //메뉴 삭제
    public void menuDelete(int tableNum, String menuNm){
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.beginTransaction();
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE TABLE_NUM = '"+tableNum+"' AND MENU_NM = '"+menuNm+"';");
            db.setTransactionSuccessful();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }


    public ArrayList<String> tableMenuInfo(int tableNum){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT MENU_NM, QUANTITY, TOTAL_PRICE FROM "+TABLE_NAME+" WHERE TABLE_NUM = "+tableNum+";",null);

        int recordCount = cursor.getCount();
        if(recordCount < 1 ){
            return null;
        }
        ArrayList<String> orderMenu = new ArrayList<String>();
        for (int i = 0 ; i < recordCount ; i++){
            cursor.moveToNext();
            orderMenu.add(cursor.getString(0));
            orderMenu.set(i,cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2));
        }
        return orderMenu;
    }


    public ArrayList<String> getTotalPrice(int tableNum){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT TOTAL_PRICE FROM "+TABLE_NAME+" WHERE TABLE_NUM = "+tableNum+";",null);
        int recordCount = cursor.getCount();
        if(recordCount < 1 ){
            return null;
        }
        ArrayList<String> totalPrice = new ArrayList<String>();
        for (int i = 0 ; i < recordCount ; i++){
            cursor.moveToNext();
            totalPrice.add(cursor.getString(0));
        }
        return totalPrice;
    }

    //계산 후 임시저장된 테이블 정리
    public void deleteTable(int tableNum){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE TABLE_NUM = '"+tableNum+"';");
        db.close();
    }

}

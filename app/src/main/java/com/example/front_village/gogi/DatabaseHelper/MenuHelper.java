package com.example.front_village.gogi.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.front_village.gogi.Entity.Menu;

import java.util.ArrayList;
import java.util.List;


public class MenuHelper extends SQLiteOpenHelper {
    String TABLE_NAME = "Menu";

    public MenuHelper(Context context, String DATABASE_NAME) {
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

    //메뉴 등록
    public void menuInsert(List<Menu> menus){
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.beginTransaction();
//            for (int i = 0 ; i < menus.size() ; i++){
////                db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+menus.get(i).getMenuNo()+"','"+menus.get(i).getMenuNm()+"', '"+menus.get(i).getMenuPrice()+"');");
////            }
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+1+"','"+"삼겹살"+"', '"+"11000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+2+"','"+"소갈비살"+"', '"+"13000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+3+"','"+"돼지갈비"+"', '"+"11000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+4+"','"+"항정살"+"', '"+"11000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+5+"','"+"김치찌개"+"', '"+"4000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+6+"','"+"냉면"+"', '"+"3000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+7+"','"+"공기밥"+"', '"+"1000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+8+"','"+"소주"+"', '"+"4000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+9+"','"+"맥주"+"', '"+"4000"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+10+"','"+"음료"+"', '"+"1500"+"');");
            db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES('"+11+"','"+"기타"+"', '"+"0"+"');");
            db.setTransactionSuccessful();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    //메뉴 이름
    public ArrayList<String> getMenu(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT DISTINCT MENU_NM FROM "+TABLE_NAME+" ORDER BY MENU_NO;",null);
        int recordCount = cursor.getCount();
        if(recordCount < 1 ){
            return null;
        }
        ArrayList<String> memnuName = new ArrayList<String>();
        for (int i = 0 ; i < recordCount ; i++){
            cursor.moveToNext();
            memnuName.add(cursor.getString(0));
            Log.i("code",cursor.getString(0));
        }
        return memnuName;
    }

//    메뉴 번호
    public String getMenuNo(String menuNm){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT DISTINCT MENU_NO FROM "+TABLE_NAME+" WHERE MENU_NM = '"+menuNm+"';",null);
        int recordCount = cursor.getCount();
        if(recordCount < 1 ){
            return null;
        }
        cursor.moveToNext();
        return cursor.getString(0);
    }

    //메뉴 가격
    public String getMenuInfo(String menuNm){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT DISTINCT MENU_PRICE FROM "+TABLE_NAME+" WHERE MENU_NM = '"+menuNm+"';",null);
        int recordCount = cursor.getCount();
        if(recordCount < 1 ){
            return null;
        }
        cursor.moveToNext();
        return cursor.getString(0);
    }


}

package com.example.front_village.gogi.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.front_village.gogi.Dialog.UpdateDialog;
import com.example.front_village.gogi.R;

import java.util.ArrayList;


public class OrderListAdapter extends BaseAdapter {


    public ArrayList<String> menu;
    public ArrayList<String> quantity;
    public ArrayList<String> price;


    public OrderListAdapter(){
       menu = new ArrayList<String>();
       quantity = new ArrayList<String>();
       price = new ArrayList<String>();

    }


    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_list_item,parent,false);

        }


        TextView orderListNum = (TextView)convertView.findViewById(R.id.order_list_num);  //순번
        TextView orderListMenu = (TextView)convertView.findViewById(R.id.order_list_menu); //메뉴
        TextView orderListQuantity = (TextView)convertView.findViewById(R.id.order_list_quantity); //수량
        TextView orderListtotal = (TextView)convertView.findViewById(R.id.order_list_total); //합계



        orderListNum.setText(String.valueOf(position+1));
        orderListMenu.setText(menu.get(position));
        orderListQuantity.setText(quantity.get(position));
        orderListtotal.setText(price.get(position));
//        }
        return convertView;
    }

    public void add(String m, String q, String p) {
        //같은 메뉴 수량 올리기
        if(menu.contains(m)){ //등록된 메뉴 찾기
            int menuNum= menu.indexOf(m); //등록된 메뉴 번호
            //가격 더하기
            Log.i("price",Integer.valueOf(price.get(menuNum))+Integer.valueOf(p)+"");
            price.set(menuNum,String.valueOf(Integer.valueOf(price.get(menuNum))+Integer.valueOf(p)));
            //수량 더하기
            Log.i("quantity",quantity.get(menuNum));
            quantity.set(menuNum,String.valueOf(Integer.valueOf(quantity.get(menuNum))+Integer.valueOf(q)));
        }
        else {
            menu.add(m);
            quantity.add(q);
            price.add(p);
        }
        this.notifyDataSetChanged();
    }

    //총합계
    public String totalPrice(){

        int total = 0;
        for (int i = 0 ; i < price.size() ; i++){
            total += Integer.valueOf(price.get(i));

        }
        return String.format("%,d",total)+"원";

    }
    //총합계 원 없는거
    public String tPrice(){

        int total = 0;
        for (int i = 0 ; i < price.size() ; i++){
            total += Integer.valueOf(price.get(i));

        }
        return String.valueOf(total);

    }
    //계산 후 리셋
    public void clear(){
        menu.clear();
        quantity.clear();
        price.clear();

    }

    //메뉴 하나의 값
    public String menuPrice(String q, String p){
        int quantity = Integer.valueOf(q);
        int price = Integer.valueOf(p);
        Log.i("price/quantity",price/quantity+"");
        return String.valueOf(price/quantity);

    }

    //수정 된 값
    public String updateMenuPrice(String q, String p){
        int quantity = Integer.valueOf(q);
        int price = Integer.valueOf(p);
        Log.i("price*quantity",price*quantity+"");
        return String.valueOf(price*quantity);
    }

    //삭제
    public void deleteMenu(int m){
        menu.remove(m);
        quantity.remove(m);
        price.remove(m);

    }

}

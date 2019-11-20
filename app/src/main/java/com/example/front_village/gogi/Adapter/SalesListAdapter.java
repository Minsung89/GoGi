package com.example.front_village.gogi.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.front_village.gogi.Entity.OrderInfo;
import com.example.front_village.gogi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SalesListAdapter extends BaseAdapter {


    public List<OrderInfo> orderInfoList;

    public SalesListAdapter(ArrayList<OrderInfo> orderInfoList){

        this.orderInfoList = orderInfoList;
    }


    @Override
    public int getCount() {
        return orderInfoList.size();
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
            convertView = inflater.inflate(R.layout.sales_list_item,parent,false);

        }


        TextView salesOrderId = (TextView)convertView.findViewById(R.id.sales_order_id);  //영수증
        TextView salesOrderTableNo = (TextView)convertView.findViewById(R.id.sales_order_table_no); //테이블번호
        TextView salesOrderDate = (TextView)convertView.findViewById(R.id.sales_order_date); //날짜
        TextView salesOrderResult = (TextView)convertView.findViewById(R.id.sales_order_result); //계산 상태
        TextView salesOrderPrice = (TextView)convertView.findViewById(R.id.sales_order_price); //총가격
        TextView salesOrderType = (TextView)convertView.findViewById(R.id.sales_order_type); //계산 타입


        salesOrderId.setText(orderInfoList.get(position).getOrderId());
        salesOrderTableNo.setText(orderInfoList.get(position).getOrderTableNo());
        Date date = orderInfoList.get(position).getOrderDate();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy / MM / dd");
        salesOrderDate.setText(dt.format(date));
        salesOrderResult.setText(orderInfoList.get(position).getOrderResult());
        salesOrderPrice.setText(orderInfoList.get(position).getTotalPrice());
        salesOrderType.setText(orderInfoList.get(position).getOrderType());




//        }
        return convertView;
    }
    //총합계
    public String totalPrice(){

        int total = 0;
        for (int i = 0 ; i < orderInfoList.size() ; i++){
            total += Integer.valueOf(orderInfoList.get(i).getTotalPrice());

        }
        return String.format("%,d",total)+"원";

    }
}

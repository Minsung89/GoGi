package com.example.front_village.gogi.Service;


import com.example.front_village.gogi.Entity.OrderDetail;
import com.example.front_village.gogi.Entity.OrderInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OrderService {

    @GET("/orderInfo/list")
    Call<List<OrderInfo>> getOrderInfo();

    @GET("/orderDetail/list")
    Call<List<OrderDetail>> getOrderDetail();
}

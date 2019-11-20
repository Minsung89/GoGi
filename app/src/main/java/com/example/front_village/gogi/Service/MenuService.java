package com.example.front_village.gogi.Service;


import com.example.front_village.gogi.Entity.Menu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MenuService {

    @GET("/menu")
    Call<List<Menu>> getMenu();

}

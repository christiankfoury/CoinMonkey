package com.example.coinmonkey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/api/v3/coins/markets?")
    Call<List<CoinClass>> getCoin(@Query("vs_currency") String vs_currency, @Query("ids") String id);

    @GET("/api/v3/coins/{id}")
    Call<Description> getCoinDescription(@Path("id") String id);
}

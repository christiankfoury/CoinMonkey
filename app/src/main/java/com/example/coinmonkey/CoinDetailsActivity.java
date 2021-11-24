package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinDetailsActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    TextView price, low, high, marketCap, hourChange, volume, description;
    String priceText, lowText, highText, marketCapText, hourChangeText, volumeText, descriptionText;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        Intent intent = getIntent();
        TextView username,symbol;
        username = findViewById(R.id.username);
        symbol = findViewById(R.id.symbol);

        price = findViewById(R.id.price);
        low = findViewById(R.id.low);
        high = findViewById(R.id.high);
        marketCap = findViewById(R.id.marketCap);
        hourChange = findViewById(R.id.hourChange);
        volume = findViewById(R.id.volume);
        description = findViewById(R.id.coinDescriptionDetails);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CoinClass>> call = apiInterface.getCoin("usd", "bitcoin");
        System.out.println(call.request().url());
        call.enqueue(new Callback<List<CoinClass>>() {
            @Override
            public void onResponse(Call<List<CoinClass>> call, Response<List<CoinClass>> response) {
//                System.out.println("hello" + response.body());
                List<CoinClass> coinList = response.body();

                priceText = String.valueOf(coinList.get(0).getCurrent_price());
                lowText = String.valueOf(coinList.get(0).getLow_24h());
                highText = String.valueOf(coinList.get(0).getHigh_24h());
                marketCapText = String.valueOf(coinList.get(0).getMarket_cap());
                hourChangeText = String.valueOf(coinList.get(0).getPrice_change_percentage_24h());
                volumeText = String.valueOf(coinList.get(0).getTotal_volume());

                price.setText(priceText);
                low.setText(lowText);
                high.setText(highText);
                marketCap.setText(marketCapText);
                hourChange.setText(hourChangeText);
                volume.setText(volumeText);
                price.setText(priceText);
            }

            @Override
            public void onFailure(Call<List<CoinClass>> call, Throwable t) {
                Log.e(TAG, "onFailure: " +  t.getLocalizedMessage());
            }
        });

//        Call<Description> call2 = apiInterface.getCoinDescription("bitcoin");
//        call2.enqueue(new Callback<Description>() {
//            @Override
//            public void onResponse(Call<Description> call, Response<Description> response) {
//                Description description = response.body();
//                descriptionText = description.getEn();
//            }
//
//            @Override
//            public void onFailure(Call<Description> call, Throwable t) {
//                Log.e(TAG, "onFailure: " +  t.getLocalizedMessage());
//            }
//        });

//        System.out.println("TEXT" + priceText);
//        System.out.println("WHAT IS HAPPNING " + priceText);
//        price.setText(priceText);
//        low.setText(lowText);
//        high.setText(highText);
//        marketCap.setText(marketCapText);
//        hourChange.setText(hourChangeText);
//        volume.setText(volumeText);
//        price.setText(priceText);
//        description.setText(descriptionText);


        username.setText(intent.getStringExtra("username"));
        symbol.setText(intent.getStringExtra("symbol"));
    }
}
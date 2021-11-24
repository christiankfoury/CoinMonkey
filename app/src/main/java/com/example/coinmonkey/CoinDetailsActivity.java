package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinDetailsActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    TextView price, low, high, marketCap, hourChange, volume, description, coinNameDetails;
    String priceText, lowText, highText, marketCapText, hourChangeText, volumeText, descriptionText;
    ImageView coinImageDetails;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        Intent intent = getIntent();
//        TextView /*username,*/symbol;
//        username = findViewById(R.id.username);
//        symbol = findViewById(R.id.symbol);

        price = findViewById(R.id.price);
        low = findViewById(R.id.low);
        high = findViewById(R.id.high);
        marketCap = findViewById(R.id.marketCap);
        hourChange = findViewById(R.id.hourChange);
        volume = findViewById(R.id.volume);
        description = findViewById(R.id.coinDescriptionDetails);

        coinNameDetails = findViewById(R.id.coinNameDetails);
        coinImageDetails = findViewById(R.id.coinImageDetails);

        String username = intent.getStringExtra("username");
        String coinName = intent.getStringExtra("coinName");
        String symbol = intent.getStringExtra("symbol");
        coinNameDetails.setText(coinName);
        coinName = coinName.toLowerCase();
        // coingecko named avalanche avalanche-2
        if (coinName.equals("avalanche"))
            coinName = "avalanche-2";
        if (coinName.equals("binance coin"))
            coinName = "binancecoin";
        if (coinName.equals("shiba inu"))
            coinName = "shiba-inu";
        if (coinName.equals("crypto.com coin"))
            coinName = "crypto-com-chain";
        coinImageDetails.setImageResource(getApplicationContext().getResources().getIdentifier(symbol.toLowerCase(),
                "drawable", getApplicationContext().getPackageName()));


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<CoinClass>> call = apiInterface.getCoin("usd", coinName);
        System.out.println(call.request().url());
        call.enqueue(new Callback<List<CoinClass>>() {
            @Override
            public void onResponse(Call<List<CoinClass>> call, Response<List<CoinClass>> response) {
                List<CoinClass> coinList = response.body();

                priceText = String.valueOf(coinList.get(0).getCurrent_price());
                lowText = String.valueOf(coinList.get(0).getLow_24h());
                highText = String.valueOf(coinList.get(0).getHigh_24h());
                marketCapText = String.valueOf(coinList.get(0).getMarket_cap());
                hourChangeText = String.valueOf(coinList.get(0).getPrice_change_percentage_24h());
                volumeText = String.valueOf(coinList.get(0).getTotal_volume());

                price.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(coinList.get(0).getCurrent_price()));
                low.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(coinList.get(0).getLow_24h()));
                high.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(coinList.get(0).getHigh_24h()));
                marketCap.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(coinList.get(0).getMarket_cap()));
                if (coinList.get(0).getPrice_change_percentage_24h() < 0) {
                    hourChange.setTextColor(Color.parseColor("#ff0000"));
                } else {
                    hourChange.setTextColor(Color.parseColor("#4CAF50"));
                }

                hourChange.setText(String.format("%.2f", coinList.get(0).getPrice_change_percentage_24h()) + "%");

                volume.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(coinList.get(0).getTotal_volume()));
            }

            @Override
            public void onFailure(Call<List<CoinClass>> call, Throwable t) {
                Log.e(TAG, "onFailure: " +  t.getLocalizedMessage());
            }
        });

        Call<CoinDescription> call2 = apiInterface.getCoinDescription(coinName);
        System.out.println(call2.request().url());
        call2.enqueue(new Callback<CoinDescription>() {
            @Override
            public void onResponse(Call<CoinDescription> call, Response<CoinDescription> response) {
                CoinDescription descriptionObject = response.body();
                System.out.println("HELLO" + descriptionObject);
                System.out.println(descriptionText);
                descriptionText = descriptionObject.getDescription().getEn();
                if (descriptionText.equals("")) {
                    descriptionText = "No description provided. Sorry!";
                }
                description.setText(descriptionText);

            }

            @Override
            public void onFailure(Call<CoinDescription> call, Throwable t) {
                Log.e(TAG, "onFailure: " +  t.getLocalizedMessage());
            }
        });
    }
}
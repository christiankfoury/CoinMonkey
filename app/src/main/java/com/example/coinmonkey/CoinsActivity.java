package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class CoinsActivity extends AppCompatActivity {

    Context context = this;
    ArrayList<String> coinNames = new ArrayList<>();
    ArrayList<String> coinSymbols = new ArrayList<>();
    ArrayList<String> coinImages = new ArrayList<>();
    private RecyclerViewAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);

        coinNames.add("Bitcoin");
        coinNames.add("Ethereum");
        coinNames.add("Binance Coin");
        coinNames.add("USD Tether");
        coinNames.add("Solana");
        coinNames.add("Cardano");
        coinNames.add("Ripple XRP");
        coinNames.add("Polkadot");
        coinNames.add("USD Coin");
        coinNames.add("Avalanche");
        coinNames.add("Dogecoin");
        coinNames.add("SHIBA INU");
        coinNames.add("Crypto.com Coin");
        coinNames.add("Terra");
        coinNames.add("Litecoin");

        coinSymbols.add("BTC");
        coinSymbols.add("ETH");
        coinSymbols.add("BNB");
        coinSymbols.add("USDT");
        coinSymbols.add("SOL");
        coinSymbols.add("ADA");
        coinSymbols.add("XRP");
        coinSymbols.add("DOT");
        coinSymbols.add("USDC");
        coinSymbols.add("AVAX");
        coinSymbols.add("DOGE");
        coinSymbols.add("SHIB");
        coinSymbols.add("CRO");
        coinSymbols.add("LUNA");
        coinSymbols.add("LTC");

//        getApplicationContext().getResources().getDrawable(getApplicationContext().getResources().getIdentifier(coinSymbols, "drawable", getApplicationContext().getPackageName()));


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        setOnClickListener();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(coinNames,coinSymbols,coinImages,this,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnClickListener(){
        listener = new RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                String coin_symbol = coinSymbols.get(position);

                Toast.makeText(getApplicationContext(),coin_symbol + " was clicked!",Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(CoinsActivity.this,)
            }
        };
    }
}
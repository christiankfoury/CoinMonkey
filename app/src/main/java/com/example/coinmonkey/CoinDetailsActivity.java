package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CoinDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        Intent intent = getIntent();
        TextView username,symbol;
        username = findViewById(R.id.username);
        symbol = findViewById(R.id.symbol);

        username.setText(intent.getStringExtra("username"));
        symbol.setText(intent.getStringExtra("symbol"));
    }
}
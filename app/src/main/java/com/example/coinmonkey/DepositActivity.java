package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DepositActivity extends AppCompatActivity {

    Button deposit;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        deposit = findViewById(R.id.depositButton);
        input = findViewById(R.id.depositEditText);

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    double userDeposit = Double.parseDouble(deposit.getText().toString());
                }
            }
        });
    }
}
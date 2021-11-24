package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DepositActivity extends AppCompatActivity {

    Button depositButton;
    EditText input;
    TextView depositLabel;
    DatabaseHelper myDB;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        depositButton = findViewById(R.id.depositButton);
        input = findViewById(R.id.depositEditText);
        depositLabel = findViewById(R.id.depositLabel);

        User user = (User) getIntent().getSerializableExtra("user");
        depositLabel.setText("$" + user.getBalance());

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    double userDeposit = Double.parseDouble(input.getText().toString().trim());
                    myDB = new DatabaseHelper(context);
                    double updatedBalance = user.getBalance();
                    updatedBalance += userDeposit;
                    myDB.updateBalance(user.getUsername(),updatedBalance);
                    Cursor cursor = myDB.getUser(user.getUsername());
                    while(cursor.moveToNext()){
                        user.setBalance(cursor.getDouble(4));
                    }
                    depositLabel.setText("$" + user.getBalance());
                    Toast.makeText(context,"$" + userDeposit + " has been deposited into your balance",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context,"The Deposit Amount has to be in the following format -> (0.00)",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
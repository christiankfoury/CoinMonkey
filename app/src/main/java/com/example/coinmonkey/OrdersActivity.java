package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

public class OrdersActivity extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        User user = (User) getIntent().getSerializableExtra("user");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrder);
        RecyclerViewAdapterOrder adapter = new RecyclerViewAdapterOrder(user.getUsername(), context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
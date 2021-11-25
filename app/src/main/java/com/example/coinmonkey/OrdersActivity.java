package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OrdersActivity extends AppCompatActivity {

    Context context = this;
    TextView noOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        User user = (User) getIntent().getSerializableExtra("user");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrder);
        noOrders = findViewById(R.id.noOrders);

        RecyclerViewAdapterOrder adapter = new RecyclerViewAdapterOrder(user.getUsername(), context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (adapter.getItemCount() == 0) {
            noOrders.setVisibility(View.VISIBLE);
        }
    }
}
package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WatchlistActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    Context context = this;
    ArrayList<String> coinNames,coinSymbols;
    private RecyclerViewAdapter.RecyclerViewClickListener listener;
    TextView noWatchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);

        noWatchlist = findViewById(R.id.noWatchlist);

        coinNames = new ArrayList<>();
        coinSymbols = new ArrayList<>();

        myDB = new DatabaseHelper(context);
        User user = (User) getIntent().getSerializableExtra("user");
        if(myDB.getWatchlistByUsername(user.getUsername()).getCount() > 0) {
            Cursor cursor = myDB.getWatchlistByUsername(user.getUsername());
            while(cursor.moveToNext()){
                coinSymbols.add(cursor.getString(1));
                coinNames.add(cursor.getString(3));
            }

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            setOnClickListener();
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(coinNames,coinSymbols,this,listener);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            if (coinNames.size() != 0) {
//                RecyclerViewAdapter adapter = new RecyclerViewAdapter(coinNames,coinSymbols,this,listener);
//                setOnClickListener();
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                System.out.println(adapter.getItemCount());
//                System.out.println("hello");
//            } else {
//                noWatchlist.setVisibility(View.VISIBLE);
//            }
        } else {
                noWatchlist.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        coinNames = new ArrayList<>();
        coinSymbols = new ArrayList<>();

        myDB = new DatabaseHelper(context);
        User user = (User) getIntent().getSerializableExtra("user");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(coinNames,coinSymbols,this,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(myDB.getWatchlistByUsername(user.getUsername()).getCount() > 0) {
            Cursor cursor = myDB.getWatchlistByUsername(user.getUsername());
            while(cursor.moveToNext()){
                coinSymbols.add(cursor.getString(1));
                coinNames.add(cursor.getString(3));
            }

            recyclerView = findViewById(R.id.recyclerView);
            setOnClickListener();
            adapter = new RecyclerViewAdapter(coinNames,coinSymbols,this,listener);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            if (coinNames.size() != 0) {
//                RecyclerViewAdapter adapter = new RecyclerViewAdapter(coinNames,coinSymbols,this,listener);
//                setOnClickListener();
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(this));
//                System.out.println(adapter.getItemCount());
//                System.out.println("hello");
//            } else {
//                noWatchlist.setVisibility(View.VISIBLE);
//            }
        } else {
            noWatchlist.setVisibility(View.VISIBLE);
        }
    }

    private void setOnClickListener(){
        listener = new RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                String coin_symbol = coinSymbols.get(position);

                Toast.makeText(getApplicationContext(),coin_symbol + " was clicked!",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(WatchlistActivity.this,CoinDetailsActivity.class);
                i.putExtra("symbol",coin_symbol);
                i.putExtra("coinName", coinNames.get(position));
                User user = null;
                if(getIntent().getExtras() != null) {
                    user = (User) getIntent().getSerializableExtra("user");
                }
                i.putExtra("user",user);
                startActivity(i);
            }
        };
    }
}
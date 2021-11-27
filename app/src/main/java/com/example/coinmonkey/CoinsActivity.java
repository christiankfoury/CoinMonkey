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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CoinsActivity extends AppCompatActivity {

    Context context = this;
    ArrayList<String> coinNames = new ArrayList<>();
    ArrayList<String> coinSymbols = new ArrayList<>();
    ArrayList<String> coinImages = new ArrayList<>();
    private RecyclerViewAdapter.RecyclerViewClickListener listener;
    Intent intent = getIntent();
    FloatingActionButton menu,home,orders,portfolio,wishlist,settings,forum;
    TextView menuText,homeText,ordersText,portfolioText,wishlistText,settingsText,forumText,liquidCash;
    boolean isFABVisible;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins);
        setFAB();

        coinNames.add("Bitcoin");
        coinNames.add("Ethereum");
        coinNames.add("Binance Coin");
        coinNames.add("Tether");
        coinNames.add("Solana");
        coinNames.add("Cardano");
        coinNames.add("Ripple");
        coinNames.add("Polkadot");
        coinNames.add("USD-Coin");
        coinNames.add("Avalanche");
        coinNames.add("Dogecoin");
        coinNames.add("SHIBA INU");
        coinNames.add("Crypto.com Coin");
        coinNames.add("Terra-Luna");
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
        liquidCash = findViewById(R.id.liquidCash);
        User user = null;
        if(getIntent().getExtras() != null) {
            user = (User) getIntent().getSerializableExtra("user");
        }

        myDB = new DatabaseHelper(getApplicationContext());
        Cursor cursor = myDB.getUser(user.getUsername());
        cursor.moveToFirst();
        liquidCash.setText("Liquid Cash: $" + cursor.getDouble(4));



        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        setOnClickListener();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(coinNames,coinSymbols,this,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setOnClickListener(){
        listener = new RecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                String coin_symbol = coinSymbols.get(position);

                Toast.makeText(getApplicationContext(),coin_symbol + " was clicked!",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(CoinsActivity.this,CoinDetailsActivity.class);
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

    private void setFAB(){
        menu = findViewById(R.id.menuButton);
        home = findViewById(R.id.homeButton);
        orders = findViewById(R.id.ordersButton);
        portfolio = findViewById(R.id.portfolioButton);
        wishlist = findViewById(R.id.wishlistButton);
        settings = findViewById(R.id.settingsButton);
        forum = findViewById(R.id.forumButton);

        menuText = findViewById(R.id.menuTextView);
        homeText = findViewById(R.id.homeTextView);
        ordersText = findViewById(R.id.ordersTextView);
        portfolioText = findViewById(R.id.portfolioTextView);
        wishlistText = findViewById(R.id.wishlistTextView);
        settingsText = findViewById(R.id.settingsTextView);
        forumText = findViewById(R.id.forumTextView);

        home.setVisibility(View.GONE);
        orders.setVisibility(View.GONE);
        portfolio.setVisibility(View.GONE);
        wishlist.setVisibility(View.GONE);
        settings.setVisibility(View.GONE);
        forum.setVisibility(View.GONE);

        menuText.setVisibility(View.GONE);
        homeText.setVisibility(View.GONE);
        ordersText.setVisibility(View.GONE);
        portfolioText.setVisibility(View.GONE);
        wishlistText.setVisibility(View.GONE);
        settingsText.setVisibility(View.GONE);
        forumText.setVisibility(View.GONE);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABVisible){
                    home.show();
                    orders.show();
                    portfolio.show();
                    wishlist.show();
                    settings.show();
                    forum.show();
                    menuText.setVisibility(View.VISIBLE);
                    homeText.setVisibility(View.VISIBLE);
                    ordersText.setVisibility(View.VISIBLE);
                    portfolioText.setVisibility(View.VISIBLE);
                    wishlistText.setVisibility(View.VISIBLE);
                    settingsText.setVisibility(View.VISIBLE);
                    forumText.setVisibility(View.VISIBLE);
                    isFABVisible = true;
                }
                else{
                    home.hide();
                    orders.hide();
                    portfolio.hide();
                    wishlist.hide();
                    settings.hide();
                    forum.hide();
                    menuText.setVisibility(View.GONE);
                    homeText.setVisibility(View.GONE);
                    ordersText.setVisibility(View.GONE);
                    portfolioText.setVisibility(View.GONE);
                    wishlistText.setVisibility(View.GONE);
                    settingsText.setVisibility(View.GONE);
                    forumText.setVisibility(View.GONE);
                    isFABVisible = false;
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoinsActivity.this,CoinsActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoinsActivity.this,OrdersActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoinsActivity.this,PortfolioActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoinsActivity.this, WatchlistActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoinsActivity.this,SettingsActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });

        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CoinsActivity.this, ForumActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        User user = null;
        if(getIntent().getExtras() != null) {
            user = (User) getIntent().getSerializableExtra("user");
        }

        myDB = new DatabaseHelper(getApplicationContext());
        Cursor cursor = myDB.getUser(user.getUsername());
        cursor.moveToFirst();
        liquidCash.setText("Liquid Cash: $" + cursor.getDouble(4));
    }
}
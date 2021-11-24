package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity {

    FloatingActionButton menu,home,orders,portfolio,wishlist,settings;
    TextView menuText,homeText,ordersText,portfolioText,wishlistText,settingsText;
    boolean isFABVisible;
    Button deposit,withdraw,changePassword,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setFAB();

        deposit = findViewById(R.id.depositButton);
        withdraw = findViewById(R.id.withdrawButton);
        changePassword = findViewById(R.id.changePasswordButton);
        logout = findViewById(R.id.logoutButton);

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,DepositActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,WithdrawActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,ChangePasswordActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });


    }

    private void setFAB(){
        menu = findViewById(R.id.menuButton);
        home = findViewById(R.id.homeButton);
        orders = findViewById(R.id.ordersButton);
        portfolio = findViewById(R.id.portfolioButton);
        wishlist = findViewById(R.id.wishlistButton);
        settings = findViewById(R.id.settingsButton);

        menuText = findViewById(R.id.menuTextView);
        homeText = findViewById(R.id.homeTextView);
        ordersText = findViewById(R.id.ordersTextView);
        portfolioText = findViewById(R.id.portfolioTextView);
        wishlistText = findViewById(R.id.wishlistTextView);
        settingsText = findViewById(R.id.settingsTextView);

        home.setVisibility(View.GONE);
        orders.setVisibility(View.GONE);
        portfolio.setVisibility(View.GONE);
        wishlist.setVisibility(View.GONE);
        settings.setVisibility(View.GONE);

        menuText.setVisibility(View.GONE);
        homeText.setVisibility(View.GONE);
        ordersText.setVisibility(View.GONE);
        portfolioText.setVisibility(View.GONE);
        wishlistText.setVisibility(View.GONE);
        settingsText.setVisibility(View.GONE);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABVisible){
                    home.show();
                    orders.show();
                    portfolio.show();
                    wishlist.show();
                    settings.show();
                    menuText.setVisibility(View.VISIBLE);
                    homeText.setVisibility(View.VISIBLE);
                    ordersText.setVisibility(View.VISIBLE);
                    portfolioText.setVisibility(View.VISIBLE);
                    wishlistText.setVisibility(View.VISIBLE);
                    settingsText.setVisibility(View.VISIBLE);
                    isFABVisible = true;
                }
                else{
                    home.hide();
                    orders.hide();
                    portfolio.hide();
                    wishlist.hide();
                    settings.hide();
                    menuText.setVisibility(View.GONE);
                    homeText.setVisibility(View.GONE);
                    ordersText.setVisibility(View.GONE);
                    portfolioText.setVisibility(View.GONE);
                    wishlistText.setVisibility(View.GONE);
                    settingsText.setVisibility(View.GONE);
                    isFABVisible = false;
                }
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,CoinsActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,OrdersActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,PortfolioActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this, WatchlistActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsActivity.this,SettingsActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
    }
}
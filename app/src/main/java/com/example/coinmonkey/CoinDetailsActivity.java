package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinDetailsActivity extends AppCompatActivity {
    ApiInterface apiInterface;
    TextView price, low, high, marketCap, hourChange, volume, description, coinNameDetails;
    String priceText, lowText, highText, marketCapText, hourChangeText, volumeText, descriptionText, finalCoinName;
    ImageView coinImageDetails, star;
    DatabaseHelper myDB;
    Context context = this;


    double priceDouble;
    EditText amount;
    Button buy;


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        Intent intent = getIntent();
//        TextView /*username,*/symbol;
//        username = findViewById(R.id.username);
//        symbol = findViewById(R.id.symbol);

        // elements in android activity
        price = findViewById(R.id.price);
        low = findViewById(R.id.low);
        high = findViewById(R.id.high);
        marketCap = findViewById(R.id.marketCap);
        hourChange = findViewById(R.id.hourChange);
        volume = findViewById(R.id.volume);
        description = findViewById(R.id.coinDescriptionDetails);

        coinNameDetails = findViewById(R.id.coinNameDetails);
        coinImageDetails = findViewById(R.id.coinImageDetails);


        amount = findViewById(R.id.amountCoinDetails);
        buy = findViewById(R.id.buy);

        // intent
        User user = (User) getIntent().getSerializableExtra("user");
        String coinName = intent.getStringExtra("coinName");
        String symbol = intent.getStringExtra("symbol");

        // set text with proper format

        star = findViewById(R.id.star);
        checkWatchlist();

        String username = intent.getStringExtra("username");
        coinName = intent.getStringExtra("coinName");
        symbol = intent.getStringExtra("symbol");

        coinNameDetails.setText(coinName);
        // lowercase coin name for api call
        coinName = coinName.toLowerCase();
        // COINGECKO ON HOW THEY WRITE THE AMES OF CRYPTO *technicalities*
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
        // API CALL FOR MARKET DATA
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

                priceDouble = coinList.get(0).getCurrent_price();
                price.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(priceDouble));
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

        // API CALL FOR DESCRIPTION
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
        String finalSymbol = symbol;
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.equals("")) {
                    Toast.makeText(getApplicationContext(), "Amount must not be empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                // in case user input an invalid number
                try{
                    // Saving user Deposit
                    double amountInput = Double.parseDouble(amount.getText().toString().trim());
                    //Making db object
                    myDB = new DatabaseHelper(context);
                    Cursor cursor = myDB.getUser(user.getUsername());
                    cursor.moveToFirst();
                    double balance = cursor.getDouble(4);

                    if (balance < amountInput) {
                        Toast.makeText(context,"Insufficient funds",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // updating user balance
                    myDB.updateBalance(user.getUsername(), balance - amountInput);

                    // amount of coins bought
                    double amountInCoin = amountInput / priceDouble;

                    Cursor cursor1 = myDB.getPortfolioUsernameCoin(user.getUsername(), finalSymbol);
                    if (cursor1.getCount() == 0) {
                        myDB.insertPortfolio(finalSymbol, user.getUsername(), amountInCoin);
                    } else {
                        cursor1.moveToFirst();
                        double currentAmount = cursor1.getDouble(3);
                        double finalAmount = currentAmount + amountInCoin;
                        myDB.updatePortfolio(user.getUsername(), finalSymbol, finalAmount);
                    }
                    myDB.insertOrder(finalSymbol, user.getUsername(), "buy", amountInCoin);
                    Toast.makeText(getApplicationContext(), "You bought " + amountInCoin + " of this coin!", Toast.LENGTH_LONG).show();
                    amount.setText("");
//                    //getting the current Balance
//                    double updatedBalance = user.getBalance();
//                    //Updating the balance
//                    updatedBalance += amountInput;
//                    //Updating the database balance
//                    myDB.updateBalance(user.getUsername(),updatedBalance);
//                    updateBalance(user);
//                    Toast.makeText(context,"$" + amountInput + " has been deposited into your balance",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"The amount has to be in the following format -> (0.00)",Toast.LENGTH_SHORT).show();
                }
            }
        });

        finalCoinName = coinName;
        String finalSymbol1 = symbol;
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB = new DatabaseHelper(context);
                if(myDB.getWatchlist(getIntent().getStringExtra("username"),getIntent().getStringExtra("symbol")).getCount() <= 0){
                    if(myDB.insertWatchlist(getIntent().getStringExtra("symbol"),getIntent().getStringExtra("username"), getIntent().getStringExtra("coinName"))){
                        Toast.makeText(context, finalSymbol1 + " has been added to the watchlist",Toast.LENGTH_SHORT).show();
                        checkWatchlist();
                    }
                    else{
                        Toast.makeText(context, finalSymbol1 + " could not be added to the watchlist",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    myDB.deleteWatchlist(getIntent().getStringExtra("username"),getIntent().getStringExtra("symbol"));
                    checkWatchlist();
                    Toast.makeText(context, finalSymbol1 + " has been removed from your watchlist",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkWatchlist(){
        star = findViewById(R.id.star);
        myDB = new DatabaseHelper(context);
        if(myDB.getWatchlist(getIntent().getStringExtra("username"),getIntent().getStringExtra("symbol")).getCount() > 0){
            star.setImageResource(R.drawable.starfavorite);
        }
        else{
            star.setImageResource(R.drawable.star);
        }
    }
}
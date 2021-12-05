package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class SellActivity extends AppCompatActivity {

    TextView holdingNameSell, coinAmountSell, coinCurrentCashSell, initialInvestmentSell;
    EditText sellAmount;
    Button sell;
    DatabaseHelper myDB;
    HashMap<String, String> symbolToNames = new HashMap<>();
    ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    User user;
    String coinName, symbol;
    double coinAmount, currentInvestmentValue, coinPrice, initialInvestment;
    double userBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        setSymbolToName();

        holdingNameSell = findViewById(R.id.holdingNameSell);
        coinAmountSell = findViewById(R.id.coinAmountSell);
        coinCurrentCashSell = findViewById(R.id.coinCurrentCashSell);
        initialInvestmentSell = findViewById(R.id.initialInvestmentSell);
        sellAmount = findViewById(R.id.sellAmount);
        sell = findViewById(R.id.sell);

        user = (User) getIntent().getSerializableExtra("user");
        symbol = getIntent().getStringExtra("symbol").toLowerCase();
        coinName = symbolToNames.get(symbol);
        System.out.println(coinName);
        System.out.println("HELLO");
        holdingNameSell.setText(coinName);

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

        myDB = new DatabaseHelper(this);
        Cursor cursor = myDB.getPortfolioUsernameCoin(user.getUsername(), symbol.toUpperCase());
        cursor.moveToFirst();
        initialInvestment = cursor.getDouble(4);
        coinAmount = cursor.getDouble(3);

        initialInvestmentSell.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(initialInvestment));
        coinAmountSell.setText(coinAmount + "");

        cursor = myDB.getUser(user.getUsername());
        cursor.moveToFirst();
        userBalance = cursor.getDouble(4);

        new GetCurrentInvestment().execute();
    }

    class GetCurrentInvestment extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SellActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDB = new DatabaseHelper(getApplicationContext());

            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            // API CALL FOR MARKET DATA
            Call<List<CoinClass>> call = apiInterface.getCoin("usd", coinName.toLowerCase());
            try {
                Response<List<CoinClass>> response = call.execute();
                List<CoinClass> coinList = response.body();
                coinPrice = coinList.get(0).getCurrent_price();
                currentInvestmentValue = coinAmount * coinPrice;
                System.out.println("coin amount " + coinAmount);
                System.out.println("coin price " + coinPrice);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (progressDialog.isShowing())
                progressDialog.dismiss();
            coinCurrentCashSell.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                    .format(currentInvestmentValue));

            sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sellAmount.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please input a value", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Double.parseDouble(sellAmount.getText().toString()) < 0) {
                        Toast.makeText(getApplicationContext(), "Invalid amount!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Double.parseDouble(sellAmount.getText().toString()) > currentInvestmentValue) {
                        Toast.makeText(getApplicationContext(), "You do not own the sufficient amount to sell!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double sellAmountDouble = Double.parseDouble(sellAmount.getText().toString());

                    System.out.println("currentinvestvalue " + currentInvestmentValue);
                    System.out.println("sellamountdouble " + sellAmountDouble);
                    System.out.println("user balance " + user.getBalance());

                    if (currentInvestmentValue - sellAmountDouble  < 0.01 && currentInvestmentValue - sellAmountDouble > 0) {
                        myDB.deletePortfolio(user.getUsername(), symbol.toUpperCase());
                        myDB.insertOrder(symbol.toUpperCase(), user.getUsername(),"sell", currentInvestmentValue / coinPrice);
                        myDB.updateBalance(user.getUsername(), userBalance + currentInvestmentValue);
                        Toast.makeText(getApplicationContext(), currentInvestmentValue + "$ have successfully sold! You have sold all of your " + symbol.toUpperCase() + " holdings!", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PortfolioActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        return;
                    }

                    if (sellAmountDouble == currentInvestmentValue) {
                        myDB.deletePortfolio(user.getUsername(), symbol.toUpperCase());
                        myDB.insertOrder(symbol.toUpperCase(), user.getUsername(),"sell", sellAmountDouble / coinPrice);
                        myDB.updateBalance(user.getUsername(), userBalance + sellAmountDouble);
                        Toast.makeText(getApplicationContext(), sellAmountDouble + "$ have successfully sold! You have sold all of your " + symbol.toUpperCase() + " holdings!", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PortfolioActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        return;
                    }

                    double newAmountOfCoins = coinAmount - sellAmountDouble / coinPrice;
                    double newInitialInvestment = newAmountOfCoins * coinPrice;
                    myDB.updatePortfolio(user.getUsername(), symbol.toUpperCase(), newAmountOfCoins, newInitialInvestment);
                    Toast.makeText(getApplicationContext(), sellAmountDouble + "$ have successfully sold!", Toast.LENGTH_SHORT).show();

                    myDB.insertOrder(symbol.toUpperCase(), user.getUsername(),"sell", sellAmountDouble / coinPrice);
                    myDB.updateBalance(user.getUsername(), userBalance + sellAmountDouble);

                    coinAmount = newAmountOfCoins;
                    currentInvestmentValue = coinPrice * coinAmount;
                    initialInvestment = newInitialInvestment;
                    System.out.println(newAmountOfCoins + " " + initialInvestment);


                    coinAmountSell.setText(coinAmount + "");
                    coinCurrentCashSell.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                            .format(currentInvestmentValue));
                    initialInvestmentSell.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                            .format(initialInvestment));
                }
            });
        }
    }

    public void setSymbolToName() {
        symbolToNames.put("btc", "Bitcoin");
        symbolToNames.put("eth", "Ethereum");
        symbolToNames.put("bnb", "Binance Coin");
        symbolToNames.put("usdt", "Tether");
        symbolToNames.put("sol", "Solana");
        symbolToNames.put("ada", "Cardano");
        symbolToNames.put("xrp", "Ripple");
        symbolToNames.put("dot", "Polkadot");
        symbolToNames.put("usdc", "USD-coin");
        symbolToNames.put("avax", "Avalanche");
        symbolToNames.put("doge", "Dogecoin");
        symbolToNames.put("shib", "Shiba Inu");
        symbolToNames.put("cro", "Crypto.com Coin");
        symbolToNames.put("luna", "Terra-Luna");
        symbolToNames.put("ltc", "Litecoin");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("ALLLOOOOOOOO");
        Intent intent = new Intent(getApplicationContext(), PortfolioActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
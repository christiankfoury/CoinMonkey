package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
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
    String coinName;
    double coinAmount, currentInvestmentValue;

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
        String symbol = getIntent().getStringExtra("symbol").toLowerCase();
        coinName = symbolToNames.get(symbol);
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
        double initialInvestment = cursor.getDouble(4);
        coinAmount = cursor.getDouble(3);

        initialInvestmentSell.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(initialInvestment));
        coinAmountSell.setText(coinAmount + "");

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
            Call<List<CoinClass>> call = apiInterface.getCoin("usd",
                    symbolToNames.get(coinName.toLowerCase()));
            try {
                Response<List<CoinClass>> response = call.execute();
                List<CoinClass> coinList = response.body();
                double price = coinList.get(0).getCurrent_price();
                currentInvestmentValue = coinAmount * price;
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
}
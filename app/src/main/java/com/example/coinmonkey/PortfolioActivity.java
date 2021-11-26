package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.graphics.Color;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PortfolioActivity extends AppCompatActivity {
    RecyclerView recyclerViewAdapterPortfolio;
    ArrayList<String> coin_symbols = new ArrayList<>();
    ArrayList<Double> amountsCash = new ArrayList<>(), amountsCoin = new ArrayList<>(), changes = new ArrayList<>();
    Intent intent = getIntent();
    DatabaseHelper myDB;
    Context context = this;
    HashMap<String, String> symbolToNames = new HashMap<>();
    ApiInterface apiInterface;
    private static final String TAG = "PortfolioActivity";
    private ProgressDialog progressDialog;
    PieChart pieChart;
    ArrayList<PieEntry> entries;
    ArrayList<Integer> colors;
    TextView totalPortfolioValue;
    Double liquidCash = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        pieChart = findViewById(R.id.pieChart);


        // hashmap for corresponding values of symbol to names,
        // because in the database, the symbol is stored, while the api (coingecko)
        // uses the name of the coin
        symbolToNames.put("btc", "bitcoin");
        symbolToNames.put("eth", "ethereum");
        symbolToNames.put("bnb", "binancecoin");
        symbolToNames.put("usdt", "tether");
        symbolToNames.put("sol", "solana");
        symbolToNames.put("ada", "cardano");
        symbolToNames.put("xrp", "ripple");
        symbolToNames.put("dot", "polkadot");
        symbolToNames.put("usdc", "usd-coin");
        symbolToNames.put("avax", "avalanche-2");
        symbolToNames.put("doge", "dogecoin");
        symbolToNames.put("shib", "shiba-inu");
        symbolToNames.put("cro", "crypto-com-chain");
        symbolToNames.put("luna", "terra-luna");
        symbolToNames.put("ltc", "litecoin");

        new GetContacts().execute();
    }

    class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(PortfolioActivity.this);
            progressDialog.setMessage("Prepare to see your gains ... hopefully");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            myDB = new DatabaseHelper(context);
            User user = (User) getIntent().getSerializableExtra("user");

            Cursor cursor = myDB.getPortfolio(user.getUsername());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.move(1);
                coin_symbols.add(cursor.getString(1));
                // NEED TO ADD INITIAL INVESTMENT IN RECYCLER VIEW
//            amountsCash.add(cursor.getDouble(2));
                amountsCoin.add(cursor.getDouble(3));

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                // API CALL FOR MARKET DATA
                Call<List<CoinClass>> call = apiInterface.getCoin("usd",
                        symbolToNames.get(cursor.getString(1).toLowerCase()));
                try {
                    Response<List<CoinClass>> response = call.execute();
                    List<CoinClass> coinList = response.body();
                    double price = coinList.get(0).getCurrent_price();
                    double currentInvestmentValue = cursor.getDouble(3) * price;
                    amountsCash.add(currentInvestmentValue);
                    changes.add((currentInvestmentValue - cursor.getDouble(4)) / cursor.getDouble(4) * 100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            if (progressDialog.isShowing())
                progressDialog.dismiss();
            recyclerViewAdapterPortfolio = findViewById(R.id.recyclerViewPortfolio);
            RecyclerViewAdapterPortfolio adapter = new RecyclerViewAdapterPortfolio(coin_symbols, amountsCash, amountsCoin, changes, getApplicationContext());
            recyclerViewAdapterPortfolio.setAdapter(adapter);
            recyclerViewAdapterPortfolio.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            setupPieChart();
            loadPieChartData();
        }

        private void loadPieChartData(){
            colors = new ArrayList<>();
            myDB = new DatabaseHelper(getApplicationContext());

            User user = (User) getIntent().getSerializableExtra("user");

            Cursor cursor = myDB.getUser(user.getUsername());

            cursor.moveToFirst();
            liquidCash = cursor.getDouble(4);


            for(int color: ColorTemplate.MATERIAL_COLORS){
                colors.add(color);
            }

            for(int color: ColorTemplate.VORDIPLOM_COLORS){
                colors.add(color);
            }

            ArrayList<PieEntry> entries = new ArrayList<>();

            entries.add(new PieEntry(liquidCash.floatValue(),"Cash"));

            for(int i = 0; i < coin_symbols.size(); i++){
                entries.add(new PieEntry(amountsCash.get(i).floatValue(),coin_symbols.get(i).toString()));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Portfolio");
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            data.setDrawValues(true);
            data.setValueFormatter(new PercentFormatter(pieChart));
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(12f);

            pieChart.setData(data);
        }

        private void setupPieChart(){
            double totalCash = 0;
            User user = (User) getIntent().getSerializableExtra("user");
            myDB = new DatabaseHelper(getApplicationContext());
            Cursor cursor = myDB.getUser(user.getUsername());

            cursor.moveToFirst();
            liquidCash = cursor.getDouble(4);

            for(int i = 0; i < amountsCash.size(); i++){
                totalCash += amountsCash.get(i);
            }
            totalCash += liquidCash;
            pieChart.setDrawHoleEnabled(true);
            pieChart.setUsePercentValues(true);
            pieChart.setEntryLabelTextSize(12f);
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setCenterText(String.format("Total Portfolio Value $ %.2f", totalCash));
            pieChart.setCenterTextSize(12);
            pieChart.getDescription().setEnabled(false);
            pieChart.animateY(1400, Easing.EaseInOutQuad);
        }

    }
}
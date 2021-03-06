package com.example.coinmonkey;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private RecyclerViewAdapterPortfolio.RecyclerViewClickListenerPortfolio listener;
    FloatingActionButton menu,home,orders,portfolio,wishlist,settings,forum;
    TextView menuText,homeText,ordersText,portfolioText,wishlistText,settingsText,forumText;
    boolean isFABVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        pieChart = findViewById(R.id.pieChart);
        setFAB();


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

        new GetPortfolio().execute();
    }

    class GetPortfolio extends AsyncTask<Void, Void, Void> {

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
            setOnClickListener();
            RecyclerViewAdapterPortfolio adapter = new RecyclerViewAdapterPortfolio(coin_symbols, amountsCash, amountsCoin, changes, getApplicationContext(), listener);
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
    private void setOnClickListener() {
        listener = new RecyclerViewAdapterPortfolio.RecyclerViewClickListenerPortfolio() {
            @Override
            public void onClick(View view, int position) {
                finish();
                Intent i = new Intent(getApplicationContext(), SellActivity.class);
                i.putExtra("user", getIntent().getSerializableExtra("user"));
                i.putExtra("symbol",coin_symbols.get(position));
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
                Intent i = new Intent(PortfolioActivity.this,CoinsActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PortfolioActivity.this,OrdersActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PortfolioActivity.this,PortfolioActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PortfolioActivity.this, WatchlistActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PortfolioActivity.this,SettingsActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });

        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PortfolioActivity.this, ForumActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
    }

}
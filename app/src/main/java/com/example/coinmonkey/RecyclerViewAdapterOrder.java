package com.example.coinmonkey;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class RecyclerViewAdapterOrder extends RecyclerView.Adapter<RecyclerViewAdapterOrder.ViewHolder> {


    ArrayList<String> coin_symbols,types;
    ArrayList<Double> amounts;
    Context myContext;
    LayoutInflater minflator;
    DatabaseHelper myDB;
    String username;

    public RecyclerViewAdapterOrder(String username, Context myContext) {
        this.myContext = myContext;
        this.username = username;
        coin_symbols = new ArrayList<>();
        types = new ArrayList<>();
        amounts = new ArrayList<>();

        getOrders();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = minflator.from(parent.getContext()).inflate(R.layout.orders_child_view,parent,false);
        return new RecyclerViewAdapterOrder.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.symbol.setText(coin_symbols.get(position));
        if(types.get(position).equals("buy")){
            holder.type.setTextColor(Color.parseColor("#4CAF50"));
        }
        else{
            holder.type.setTextColor(Color.parseColor("#ff0000"));
        }
        String type = types.get(position);
        type = type.substring(0,1).toUpperCase() + type.substring(1, type.length());
        holder.type.setText(type);
        holder.amount.setText(amounts.get(position).toString());

        if (position % 2 == 1) {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#E6B99F"));
        } else {
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#964B00"));
        }
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.scale));
    }

    @Override
    public int getItemCount() {
        return coin_symbols.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView symbol,type,amount;
        LinearLayout parent;
        RelativeLayout relativeLayout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbol = itemView.findViewById(R.id.coinSymbolOrder);
            type = itemView.findViewById(R.id.coinTypeOrder);
            amount = itemView.findViewById(R.id.coinAmountOrder);
            parent = itemView.findViewById(R.id.recyclerView2);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutOrderRV);
            cardView = itemView.findViewById(R.id.cardViewOrder);

        }
    }

    public void getOrders(){
        myDB = new DatabaseHelper(myContext);
        Cursor cursor = myDB.getOrder(username);
        while(cursor.moveToNext()){
            coin_symbols.add(cursor.getString(1));
            types.add(cursor.getString(3));
            amounts.add(cursor.getDouble(4));
        }
    }
}

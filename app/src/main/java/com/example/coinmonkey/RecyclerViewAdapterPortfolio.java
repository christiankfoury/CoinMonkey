package com.example.coinmonkey;

import android.content.Context;
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

public class RecyclerViewAdapterPortfolio  extends RecyclerView.Adapter<RecyclerViewAdapterPortfolio.ViewHolder> {
    ArrayList<String> coin_symbols;
    ArrayList<Double> amountsCash, amountsCoin, changes;
    Context myContext;
    LayoutInflater minflator;
    private RecyclerViewClickListenerPortfolio listener;

    public RecyclerViewAdapterPortfolio(ArrayList<String> coin_symbols, ArrayList<Double> amountsCash,
                                        ArrayList<Double> amountsCoin, ArrayList<Double> changes, Context myContext,
                                        RecyclerViewClickListenerPortfolio listener) {
        this.coin_symbols = coin_symbols;
        this.amountsCash = amountsCash;
        this.amountsCoin = amountsCoin;
        this.changes = changes;
        this.myContext = myContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterPortfolio.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = minflator.from(parent.getContext()).inflate(R.layout.portfolio_child_view,parent,false);
        return new RecyclerViewAdapterPortfolio.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterPortfolio.ViewHolder holder, int position) {
        holder.coin_symbol.setText(coin_symbols.get(position));

        if(changes.get(position) >= 0){
            holder.change.setTextColor(Color.parseColor("#4CAF50"));
        }
        else{
            holder.change.setTextColor(Color.parseColor("#ff0000"));
        }
        holder.change.setText(String.format("%.2f", changes.get(position)) + " %");
        holder.amountCoin.setText(amountsCoin.get(position).toString());
        holder.amountCash.setText(String.format("$ %.2f", amountsCash.get(position)));

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView coin_symbol, amountCash,amountCoin, change;
        LinearLayout parent;
        RelativeLayout relativeLayout;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coin_symbol = itemView.findViewById(R.id.coinSymbolPortfolio);
            amountCash = itemView.findViewById(R.id.coinCurrentCashPortfolio);
            amountCoin = itemView.findViewById(R.id.coinAmountPortfolio);
            change = itemView.findViewById(R.id.coinChangePortfolio);
            parent = itemView.findViewById(R.id.recyclerView3);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutPortfolioRV);
            cardView = itemView.findViewById(R.id.cardViewPortfolio);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }

    }
    public interface RecyclerViewClickListenerPortfolio {
        void onClick(View view,int position);
    }
}

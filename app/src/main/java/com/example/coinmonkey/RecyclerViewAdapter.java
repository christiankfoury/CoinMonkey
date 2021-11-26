package com.example.coinmonkey;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    ArrayList<String> coinNameList,coinSymbolList,coinImagesList;
    Context myContext;
    LayoutInflater minflator;
    private RecyclerViewClickListener listener;

    public RecyclerViewAdapter(ArrayList<String> coinNameList, ArrayList<String> coinSymbolList, Context myContext, RecyclerViewClickListener listener) {
        this.coinNameList = coinNameList;
        this.coinSymbolList = coinSymbolList;
        this.myContext = myContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = minflator.from(parent.getContext()).inflate(R.layout.coins_child_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(coinNameList.get(position));
        holder.symbol.setText(coinSymbolList.get(position));
        holder.image.setImageResource(myContext.getResources().getIdentifier(coinSymbolList.get(position).toLowerCase(), "drawable", myContext.getPackageName()));
//        if (position % 2 == 1) {
//            holder.relativeLayout.setBackgroundColor(Color.parseColor("#E6B99F"));
//        } else {
//            holder.relativeLayout.setBackgroundColor(Color.parseColor("#964B00"));
//        }
//        getApplicationContext().getResources().getDrawable(getApplicationContext().getResources().getIdentifier(coinSymbols, "drawable", getApplicationContext().getPackageName()));
    }

    @Override
    public int getItemCount() {
        return coinSymbolList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,symbol;
        ImageView image;
        LinearLayout parent;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.coinName);
            symbol = itemView.findViewById(R.id.coinSymbol);
            image = itemView.findViewById(R.id.coinImage);
            parent = itemView.findViewById(R.id.parent_layout);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutCoinsRV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
}

package com.example.coinmonkey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterReply extends RecyclerView.Adapter<RecyclerViewAdapterReply.ViewHolder>{

    ArrayList<String> usernames,messages,timestamps;
    Context myContext;
    LayoutInflater minflator;
    DatabaseHelper myDB;

    public RecyclerViewAdapterReply(ArrayList<String> usernames, ArrayList<String> messages, ArrayList<String> timestamps, Context myContext) {
        this.usernames = usernames;
        this.messages = messages;
        this.timestamps = timestamps;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = minflator.from(parent.getContext()).inflate(R.layout.replies_child_view,parent,false);
        return new RecyclerViewAdapterReply.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(usernames.get(position));
        holder.message.setText(messages.get(position));
        holder.timestamp.setText(timestamps.get(position));
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView username,message,timestamp;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.replyChildUsername);
            message = itemView.findViewById(R.id.replyChildMessage);
            timestamp = itemView.findViewById(R.id.replyChildTimestamp);
            linearLayout = itemView.findViewById(R.id.childLinearLayout);
        }
    }
}

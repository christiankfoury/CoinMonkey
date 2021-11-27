package com.example.coinmonkey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapterPost extends RecyclerView.Adapter<RecyclerViewAdapterPost.ViewHolder>{

    ArrayList<String> usernames,messages,timestamps;
    Context myContext;
    LayoutInflater minflator;
    DatabaseHelper myDB;
    private RecyclerViewAdapterPost.RecyclerViewClickListener listener;

    public RecyclerViewAdapterPost(ArrayList<String> usernames, ArrayList<String> messages, ArrayList<String> timestamps, Context myContext, RecyclerViewAdapterPost.RecyclerViewClickListener listener) {
        this.usernames = usernames;
        this.messages = messages;
        this.timestamps = timestamps;
        this.myContext = myContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = minflator.from(parent.getContext()).inflate(R.layout.posts_child_view,parent,false);
        return new RecyclerViewAdapterPost.ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView username,message,timestamp;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.postUsername);
            message = itemView.findViewById(R.id.postMessage);
            timestamp = itemView.findViewById(R.id.postTimestamp);
            linearLayout = itemView.findViewById(R.id.parent_layout);
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

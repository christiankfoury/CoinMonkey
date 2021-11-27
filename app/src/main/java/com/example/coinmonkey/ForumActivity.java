package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    ArrayList<Integer> postIds = new ArrayList<>();
    ArrayList<String> postUsernames = new ArrayList<>();
    ArrayList<String> postMessages = new ArrayList<>();
    ArrayList<String> postTimestamps = new ArrayList<>();
    private RecyclerViewAdapterPost.RecyclerViewClickListenerPost listener;
    Button createPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        createPost = findViewById(R.id.createPostButton);

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForumActivity.this,CreatePostActivity.class);
                User user = (User) getIntent().getSerializableExtra("user");
                i.putExtra("user",user);
                startActivity(i);
            }
        });

        getPosts();

        RecyclerView recyclerView = findViewById(R.id.forumRecyclerView);
        setOnClickListener();
        RecyclerViewAdapterPost adapter = new RecyclerViewAdapterPost(postUsernames,postMessages,postTimestamps,this,listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getPosts(){
        myDB = new DatabaseHelper(this);
        if(myDB.getAllPosts().getCount() > 0){
            Cursor cursor = myDB.getAllPosts();
            while(cursor.moveToNext()){
                postIds.add(cursor.getInt(0));
                postUsernames.add(cursor.getString(1));
                postMessages.add(cursor.getString(2));
                postTimestamps.add(cursor.getString(3));
            }
        }
        else{
            String idk = "idk";
        }
    }

    private void setOnClickListener(){
        listener = new RecyclerViewAdapterPost.RecyclerViewClickListenerPost() {
            @Override
            public void onClick(View view, int position) {
                int post_id = postIds.get(position);
                Toast.makeText(getApplicationContext(), "Message " + post_id + " was clicked!", Toast.LENGTH_SHORT).show();
                User user = (User) getIntent().getSerializableExtra("user");
                Intent i = new Intent(ForumActivity.this,PostActivity.class);
                i.putExtra("user",user);
                i.putExtra("id",post_id);
                startActivity(i);
            }
        };
    }
}
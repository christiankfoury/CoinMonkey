package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    ArrayList<Integer> postIds = new ArrayList<>();
    ArrayList<String> postUsernames = new ArrayList<>();
    ArrayList<String> postMessages = new ArrayList<>();
    ArrayList<String> postTimestamps = new ArrayList<>();
    private RecyclerViewAdapterPost.RecyclerViewClickListenerPost listener;
    Button createPost;
    FloatingActionButton menu,home,orders,portfolio,wishlist,settings,forum;
    TextView menuText,homeText,ordersText,portfolioText,wishlistText,settingsText,forumText;
    boolean isFABVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        setFAB();

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
                Intent i = new Intent(ForumActivity.this,CoinsActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForumActivity.this,OrdersActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForumActivity.this,PortfolioActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForumActivity.this, WatchlistActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForumActivity.this,SettingsActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });

        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForumActivity.this, ForumActivity.class);
                i.putExtra("user",getIntent().getSerializableExtra("user"));
                startActivity(i);
            }
        });
    }
}
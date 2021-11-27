package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    ArrayList<String> usernames = new ArrayList<>();
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> timestamps = new ArrayList<>();
    TextView usernameText,messageText,timestampText;
    EditText replyInput;
    Button createReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        usernameText = findViewById(R.id.usernameReplyTextView);
        messageText = findViewById(R.id.messageReplyTextView);
        timestampText = findViewById(R.id.timestampReplyTextView);
        replyInput = findViewById(R.id.replyEditText);
        createReply = findViewById(R.id.postReplyButton);

        setMessage();
        getReplies();

        createReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB = new DatabaseHelper(getApplicationContext());
                User user = (User) getIntent().getSerializableExtra("user");
                if(!replyInput.getText().toString().trim().isEmpty()){
                    if(myDB.insertReply(Integer.valueOf(getIntent().getStringExtra("id")),user.getUsername(),replyInput.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Reply has been posted", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Reply could not be posted", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Reply Message can't be empty", Toast.LENGTH_SHORT).show();

                }

            }
        });

        RecyclerView recyclerView = findViewById(R.id.repliesRecyclerView);
        RecyclerViewAdapterReply adapter = new RecyclerViewAdapterReply(usernames,messages,timestamps,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setMessage(){
        myDB = new DatabaseHelper(this);
        if(myDB.getPostByID(Integer.valueOf(getIntent().getStringExtra("id"))).getCount() > 0){
            Cursor cursor = myDB.getPostByID(Integer.valueOf(getIntent().getStringExtra("id")));
            cursor.moveToFirst();
            usernameText.setText(cursor.getString(1));
            messageText.setText(cursor.getString(2));
            timestampText.setText(cursor.getString(3));
        }
        else{
            String idk = "idk";
        }
    }

    private void getReplies(){
        myDB = new DatabaseHelper(this);
        if(myDB.getRepliesByPost(getIntent().getStringExtra("id")).getCount() > 0){
            Cursor cursor = myDB.getRepliesByPost(getIntent().getStringExtra("id"));
            while(cursor.moveToNext()){
                usernames.add(cursor.getString(2));
                messages.add(cursor.getString(3));
                timestamps.add(cursor.getString(4));
            }
        }
        else{
            String idk = "idk";
        }
    }
}
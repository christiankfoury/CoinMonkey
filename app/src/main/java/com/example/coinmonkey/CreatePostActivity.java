package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePostActivity extends AppCompatActivity {

    Button createPostButton,cancelPostButton;
    EditText postMessage;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        createPostButton = findViewById(R.id.createButton);
        cancelPostButton = findViewById(R.id.cancelPostButton);
        postMessage = findViewById(R.id.messageEditText);

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });

        cancelPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = (User) getIntent().getSerializableExtra("user");
                Intent i = new Intent(CreatePostActivity.this,ForumActivity.class);
                i.putExtra("user",user);
                startActivity(i);
            }
        });
    }

    private void createPost(){
        myDB = new DatabaseHelper(this);
        User user = (User) getIntent().getSerializableExtra("user");
        String username = user.getUsername();
        if(!postMessage.getText().toString().trim().isEmpty()){
            if(myDB.insertPost(username,postMessage.getText().toString())){
                Toast.makeText(this,"Your message has been posted!",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(CreatePostActivity.this,ForumActivity.class);
                i.putExtra("user",user);
                startActivity(i);
            }
            else{
                Toast.makeText(this,"Your message could not be posted.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
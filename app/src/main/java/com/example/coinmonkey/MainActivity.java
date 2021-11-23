package com.example.coinmonkey;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView signUp;
    EditText usernameInput,passwordInput;
    Button login;
    DatabaseHelper myDb;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUp = findViewById(R.id.joinHereSignUp);
        usernameInput = findViewById(R.id.logInUsername);
        passwordInput = findViewById(R.id.logInPassword);
        login = findViewById(R.id.login);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb = new DatabaseHelper(context);

               if (myDb.getUser(usernameInput.getText().toString().trim()).getCount() == 0){
                   Toast.makeText(context, "This username does not exist!", Toast.LENGTH_SHORT).show();
               }
               else{
                   Cursor cursor = myDb.getUser(usernameInput.getText().toString().trim());
                   cursor.moveToFirst();
                   User user = new User(cursor.getString(0).toString(), cursor.getString(1).toString(),
                           cursor.getString(2).toString(), cursor.getString(3).toString(), cursor.getDouble(4));

                   if(!user.getPassword().equals(passwordInput.getText().toString().trim())){
                       Toast.makeText(context, "The username/password combination is invalid!", Toast.LENGTH_SHORT).show();
                   }
                   else{
                       Intent i = new Intent(MainActivity.this,CoinsActivity.class);
                       i.putExtra("user",user);
                       startActivity(i);
                   }
               }
            }
        });


    }
}
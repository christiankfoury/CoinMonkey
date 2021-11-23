package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    TextView clickHereLogIn;
    EditText signUpFirstName, signUpLastName, signUpUsername, signUpPassword, signUpConfirmPassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpFirstName = findViewById(R.id.signUpFirstName);
        signUpLastName = findViewById(R.id.signUpLastName);
        signUpUsername = findViewById(R.id.signUpUsermame);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpConfirmPassword = findViewById(R.id.signUpConfirmPassword);
        clickHereLogIn = findViewById(R.id.clickHereLogIn);
        register = findViewById(R.id.register);
        myDB = new DatabaseHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = signUpUsername.getText().toString();
                String firstName = signUpFirstName.getText().toString();
                String lastName = signUpLastName.getText().toString();
                String password = signUpPassword.getText().toString();
                String confirmPassword = signUpConfirmPassword.getText().toString();
                if (firstName.trim().equals("") || lastName.trim().equals("") || username.trim().equals("") ||
                        password.trim().equals("") || confirmPassword.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Fill up all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                if (myDB.getUser(username).getCount() == 1) {
                    Toast.makeText(getApplicationContext(), "This username already exists", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }
                myDB.insertUser(username, firstName, lastName, password);
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });

        clickHereLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });
    }
}
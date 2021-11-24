package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText currentPassword,newPassword,newPasswordConfirm;
    Button changePassword;
    Context context = this;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = findViewById(R.id.currentPasswordEditText);
        newPassword = findViewById(R.id.newPasswordEditText);
        newPasswordConfirm = findViewById(R.id.newPasswordConfirmEditText);
        changePassword = findViewById(R.id.changePasswordButton);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = (User) getIntent().getSerializableExtra("user");

                if(!user.getPassword().equals(currentPassword.getText().toString())){
                    Toast.makeText(context,"The current password input is not correct",Toast.LENGTH_SHORT).show();
                }
                else if(!newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())){
                    Toast.makeText(context,"The new password and password confirmation are not identical",Toast.LENGTH_SHORT).show();
                }
                else{
                    myDB = new DatabaseHelper(context);
                    myDB.updatePassword(user.getUsername(),newPassword.getText().toString());
                    Toast.makeText(context,"Your Password has been updated",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
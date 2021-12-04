package com.example.coinmonkey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class ForgotPasswordActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setFragment();
    }

    public void setFragment(){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EnterUsernameFragment enterUsernameFragment = new EnterUsernameFragment();
        fragmentTransaction.add(R.id.forgotPasswordFrameLayout, enterUsernameFragment, null);
        fragmentTransaction.commit();
    }
}
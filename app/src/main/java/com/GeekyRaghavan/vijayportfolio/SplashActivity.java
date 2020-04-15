package com.GeekyRaghavan.vijayportfolio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import static com.GeekyRaghavan.vijayportfolio.Constants.DAY_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.DEFAULT_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.NIGHT_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.NIGHT_MODE_SP;
import static com.GeekyRaghavan.vijayportfolio.Constants.SHARED_PREFERENCE;

public class SplashActivity extends AppCompatActivity {

    int darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);

        darkMode = preferences.getInt(NIGHT_MODE_SP, DAY_MODE);

        if (darkMode == DAY_MODE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else if (darkMode == NIGHT_MODE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if (darkMode == DEFAULT_MODE){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}

package com.GeekyRaghavan.vijayportfolio;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.GeekyRaghavan.vijayportfolio.Constants.DAY_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.DEFAULT_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.NIGHT_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.NIGHT_MODE_SP;
import static com.GeekyRaghavan.vijayportfolio.Constants.SHARED_PREFERENCE;

public class SettingActivity extends AppCompatActivity {

    private LinearLayout ll_theme;
    private TextView tv_theme;
    private LinearLayout ll_linkedin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        int dayNightMode = preferences.getInt(NIGHT_MODE_SP, DAY_MODE);

        ll_theme = findViewById(R.id.ll_theme);
        tv_theme = findViewById(R.id.tv_theme);
        ll_linkedin = findViewById(R.id.ll_linkedin);
        if (dayNightMode == DAY_MODE){
            tv_theme.setText("Light");
        }else if (dayNightMode == NIGHT_MODE){
            tv_theme.setText("Dark");
        }else if (dayNightMode == DEFAULT_MODE){
            tv_theme.setText("Default");
        }
        ll_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("Choose an animal");
// add a list
                String[] animals = {"Light", "Dark", "Default"};
                builder.setItems(animals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Editor editor = preferences.edit();
                        switch (which) {
                            case 0: // horse
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                editor.putInt(NIGHT_MODE_SP, DAY_MODE);
                                break;
                            case 1: // cow
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                editor.putInt(NIGHT_MODE_SP, NIGHT_MODE);
                                break;
                            case 2: // camel
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                editor.putInt(NIGHT_MODE_SP, DEFAULT_MODE);
                                break;
                        }
                        editor.apply();
                    }
                });
// create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ll_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(SettingActivity.this, R.color.colorAccent));
                builder.addDefaultShareMenuItem();


                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(SettingActivity.this, Uri.parse("https://www.linkedin.com/in/vijay-raghavan-159810129/"));
            }
        });
    }
}

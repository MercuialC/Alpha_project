package com.rocketboys100.playfuzhou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rocketboys100.playfuzhou.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();
    }
}

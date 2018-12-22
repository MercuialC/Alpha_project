package com.rocketboys100.playfuzhou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rocketboys100.playfuzhou.R;

public class myCustomerServices extends AppCompatActivity {

    private Button btn_backCS;
    private Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_customer_services);
        getSupportActionBar().hide();
        btn_backCS=findViewById(R.id.btn_CSback);
        btn_send=findViewById(R.id.btn_send);
        btn_backCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

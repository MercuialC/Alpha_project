package com.example.administrator.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class history extends AppCompatActivity implements View.OnClickListener {

    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}

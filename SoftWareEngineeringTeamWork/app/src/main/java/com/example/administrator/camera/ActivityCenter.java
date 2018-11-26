package com.example.administrator.camera;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;

public class ActivityCenter extends AppCompatActivity {
    private RadioButton rb_return;
    private void setDrawleft(RadioButton button, int res) {
        Drawable drawable = getResources().getDrawable(res);
        drawable.setBounds(0, 0, 40, 40);
        button.setCompoundDrawables(drawable, null, null, null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        getSupportActionBar().hide();
        rb_return = findViewById(R.id.rb_return);
        setDrawleft(rb_return,R.drawable.leftarrow);
    }
}

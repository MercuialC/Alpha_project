package com.example.administrator.camera.costom_layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.camera.R;


public class UD_text extends LinearLayout {
    private TextView tup;
    private TextView tdown;
    public UD_text(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.ud_text, this);
        tdown = findViewById(R.id.cdown);
        tup = findViewById(R.id.cup);
    }
    public void setTup(String s)
    {
        tup.setText(s);
    }
    public void setTdown(String s)
    {
        tdown.setText(s);
    }
    public String getupText()
    {
        return tup.getText().toString();
    }
    public String getdownText()
    {
        return tdown.getText().toString();
    }
}

package com.rocketboys100.playfuzhou.costom_layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocketboys100.playfuzhou.R;


public class RL_text extends LinearLayout {
    private TextView tleft;
    private TextView tright;

    public RL_text(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_twotext, this);
        tleft = findViewById(R.id.cleft);
        tright = findViewById(R.id.cright);
    }

    /*public RL_text(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.custom_twotext, this);
        tleft = findViewById(R.id.cleft);
        tright = findViewById(R.id.cright);
    }*/

    public void setrightText(String s)
    {
        tright.setText(s);
    }
    public void setleftText(String s)
    {
        tleft.setText(s);
    }
    public String getrightText()
    {
        return tright.getText().toString();
    }
    public String getleftText()
    {
        return tleft.getText().toString();
    }
}

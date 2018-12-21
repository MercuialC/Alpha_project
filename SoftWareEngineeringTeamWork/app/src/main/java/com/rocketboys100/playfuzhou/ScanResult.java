package com.rocketboys100.playfuzhou;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Time;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ScanResult extends AppCompatActivity {



    public class SendDanmu {
        private TranslateAnimation translateAnimation;
        //我用的是TextView.这个看自己喜好。这里的boolean量是我用来实现两种弹幕特效
        public SendDanmu(TextView textView, RelativeLayout relativeLayout){
            int length=relativeLayout.getBottom()-relativeLayout.getTop()-100;      //获取relativelayout的长度
            int y=relativeLayout.getTop()+(int)(Math.random()*length)+100;       //设置弹幕随机产生的y坐标
//          //天女散花型
//                translateAnimation=new TranslateAnimation(relativeLayout.getLeft(),relativeLayout.getRight(),
//                        relativeLayout.getRight(),y);
            //水平弹幕
            translateAnimation=new TranslateAnimation(0f,relativeLayout.getRight(),
                    y,y);
            translateAnimation.setDuration(3000);
            textView.setAnimation(translateAnimation);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            translateAnimation.start();
        }
    }

    private Button bBack3;
    private RelativeLayout root;
    private Random random = new Random(System.currentTimeMillis());
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        getSupportActionBar().hide();
        bBack3 =findViewById(R.id.btn_back3);
        root  = findViewById(R.id.re);
        root.getBackground().setAlpha(0);
        isrun();
        bBack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void adddamu(boolean text) {
        String[] str = {"菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼菠萝牛逼","菠萝马中马","菠萝带你timi带你飞","我是菠萝！"};
        int[] color = {R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark};//{R.color.red, R.color.blue, R.color.white,R.color.yellow};

        TextView tv = new TextView(this);
        tv.setTextSize(15);
        tv.setTextColor(Color.rgb(random.nextInt(256),random.nextInt(256), random.nextInt(256)));
        tv.setText(str[(int) (Math.random() * str.length)]);

        root.addView(tv);
        new SendDanmu(tv, root);
    }
    //使用计时器+handle持续产生弹幕。并清除界面产生的控件
    public void isrun() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                i++;
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, new Date(), 700);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (i == 3) {
                //清除产生的控件
                root.removeAllViews();
                i = 0;
            }
            adddamu(false);
            super.handleMessage(msg);
        }
    };
}

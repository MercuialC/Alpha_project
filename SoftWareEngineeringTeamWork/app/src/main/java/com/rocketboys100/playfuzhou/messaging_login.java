package com.rocketboys100.playfuzhou;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;
import com.rocketboys100.playfuzhou.R;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class messaging_login extends AppCompatActivity {
    private EditText phone;
    private EditText textcode;
    private Button sendmsg;
    private Button login;
    private String phoneNum="";
    static int i=60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_messaging_login);
        phone=(EditText)findViewById(R.id.tel_text);
        textcode=(EditText)findViewById(R.id.code_text);
        sendmsg=(Button)findViewById(R.id.sendcode_btn);
        login=(Button)findViewById(R.id.login_btn);

        MobSDK.init(messaging_login.this, "28cc0f2893be2", "2e9afed719b3cd8101acb0834a0cc88f");
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);

        sendmsg.setOnClickListener((View.OnClickListener) this);
        login.setOnClickListener((View.OnClickListener) this);
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                sendmsg.setText(i + " s");
            } else if (msg.what == -2) {
                sendmsg.setText("重新发送");
                sendmsg.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 提交验证码成功,调用注册接口，之后直接登录
                        //当号码来自短信注册页面时调用登录注册接口
                        //当号码来自绑定页面时调用绑定手机号码接口

                        Toast.makeText(getApplicationContext(), "登录成功",
                                Toast.LENGTH_SHORT).show();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Log.e("asd", "des: " + des);
                            Toast.makeText(messaging_login.this, des, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        //do something
                    }
                }
            }
        }
    };


    public void onClick(View v) {
        phoneNum = phone.getText().toString().trim();
        switch (v.getId()) {
            case R.id.sendcode_btn:
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getVerificationCode("86", phoneNum);
                sendmsg.setClickable(false);
                //开始倒计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-1);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-2);
                    }
                }).start();
                break;
            case R.id.login_btn:
                String code = textcode.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getApplicationContext(), "手机号码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", phoneNum, code);
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁回调监听接口
        SMSSDK.unregisterAllEventHandler();
    }

}

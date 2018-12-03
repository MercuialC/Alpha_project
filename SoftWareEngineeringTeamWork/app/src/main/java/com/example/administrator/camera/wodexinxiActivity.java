package com.example.administrator.camera;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.administrator.camera.costom_layouts.RL_text;
import com.example.administrator.camera.costom_layouts.UD_text;


public class wodexinxiActivity extends AppCompatActivity {

    private Button avatar;
    private RL_text nizi;
    private RL_text account;
    private RL_text gender;
    private UD_text motto;
    private UD_text location;
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private RadioButton btn_reinfo;
    private void setDrawleft(RadioButton button, int res) {
        Drawable drawable = getResources().getDrawable(res);
        drawable.setBounds(0, 0, 40, 40);
        button.setCompoundDrawables(drawable, null, null, null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wodexinxi);
        getSupportActionBar().hide();
        avatar = findViewById(R.id.btn_avatar);
        nizi = findViewById(R.id.btn_nizi);
        account = findViewById(R.id.btn_account);
        gender = findViewById(R.id.btn_gender);
        motto = findViewById(R.id.btn_motto);
        location = findViewById(R.id.btn_location);
        btn_reinfo = findViewById(R.id.rb_return_info);
        mContext = wodexinxiActivity.this;
        setDrawleft(btn_reinfo,R.drawable.leftarrow);
        account.setleftText("账户");
        account.setrightText("账户待定");
        gender.setleftText("性别");
        gender.setrightText("女");
        location.setTup("地区");
        location.setTdown("这里那里");
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_reinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nizi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                final EditText inputServer = new EditText(mContext);
                alert = builder.setTitle("昵称").setView(inputServer).setNegativeButton("取消",null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nizi.setrightText(inputServer.getText().toString());
                    }
                }).create();
                alert.show();
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] sex = new String[]{"男", "女"};
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                alert = builder.setTitle("性别").setSingleChoiceItems(sex, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                gender.setrightText("男");
                                break;
                            case 1:
                                gender.setrightText("女");
                                break;
                        }

                    }
                }).create();
                alert.show();
            }
        });
        motto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                final EditText inputServer = new EditText(mContext);
                alert = builder.setTitle("签名").setView(inputServer).setNegativeButton("取消",null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        motto.setTdown(inputServer.getText().toString());
                    }
                }).create();
                alert.show();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                final EditText inputServer = new EditText(mContext);
                alert = builder.setTitle("地区").setView(inputServer).setNegativeButton("取消",null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        location.setTdown(inputServer.getText().toString());
                    }
                }).create();
                alert.show();
            }
        });
    }

}


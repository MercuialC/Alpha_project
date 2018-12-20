package com.rocketboys100.playfuzhou;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private ImageView iv_myhead;
    private TextView tv_myname;
    private TextView tv_mygender;
    private TextView tv_mycity;
    private Button btn_return;
    private RelativeLayout rl_head;
    private RelativeLayout rl_name;
    private RelativeLayout rl_gender;
    private RelativeLayout rl_city;
    private AlertDialog alert;
    private AlertDialog.Builder builder;

    void init() {
        mContext = this;
        iv_myhead = findViewById(R.id.iv_myhead);
        tv_myname =  findViewById(R.id.tv_myname);
        tv_mygender = findViewById(R.id.tv_mygender);
        tv_mycity = findViewById(R.id.tv_mycity);
        btn_return = findViewById(R.id.btn_return);
        rl_head = findViewById(R.id.rl_head);
        rl_name = findViewById(R.id.rl_name);
        rl_gender = findViewById(R.id.rl_gender);
        rl_city = findViewById(R.id.rl_city);

        iv_myhead.setImageBitmap(UserFragment.userHead);
        tv_myname.setText(UserFragment.userName);
        tv_mygender.setText(UserFragment.userGender);
        tv_mycity.setText(UserFragment.userCity);
        btn_return.setOnClickListener(this);
        rl_head.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_gender.setOnClickListener(this);
        rl_city.setOnClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wodexinxi);
        getSupportActionBar().hide();

        init();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_return:
                finish();
                break;

            case R.id.rl_head:
                break;

            case R.id.rl_name:
                builder = new AlertDialog.Builder(mContext);
                final EditText inputServer = new EditText(mContext);
                alert = builder.setTitle("请输入新的昵称").setView(inputServer).setNegativeButton("取消",null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserFragment.userName = inputServer.getText().toString();
                        tv_myname.setText(UserFragment.userName);
                    }
                }).create();
                alert.show();
                break;

            case R.id.rl_gender:
                final String[] sex = new String[]{"男", "女","保密"};
                builder = new AlertDialog.Builder(mContext);
                alert = builder.setTitle("请选择性别").setSingleChoiceItems(sex, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserFragment.userGender = sex[which];
                        tv_mygender.setText(UserFragment.userGender);
                    }
                }).create();
                alert.show();
                break;

            case R.id.rl_city:
                final String[] cuties = new String[]{"福州","泉州","漳州","南平","三明","龙岩","莆田","宁德"};
                builder = new AlertDialog.Builder(mContext);
                alert = builder.setTitle("请选择城市").setSingleChoiceItems(cuties, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserFragment.userCity = cuties[which];
                        tv_mycity.setText(UserFragment.userCity);
                    }
                }).create();
                alert.show();
                break;
        }
    }
}

//package com.rocketboys100.playfuzhou;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//
//import com.rocketboys100.playfuzhou.R;
//import com.rocketboys100.playfuzhou.costom_layouts.RL_text;
//import com.rocketboys100.playfuzhou.costom_layouts.UD_text;
//
//
//public class UserInfoActivity extends AppCompatActivity {
//
//    private Button avatar;
//    private RL_text nizi;
//    private RL_text account;
//    private RL_text gender;
//    private UD_text motto;
//    private UD_text location;
//    private Context mContext;
//    private AlertDialog alert = null;
//    private AlertDialog.Builder builder = null;
//    private RadioButton btn_reinfo;
//    private void setDrawleft(RadioButton button, int res) {
//        Drawable drawable = getResources().getDrawable(res);
//        drawable.setBounds(0, 0, 40, 40);
//        button.setCompoundDrawables(drawable, null, null, null);
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wodexinxi);
//        getSupportActionBar().hide();
//        avatar = findViewById(R.id.btn_avatar);
//        nizi = findViewById(R.id.btn_nizi);
//        account = findViewById(R.id.btn_account);
//        gender = findViewById(R.id.btn_gender);
//        motto = findViewById(R.id.btn_motto);
//        location = findViewById(R.id.btn_location);
//        btn_reinfo = findViewById(R.id.rb_return_info);
//        mContext = UserInfoActivity.this;
//        setDrawleft(btn_reinfo,R.drawable.leftarrow);
//        account.setleftText("账户");
//        account.setrightText("账户待定");
//        gender.setleftText("性别");
//        gender.setrightText("女");
//        location.setTup("地区");
//        location.setTdown("这里那里");
//        avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        btn_reinfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        nizi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert = null;
//                builder = new AlertDialog.Builder(mContext);
//                final EditText inputServer = new EditText(mContext);
//                alert = builder.setTitle("昵称").setView(inputServer).setNegativeButton("取消",null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        nizi.setrightText(inputServer.getText().toString());
//                    }
//                }).create();
//                alert.show();
//            }
//        });
//        account.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        gender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String[] sex = new String[]{"男", "女"};
//                alert = null;
//                builder = new AlertDialog.Builder(mContext);
//                alert = builder.setTitle("性别").setSingleChoiceItems(sex, 2, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                gender.setrightText("男");
//                                break;
//                            case 1:
//                                gender.setrightText("女");
//                                break;
//                        }
//
//                    }
//                }).create();
//                alert.show();
//            }
//        });
//        motto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert = null;
//                builder = new AlertDialog.Builder(mContext);
//                final EditText inputServer = new EditText(mContext);
//                alert = builder.setTitle("签名").setView(inputServer).setNegativeButton("取消",null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        motto.setTdown(inputServer.getText().toString());
//                    }
//                }).create();
//                alert.show();
//            }
//        });
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert = null;
//                builder = new AlertDialog.Builder(mContext);
//                final EditText inputServer = new EditText(mContext);
//                alert = builder.setTitle("地区").setView(inputServer).setNegativeButton("取消",null).setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        location.setTdown(inputServer.getText().toString());
//                    }
//                }).create();
//                alert.show();
//            }
//        });
//    }
//
//}
//

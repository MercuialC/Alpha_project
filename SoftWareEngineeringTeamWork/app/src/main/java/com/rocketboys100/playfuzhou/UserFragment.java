package com.rocketboys100.playfuzhou;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener {


    private Button btn_coll;
    private Button btn_mom;
    private Button btn_info;
    private Button btn_activity;
    private  Button btn_cust;
    private Button btn_setting;
    private Button btn_userInfo;
    private Button btn_logout;
    private ImageView iv_userHead;
    private TextView tv_userName;
    private static Context mContext;
    public static String userName;
    public static String userOpenID;
    public static Bitmap userHead;
    public static String userHeadPath;
    public static String userCity;
    public static String userGender;
    private boolean hasLogined = false;
    public static final int RequestCode_Login = 0;


    private void setDrawleft(Button button, int res) {
        Drawable drawable = getResources().getDrawable(res);
        drawable.setBounds(0, 0, 50, 50);
        button.setCompoundDrawables(drawable, null, null, null);
    }
    private void setDrawright(Button button, int res) {
        Drawable drawable = getResources().getDrawable(res);
        drawable.setBounds(0, 0, 40, 40);
        button.setCompoundDrawables(null, null, drawable, null);
    }
    private void setDraw(Button button, int left,int right)
    {
        Drawable dleft = getResources().getDrawable(left);
        Drawable dright = getResources().getDrawable(right);
        dleft.setBounds(0, 0, 50, 50);
        dright.setBounds(0, 0, 50, 50);
        button.setCompoundDrawables(dleft, null, dright, null);
    }

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(Context context) {
        UserFragment fragment = new UserFragment();
        mContext = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        btn_coll = view.findViewById(R.id.btn_collection);
        btn_mom = view.findViewById(R.id.btn_moments);
        btn_info = view.findViewById(R.id.btn_info);
        btn_activity = view.findViewById(R.id.btn_activity);
        btn_cust = view.findViewById(R.id.btn_CustomerService);
        btn_userInfo = view.findViewById(R.id.btn_userInfo);
        btn_logout = view.findViewById(R.id.btn_logout);
        iv_userHead = view.findViewById(R.id.iv_userHead);
        tv_userName = view.findViewById(R.id.tv_userName);
        btn_setting = view.findViewById(R.id.btn_setting);

        btn_userInfo.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_coll.setOnClickListener(this);
        btn_mom.setOnClickListener(this);
        btn_info.setOnClickListener(this);
        btn_activity.setOnClickListener(this);
        btn_cust.setOnClickListener(this);
        btn_activity.setOnClickListener(this);
        btn_setting.setOnClickListener(this);

        btn_logout.setVisibility(View.INVISIBLE);

        setDrawright(btn_userInfo,R.drawable.rightarow);
        setDraw(btn_coll,R.drawable.collection,R.drawable.rightarow);
        setDraw(btn_mom,R.drawable.moments,R.drawable.rightarow);
        setDraw(btn_info,R.drawable.info,R.drawable.rightarow);
        setDraw(btn_activity,R.drawable.activity,R.drawable.rightarow);
        setDraw(btn_cust,R.drawable.customerservice,R.drawable.rightarow);

        return view;
    }

    private void updateUserInfoBolck(){
        if(hasLogined){
            iv_userHead.setImageBitmap(userHead);
            tv_userName.setText(userName);
            btn_userInfo.setText("个人信息");
            btn_logout.setVisibility(View.VISIBLE);
        }else{
            iv_userHead.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.head_boy ));
            tv_userName.setText("未登录");
            btn_userInfo.setText("登录/注册");
            btn_logout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.btn_userInfo:
                if(hasLogined) {
                    System.out.println("进入用户信息详情页");
                    intent = new Intent(getActivity(),UserInfoActivity.class);
                    startActivity(intent);
                    break;
                }else{
                    hasLogined = true;
                    intent = new Intent(getActivity(),Login.class);
                    startActivityForResult(intent, RequestCode_Login);
                }
                break;
            case R.id.btn_logout:
                hasLogined = false;
                updateUserInfoBolck();
                break;
            case R.id.btn_activity:
                intent = new Intent(getActivity(),ActivityCenter.class);
                startActivity(intent);
                break;
            case R.id.btn_setting:
                intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_collection:
                intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_info:
                break;
            case R.id.btn_CustomerService:
                intent = new Intent(getActivity(),myCustomerServices.class);
                startActivity(intent);
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == this.RequestCode_Login && resultCode == Login.RequestCode_Login)
        {
            userName = data.getStringExtra("userName");
            userOpenID = data.getStringExtra("userOpenID");
            userHeadPath = data.getStringExtra("userHeadPath");
            userCity = data.getStringExtra("userCity");
            userGender = data.getStringExtra("userGender");
            userHead = BitmapFactory.decodeFile(userHeadPath);
            updateUserInfoBolck();
        }
    }
}

package com.example.administrator.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_head;
    private Button btn_login;
    private Button btn_login_else;
    private Button btn_forget;
    private Button btn_register;
    private EditText et_userName;
    private EditText et_userPwd;
    private View v_login_else;
    private AlertDialog ad_qq_wechat;

    private static final String TAG = "Login";
    private static final String APP_ID = "1107961368";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;

    private String userOpenID;
    private String userName;
    private String userPwd;
    private Bitmap userHead;

    public static final int RequestCode_Login = 0;

    private void initUI() {
        iv_head = findViewById(R.id.iv_head);
        btn_login = findViewById(R.id.btn_login);
        btn_login_else = findViewById(R.id.btn_login_else);
        btn_forget = findViewById(R.id.btn_forget);
        btn_register = findViewById(R.id.btn_register);
        et_userName = findViewById(R.id.et_userName);
        et_userPwd = findViewById(R.id.et_userPwd);

        btn_login.setOnClickListener(this);
        btn_login_else.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }
    private void setDrawleft(EditText editText,int res)
    {
        Drawable drawable=getResources().getDrawable(res);
        drawable.setBounds(0,0,35,35);
        editText.setCompoundDrawables(drawable,null,null,null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);

        getSupportActionBar().hide();


        initUI();
        setDrawleft(et_userName,R.drawable.user);
        setDrawleft(et_userPwd,R.drawable.password);
        mTencent = Tencent.createInstance(APP_ID, Login.this.getApplicationContext());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userName = et_userName.getText().toString().trim();
                userPwd = et_userPwd.getText().toString().trim();
                Toast.makeText(this, userName + "\n" +userPwd , Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_login_else:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                v_login_else = View.inflate(this, R.layout.login_else, null);
                Button btn_qq = v_login_else.findViewById(R.id.btn_qq);
                Button btn_wechat = v_login_else.findViewById(R.id.btn_wechat);
                btn_qq.setOnClickListener(this);
                btn_wechat.setOnClickListener(this);
                builder.setView(v_login_else);
                ad_qq_wechat = builder.show();
                break;

            case R.id.btn_qq:
                Toast.makeText(this, "使用QQ登录", Toast.LENGTH_SHORT).show();
                mIUiListener = new BaseUiListener();
                mTencent.login(Login.this, "all", mIUiListener);
                ad_qq_wechat.hide();

                break;

            case R.id.btn_wechat:
                Toast.makeText(this, "use wechat", Toast.LENGTH_SHORT).show();
                ad_qq_wechat.hide();
                break;
            case R.id.btn_forget:
                Toast.makeText(this, "forget", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_register:
                Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, "undefined", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    private void loginFinished(String path){
        Intent intent = new Intent();
        intent.putExtra("userName", userName);
        intent.putExtra("userOpenID", userOpenID);
        intent.putExtra("userHeadPath", path);
        setResult(RequestCode_Login,intent);
        finish();
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(Login.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                //获取用户openID
                String openID = obj.getString("openid");//用于唯一标识用户身份（每一个openid与QQ号码对应）。
                String accessToken = obj.getString("access_token");//用户进行应用邀请、分享、支付等基本业务请求的凭据。
                String expires = obj.getString("expires_in");//access_token的有效时间，在有效期内可以发起业务请求，过期失效。
                userOpenID = openID;

                //获取用户名称、用户头像
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG, "登录成功" + response.toString());
                        JSONObject jsonFile = (JSONObject) response;
                        try {
                            userName = jsonFile.getString("nickname");
                            String userHeadURL = jsonFile.getString("figureurl_qq_2");
                            URL url = new URL(userHeadURL);
                            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setReadTimeout(5000);
                            if(connection.getResponseCode()==200)//success
                            {
                                System.out.println("get userName: " + userName);
                                System.out.println("get userOpenID: " + userOpenID);

                                InputStream is = connection.getInputStream();
                                File file = new File(getCacheDir(),"userHead.png");
                                FileOutputStream fos = new FileOutputStream(file);
                                int len = -1;
                                byte[] buffer = new byte[1024];
                                while( (len=is.read(buffer)) != -1 ){
                                    fos.write(buffer,0,len);
                                }
                                fos.close();
                                is.close();

                                loginFinished(file.getPath());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(Login.this, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(Login.this, "授权取消", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}

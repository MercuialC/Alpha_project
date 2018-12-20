package com.rocketboys100.playfuzhou;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.rocketboys100.playfuzhou.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String serverURL= "http://192.168.137.85:8080/";

    private Button btn_camera;
    private RadioButton btncamera;
    private RadioButton btnhome;
    private RadioButton btnuser;
    private RadioGroup mRgTab;
    private List<Fragment> mFragmentList = new ArrayList<>();

    private RelativeLayout rl_un;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        context = getApplicationContext();
        btncamera = findViewById(R.id.rb_camera);
        btnhome = findViewById(R.id.rb_home);
        btnuser = findViewById(R.id.rb_user);
        btn_camera = findViewById(R.id.btn_camera);

        // setDrawleft(btncamera,R.drawable.camera_black);
        // setDrawleft(btncamera,R.drawable.camera_red);
        // setDrawleft(btnhome,R.drawable.home_black);
        //setDrawleft(btnhome,R.drawable.hong_red);
        //setDrawleft(btnuser,R.drawable.user_black);
        // setDrawleft(btnuser,R.drawable.user_red);

        mRgTab = (RadioGroup) findViewById(R.id.rg_main);
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        changeFragment(HomeFragment.class.getName());
                        break;
                    case R.id.rb_camera:
                        changeFragment(CameraFragment.class.getName());
                        break;
                    case R.id.rb_user:
                        changeFragment(UserFragment.class.getName());
                        break;
                }
            }
        });
        if (savedInstanceState == null) {
            changeFragment(CameraFragment.class.getName());
        }

    }

    public void changeFragment(String tag) {
        hideFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
      //  fragment = CameraFragment.newInstance();
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (tag.equals(CameraFragment.class.getName())) {
                fragment = CameraFragment.newInstance(this);
            } else if (tag.equals(HomeFragment.class.getName())) {
                fragment = HomeFragment.newInstance();
            } else if (tag.equals(UserFragment.class.getName())) {
                fragment = UserFragment.newInstance(getApplicationContext());
                Activity activity = fragment.getActivity();
            }
            mFragmentList.add(fragment);
            transaction.add(R.id.fl_container, fragment, fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();
}
    /**
     * hide all fragment
     */
    private void hideFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Fragment f : mFragmentList) {
            ft.hide(f);
        }
        ft.commit();
    }
}

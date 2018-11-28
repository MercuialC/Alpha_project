package com.example.administrator.camera;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class ActivityCenter extends AppCompatActivity {
    private RadioButton rb_return;
    private RadioGroup mRgTab;
    private RadioButton rb_atten;
    private RadioButton rb_recom;

    private List<Fragment> mFragmentList = new ArrayList<>();

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
        rb_atten = findViewById(R.id.rb_atten);
        rb_recom = findViewById(R.id.rb_recom);
        setDrawleft(rb_return,R.drawable.leftarrow);
        mRgTab =  findViewById(R.id.rg_topmain);
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_atten:
                        changeFragment(AttenFragment.class.getName());
                        break;
                    case R.id.rb_recom:
                        changeFragment(RecomFragment.class.getName());
                        break;
                    case R.id.rb_return:
                        finish();
                        break;
                }
            }
        });
        if (savedInstanceState == null) {
            changeFragment(AttenFragment.class.getName());
        }
    }
    public void changeFragment(String tag) {
        hideFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            if (tag.equals(AttenFragment.class.getName())) {
                fragment = AttenFragment.newInstance(this);
            } else if (tag.equals(RecomFragment.class.getName())) {
                fragment = RecomFragment.newInstance();
                Activity activity = fragment.getActivity();
            }
            mFragmentList.add(fragment);
            transaction.add(R.id.fl_container_acti, fragment, fragment.getClass().getName());
        }
        transaction.commitAllowingStateLoss();
    }
    private void hideFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Fragment f : mFragmentList) {
            ft.hide(f);
        }
        ft.commit();
    }
}

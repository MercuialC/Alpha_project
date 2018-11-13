package com.example.administrator.camera;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {


    private Button btncoll;
    private Button btnmom;
    private Button btninfo;
    private Button btnactivity;
    private  Button btncust;
    private Button btnuserinfo;



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

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        RelativeLayout rl_un = view.findViewById(R.id.rl_logined);
        btncoll = view.findViewById(R.id.btn_collection);
        btnmom = view.findViewById(R.id.btn_moments);
        btninfo = view.findViewById(R.id.btn_info);
        btnactivity = view.findViewById(R.id.btn_activity);
        btncust = view.findViewById(R.id.btn_CustomerService);
        btnuserinfo = view.findViewById(R.id.btn_userInfo);
        setDrawright(btnuserinfo,R.drawable.rightarow);
        setDraw(btncoll,R.drawable.collection,R.drawable.rightarow);
        setDraw(btnmom,R.drawable.moments,R.drawable.rightarow);
        setDraw(btninfo,R.drawable.info,R.drawable.rightarow);
        setDraw(btnactivity,R.drawable.activity,R.drawable.rightarow);
        setDraw(btncust,R.drawable.customerservice,R.drawable.rightarow);




        //rl_un.setVisibility(View.INVISIBLE);


        return view;
    }

}

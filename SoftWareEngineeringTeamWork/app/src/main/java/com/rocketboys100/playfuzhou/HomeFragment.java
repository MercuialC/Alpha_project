package com.rocketboys100.playfuzhou;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rocketboys100.playfuzhou.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btn_map;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btn_map = view.findViewById(R.id.btn_map);
        btn_map.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_map:
                Toast.makeText(getActivity(), "打开地图", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),gaodemap.class);
                startActivity(intent);
                break;
        }
    }
}

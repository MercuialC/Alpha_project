package com.rocketboys100.playfuzhou;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocketboys100.playfuzhou.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecomFragment extends Fragment {


    public RecomFragment() {
        // Required empty public constructor
    }

    public static RecomFragment newInstance() {
        RecomFragment fragment = new RecomFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recom, container, false);
    }

}

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
public class AttenFragment extends Fragment {

    private ActivityCenter activity;

    public AttenFragment() {
        // Required empty public constructor
    }

    public static AttenFragment newInstance(ActivityCenter activity) {
        AttenFragment fragment = new AttenFragment();
        fragment.activity = activity;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_atten, container, false);
    }

}

package com.example.sungh.pettie.News.NewsViewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sungh.pettie.R;

/**
 * Created by sungh on 2017/5/15.
 */

public class KnowledgeViewpager extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.viewpager_two, container, false);
    }
}

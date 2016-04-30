package com.example.arthome.newexchangeworld.ItemPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.R;

import java.util.ArrayList;

/**
 * Created by arthome on 2016/4/10.
 */
public class ItemFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> items = new ArrayList<>();

    public ItemFragment(){};

    public static ItemFragment newInstance(){
        ItemFragment fragment = new ItemFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //原本的initView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //列數為2
        int spanCount = 2;
        mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //建構臨時數據源
        for (int i = 0; i < 10; i++) {
            items.add("item"+i);
        }
        mAdapter = new ItemAdapter(items);
        mRecyclerView.setAdapter(mAdapter);
    }
}

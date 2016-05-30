package com.example.arthome.newexchangeworld.ItemPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.R;

/**
 * Created by arthome on 2016/5/31.
 */
public class ItemCategory extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_category, container, false);
    }

    public static ItemCategory newInstance() {
        ItemCategory fragment = new ItemCategory();
        return fragment;
    }
}

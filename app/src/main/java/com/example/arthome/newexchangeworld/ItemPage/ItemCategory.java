package com.example.arthome.newexchangeworld.ItemPage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.Constant;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.util.StringTool;

/**
 * Created by arthome on 2016/5/31.
 */
public class ItemCategory extends Fragment implements View.OnClickListener {
    View layoutView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.item_category, container, false);
        findUIviewOnClick();
        return layoutView;
    }

    public static ItemCategory newInstance() {
        ItemCategory fragment = new ItemCategory();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_3c:
                setBundleAndTranscation(Constant.CATEGORY_3C);
                break;
            case R.id.category_others:
                setBundleAndTranscation(Constant.CATEGORY_OTHERS);
            case R.id.category_cosmetic:
                setBundleAndTranscation(Constant.CATEGORY_COSMETIC);
                break;
            case R.id.category_clothes:
                setBundleAndTranscation(Constant.CATEGORY_CLOTHES);
                break;
            case R.id.category_textbooks:
                setBundleAndTranscation(Constant.CATEGORY_TEXTBOOKS);
                break;
            case R.id.category_books:
                setBundleAndTranscation(Constant.CATEGORY_BOOKS);
                break;
        }
    }

    private void setBundleAndTranscation(String category) {
        Bundle bundle;
        Fragment fragment;
        FragmentTransaction transaction;
        bundle = new Bundle();
        bundle.putString(Constant.INTENT_CATEGORY, category);
        fragment = ItemFragment.newInstance();
        fragment.setArguments(bundle);

        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.item_container, fragment).addToBackStack(null);
        transaction.commit();
    }

    private void findUIviewOnClick() {
        TextView textView = (TextView) layoutView.findViewById(R.id.category_3c);
        textView.setOnClickListener(this);
        textView = (TextView) layoutView.findViewById(R.id.category_books);
        textView.setOnClickListener(this);
        textView = (TextView) layoutView.findViewById(R.id.category_textbooks);
        textView.setOnClickListener(this);
        textView = (TextView) layoutView.findViewById(R.id.category_clothes);
        textView.setOnClickListener(this);
        textView = (TextView) layoutView.findViewById(R.id.category_cosmetic);
        textView.setOnClickListener(this);
        textView = (TextView) layoutView.findViewById(R.id.category_others);
        textView.setOnClickListener(this);
    }
}

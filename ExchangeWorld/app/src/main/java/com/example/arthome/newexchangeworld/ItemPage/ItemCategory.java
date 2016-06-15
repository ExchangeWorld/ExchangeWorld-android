package com.example.arthome.newexchangeworld.ItemPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.MapFragment;
import com.example.arthome.newexchangeworld.R;
import com.example.arthome.newexchangeworld.tabFragment;

/**
 * Created by arthome on 2016/5/31.
 */
public class ItemCategory extends Fragment implements View.OnClickListener {
        View layoutView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView =  inflater.inflate(R.layout.item_category, container, false);
        findUIviewOnClick();
        return layoutView;
    }

    public static ItemCategory newInstance() {
        ItemCategory fragment = new ItemCategory();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(getContext(),Integer.toString(v.getId())+" pressed",Toast.LENGTH_SHORT).show();
        switch (v.getId()){
            case R.id.category_3c:
                //TODO find fragment then transition
                //Fragment page = getParentFragment().getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.Viewpager + ":1");
                //if(page!=null){
                    //Toast.makeText(getContext(),"good",Toast.LENGTH_SHORT).show();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_category, ItemFragment.newInstance()).addToBackStack(null);
                    transaction.commit();
                //}
                //else {
                //    Toast.makeText(getContext(),"bad",Toast.LENGTH_SHORT).show();
                //}

                break;
            case R.id.category_others:
                break;
            case R.id.category_cosmetic:
                break;
            case R.id.category_clothes:
                break;
            case R.id.category_textbooks:
                break;
            case R.id.category_books:
                break;
        }
    }

    private void findUIviewOnClick(){
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

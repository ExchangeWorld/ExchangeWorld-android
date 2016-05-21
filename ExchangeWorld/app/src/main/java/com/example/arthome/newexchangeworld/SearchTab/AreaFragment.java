package com.example.arthome.newexchangeworld.SearchTab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.MapFragment;
import com.example.arthome.newexchangeworld.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by SSD_win8 on 2016/5/21.
 */
public class AreaFragment extends Fragment implements View.OnClickListener {
    AreaSelectedListener areaCallback;
    public interface AreaSelectedListener{
        public void MapZooming(LatLng latlng);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area, container, false);
        ImageView taiwan = (ImageView) view.findViewById(R.id.imageView4);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.imageView5);
        taiwan.setOnClickListener(this);
        return view;
    }

    public static AreaFragment newInstance() {
        AreaFragment fragment = new AreaFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView4 :
                Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
                View vv = getParentFragment().getView();
                ViewPager page = (ViewPager)vv.findViewById(R.id.Viewpager);
                page.setCurrentItem(0);
                LatLng latLng = new LatLng(24.144403, 120.646736);
                areaCallback.MapZooming(latLng);
                break;
            case R.id.imageView5 :
                Toast.makeText(getContext(),"5",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

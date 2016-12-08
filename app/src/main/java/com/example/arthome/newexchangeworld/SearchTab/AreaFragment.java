package com.example.arthome.newexchangeworld.SearchTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.arthome.newexchangeworld.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by SSD_win8 on 2016/5/21.
 */
public class AreaFragment extends Fragment implements View.OnClickListener {
    AreaSelectedListener areaCallback;
    public interface AreaSelectedListener{
        public void MapZooming(LatLng latlng, int zoomSize);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area, container, false);
       ImageView taiwan = (ImageView) view.findViewById(R.id.im_taiwan);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_keelung);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_taipei);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_newtaipeicity);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_taoyuan);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_hsinchucity);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_hsinchu);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_miaoli);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_taichung);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_nantou);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_changhua);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_yunlin);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_chiayi);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_chiayicity);
        taiwan.setOnClickListener(this);
        taiwan = (ImageView) view.findViewById(R.id.im_tainan);
        taiwan.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachFragment(getParentFragment());
    }

    public void onAttachFragment(Fragment fragment)
    {
        try
        {
            areaCallback = (AreaSelectedListener)fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }

    public static AreaFragment newInstance() {
        AreaFragment fragment = new AreaFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        LatLng latLng;
        switch (v.getId()){
            case R.id.im_taiwan :
                latLng = new LatLng(23.899582, 121.063496);
                areaCallback.MapZooming(latLng,7);
                moveTomap();
                break;
            case R.id.im_keelung :
                latLng = new LatLng(25.130554, 121.739196);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_taipei :
                latLng = new LatLng(25.046156, 121.516632);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_newtaipeicity :
                latLng = new LatLng(25.001949, 121.507513);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_taoyuan :
                latLng = new LatLng(24.990299, 121.313124);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_hsinchucity :
                latLng = new LatLng(24.812201, 120.966795);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_hsinchu :
                latLng = new LatLng(24.838437, 121.010540);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_miaoli :
                latLng = new LatLng(24.569335, 120.822318);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_taichung :
                latLng = new LatLng(24.150844, 120.663687);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_nantou :
                latLng = new LatLng(23.968105, 120.961614);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_changhua :
                latLng = new LatLng(24.081202, 120.538816);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_yunlin :
                latLng = new LatLng(23.711487, 120.541342);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_chiayi :
                latLng = new LatLng(23.455825, 120.291164);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_chiayicity :
                latLng = new LatLng(23.478342, 120.441797);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
            case R.id.im_tainan :
                latLng = new LatLng(22.991102, 120.171242);
                areaCallback.MapZooming(latLng,15);
                moveTomap();
                break;
        }
    }
    public void moveTomap(){
        View vv = getParentFragment().getView();
        ViewPager page = (ViewPager)vv.findViewById(R.id.userpage_viewpager);
        page.setCurrentItem(0); // mapfragment is at 0
    }
}

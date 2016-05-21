package com.example.arthome.newexchangeworld;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SupportMapFragment mSupportMapFragment;

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {
                        mMap =googleMap;
                        googleMap.getUiSettings().setAllGesturesEnabled(true);

                        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                            mMap.setMyLocationEnabled(true);
                        } //check location permission

                        LatLng sydney = new LatLng(-34, 151);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        sydney = new LatLng(24.989042, 121.546373);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("世新大學"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
                    }
                }

            });
        }


        return inflater.inflate(R.layout.map, container, false);
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();

        return fragment;
    }


}
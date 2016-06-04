package com.example.arthome.newexchangeworld;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arthome.newexchangeworld.Models.GoodsModel;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
                        new downloadGoodsAPI().execute("http://exwd.csie.org:43002/api/goods/search");
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
    public void move(LatLng latlng,int zoomSize){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoomSize));
    }

    public class downloadGoodsAPI extends AsyncTask<String,String,List<GoodsModel>> {

        @Override
        protected List<GoodsModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //int goods_count=1;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect(); // it still works without this line, don't know why
                List<GoodsModel> goodsModelList = new ArrayList<>();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String sJson;
                sJson= buffer.toString();

                JSONArray jsonArray= null; //try and catch?
                try {
                    jsonArray = new JSONArray(sJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GoodsModel goodsModel = null;
                for(int i=0;i<jsonArray.length();i++){
                    Gson gson = new Gson();
                    try {
                        goodsModel = gson.fromJson(jsonArray.get(i).toString(), GoodsModel.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(goodsModel!=null) {
                        //Log.i("oscart",Integer.toString(i)+goodsModel.getName());
                        goodsModelList.add(goodsModel);
                    }
                }

                return goodsModelList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<GoodsModel> result) {
            super.onPostExecute(result);
            //mText.setText(result.toString());
            //TODO need to set data to list
            if(result != null){
                setGoodsMap(result);
            }
        }
    }
    public void setGoodsMap(List<GoodsModel> listGoodsModel){
        for(int i=0;i<listGoodsModel.size();i++){
            double lat = listGoodsModel.get(i).getPosition_x();
            double lng = listGoodsModel.get(i).getPosition_y();
            String title = listGoodsModel.get(i).getName();
            LatLng sydney = new LatLng(lng, lat); //check if x is lat or x is lng
            Log.i("oscart", title + " " + Double.toString(lat));
            mMap.addMarker(new MarkerOptions().position(sydney).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15)); //for test, remove later
        }
    }

}
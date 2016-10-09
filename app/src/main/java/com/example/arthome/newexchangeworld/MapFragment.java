package com.example.arthome.newexchangeworld;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ItemPage.ItemDetailActivity;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    private GoogleMap mMap;
    private Button cancelButton, uploadButton;
    private Marker draggableMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        cancelButton = (Button) view.findViewById(R.id.map_cancel_button);
        uploadButton = (Button) view.findViewById(R.id.map_upload_button);

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
                        mMap = googleMap;
                        googleMap.getUiSettings().setAllGesturesEnabled(true);

                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            mMap.setMyLocationEnabled(true);
                        } //check location permission

                        LatLng sydney = new LatLng(-34, 151);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        sydney = new LatLng(24.989042, 121.546373);
//                        mMap.addMarker(new MarkerOptions().position(sydney).title("世新大學").draggable(true));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
                        mMap.setOnInfoWindowClickListener(myInfoWindowClickListener());
                        mMap.setOnMarkerClickListener(myMarkerClickListener());
                        mMap.setOnMarkerDragListener(myMarkerDragListener());
                        new downloadGoodsAPI().execute("http://exwd.csie.org:43002/api/goods/search");
                    }
                }
            });
        }
        return view;
    }

    @NonNull
    private GoogleMap.OnMarkerClickListener myMarkerClickListener() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (allMarkersMap.containsKey(marker)) {
                    marker.showInfoWindow();
                    LatLng latLng = marker.getPosition();
                    //latLng = new LatLng(latLng.latitude, latLng.longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.moveCamera(CameraUpdateFactory.scrollBy(0, -200));//move
                } else {

                }
                return true; // if return true, will not do default(move to center and show infowindow)
            }
        };
    }

    @NonNull
    private GoogleMap.OnInfoWindowClickListener myInfoWindowClickListener() {
        return new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                intent.putExtra("goodModel", new Gson().toJson(allMarkersMap.get(marker)));
                startActivity(intent);
            }
        };
    }

    private GoogleMap.OnMarkerDragListener myMarkerDragListener() {
        return new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(getContext(), marker.getPosition().latitude + "\n" + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();

        return fragment;
    }

    public void move(LatLng latlng, int zoomSize) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomSize));
    }

    public class downloadGoodsAPI extends AsyncTask<String, String, List<GoodsModel>> {

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
                sJson = buffer.toString();

                JSONArray jsonArray = null; //try and catch?
                try {
                    jsonArray = new JSONArray(sJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GoodsModel goodsModel = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    Gson gson = new Gson();
                    try {
                        goodsModel = gson.fromJson(jsonArray.get(i).toString(), GoodsModel.class);
                        String goods_imageUrl = goodsModel.getPhoto_path();
                        goods_imageUrl = goods_imageUrl.substring(2, goods_imageUrl.length() - 2);
                        goodsModel.setPhoto_path(goods_imageUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (goodsModel != null) {
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
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
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
            if (result != null) {
                setGoodsMap(result);
            }
        }
    }

    Map<Marker, GoodsModel> allMarkersMap = new HashMap<Marker, GoodsModel>();  //hash map for infowindow
    ImageView user_image;

    public void setGoodsMap(List<GoodsModel> listGoodsModel) {
        BitmapDescriptor icon;
        for (int i = 0; i < listGoodsModel.size(); i++) {
            double lat = listGoodsModel.get(i).getPosition_x();
            double lng = listGoodsModel.get(i).getPosition_y();
            String title = listGoodsModel.get(i).getName();
            LatLng sydney = new LatLng(lng, lat); //check if x is lat or x is lng
            Log.i("oscart", title + " " + Double.toString(lat));
            switch (listGoodsModel.get(i).getCategory()) {//TODO add all category
                case "Textbooks":
                    icon = getBitmapDescriptor(R.drawable.category_textbooks);
                    break;
                case "Others":
                    icon = getBitmapDescriptor(R.drawable.category_others);
                    break;
                case "Books":
                    icon = getBitmapDescriptor(R.drawable.category_books);
                    break;
                default:
                    icon = getBitmapDescriptor(R.drawable.category_books);
                    break;
            }
            Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title(title).icon(icon));
            allMarkersMap.put(marker, listGoodsModel.get(i));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15)); //for test, remove later
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {//InfoWindow
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {    //TODO finish infowindow
                Log.i("oscart", marker.getId() + marker.getTitle());
                View view = getActivity().getLayoutInflater().inflate(R.layout.item_goods, null);
                TextView goods_textView = (TextView) view.findViewById(R.id.id_goods_name);
                TextView user_textView = (TextView) view.findViewById(R.id.id_user_name);
                ImageView category_image = (ImageView) view.findViewById(R.id.category_image);
                ImageView goods_image = (ImageView) view.findViewById(R.id.goods_image);
                user_image = (ImageView) view.findViewById(R.id.user_image);
                GoodsModel good = allMarkersMap.get(marker);
                user_textView.setText(good.getOwner().getName());
                goods_textView.setText(good.getName());
                if (marker.isInfoWindowShown()) { //TODO bug, have to click twice to show pic
                    Picasso.with(getContext()).load(good.getPhoto_path()).into(goods_image);
                    Log.i("oscart", "shown");
                } else { // if it's the first time, load the image with the callback set
                    Picasso.with(getContext()).load(good.getPhoto_path()).into(goods_image, new InfoWindowRefresher(marker));
                    Log.i("oscart", "not shown");
                }
                return view;
            }
        });
    }

    private class InfoWindowRefresher implements com.squareup.picasso.Callback {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            if (markerToRefresh != null && markerToRefresh.isInfoWindowShown()) {
                Log.i("oscart", "success");
                markerToRefresh.showInfoWindow();
            }
        }

        @Override
        public void onError() {
            Log.i("oscart", "error");
        }
    }


    //google marker cant add vector image
    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(getContext(), id);
        int h = vectorDrawable.getIntrinsicHeight();
        int w = vectorDrawable.getIntrinsicWidth();
        vectorDrawable.setBounds(0, 0, 80, 80);//set to size
        Bitmap bm = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);//set to size
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    public void setDraggableMarker() {
//        LatLng sydney = new LatLng(24.989042, 121.546373);
        LatLng myLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        draggableMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("長按並拖曳定位").draggable(true));
    }
}
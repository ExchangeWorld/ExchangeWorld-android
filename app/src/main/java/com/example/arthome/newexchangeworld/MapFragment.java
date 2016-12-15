package com.example.arthome.newexchangeworld;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.ItemPage.ItemDetailActivity;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.PostModel;
import com.example.arthome.newexchangeworld.Models.UploadImageModel;
import com.example.arthome.newexchangeworld.MyPage.MyItemDetailActivity;
import com.example.arthome.newexchangeworld.util.CategoryTool;
import com.example.arthome.newexchangeworld.util.StringTool;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment implements View.OnClickListener {

    private GoogleMap mMap;
    private Button cancelButton, uploadButton;
    private Marker draggableMarker;
    private PostModel postModelDetail;
    String exToken;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        cancelButton = (Button) view.findViewById(R.id.map_cancel_button);
        uploadButton = (Button) view.findViewById(R.id.map_upload_button);

        cancelButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

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
//                        new downloadGoodsAPI().execute("http://exwd.csie.org:43002/api/goods/search");
                        downloadGoods();    //下載物品&設定地圖上的icon
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
                    mMap.moveCamera(CameraUpdateFactory.scrollBy(0, -250));//move
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
                GoodsModel goodsModel = allMarkersMap.get(marker);
                if (user != null && user.getUid() == goodsModel.getOwner().getUid()) {  //if the goods is the current users, start MyItemDetailActivity
                    Intent intent = new Intent(getActivity(), MyItemDetailActivity.class);
                    intent.putExtra(Constant.INTENT_GOODS, new Gson().toJson(allMarkersMap.get(marker)));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                    intent.putExtra(Constant.INTENT_GOODS, new Gson().toJson(allMarkersMap.get(marker)));
                    startActivity(intent);
                }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_cancel_button:
                setUploadView(false);
                break;
            case R.id.map_upload_button:
                postModelDetail.setPosition_x((float) draggableMarker.getPosition().longitude);
                postModelDetail.setPosition_y((float) draggableMarker.getPosition().latitude);
//                new  uploadImageTask().execute(postModelDetail);

                Observable<ResponseBody> call = new RestClient().getExchangeService().upLoadImageRxJava(user.getExToken(),
                        new UploadImageModel(convertPathTOBase(postModelDetail.getPhoto_path())));
                call.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {
                                System.out.println(">>>onCompleted");
                                Call<ResponseBody> uploadGood = new RestClient().getExchangeService().upLoadGoods(user.getExToken(), postModelDetail);
                                uploadGood.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.code() == 201) {
                                            Toast.makeText(getContext(), "上傳成功", Toast.LENGTH_SHORT).show();
                                            downloadGoods();
                                        } else
                                            Toast.makeText(getContext(), "上傳失敗 status code錯誤", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getContext(), "上傳物品失敗 onFailure", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getContext(), "上傳圖片失敗 onError", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    postModelDetail.setPhoto_path(responseBody.string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                setUploadView(false);

                break;
        }
    }

    Map<Marker, GoodsModel> allMarkersMap = new HashMap<Marker, GoodsModel>();  //hash map for infowindow
    ImageView user_image;

    public void setGoodsMap(List<GoodsModel> listGoodsModel) {
        mMap.clear();   //清除所有marker
        allMarkersMap.clear();

        BitmapDescriptor icon;
        for (int i = 0; i < listGoodsModel.size(); i++) {
            double lat = listGoodsModel.get(i).getPosition_x();
            double lng = listGoodsModel.get(i).getPosition_y();
            String title = listGoodsModel.get(i).getName();
            LatLng sydney = new LatLng(lng, lat);
            Log.i("oscart", title + " " + Double.toString(lat));
            icon = getBitmapDescriptor(CategoryTool.INSTANCE.getCategoryDrawableID(listGoodsModel.get(i).getCategory()));

            Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title(title).icon(icon));
            allMarkersMap.put(marker, listGoodsModel.get(i));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15)); //for test, remove later
        }
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {//InfoWindow
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Log.i("oscart", marker.getId() + marker.getTitle());
                View view = getActivity().getLayoutInflater().inflate(R.layout.item_goods, null);
                TextView goods_textView = (TextView) view.findViewById(R.id.item_history_other_goods_name);
                TextView user_textView = (TextView) view.findViewById(R.id.item_history_other_user_name);
                ImageView category_image = (ImageView) view.findViewById(R.id.item_history_other_category_image);
                ImageView goods_image = (ImageView) view.findViewById(R.id.item_history_other_goods_image);
                user_image = (ImageView) view.findViewById(R.id.item_history_other_user_image);
                GoodsModel good = allMarkersMap.get(marker);
                user_textView.setText(good.getOwner().getName());
                goods_textView.setText(good.getName());
                if (marker.isInfoWindowShown()) {
                    Picasso.with(getContext()).load(StringTool.INSTANCE.getFirstPhotoURL(good.getPhoto_path())
                    ).into(goods_image);
                } else { // if it's the first time, load the image with the callback set
                    Picasso.with(getContext()).load(StringTool.INSTANCE.getFirstPhotoURL(good.getPhoto_path())
                    ).into(goods_image, new InfoWindowRefresher(marker));
                }
                return view;
            }
        });
    }


    public void setPostModelDetail(PostModel postModelDetail) {
        this.postModelDetail = postModelDetail;
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

    private void setDraggableMarker() {
        LatLng myLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        draggableMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("長按並拖曳定位").draggable(true));
    }

    public void setUploadView(boolean isUpload) {
        if (isUpload) {
            cancelButton.setVisibility(View.VISIBLE);
            uploadButton.setVisibility(View.VISIBLE);
            setDraggableMarker();
        } else {
            draggableMarker.remove();
            cancelButton.setVisibility(View.GONE);
            uploadButton.setVisibility(View.GONE);
        }
    }

    private String convertPathTOBase(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] byteImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteImage, Base64.NO_WRAP); //NO_WRAP才不會出現換行
        return encodedImage;
    }

    public void downloadGoods() {
        Call<List<GoodsModel>> downloadGoodsCall = new RestClient().getExchangeService().downloadGoods();
        downloadGoodsCall.enqueue(new Callback<List<GoodsModel>>() {
            @Override
            public void onResponse(Call<List<GoodsModel>> call, Response<List<GoodsModel>> response) {
                if (response.code() == 200) {
                    List<GoodsModel> goodsModelList = response.body();
                    setGoodsMap(goodsModelList);
                } else {
                    Toast.makeText(getActivity(), "下載物品失敗 status Code錯誤", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GoodsModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "下載物品失敗 onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == Constant.PERMISSION_LOCATION
//                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//        }


    }

    @Override
    public void onResume() {
        super.onResume();
        user = null;
        if (RealmManager.INSTANCE.retrieveUser().size()!=0) {
            user = RealmManager.INSTANCE.retrieveUser().get(0);
            exToken = user.getExToken();
        }
    }
}
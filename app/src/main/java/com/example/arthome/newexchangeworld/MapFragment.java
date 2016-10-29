package com.example.arthome.newexchangeworld;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.facebook.Profile;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        new downloadGoodsAPI().execute("http://exwd.csie.org:43002/api/goods/search");
                    }
                }
            });
        }

        if (Profile.getCurrentProfile() != null) {
            user = RealmManager.INSTANCE.retrieveUser().get(0);
            exToken = user.getExToken();
            System.out.println(">>>map找到user name is " + user.getUserName());
            System.out.println(">>>map找到user EXToken is " + user.getExToken());
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
                Call<ResponseBody> call = new RestClient().getExchangeService().upLoadImage(user.getExToken(),
                        new UploadImageModel(convertPathTOBase(postModelDetail.getPhoto_path())));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {   //上傳成功
                            try {
                                postModelDetail.setPhoto_path(response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (response.code() == 403) {
                            Toast.makeText(getContext(), "Token過期 上傳圖片失敗", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "上傳圖片失敗 onFailure", Toast.LENGTH_SHORT).show();
                    }
                });

                setUploadView(false);
                break;
        }
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


    public void setPostModelDetail(PostModel postModelDetail) {
        this.postModelDetail = postModelDetail;
//        System.out.println("Base64 is:\n"+convertPathTOBase(postModelDetail.getPhoto_path()));
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
//        LatLng sydney = new LatLng(24.989042, 121.546373);
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

    public class postTask extends AsyncTask<PostModel, String, Integer> {

        private int statusCode;

        @Override
        protected Integer doInBackground(PostModel... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://exwd.csie.org:43002/api/goods?token=" + exToken);
            post.addHeader("content-type", "application/json");
            try {
                PostModel postModel = params[0];
                String jsonString = new Gson().toJson(postModel);
                HttpEntity entity = new StringEntity(jsonString, HTTP.UTF_8);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                entity = response.getEntity();
                jsonString = EntityUtils.toString(entity);
                System.out.println(">>>return String=" + jsonString);
                statusCode = response.getStatusLine().getStatusCode();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return statusCode;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 201) {  //post success
                Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Post fail", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class uploadImageTask extends AsyncTask<PostModel, String, AsyncWrapper> {

        private AsyncWrapper asyncWrapper;

        @Override
        protected AsyncWrapper doInBackground(PostModel... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://exwd.csie.org:43002/api/upload/image?token=" + exToken);
            post.addHeader("content-type", "application/json");
            try {
                PostModel postModel = params[0];
                //TODO 這邊可能要改成傳path就好 不用PostModel
                UploadImageModel uploadImageModel = new UploadImageModel(convertPathTOBase(postModel.getPhoto_path()));
                String jsonString = new Gson().toJson(uploadImageModel);
                HttpEntity entity = new StringEntity(jsonString, HTTP.UTF_8);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                entity = response.getEntity();
                jsonString = EntityUtils.toString(entity);


                postModel.setPhoto_path(jsonString);
                asyncWrapper = new AsyncWrapper();
                asyncWrapper.setPostModel(postModel);
                asyncWrapper.setStatusCode(response.getStatusLine().getStatusCode());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return asyncWrapper;
        }

        @Override
        protected void onPostExecute(AsyncWrapper asyncWrapper) {
            super.onPostExecute(asyncWrapper);
            if (asyncWrapper.getStatusCode() == 200) {  //post success
                new postTask().execute(asyncWrapper.getPostModel());
            } else {
                Toast.makeText(getContext(), "上傳圖片失敗", Toast.LENGTH_SHORT).show();
            }
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
}
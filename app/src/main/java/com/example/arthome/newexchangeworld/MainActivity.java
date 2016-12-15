package com.example.arthome.newexchangeworld;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import static android.Manifest.permission.*;

import android.widget.TextView;
import android.widget.Toast;

import com.example.arthome.newexchangeworld.Models.PostModel;
import com.example.arthome.newexchangeworld.util.CommonAPI;
import com.example.arthome.newexchangeworld.util.DateTool;

import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static MyWebSocketClient myWebSocketClient;
    private TabFragment tabFragment = TabFragment.newInstance();
    private MyPageFragment myPageFragment = MyPageFragment.newInstance();
    private ImageView userPhoto;
    private TextView userName;
    private TextView userLocation;
    private User user;
    private MapFragment mapFragment;
    private NavigationView navigationView;
    private FloatingActionButton cameraButton;

    public void camera() {
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, 0);
        else
            toGallery();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this);
        Realm.init(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cameraButton = (FloatingActionButton) findViewById(R.id.cameraFab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        marshmallowGPSPremissionCheck();

        //for circle head view
        View headerView = navigationView.getHeaderView(0); // for Navigation findViewById
        userPhoto = (ImageView) headerView.findViewById(R.id.nav_user_image_view);
        userName = (TextView) headerView.findViewById(R.id.nav_user_name);
        userLocation = (TextView) headerView.findViewById(R.id.nav_user_location);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, tabFragment);
            transaction.commit();
            cameraButton.setVisibility(View.VISIBLE);
        }
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    camera();
                } else {
                    Toast.makeText(getApplicationContext(), "登入後才能上傳照片", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        if (RealmManager.INSTANCE.retrieveUser().size() == 0) {
            setUserLoginView(false);
        } else {
            user = RealmManager.INSTANCE.retrieveUser().get(0);
            if (user.getLastTokenDate() == null) {
                CommonAPI.INSTANCE.getExToken(user.getIdentity(), this);
                user = RealmManager.INSTANCE.retrieveUser().get(0);
            } else {
                if (!DateTool.INSTANCE.isSameDay(user.getLastTokenDate(), new Date())) {  //Token過期 重新取得
                    CommonAPI.INSTANCE.getExToken(user.getIdentity(), this);
                    user = RealmManager.INSTANCE.retrieveUser().get(0);
                }
            }
            setUserLoginView(true);
        }


        if (user != null) {
            try {
                myWebSocketClient = new MyWebSocketClient(new URI("ws://exwd.csie.org:43002/?token=" + user.getExToken()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            myWebSocketClient.connect();
        }
        super.onResume();
    }

    public void toGallery() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, pictureActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResult) {
        if (requestCode == 0)
            toGallery();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
    }

    @Override
    public void onBackPressed() {
        //// TODO: need fix to be better
        //for backbutton in Item_Category
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    for (Fragment childfragnested : childFm.getFragments()) {
                        FragmentManager childFmNestManager = childfragnested.getFragmentManager();
                        if (childfragnested.isVisible()) {
                            childFmNestManager.popBackStack();
                            return;
                        }
                    }
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) { //從post Activity回到Main會call
        super.onNewIntent(intent);
        if (intent.getFlags() == Intent.FLAG_ACTIVITY_CLEAR_TOP) {
            mapFragment = tabFragment.getMapFragment();
            PostModel postModel = (PostModel) intent.getExtras().getSerializable("postInfo");
            System.out.println(">>>post " + postModel.getName());
            mapFragment.setPostModelDetail(postModel);
            mapFragment.setUploadView(true);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.cameraFab);
        if (id == R.id.drawer_searchPage) {
            transaction.replace(R.id.content_frame, tabFragment);
            transaction.commit();
            cameraButton.setVisibility(View.VISIBLE);
        } else if (id == R.id.drawer_myPage) {
            transaction.replace(R.id.content_frame, myPageFragment);
            transaction.commit();
            cameraButton.setVisibility(View.INVISIBLE);
        } else if (id == R.id.drawer_accountSetting) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Login.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this); //fix multidex can't find class bug
    }

    //for circle view
    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public static MyWebSocketClient getMyWebSocketClient() {
        return myWebSocketClient;
    }

    private void marshmallowGPSPremissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    Constant.PERMISSION_LOCATION);
        } else {
            //   gps functions.
        }
    }

    private void setUserLoginView(boolean isLogin) {
        if (isLogin) {
            Picasso.with(this).load(user.getPhotoPath()).transform(new CircleTransform()).into(userPhoto);
            userName.setText(user.getUserName());
            navigationView.getMenu().findItem(R.id.drawer_myPage).setEnabled(true);
        } else {
            userPhoto.setImageResource(R.drawable.default_user);
            userName.setText("請登入");
            navigationView.getMenu().findItem(R.id.drawer_myPage).setEnabled(false);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, tabFragment);
            transaction.commit();
        }
    }
}

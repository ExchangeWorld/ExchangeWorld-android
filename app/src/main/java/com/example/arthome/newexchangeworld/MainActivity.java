package com.example.arthome.newexchangeworld;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
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
import android.widget.Toast;
import static android.Manifest.permission.*;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.ExchangeAPI.RestClient;
import com.example.arthome.newexchangeworld.Models.AuthenticationModel;
import com.example.arthome.newexchangeworld.Models.FaceBookUser;
import com.example.arthome.newexchangeworld.Models.PostModel;
import com.example.arthome.newexchangeworld.Models.UserModel;
import com.facebook.Profile;
import com.google.gson.Gson;


import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabFragment tabFragment = TabFragment.newInstance();
    private ImageView userPhoto;
    private TextView userName;
    private TextView userLocation;

    public void camera(View view){
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this,READ_EXTERNAL_STORAGE);
        if(permission!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE},0);
        else
            toGallery();

    }

/*    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Bitmap mbmp = (Bitmap) data.getExtras().get("data");
            ImageView CameraV = (ImageView) findViewById(R.id.cameraImageView);
            CameraV.setImageBitmap(mbmp);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(this);
        Realm.init(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.cameraFab);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //for circle head view
        View headerView = navigationView.getHeaderView(0); // for Navigation findViewById
        userPhoto = (ImageView) headerView.findViewById(R.id.nav_user_image_view);
        userName = (TextView) headerView.findViewById(R.id.nav_user_name);
        userLocation = (TextView) headerView.findViewById(R.id.nav_user_location);
//        Bitmap myhead = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.myhead);
//        userPhoto.setImageBitmap(getCroppedBitmap(myhead));

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, tabFragment);
            transaction.commit();
            cameraButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Profile.getCurrentProfile()!=null) {
            String id = Profile.getCurrentProfile().getId();
            System.out.println(">>>main id= " + id);
            if (RealmManager.INSTANCE.retrieveUser().size() == 0) {
                User user = new User();
                user.setIdentity(Profile.getCurrentProfile().getId());
                user.setUserName(Profile.getCurrentProfile().getName());

                RealmManager.INSTANCE.createUser(user);
            } else {
                User user = RealmManager.INSTANCE.retrieveUser().get(0);
                //TODO  檢查日期 隔天才需要再拿一次EXchangeWORLD TOKEN
//                new getTokenTask().execute(user.getIdentity());

                Picasso.with(this).load(user.getPhotoPath()).transform(new CircleTransform()).into(userPhoto);
                userName.setText(user.getUserName());

            }
        }
    }



    public class getTokenTask extends AsyncTask<String,String,String> {

        private AuthenticationModel authenticationModel;

        @Override
        protected String doInBackground(String... params) {
            FaceBookUser user = new FaceBookUser();
            user.setIdentity(params[0]);        //傳入FB ID
            String body = new Gson().toJson(user);
            System.out.println(">>>body="+body);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://exwd.csie.org:43002/api/authenticate/login");
            post.addHeader("content-type","application/json");
            try {
                HttpEntity entity = new StringEntity(body);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                entity = response.getEntity();
                String jsonString = EntityUtils.toString(entity);
                System.out.println(">>>return String=" + jsonString);
                authenticationModel = new Gson().fromJson(jsonString, AuthenticationModel.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return authenticationModel.getToken();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            EXtoken = s;
            User user = RealmManager.INSTANCE.retrieveUser().get(0);
            user.setExToken(s);
            RealmManager.INSTANCE.createUser(user);
//            new postTask().execute(s,itemName,itemDescription);
        }
    }

    public void toGallery(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, pictureActivity.class);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permission,int [] grantResult) {
        if(requestCode==0)
            toGallery();
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
        if (intent.getFlags() == Intent.FLAG_ACTIVITY_CLEAR_TOP){

            MapFragment mapFragment = tabFragment.getMapFragment();
            PostModel postModel = (PostModel) intent.getExtras().getSerializable("postInfo");
            System.out.println(">>>post "+postModel.getName());
            mapFragment.setPostModelDetail(postModel);
            mapFragment.setUploadView(true);
        }
    }

    /*              three dot setting on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Setting pressed", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FloatingActionButton cameraButton = (FloatingActionButton) findViewById(R.id.cameraFab);
        if (id == R.id.drawer_searchPage) {
            transaction.replace(R.id.content_frame, TabFragment.newInstance());
            transaction.commit();
            cameraButton.setVisibility(View.VISIBLE);
        } else if (id == R.id.drawer_myPage) {
            transaction.replace(R.id.content_frame, MyPageFragment.newInstance());
            transaction.commit();
            cameraButton.setVisibility(View.INVISIBLE);
        } else if (id == R.id.drawer_accountSetting) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, TestActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            transaction.replace(R.id.content_frame, oneFragment.newInstance("one", "one"));
            transaction.commit();
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
}

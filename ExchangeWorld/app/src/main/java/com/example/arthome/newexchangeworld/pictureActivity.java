package com.example.arthome.newexchangeworld;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arthome.newexchangeworld.ItemPage.PhotoAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.*;

public class pictureActivity extends AppCompatActivity {

    private DisplayMetrics mPhone;
    private final static int CAMERA = 2;
    private PhotoAdapter photoAdapter;
    private List<String> thumbs;  //存放縮圖的id
    private List<String> imagePaths;  //存放圖片的路徑
    private GridView gallery;
    private Button cameraButton;
    private ImageView CameraV;
    private EditText postText;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        cameraButton = (Button) findViewById(R.id.camerabutton);
        gallery = (GridView) findViewById(R.id.GalleryView);
        CameraV = (ImageView) findViewById(R.id.PhotoView);
        postText = (EditText) findViewById(R.id.postText);
        postText.setVisibility(View.GONE);
        CameraV.setVisibility(View.GONE);


  //     ContentResolver cr = getContentResolver();
       String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };


        //查詢SD卡的圖片
        Cursor cursor = managedQuery(Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        thumbs = new ArrayList<String>();
        imagePaths = new ArrayList<String>();

        for (int i = 0; i < cursor.getCount(); i++) {

            cursor.moveToPosition(i);
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));// ID
            thumbs.add(id + "");

            String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑

            imagePaths.add(filepath);
        }

        cursor.close();
        photoAdapter = new PhotoAdapter(pictureActivity.this, thumbs);
        gallery.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   ContentValues value = new ContentValues();
                value.put(Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI,value);*/
                Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
                SimpleDateFormat tmpTime = new SimpleDateFormat("yyyyMMdd_HHmmss");
                File tmpfile = new File(Environment.getExternalStorageDirectory(),tmpTime.format(new Date())+".jpg");
                Uri uri = Uri.fromFile(tmpfile);
                intent.putExtra(EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if ((requestCode == CAMERA)) {
                Bitmap mbmp = (Bitmap) data.getExtras().getParcelable("data");
                CameraV.setImageBitmap(mbmp);
                CameraV.setScaleType(ImageView.ScaleType.FIT_XY);
                postText.setVisibility(View.VISIBLE);
                CameraV.setVisibility(View.VISIBLE);
                cameraButton.setVisibility(View.GONE);
                gallery.setVisibility(View.GONE);
            }
        }
    }

    public void setImageView(int position){
        Bitmap bm = BitmapFactory.decodeFile(imagePaths.get(position));
        CameraV.setImageBitmap(bm);
        CameraV.setScaleType(ImageView.ScaleType.FIT_XY);
        postText.setVisibility(View.VISIBLE);
        CameraV.setVisibility(View.VISIBLE);
        cameraButton.setVisibility(View.GONE);
        gallery.setVisibility(View.GONE);
    }
}



